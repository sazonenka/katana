package by.sazonenka.katana.persistence.interceptor;

import static by.sazonenka.katana.persistence.service.ServiceTestData.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.persistence.service.GenericServiceTest;

/**
 * @author Aliaksandr Sazonenka
 */
@ContextConfiguration({"/spring/spring-interceptor.xml"})
public class ConstraintConfigModificationTrackerTest extends GenericServiceTest {

  private static final String METHOD_SAVE = "save";
  private static final String METHOD_DELETE = "delete";

  @Inject private ConstraintConfigModificationTracker tracker;

  private ValidationRule rule;
  private OutputFile file;
  private OutputField field;

  private final Multimap<Object, MutationMethod> mutators = ArrayListMultimap.create();

  @PostConstruct
  public void populateConfigMutators() {
    mutators.putAll(ruleService, ImmutableList.of(
        new MutationMethod(METHOD_SAVE,
            new Supplier<Object[]>() {
              @Override
              public Object[] get() {
                return new Object[] { rule };
              }
            }, 0,
            new Function<Object, ConstraintConfig>() {
              @Override
              public ConstraintConfig apply(Object input) {
                if (input instanceof ValidationRule) {
                  return ((ValidationRule) input).getConfig();
                }
                return null;
              }
            }),
        new MutationMethod(METHOD_DELETE,
            new Supplier<Object[]>() {
              @Override
              public Object[] get() {
                return new Object[] { RULE_1_ID };
              }
            }, 0,
            new Function<Object, ConstraintConfig>() {
              @Override
              public ConstraintConfig apply(Object input) {
                if (input instanceof Long) {
                  Long ruleId = (Long) input;
                  return ruleDao.get(ruleId).getConfig();
                }
                return null;
              }
            })
        ));
    mutators.putAll(fileService, ImmutableList.of(
        new MutationMethod(METHOD_SAVE,
            new Supplier<Object[]>() {
              @Override
              public Object[] get() {
                return new Object[] { file };
              }
            }, 0,
            new Function<Object, ConstraintConfig>() {
              @Override
              public ConstraintConfig apply(Object input) {
                if (input instanceof OutputFile) {
                  return ((OutputFile) input).getConfig();
                }
                return null;
              }
            }),
        new MutationMethod(METHOD_DELETE,
            new Supplier<Object[]>() {
              @Override
              public Object[] get() {
                return new Object[] { FILE_1_ID };
              }
            }, 0,
            new Function<Object, ConstraintConfig>() {
              @Override
              public ConstraintConfig apply(Object input) {
                if (input instanceof Long) {
                  Long fileId = (Long) input;
                  return fileDao.get(fileId).getConfig();
                }
                return null;
              }
            })
        ));
    mutators.putAll(fieldService, ImmutableList.of(
        new MutationMethod(METHOD_SAVE,
            new Supplier<Object[]>() {
              @Override
              public Object[] get() {
                return new Object[] { field };
              }
            }, 0,
            new Function<Object, ConstraintConfig>() {
              @Override
              public ConstraintConfig apply(Object input) {
                if (input instanceof OutputField) {
                  return ((OutputField) input).getFile().getConfig();
                }
                return null;
              }
            }),
        new MutationMethod(METHOD_DELETE,
            new Supplier<Object[]>() {
              @Override
              public Object[] get() {
                return new Object[] { FIELD_1_ID };
              }
            }, 0,
            new Function<Object, ConstraintConfig>() {
              @Override
              public ConstraintConfig apply(Object input) {
                if (input instanceof Long) {
                  Long fieldId = (Long) input;
                  return fieldDao.get(fieldId).getFile().getConfig();
                }
                return null;
              }
            })
        ));
  }

