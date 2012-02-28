package by.sazonenka.katana.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import by.sazonenka.katana.persistence.service.ConstraintConfigService;
import by.sazonenka.katana.web.model.upload.UploadStatusModel;
import by.sazonenka.katana.xml.service.XmlPersisterException;
import by.sazonenka.katana.xml.service.XmlValidatorException;

import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;

/**
 * @author Aliaksandr Sazonenka
 */
@Controller
public final class ApplicationController {

  private final ConstraintConfigService configService;
  private final Gson gson;

  @Inject
  public ApplicationController(ConstraintConfigService configService, Gson gson) {
    this.configService = Preconditions.checkNotNull(configService);
    this.gson = Preconditions.checkNotNull(gson);
  }

  @RequestMapping(value = "/directory.htm")
  public ModelAndView directory() {
    return new ModelAndView();
  }

  @RequestMapping(value = "/config.htm")
  public ModelAndView config(@RequestParam("id") Long id) {
    if (isConfigExists(id)) {
      ModelAndView mv = new ModelAndView();
      mv.addObject("configId", id);
      return mv;
    } else {
      return new ModelAndView("configNotFound");
    }
  }

  @RequestMapping(value = "/download.htm")
  public ModelAndView download(@RequestParam("id") Long id, HttpServletResponse response)
      throws IOException, XmlPersisterException {
    if (isConfigExists(id)) {
      byte[] configSource = configService.saveToBuffer(id);
      String configName = configService.get(id).getName() + ".xml";

      response.setContentType("text/xml");
      response.setContentLength(configSource.length);
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + configName + "\"");
      FileCopyUtils.copy(configSource, response.getOutputStream());

      return null;
    } else {
      return new ModelAndView("configNotFound");
    }
  }

  @RequestMapping(value = "/upload.htm")
  public ModelAndView upload(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    MultipartFile multipartFile = multipartRequest.getFile("file");

    UploadStatusModel uploadStatus = UploadStatusModel.forSuccess();
    try {
      configService.loadFromBuffer(multipartFile.getBytes());
    } catch (XmlPersisterException e) {
      uploadStatus = UploadStatusModel.forFailure(e.getMessage());
    } catch (XmlValidatorException e) {
      uploadStatus = UploadStatusModel.forFailure(e.getMessage());
    }

    PrintWriter writer = response.getWriter();
    writer.append(gson.toJson(uploadStatus));
    writer.close();

    return null;
  }

  @RequestMapping(value = "/404.htm")
  public ModelAndView error404() {
    return new ModelAndView();
  }

  @RequestMapping(value = "/500.htm")
  public ModelAndView error500() {
    return new ModelAndView();
  }

  private boolean isConfigExists(Long configId) {
    return configService.get(configId) != null;
  }

}
