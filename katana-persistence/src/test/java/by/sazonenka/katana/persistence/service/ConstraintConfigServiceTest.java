package by.sazonenka.katana.persistence.service;

import static by.sazonenka.katana.persistence.converter.XmlConverterTestData.createConfigXml;
import static by.sazonenka.katana.persistence.service.ServiceTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.xml.domain.ConstraintConfigXml;
import by.sazonenka.katana.xml.service.XmlPersisterException;
import by.sazonenka.katana.xml.service.XmlValidatorException;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class ConstraintConfigServiceTest extends GenericServiceTest {

  @Test
  public void get() {
    // Given
    ConstraintConfig config = getConfig1();
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    // Run
    ConstraintConfig actualConfig = configService.get(CONFIG_1_ID);
    // Verify
    verify(configDao).get(CONFIG_1_ID);
    // Assert
    assertThat(actualConfig, is(config));
  }

  @Test
  public void save() {
    // Given
    ConstraintConfig config = getConfig1();
    // Expect
    when(configDao.save(config)).thenReturn(config);
    // Run
    ConstraintConfig savedConfig = configService.save(config);
    // Verify
    verify(configDao).save(config);
    // Assert
    assertThat(savedConfig, is(config));
  }

  @Test
  public void delete() {
    // Given
    ConstraintConfig config = getConfig1();
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    // Run
    configService.delete(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(configDao).delete(config);
  }

  @Test
  public void findAllConfigs() {
    // Given
    List<ConstraintConfig> configs = Lists.newArrayList(getConfig1(), getConfig1(), getConfig1());
    // Expect
    when(configDao.findAll()).thenReturn(configs);
    // Run
    List<ConstraintConfig> actualConfigs = configService.findAll();
    // Verify
    verify(configDao).findAll();
    // Assert
    assertThat(actualConfigs, is(configs));
  }

  @Test
  public void getConfigCount() {
    // Expect
    when(configDao.getCount()).thenReturn(CONFIGS_COUNT);
    // Run
    long actualCount = configService.getCount();
    // Verify
    verify(configDao).getCount();
    // Assert
    assertThat(actualCount, is(CONFIGS_COUNT));
  }

  @Test
  public void saveToBuffer() throws XmlPersisterException {
    // Given
    ConstraintConfigXml configXml = createConfigXml();
    byte[] expectedBuffer = {1, 2, 3};
    // Expect
    when(xmlConverter.loadConfigXml(CONFIG_1_ID)).thenReturn(configXml);
    when(xmlPersister.saveToBuffer(configXml)).thenReturn(expectedBuffer);
    // Run
    byte[] actualBuffer = configService.saveToBuffer(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(xmlConverter, xmlPersister);
    inOrder.verify(xmlConverter).loadConfigXml(CONFIG_1_ID);
    inOrder.verify(xmlPersister).saveToBuffer(configXml);
    // Assert
    assertThat(actualBuffer, is(expectedBuffer));
  }

  @Test
  public void saveToString() throws XmlPersisterException {
    // Given
    ConstraintConfigXml configXml = createConfigXml();
    String expectedString = "1 2 3";
    // Expect
    when(xmlConverter.loadConfigXml(CONFIG_1_ID)).thenReturn(configXml);
    when(xmlPersister.saveToString(configXml)).thenReturn(expectedString);
    // Run
    String actualString = configService.saveToString(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(xmlConverter, xmlPersister);
    inOrder.verify(xmlConverter).loadConfigXml(CONFIG_1_ID);
    inOrder.verify(xmlPersister).saveToString(configXml);
    // Assert
    assertThat(actualString, is(expectedString));
  }

  @Test
  public void loadFromBuffer() throws XmlPersisterException, XmlValidatorException {
    // Given
    ConstraintConfigXml configXml = createConfigXml();
    byte[] bufferToLoad = {1, 2, 3};
    // Expect
    when(xmlPersister.loadFromBuffer(bufferToLoad)).thenReturn(configXml);
    // Run
    configService.loadFromBuffer(bufferToLoad);
    // Verify
    InOrder inOrder = inOrder(xmlConverter, xmlPersister, xmlValidator);
    inOrder.verify(xmlPersister).loadFromBuffer(bufferToLoad);
    inOrder.verify(xmlValidator).validate(configXml);
    inOrder.verify(xmlConverter).saveConfigXml(configXml);
  }

}
