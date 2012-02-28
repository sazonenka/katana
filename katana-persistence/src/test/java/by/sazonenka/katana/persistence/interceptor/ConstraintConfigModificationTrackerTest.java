package by.sazonenka.katana.persistence.interceptor;

import static by.sazonenka.katana.persistence.service.ServiceTestData.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.persistence.service.GenericServiceTest;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author Aliaksandr Sazonenka
 */
public class ConstraintConfigModificationTrackerTest extends GenericServiceTest {

  private List<ConfigMutator> mutators;

  private ConstraintConfigModificationTracker tracker;

  @Before
  public void setUp() {
    mutators = ImmutableList.of(
      new ConfigMutator(ruleService,
          ImmutableMap.of(
              "save", new Object[] { getRule1() },
              "delete", new Object[] { getRule1() }
              ),
          ImmutableList.of("get", "deleteById", "findByConfig"),
          new Function<Object, ConstraintConfig>() {
            @Override
            public ConstraintConfig apply(Object entity) {
              if (entity instanceof ValidationRule) {
                return ((ValidationRule) entity).getConfig();
              }
              return null;
            }
          }),
      new ConfigMutator(fileService,
          ImmutableMap.of(
              "save", new Object[] { getFile1() },
              "delete", new Object[] { getFile1() }
              ),
          ImmutableList.of("get", "deleteById", "findByConfig", "findByParent"),
          new Function<Object, ConstraintConfig>() {
            @Override
            public ConstraintConfig apply(Object entity) {
              if (entity instanceof OutputFile) {
                return ((OutputFile) entity).getConfig();
              }
              return null;
            }
          }),
      new ConfigMutator(fieldService,
          ImmutableMap.of(
              "save", new Object[] { getField1() },
              "delete", new Object[] { getField1() }
              ),
          ImmutableList.of("get", "deleteById", "findByFile", "findByRule"),
          new Function<Object, ConstraintConfig>() {
            @Override
            public ConstraintConfig apply(Object entity) {
              if (entity instanceof OutputField) {
                return ((OutputField) entity).getFile().getConfig();
              }
              return null;
            }
          })
      );

    tracker = new ConstraintConfigModificationTracker(configDao);
  }

  @Test
  public void triggerOnMutationMethods() {
    for (ConfigMutator mutator : mutators) {
      Object target = mutator.getMutatorObject();
      for (String mutationMethod : mutator.getMutationMethods().keySet()) {
        reset(configDao);

        Object[] params = mutator.getMutationMethods().get(mutationMethod);

        ConstraintConfig config = mutator.getConfigSupplier().apply(params[0]);
        Date modified = config.getModified();

        JoinPoint joinPoint = createJoinPoint(target, mutationMethod, params);
        tracker.saveConfigModificationDate(joinPoint);

        verify(configDao).save(config);
        assertThat(config.getModified(), greaterThan(modified));
      }
    }
  }

  @Test
  public void notTriggerOnNonMutationMethods() {
    for (ConfigMutator mutator : mutators) {
      Object target = mutator.getMutatorObject();
      for (String nonMutationMethod : mutator.getNonMutationMethods()) {
        reset(configDao);

        JoinPoint joinPoint = createJoinPoint(target, nonMutationMethod);
        tracker.saveConfigModificationDate(joinPoint);

        verify(configDao, never()).save(any(ConstraintConfig.class));
      }
    }
  }

  private JoinPoint createJoinPoint(Object target, String methodName, Object... params) {
    JoinPoint joinPoint = mock(JoinPoint.class);

    when(joinPoint.getTarget()).thenReturn(target);

    Signature signature = mock(Signature.class);
    when(signature.getName()).thenReturn(methodName);
    when(joinPoint.getSignature()).thenReturn(signature);

    when(joinPoint.getArgs()).thenReturn(params);

    return joinPoint;
  }

  private static final class ConfigMutator {

    private final Object mutatorObject;
    private final Map<String, Object[]> mutationMethods;
    private final List<String> nonMutationMethods;
    private final Function<Object, ConstraintConfig> configSupplier;

    ConfigMutator(Object mutatorObject, Map<String, Object[]> mutationMethods,
        List<String> nonMutationMethods, Function<Object, ConstraintConfig> configSupplier) {
      this.mutatorObject = Preconditions.checkNotNull(mutatorObject);
      this.mutationMethods = Preconditions.checkNotNull(mutationMethods);
      this.nonMutationMethods = Preconditions.checkNotNull(nonMutationMethods);
      this.configSupplier = Preconditions.checkNotNull(configSupplier);
    }

    public Object getMutatorObject() { return mutatorObject; }
    public Map<String, Object[]> getMutationMethods() { return mutationMethods; }
    public List<String> getNonMutationMethods() { return nonMutationMethods; }
    public Function<Object, ConstraintConfig> getConfigSupplier() { return configSupplier; }

  }

}
