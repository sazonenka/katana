package by.sazonenka.katana.xml.service;

import static by.sazonenka.katana.xml.service.ServiceTestData.*;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

/**
 * @author Aliaksandr Sazonenka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring-xml.xml" })
public class XmlValidatorTest {

  @Inject XmlPersister persister;
  @Inject XmlValidator validator;

  @Test
  public void validConfigsAreParsedAndValidatedWithoutErrors() throws IOException {
    for (String configPath : VALID_CONFIGS) {
      try {
        byte[] configBuffer = getBufferInClassPath(configPath);
        ConstraintConfigXml config = persister.loadFromBuffer(configBuffer);
        validator.validate(config);
      } catch (XmlPersisterException e) {
        fail(configPath + " is expected to have a valid schema. Got XmlPersisterException instead.");
      } catch (XmlValidatorException e) {
        fail(configPath + " is expected to be a valid config. Got XmlValidatorException instead.");
      }
    }
  }

  @Test
  public void invalidConfigsAreParsedWithErrors() throws IOException {
    for (String configPath : INVALID_BY_SCHEMA_CONFIGS) {
      try {
        byte[] configBuffer = getBufferInClassPath(configPath);
        persister.loadFromBuffer(configBuffer);
        fail(configPath + " is expected to have an invalid schema and cause XmlPersisterException.");
      } catch (XmlPersisterException e) {
        /* Configs with invalid schema should cause XmlPersisterException while parsing. */
      }
    }
  }

  @Test
  public void invalidConfigsAreValidatedWithErrors() throws IOException {
    for (String configPath : INVALID_BY_RULES_CONFIGS) {
      try {
        byte[] configBuffer = getBufferInClassPath(configPath);
        ConstraintConfigXml config = persister.loadFromBuffer(configBuffer);
        validator.validate(config);
        fail(configPath + " is expected to be an invalid config and cause XmlValidatorException.");
      } catch (XmlPersisterException e) {
        fail(configPath + " is expected to have a valid schema. Got XmlPersisterException instead.");
      } catch (XmlValidatorException e) {
        /* Invalid configs should cause XmlValidatorException while validating. */
      }
    }
  }

}
