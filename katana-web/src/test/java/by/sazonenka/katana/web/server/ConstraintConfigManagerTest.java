package by.sazonenka.katana.web.server;

import static by.sazonenka.katana.web.server.ManagerTestData.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import com.google.common.collect.Lists;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.web.client.config.ConfigWarningTracker.Warning;
import by.sazonenka.katana.web.model.ConstraintConfigModel;
import by.sazonenka.katana.xml.service.XmlPersisterException;

/**
 * @author Aliaksandr Sazonenka
 */
public class ConstraintConfigManagerTest extends GenericManagerTest {

  @Test
  public void getAll() throws Exception {
    // Given
    List<ConstraintConfig> expectedConfigs = Lists.newArrayList(getConfig1(),
        getConfig1(), getConfig1());
    List<ConstraintConfigModel> expectedConfigModels = Lists.newArrayList(getConfigModel1(),
        getConfigModel1(), getConfigModel1());
    // Expect
    when(configService.findAll()).thenReturn(expectedConfigs);
    // Run
    List<ConstraintConfigModel> actualConfigModels = configManager.findAll();
    // Verify
    verify(configService).findAll();
    // Assert
    assertThat(actualConfigModels, is(expectedConfigModels));
  }

  @Test
  public void saveNewConfig() throws Exception {
    // Given
    ConstraintConfig newConfig = getNewConfig1();
    ConstraintConfig savedConfig = getConfig1();

    ConstraintConfigModel newConfigModel = getNewConfigModel1();
    ConstraintConfigModel expectedConfigModel = getConfigModel1();
    // Expect
    when(configService.save(newConfig)).thenReturn(savedConfig);
    // Run
    ConstraintConfigModel actualConfigModel = configManager.save(newConfigModel);
    // Verify
    verify(configService).save(newConfig);
    // Assert
    assertThat(actualConfigModel, is(expectedConfigModel));
  }

  @Test
  public void updateConfig() throws Exception {
    // Given
    ConstraintConfig config = getConfig1();

    ConstraintConfigModel configModel = getConfigModel1();
    String expectedConfigName = configModel.getName() + "_updated";
    configModel.setName(expectedConfigName);
    // Expect
    when(configService.get(CONFIG_1_ID)).thenReturn(config);
    when(configService.save(config)).thenReturn(config);
    // Run
    ConstraintConfigModel actualConfigModel = configManager.save(configModel);
    // Verify
    verify(configService).get(CONFIG_1_ID);
    verify(configService).save(config);
    // Assert
    assertThat(actualConfigModel, is(configModel));
    assertThat(actualConfigModel.getName(), is(expectedConfigName));
  }

  @Test
  public void delete() throws Exception {
    // Run
    configManager.delete(CONFIG_1_ID);
    // Verify
    verify(configService).delete(CONFIG_1_ID);
  }

  @Test
  public void loadXml() throws Exception {
    // Given
    String expectedXml = "1 2 3";
    // Expect
    when(configService.saveToString(CONFIG_1_ID)).thenReturn(expectedXml);
    // Run
    String actualXml = configManager.loadXml(CONFIG_1_ID);
    // Verify
    verify(configService).saveToString(CONFIG_1_ID);
    // Assert
    assertThat(actualXml, is(expectedXml));
  }

  @Test
  public void loadXmlWithException() throws Exception {
    // Given
    String expectedXml = "";
    // Expect
    when(configService.saveToString(CONFIG_1_ID)).thenThrow(new XmlPersisterException(""));
    // Run
    String actualXml = configManager.loadXml(CONFIG_1_ID);
    // Verify
    verify(configService).saveToString(CONFIG_1_ID);
    // Assert
    assertThat(actualXml, is(expectedXml));
  }

  @Test
  public void checkConfigForNameDuplicatesWithWarnings() throws Exception {
    // Expect
    when(ruleService.checkRuleNamesForDuplicates(CONFIG_1_ID)).thenReturn(true);
    when(fileService.checkFileNamesForDuplicates(CONFIG_1_ID)).thenReturn(false);
    when(fieldService.checkFieldNamesForDuplicates(CONFIG_1_ID)).thenReturn(true);
    // Run
    List<Warning> warnings = configManager.checkConfigForNameDuplicates(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(ruleService, fileService, fieldService);
    inOrder.verify(ruleService).checkRuleNamesForDuplicates(CONFIG_1_ID);
    inOrder.verify(fileService).checkFileNamesForDuplicates(CONFIG_1_ID);
    inOrder.verify(fieldService).checkFieldNamesForDuplicates(CONFIG_1_ID);
    // Assert
    assertThat(warnings, notNullValue());
    assertThat(warnings.size(), is(2));
    assertThat(warnings.contains(Warning.RULE_NAME_DUPLICATE), is(true));
    assertThat(warnings.contains(Warning.FILE_NAME_DUPLICATE), is(false));
    assertThat(warnings.contains(Warning.FIELD_NAME_DUPLICATE), is(true));
  }

  @Test
  public void checkConfigForNameDuplicatesWithoutWarnings() throws Exception {
    // Expect
    when(ruleService.checkRuleNamesForDuplicates(CONFIG_1_ID)).thenReturn(false);
    when(fileService.checkFileNamesForDuplicates(CONFIG_1_ID)).thenReturn(false);
    when(fieldService.checkFieldNamesForDuplicates(CONFIG_1_ID)).thenReturn(false);
    // Run
    List<Warning> warnings = configManager.checkConfigForNameDuplicates(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(ruleService, fileService, fieldService);
    inOrder.verify(ruleService).checkRuleNamesForDuplicates(CONFIG_1_ID);
    inOrder.verify(fileService).checkFileNamesForDuplicates(CONFIG_1_ID);
    inOrder.verify(fieldService).checkFieldNamesForDuplicates(CONFIG_1_ID);
    // Assert
    assertThat(warnings, notNullValue());
    assertThat(warnings.size(), is(0));
  }

}
