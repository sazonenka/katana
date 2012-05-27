package by.sazonenka.katana.persistence.service;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.sazonenka.katana.persistence.converter.XmlConverter;
import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.dao.OutputFieldDao;
import by.sazonenka.katana.persistence.dao.OutputFileDao;
import by.sazonenka.katana.persistence.dao.ValidationRuleDao;
import by.sazonenka.katana.xml.service.XmlPersister;
import by.sazonenka.katana.xml.service.XmlValidator;

/**
 * @author Aliaksandr Sazonenka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-service-test.xml", "/spring/spring-service.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class GenericServiceTest {

  @Inject protected ConstraintConfigDao configDao;
  @Inject protected ValidationRuleDao ruleDao;
  @Inject protected OutputFileDao fileDao;
  @Inject protected OutputFieldDao fieldDao;
  @Inject protected XmlConverter xmlConverter;
  @Inject protected XmlPersister xmlPersister;
  @Inject protected XmlValidator xmlValidator;

  @Inject protected ConstraintConfigService configService;
  @Inject protected ValidationRuleService ruleService;
  @Inject protected OutputFileService fileService;
  @Inject protected OutputFieldService fieldService;

}
