package by.sazonenka.katana.web.server;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.persistence.service.ValidationRuleService;
import by.sazonenka.katana.web.client.managers.ManagerEndpoints;
import by.sazonenka.katana.web.client.managers.ValidationRuleManager;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
@Controller
public final class ValidationRuleManagerImpl
    extends GenericManagerImpl
    implements ValidationRuleManager {

  private static final long serialVersionUID = 264480728371943499L;

  private final ValidationRuleService ruleService;
  private final Mapper mapper;

  @Inject
  public ValidationRuleManagerImpl(ValidationRuleService ruleService, Mapper mapper) {
    this.ruleService = Preconditions.checkNotNull(ruleService);
    this.mapper = Preconditions.checkNotNull(mapper);
  }

  @RequestMapping("/" + ManagerEndpoints.VALIDATION_RULE_MANAGER_URI)
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    return super.handleRequest(request, response);
  }

  @Override
  public List<ValidationRuleModel> findByConfig(Long configId) throws Exception {
    try {
      List<ValidationRule> rules = ruleService.findByConfig(configId);

      return Lists.newArrayList(Lists.transform(rules,
          new Function<ValidationRule, ValidationRuleModel>() {
            @Override
            public ValidationRuleModel apply(ValidationRule domain) {
              return mapper.map(domain, ValidationRuleModel.class);
            }
          }));
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public ValidationRuleModel save(ValidationRuleModel model) throws Exception {
    try {
      ValidationRule rule = null;
      if (model.getId() == null) {
        rule = new ValidationRule();
      } else {
        rule = ruleService.get(model.getId());
      }

      mapper.map(model, rule);

      rule = ruleService.save(rule);
      return mapper.map(rule, ValidationRuleModel.class);
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void swap(Long ruleId1, Long ruleId2) throws Exception {
    try {
      ValidationRule rule1 = ruleService.get(ruleId1);
      int orderOfRule1 = rule1.getOrderInConfig();

      ValidationRule rule2 = ruleService.get(ruleId2);
      int orderOfRule2 = rule2.getOrderInConfig();

      rule1.setOrderInConfig(orderOfRule2);
      ruleService.save(rule1);

      rule2.setOrderInConfig(orderOfRule1);
      ruleService.save(rule2);
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void delete(Long id) throws Exception {
    try {
      ruleService.delete(id);
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

}
