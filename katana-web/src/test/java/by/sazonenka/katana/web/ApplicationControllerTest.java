package by.sazonenka.katana.web;

import static by.sazonenka.katana.web.server.ManagerTestData.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.net.HttpHeaders;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.service.ConstraintConfigService;
import by.sazonenka.katana.xml.service.XmlPersisterException;
import by.sazonenka.katana.xml.service.XmlValidatorException;

/**
 * @author Aliaksandr Sazonenka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring-web-test.xml", "/spring-web.xml" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationControllerTest {

  @Inject private ConstraintConfigService configService;

  @Inject private ApplicationController controller;

  @Test
  public void goDirectory() {
    // Run
    ModelAndView modelAndView = controller.directory();
    // Assert
    assertThat(modelAndView.isEmpty(), is(true));
  }

  @Test
  public void goConfig() {
    // Expect
    when(configService.get(CONFIG_1_ID)).thenReturn(getConfig1());
    // Run
    ModelAndView modelAndView = controller.config(CONFIG_1_ID);
    // Assert
    assertThat(modelAndView.isEmpty(), is(false));
    assertThat(modelAndView.getViewName(), nullValue());
    assertThat(modelAndView.getModel().size(), is(1));
    assertThat((Long) modelAndView.getModel().get("configId"), is(CONFIG_1_ID));
  }

  @Test
  public void goConfigWithFakeId() {
    // Expect
    when(configService.get(CONFIG_1_ID)).thenReturn(null);
    // Run
    ModelAndView modelAndView = controller.config(CONFIG_1_ID);
    // Assert
    assertThat(modelAndView.isEmpty(), is(false));
    assertThat(modelAndView.getViewName(), is("configNotFound"));
    assertThat(modelAndView.getModel().size(), is(0));
  }

  @Test
  public void goDownload() throws IOException, XmlPersisterException {
    // Given
    ConstraintConfig config = getConfig1();
    byte[] configSource = { 1, 2, 3 };
    MockHttpServletResponse response = new MockHttpServletResponse();
    // Expect
    when(configService.get(CONFIG_1_ID)).thenReturn(config);
    when(configService.saveToBuffer(CONFIG_1_ID)).thenReturn(configSource);
    // Run
    ModelAndView modelAndView = controller.download(CONFIG_1_ID, response);
    // Assert
    assertThat(modelAndView, nullValue());

    assertThat(response.getContentType(), is("text/xml"));
    assertThat(response.getContentLength(), is(3));
    assertThat((String) response.getHeader(HttpHeaders.CONTENT_DISPOSITION),
        is("attachment; filename=\"config-1.xml\""));
    assertThat(response.getContentAsByteArray(), is(configSource));
  }

  @Test
  public void goDownloadWithFakeId() throws IOException, XmlPersisterException {
    // Given
    HttpServletResponse response = new MockHttpServletResponse();
    // Expect
    when(configService.get(CONFIG_1_ID)).thenReturn(null);
    // Run
    ModelAndView modelAndView = controller.download(CONFIG_1_ID, response);
    // Assert
    assertThat(modelAndView.isEmpty(), is(false));
    assertThat(modelAndView.getViewName(), is("configNotFound"));
    assertThat(modelAndView.getModel().size(), is(0));
  }

  @Test
  public void goUpload() throws IOException, XmlPersisterException, XmlValidatorException {
    // Given
    byte[] configSource = { 1, 2, 3 };

    MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
    request.addFile(new MockMultipartFile("file", configSource));

    MockHttpServletResponse response = new MockHttpServletResponse();
    // Run
    ModelAndView modelAndView = controller.upload(request, response);
    // Verify
    ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
    verify(configService).loadFromBuffer(captor.capture());
    // Assert
    assertThat(modelAndView, nullValue());

    assertThat(captor.getValue(), is(configSource));
    assertThat(response.getContentAsString(), is("{\"status\":\"SUCCESS\"}"));
  }

  @Test
  public void goUploadValidationFailed() throws IOException, XmlPersisterException,
      XmlValidatorException {
    // Given
    byte[] configSource = { 1, 2, 3 };

    MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
    request.addFile(new MockMultipartFile("file", configSource));

    MockHttpServletResponse response = new MockHttpServletResponse();
    // Expect
    doThrow(new XmlValidatorException("validation failed"))
        .when(configService).loadFromBuffer(configSource);
    // Run
    ModelAndView modelAndView = controller.upload(request, response);
    // Verify
    ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
    verify(configService).loadFromBuffer(captor.capture());
    // Assert
    assertThat(modelAndView, nullValue());

    assertThat(captor.getValue(), is(configSource));
    assertThat(response.getContentAsString(),
        is("{\"status\":\"FAILURE\",\"description\":\"validation failed\"}"));
  }

  @Test
  public void goError404() {
    // Run
    ModelAndView modelAndView = controller.error404();
    // Assert
    assertThat(modelAndView.isEmpty(), is(true));
  }

  @Test
  public void goError500() {
    // Run
    ModelAndView modelAndView = controller.error500();
    // Assert
    assertThat(modelAndView.isEmpty(), is(true));
  }

}
