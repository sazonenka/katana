package by.sazonenka.katana.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.dao.OutputFileDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.service.OutputFileService;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * This class implements business operations with {@link OutputFile}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Service
public final class OutputFileServiceImpl
    extends GenericServiceImpl<OutputFile, Long, OutputFileDao>
    implements OutputFileService {

  private final ConstraintConfigDao configDao;

  @Inject
  public OutputFileServiceImpl(ConstraintConfigDao configDao, OutputFileDao fileDao) {
    super(fileDao);

    this.configDao = Preconditions.checkNotNull(configDao);
  }

  @Override
  public OutputFile save(OutputFile file) {
    Preconditions.checkNotNull(file, "Got unexpected null 'file' passed to the method.");

    if (file.getId() == null) {
      Long configId = file.getConfig().getId();
      ConstraintConfig config = configDao.get(configId);

      file.setOrderInConfig((int) dao.getCountByConfig(config));
    }

    return dao.save(file);
  }

  @Override
  public void delete(Long fileId) {
    Preconditions.checkNotNull(fileId, "Got unexpected null 'fileId' passed to the method.");

    OutputFile fileToDelete = dao.get(fileId);
    dao.delete(fileToDelete);

    verifyOrderOfFiles(fileToDelete.getConfig());
  }

  private void verifyOrderOfFiles(ConstraintConfig config) {
    List<OutputFile> allFilesByConfig = dao.findByConfig(config);
    for (int i = 0; i < allFilesByConfig.size(); ++i) {
      OutputFile file = allFilesByConfig.get(i);
      if (file.getOrderInConfig() != i) {
        file.setOrderInConfig(i);
        dao.save(file);
      }
    }
  }

  @Override
  public List<OutputFile> findByConfig(Long configId) {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    return dao.findByConfig(configDao.get(configId));
  }

  @Override
  public List<OutputFile> findByParent(Long parentId) {
    Preconditions.checkNotNull(parentId, "Got unexpected null 'parentId' passed to the method.");

    return dao.findByParent(dao.get(parentId));
  }

  @Override
  public boolean checkFileNamesForDuplicates(Long configId) {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    List<OutputFile> files = dao.findByConfig(configDao.get(configId));
    try {
      Maps.uniqueIndex(files, new Function<OutputFile, String>() {
        @Override
        public String apply(OutputFile file) {
          return file.getName();
        }
      });
    } catch (IllegalArgumentException e) {
      return true;
    }
    return false;
  }

}
