package by.sazonenka.katana.persistence.converter;

import static by.sazonenka.katana.persistence.converter.XmlConverterTestData.createConfigXml;
import static by.sazonenka.katana.persistence.service.ServiceTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.dao.OutputFieldDao;
import by.sazonenka.katana.persistence.dao.OutputFileDao;
import by.sazonenka.katana.persistence.dao.ValidationRuleDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

/**
 * @author Aliaksandr Sazonenka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-converter-test.xml", "/spring/spring-converter.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class XmlConverterTest {

  @Inject private ConstraintConfigDao configDao;
  @Inject private ValidationRuleDao ruleDao;
  @Inject private OutputFileDao fileDao;
  @Inject private OutputFieldDao fieldDao;

  @Inject private XmlConverter xmlConverter;

  @Test
  public void testLoadConfigXml() {
    // Given
    ConstraintConfigXml configXml = createConfigXml();
    ConstraintConfig config = getConfig1();

    List<ValidationRule> rules = Lists.newArrayList(getRule1());

    OutputFile file1 = getFile1();
    file1.setId(FILE_1_ID);
    OutputFile file2 = getFile2();
    file2.setId(FILE_2_ID);
    List<OutputFile> files = Lists.newArrayList(file1, file2);

    List<OutputField> fieldsInFile1 = Lists.newArrayList(getField1());
    List<OutputField> fieldsInFile2 = Lists.newArrayList(getField2());
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(ruleDao.findByConfig(config)).thenReturn(rules);
    when(fileDao.findByConfig(config)).thenReturn(files);
    when(fieldDao.findByFile(file1)).thenReturn(fieldsInFile1);
    when(fieldDao.findByFile(file2)).thenReturn(fieldsInFile2);
    // Run
    ConstraintConfigXml actualConfig = xmlConverter.loadConfigXml(CONFIG_1_ID);
    // Assert
    assertThat(actualConfig, is(configXml));
  }

  @Test
  public void testSaveConfigXml() {
    // Given
    ConstraintConfigXml configXml = createConfigXml();
    ConstraintConfig config = getConfig1();

    ValidationRule rule1 = getRule1();

    OutputFile file1 = getFile1();
    OutputFile file2 = getFile2();

    OutputField field1 = getField1();
    OutputField field2 = getField2();
    // Expect
    when(configDao.save(config)).thenReturn(config);
    when(ruleDao.findByNameAndConfig(rule1.getName(), config)).thenReturn(rule1);
    when(fileDao.findByNameAndConfig(file1.getName(), config)).thenReturn(file1);
    when(fileDao.findByNameAndConfig(file2.getName(), config)).thenReturn(file2);
    // Run
    xmlConverter.saveConfigXml(configXml);
    // Verify
    InOrder inOrder = inOrder(configDao, ruleDao, fileDao, fieldDao);
    inOrder.verify(configDao).save(config);
    inOrder.verify(ruleDao).save(rule1);
    inOrder.verify(fileDao).save(file1);
    inOrder.verify(fileDao).save(file2);
    inOrder.verify(fieldDao).save(field1);
    inOrder.verify(fieldDao).save(field2);
  }

}
