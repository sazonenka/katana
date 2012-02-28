package by.sazonenka.katana.persistence.interceptor;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.aspectj.lang.JoinPoint;
import org.springframework.transaction.annotation.Transactional;

import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.persistence.service.OutputFieldService;
import by.sazonenka.katana.persistence.service.OutputFileService;
import by.sazonenka.katana.persistence.service.ValidationRuleService;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * @author Aliaksandr Sazonenka
 */
public class ConstraintConfigModificationTracker {

  private static final String METHOD_SAVE = "save";
  private static final String METHOD_DELETE = "delete";

  private static final ImmutableList<ConfigMutator> CONFIG_MUTATORS = ImmutableList.of(
      new ConfigMutator(ValidationRuleService.class,
          ImmutableList.of(METHOD_SAVE, METHOD_DELETE),
          new Function<Object, ConstraintConfig>() {
            @Override
            public ConstraintConfig apply(Object entity) {
              if (entity instanceof ValidationRule) {
                return ((ValidationRule) entity).getConfig();
              }
              return null;
            }
          }),
      new ConfigMutator(OutputFileService.class,
          ImmutableList.of(METHOD_SAVE, METHOD_DELETE),
          new Function<Object, ConstraintConfig>() {
            @Override
            public ConstraintConfig apply(Object entity) {
              if (entity instanceof OutputFile) {
                return ((OutputFile) entity).getConfig();
              }
              return null;
            }
          }),
      new ConfigMutator(OutputFieldService.class,
          ImmutableList.of(METHOD_SAVE, METHOD_DELETE),
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

  private final ConstraintConfigDao constraintConfigDao;

  @Inject
  public ConstraintConfigModificationTracker(ConstraintConfigDao constraintConfigDao) {
    this.constraintConfigDao = Preconditions.checkNotNull(constraintConfigDao);
  }

  @Transactional
  public void saveConfigModificationDate(JoinPoint joinPoint) {
    Object target = joinPoint.getTarget();
    for (ConfigMutator mutator : CONFIG_MUTATORS) {
      if (mutator.getMutatorClass().isInstance(target)) {
        String method = joinPoint.getSignature().getName();
        if (mutator.getMutationMethods().contains(method)) {
          Object entity = joinPoint.getArgs()[0];

          ConstraintConfig config = mutator.getConfigSupplier().apply(entity);
          if (config != null) {
            config.setModified(new Date());
            constraintConfigDao.save(config);
          }
        }
        break;
      }
    }
  }

  private static final class ConfigMutator {

    private final Class<?> mutatorClass;
    private final List<String> mutationMethods;
    private final Function<Object, ConstraintConfig> configSupplier;

    ConfigMutator(Class<?> mutatorClass,
        List<String> mutationMethods,
        Function<Object, ConstraintConfig> configSupplier) {
      this.mutatorClass = Preconditions.checkNotNull(mutatorClass);
      this.mutationMethods = Preconditions.checkNotNull(mutationMethods);
      this.configSupplier = Preconditions.checkNotNull(configSupplier);
    }

    public Class<?> getMutatorClass() { return mutatorClass; }
    public List<String> getMutationMethods() { return mutationMethods; }
    public Function<Object, ConstraintConfig> getConfigSupplier() { return configSupplier; }

  }

}
