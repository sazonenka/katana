package by.sazonenka.katana.web.server;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.sazonenka.katana.persistence.service.ConstraintConfigService;
import by.sazonenka.katana.persistence.service.OutputFieldService;
import by.sazonenka.katana.persistence.service.OutputFileService;
import by.sazonenka.katana.persistence.service.ValidationRuleService;
import by.sazonenka.katana.web.client.managers.ConstraintConfigManager;
import by.sazonenka.katana.web.client.managers.OutputFieldManager;
import by.sazonenka.katana.web.client.managers.OutputFileManager;
import by.sazonenka.katana.web.client.managers.ValidationRuleManager;

/**
 * @author Aliaksandr Sazonenka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-web-test.xml", "/spring/spring-web.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class GenericManagerTest {

  @Inject protected ConstraintConfigService configService;
  @Inject protected ValidationRuleService ruleService;
  @Inject protected OutputFileService fileService;
  @Inject protected OutputFieldService fieldService;

  @Inject protected Mapper mapper;

  @Inject protected ConstraintConfigManager configManager;
  @Inject protected ValidationRuleManager ruleManager;
  @Inject protected OutputFileManager fileManager;
  @Inject protected OutputFieldManager fieldManager;

}
