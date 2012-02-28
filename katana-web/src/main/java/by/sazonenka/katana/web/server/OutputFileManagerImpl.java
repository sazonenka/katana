package by.sazonenka.katana.web.server;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.service.OutputFileService;
import by.sazonenka.katana.web.client.managers.ManagerEndpoints;
import by.sazonenka.katana.web.client.managers.OutputFileManager;
import by.sazonenka.katana.web.model.OutputFileModel;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
@Controller
public final class OutputFileManagerImpl
    extends GenericManagerImpl
    implements OutputFileManager {

  private static final long serialVersionUID = -8734079478609462377L;

  private final OutputFileService fileService;
  private final Mapper mapper;

  @Inject
  public OutputFileManagerImpl(OutputFileService fileService, Mapper mapper) {
    this.fileService = Preconditions.checkNotNull(fileService);
    this.mapper = Preconditions.checkNotNull(mapper);
  }

  @RequestMapping("/" + ManagerEndpoints.OUTPUT_FILE_MANAGER_URI)
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    return super.handleRequest(request, response);
  }

  @Override
  public List<OutputFileModel> findByConfig(Long configId) throws Exception {
    try {
      List<OutputFile> files = fileService.findByConfig(configId);

      return Lists.newArrayList(Lists.transform(files,
          new Function<OutputFile, OutputFileModel>() {
            @Override
            public OutputFileModel apply(OutputFile domain) {
              return mapper.map(domain, OutputFileModel.class);
            }
          }));
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public OutputFileModel save(OutputFileModel model) throws Exception {
    try {
      OutputFile file = null;
      if (model.getId() == null) {
        file = new OutputFile();
      } else {
        file = fileService.get(model.getId());
      }

      mapper.map(model, file);

      file = fileService.save(file);
      return mapper.map(file, OutputFileModel.class);
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void delete(List<Long> ids) throws Exception {
    try {
      for (Long id : ids) {
        fileService.delete(id);
      }
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void reorder(List<Long> ids) throws Exception {
    try {
      for (int i = 0; i < ids.size(); ++i) {
        OutputFile file = fileService.get(ids.get(i));
        file.setOrderInConfig(i);
        fileService.save(file);
      }
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public boolean map(List<Long> ids, Long parentId) throws Exception {
    try {
      OutputFile parent = fileService.get(parentId);
      for (Long id : ids) {
        List<OutputFile> filesThatExtendCurrent = fileService.findByParent(id);
        if (filesThatExtendCurrent == null || filesThatExtendCurrent.isEmpty()) {
          OutputFile file = fileService.get(id);
          file.setParent(parent);
          fileService.save(file);
        } else {
          return false;
        }
      }
      return true;
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public void unmap(List<Long> ids) throws Exception {
    try {
      for (Long id : ids) {
        OutputFile file = fileService.get(id);
        file.setParent(null);
        fileService.save(file);
      }
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

  @Override
  public List<OutputFileModel> refresh(List<Long> ids) throws Exception {
    try {
      List<OutputFileModel> files = Lists.newArrayList();
      for (Long id : ids) {
        OutputFile domain = fileService.get(id);
        files.add(mapper.map(domain, OutputFileModel.class));
      }
      return files;
    } catch (Throwable e) {
      throw wrapUnexpected(e);
    }
  }

}
