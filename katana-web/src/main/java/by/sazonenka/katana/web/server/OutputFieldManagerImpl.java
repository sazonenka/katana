package by.sazonenka.katana.web.server;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.persistence.service.OutputFieldService;
import by.sazonenka.katana.persistence.service.ValidationRuleService;
import by.sazonenka.katana.web.client.managers.ManagerEndpoints;
import by.sazonenka.katana.web.client.managers.OutputFieldManager;
import by.sazonenka.katana.web.model.OutputFieldModel;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
@Controller
public final class OutputFieldManagerImpl
    extends GenericManagerImpl
    implements OutputFieldManager {

  private static final long serialVersionUID = -8734079478609462377L;

  private final OutputFieldService fieldService;
  private final ValidationRuleService ruleService;
  private final Mapper mapper;

  @Inject
  public OutputFieldManagerImpl(OutputFieldService fieldService,
      ValidationRuleService ruleService, Mapper mapper) {
    this.fieldService = Preconditions.checkNotNull(fieldService);
    this.ruleService = Preconditions.checkNotNull(ruleService);
    this.mapper = Preconditions.checkNotNull(mapper);
  }

  @RequestMapping("/" + ManagerEndpoints.OUTPUT_FIELD_MANAGER_URI)
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    return super.handleRequest(request, response);
  }

  @Override
  public List<OutputFieldModel> findByFile(Long fileId) throws Exception {
    try {
      List<OutputField> fields = fieldService.findByFile(fileId);

      return Lists.newArrayList(Lists.transform(fields,
          new Function<OutputField, OutputFieldModel>() {
            @Override
            public OutputFieldModel apply(OutputField domain) {
              return mapper.map(domain, OutputFieldModel.class);
            }
          }));
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public OutputFieldModel save(OutputFieldModel model) throws Exception {
    try {
      OutputField field = null;
      if (model.getId() == null) {
        field = new OutputField();
      } else {
        field = fieldService.get(model.getId());
      }

      mapper.map(model, field);

      field = fieldService.save(field);
      return mapper.map(field, OutputFieldModel.class);
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void delete(List<Long> ids) throws Exception {
    try {
      for (Long id : ids) {
        fieldService.delete(id);
      }
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void reorder(List<Long> ids) throws Exception {
    try {
      for (int i = 0; i < ids.size(); ++i) {
        OutputField field = fieldService.get(ids.get(i));
        field.setOrderInFile(i);
        fieldService.save(field);
      }
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void map(List<Long> ids, Long ruleId) throws Exception {
    try {
      ValidationRule rule = ruleService.get(ruleId);
      for (Long id : ids) {
        OutputField field = fieldService.get(id);
        field.setRule(rule);
        fieldService.save(field);
      }
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void unmap(List<Long> ids) throws Exception {
    try {
      for (Long id : ids) {
        OutputField field = fieldService.get(id);
        field.setRule(null);
        fieldService.save(field);
      }
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public List<OutputFieldModel> refresh(List<Long> ids) throws Exception {
    try {
      List<OutputFieldModel> fields = Lists.newArrayList();
      for (Long id : ids) {
        OutputField domain = fieldService.get(id);
        fields.add(mapper.map(domain, OutputFieldModel.class));
      }
      return fields;
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

}
