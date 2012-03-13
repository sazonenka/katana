package by.sazonenka.katana.persistence.interceptor;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.dao.OutputFieldDao;
import by.sazonenka.katana.persistence.dao.OutputFileDao;
import by.sazonenka.katana.persistence.dao.ValidationRuleDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.persistence.service.OutputFieldService;
import by.sazonenka.katana.persistence.service.OutputFileService;
import by.sazonenka.katana.persistence.service.ValidationRuleService;

/**
 * @author Aliaksandr Sazonenka
 */
public class ConstraintConfigModificationTracker {

  private static final String METHOD_SAVE = "save";
  private static final String METHOD_DELETE = "delete";

  private final ConstraintConfigDao configDao;
  private final ValidationRuleDao ruleDao;
  private final OutputFileDao fileDao;
  private final OutputFieldDao fieldDao;

  private final Multimap<Class<?>, MutationMethod> configMutators = ArrayListMultimap.create();

  @Inject
  public ConstraintConfigModificationTracker(ConstraintConfigDao configDao,
      ValidationRuleDao ruleDao, OutputFileDao fileDao, OutputFieldDao fieldDao) {
    this.configDao = Preconditions.checkNotNull(configDao);
    this.ruleDao = Preconditions.checkNotNull(ruleDao);
    this.fileDao = Preconditions.checkNotNull(fileDao);
    this.fieldDao = Preconditions.checkNotNull(fieldDao);
  }

  @PostConstruct
  public void populateConfigMutators() {
    configMutators.putAll(ValidationRuleService.class, ImmutableList.of(
        new MutationMethod(METHOD_SAVE, 0, new Function<Object, ConstraintConfig>() {
          @Override
          public ConstraintConfig apply(Object input) {
            if (input instanceof ValidationRule) {
              return ((ValidationRule) input).getConfig();
            }
            return null;
          }
        }),
        new MutationMethod(METHOD_DELETE, 0, new Function<Object, ConstraintConfig>() {
          @Override
          public ConstraintConfig apply(Object input) {
            if (input instanceof Long) {
              Long ruleId = (Long) input;
              return ruleDao.get(ruleId).getConfig();
            }
            return null;
          }
        })));
    configMutators.putAll(OutputFileService.class, ImmutableList.of(
        new MutationMethod(METHOD_SAVE, 0, new Function<Object, ConstraintConfig>() {
          @Override
          public ConstraintConfig apply(Object input) {
            if (input instanceof OutputFile) {
              return ((OutputFile) input).getConfig();
            }
            return null;
          }
        }),
        new MutationMethod(METHOD_DELETE, 0, new Function<Object, ConstraintConfig>() {
          @Override
          public ConstraintConfig apply(Object input) {
            if (input instanceof Long) {
              Long fileId = (Long) input;
              return fileDao.get(fileId).getConfig();
            }
            return null;
          }
        })));
    configMutators.putAll(OutputFieldService.class, ImmutableList.of(
        new MutationMethod(METHOD_SAVE, 0, new Function<Object, ConstraintConfig>() {
          @Override
          public ConstraintConfig apply(Object input) {
            if (input instanceof OutputField) {
              Long fileId = ((OutputField) input).getFile().getId();
              return fileDao.get(fileId).getConfig();
            }
            return null;
          }
        }),
        new MutationMethod(METHOD_DELETE, 0, new Function<Object, ConstraintConfig>() {
          @Override
          public ConstraintConfig apply(Object input) {
            if (input instanceof Long) {
              Long fieldId = (Long) input;
              return fieldDao.get(fieldId).getFile().getConfig();
            }
            return null;
          }
        })));
  }

  @Transactional
  public Object saveConfigModificationDate(ProceedingJoinPoint joinPoint) throws Throwable {
    Long configId = getConfigId(joinPoint);

    Object result = joinPoint.proceed();

    if (configId != null) {
      ConstraintConfig config = configDao.get(configId);
      config.setModified(new Date());
      configDao.save(config);
    }

    return result;
  }

  private Long getConfigId(ProceedingJoinPoint joinPoint) {
    Object target = joinPoint.getTarget();
    for (Class<?> mutatorClass : configMutators.keySet()) {
      if (mutatorClass.isInstance(target)) {
        String methodName = joinPoint.getSignature().getName();
        for (MutationMethod mutationMethod : configMutators.get(mutatorClass)) {
          if (methodName.equals(mutationMethod.getMethodName())) {
            Object entity = joinPoint.getArgs()[mutationMethod.getConfigArgIndex()];
            return mutationMethod.getConfigSupplier().apply(entity).getId();
          }
        }
        break;
      }
    }
    return null;
  }

  private static final class MutationMethod {

    private final String methodName;
    private final int configArgIndex;
    private final Function<Object, ConstraintConfig> configSupplier;

    MutationMethod(String methodName, int configArgIndex,
        Function<Object, ConstraintConfig> configSupplier) {
      this.methodName = Preconditions.checkNotNull(methodName);
      this.configArgIndex = Preconditions.checkNotNull(configArgIndex);
      this.configSupplier = Preconditions.checkNotNull(configSupplier);
    }

    public String getMethodName() { return methodName; }
    public int getConfigArgIndex() { return configArgIndex; }
    public Function<Object, ConstraintConfig> getConfigSupplier() { return configSupplier; }

  }

}
