package by.sazonenka.katana.web.server;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.service.ConstraintConfigService;
import by.sazonenka.katana.persistence.service.OutputFieldService;
import by.sazonenka.katana.persistence.service.OutputFileService;
import by.sazonenka.katana.persistence.service.ValidationRuleService;
import by.sazonenka.katana.web.client.config.ConfigWarningTracker.Warning;
import by.sazonenka.katana.web.client.managers.ConstraintConfigManager;
import by.sazonenka.katana.web.client.managers.ManagerEndpoints;
import by.sazonenka.katana.web.model.ConstraintConfigModel;
import by.sazonenka.katana.xml.service.XmlPersisterException;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
@Controller
public final class ConstraintConfigManagerImpl
    extends GenericManagerImpl
    implements ConstraintConfigManager {

  private static final long serialVersionUID = -8141965250596060725L;

  private final ConstraintConfigService configService;
  private final ValidationRuleService ruleService;
  private final OutputFileService fileService;
  private final OutputFieldService fieldService;

  private final Mapper mapper;

  @Inject
  public ConstraintConfigManagerImpl(ConstraintConfigService configService,
      ValidationRuleService ruleService, OutputFileService fileService,
      OutputFieldService fieldService, Mapper mapper) {
    this.configService = Preconditions.checkNotNull(configService);
    this.ruleService = Preconditions.checkNotNull(ruleService);
    this.fileService = Preconditions.checkNotNull(fileService);
    this.fieldService = Preconditions.checkNotNull(fieldService);

    this.mapper = Preconditions.checkNotNull(mapper);
  }

  @RequestMapping("/" + ManagerEndpoints.CONSTRAINT_CONFIG_MANAGER_URI)
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    return super.handleRequest(request, response);
  }

  @Override
  public List<ConstraintConfigModel> findAll() throws Exception {
    try {
      List<ConstraintConfig> configs = configService.findAll();

      return Lists.newArrayList(Lists.transform(configs,
          new Function<ConstraintConfig, ConstraintConfigModel>() {
            @Override
            public ConstraintConfigModel apply(ConstraintConfig domain) {
              return mapper.map(domain, ConstraintConfigModel.class);
            }
          }));
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public ConstraintConfigModel save(ConstraintConfigModel model) throws Exception {
    try {
      ConstraintConfig config = null;
      if (model.getId() == null) {
        config = new ConstraintConfig();
      } else {
        config = configService.get(model.getId());
      }

      mapper.map(model, config);

      config = configService.save(config);
      return mapper.map(config, ConstraintConfigModel.class);
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void delete(Long id) throws Exception {
    try {
      configService.delete(id);
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public String loadXml(Long id) throws Exception {
    try {
      return configService.saveToString(id);
    } catch (XmlPersisterException e) {
      return "";
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public List<Warning> checkConfigForNameDuplicates(Long id) throws Exception {
    try {
      List<Warning> warnings = Lists.newArrayList();

      if (ruleService.checkRuleNamesForDuplicates(id)) {
        warnings.add(Warning.RULE_NAME_DUPLICATE);
      }
      if (fileService.checkFileNamesForDuplicates(id)) {
        warnings.add(Warning.FILE_NAME_DUPLICATE);
      }
      if (fieldService.checkFieldNamesForDuplicates(id)) {
        warnings.add(Warning.FIELD_NAME_DUPLICATE);
      }

      return warnings;
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

}