  private void resetMocks() {
    reset(configDao, ruleDao, fileDao, fieldDao);

    rule = getRule1();
    rule.getConfig().setId(CONFIG_1_ID);
    file = getFile1();
    file.getConfig().setId(CONFIG_1_ID);
    field = getField1();
    field.getFile().setId(FILE_1_ID);
    field.getFile().getConfig().setId(CONFIG_1_ID);

    when(ruleDao.get(RULE_1_ID)).thenReturn(rule);
    when(fileDao.get(FILE_1_ID)).thenReturn(file);
    when(fieldDao.get(FIELD_1_ID)).thenReturn(field);
  }

  @Test
  public void triggerOnMutationMethods() throws Throwable {
    for (Object target : mutators.keySet()) {
      for (MutationMethod mutationMethod : mutators.get(target)) {
        resetMocks();

        String methodName = mutationMethod.getMethodName();
        Object[] params = mutationMethod.getMethodArgs();

        int configArgIndex = mutationMethod.getConfigArgIndex();
        ConstraintConfig config = mutationMethod.getConfigSupplier().apply(params[configArgIndex]);
        Date modified = config.getModified();

        when(configDao.get(CONFIG_1_ID)).thenReturn(config);

        ProceedingJoinPoint joinPoint = createJoinPoint(target, methodName, params);
        tracker.saveConfigModificationDate(joinPoint);

        verify(configDao).save(config);
        assertThat(config.getModified(), greaterThan(modified));
      }
    }
  }

  @Test
  public void notTriggerOnNonMutationMethods() throws Throwable {
    Multimap<Object, String> nonMutationMethods = ArrayListMultimap.create();
    nonMutationMethods.putAll(configService,
        ImmutableList.of("get", "save", METHOD_DELETE, "findAll", "getCount", "saveToBuffer",
            "saveToString", "loadFromBuffer"));
    nonMutationMethods.putAll(ruleService,
        ImmutableList.of("get", "findByConfig", "checkRuleNamesForDuplicates"));
    nonMutationMethods.putAll(fileService,
        ImmutableList.of("get", "findByConfig", "findByParent", "checkFileNamesForDuplicates"));
    nonMutationMethods.putAll(fieldService,
        ImmutableList.of("get", "findByFile", "findByRule", "checkFieldNamesForDuplicates"));

    for (Object target : nonMutationMethods.keySet()) {
      for (String methodName : nonMutationMethods.get(target)) {
        reset(configDao);

        ProceedingJoinPoint joinPoint = createJoinPoint(target, methodName);
        tracker.saveConfigModificationDate(joinPoint);

        verify(configDao, never()).save(any(ConstraintConfig.class));
      }
    }
  }

  private ProceedingJoinPoint createJoinPoint(Object target, String methodName, Object... params) {
    ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

    when(joinPoint.getTarget()).thenReturn(target);

    Signature signature = mock(Signature.class);
    when(signature.getName()).thenReturn(methodName);
    when(joinPoint.getSignature()).thenReturn(signature);

    when(joinPoint.getArgs()).thenReturn(params);

    return joinPoint;
  }

  private static final class MutationMethod {

    private final String methodName;
    private final Supplier<Object[]> methodArgsSupplier;
    private final int configArgIndex;
    private final Function<Object, ConstraintConfig> configSupplier;

    MutationMethod(String methodName, Supplier<Object[]> methodArgsSupplier, int configArgIndex,
        Function<Object, ConstraintConfig> configSupplier) {
      this.methodName = Preconditions.checkNotNull(methodName);
      this.methodArgsSupplier = Preconditions.checkNotNull(methodArgsSupplier);
      this.configArgIndex = Preconditions.checkNotNull(configArgIndex);
      this.configSupplier = Preconditions.checkNotNull(configSupplier);
    }

    public String getMethodName() { return methodName; }
    public Object[] getMethodArgs() { return methodArgsSupplier.get(); }
    public int getConfigArgIndex() { return configArgIndex; }
    public Function<Object, ConstraintConfig> getConfigSupplier() { return configSupplier; }

  }

}
