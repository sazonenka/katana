package by.sazonenka.katana.xml.service;

import static by.sazonenka.katana.xml.service.ServiceTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Charsets;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

/**
 * @author Aliaksandr Sazonenka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-xml.xml"})
public class XmlPersisterTest {

  @Inject XmlPersister persister;

  @Test
  public void saveToBuffer() throws Exception {
    byte[] existingBuffer = getBufferInClassPath(TEST_CONFIG);
    String expectedContent = new String(existingBuffer, Charsets.UTF_8);

    byte[] targetBuffer = persister.saveToBuffer(createConstraintConfig());
    String actualContent = new String(targetBuffer, Charsets.UTF_8);

    assertThat("The serialized XML differs from the expected.",
        actualContent, is(expectedContent));
  }

  @Test(expected = IllegalArgumentException.class)
  public void saveNullConfigToBuffer() throws Exception {
    persister.saveToBuffer(null);
  }

  @Test
  public void saveToString() throws Exception {
    byte[] existingBuffer = getBufferInClassPath(TEST_CONFIG);
    String expectedContent = new String(existingBuffer, Charsets.UTF_8);

    String actualContent = persister.saveToString(createConstraintConfig());

    assertThat("The serialized XML differs from the expected.",
        actualContent, is(expectedContent));
  }

  @Test(expected = IllegalArgumentException.class)
  public void saveNullConfigToString() throws Exception {
    persister.saveToString(null);
  }

  @Test
  public void loadExistingBuffer() throws Exception {
    byte[] existingBuffer = getBufferInClassPath(TEST_CONFIG);

    ConstraintConfigXml constraintConfig = persister.loadFromBuffer(existingBuffer);
    assertThat("The loaded constraint config differs from the expected.",
        constraintConfig, is(createConstraintConfig()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void loadNullBuffer() throws Exception {
    persister.loadFromBuffer(null);
  }

}
