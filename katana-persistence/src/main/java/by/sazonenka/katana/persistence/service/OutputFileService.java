package by.sazonenka.katana.persistence.service;

import java.util.List;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputFile;

/**
 * Interface that specifies a set of business operations
 * which can be performed on {@link OutputFile} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericService
 */
public interface OutputFileService extends GenericService<OutputFile, Long> {

  /**
   * Returns a list of all {@link OutputFile}s
   * associated with a {@link ConstraintConfig} with the given <code>configId</code>.
   */
  List<OutputFile> findByConfig(Long configId);

  /**
   * Returns a list of all {@link OutputFile}s
   * associated with a parent {@link OutputFile} with the given <code>parentId</code>.
   */
  List<OutputFile> findByParent(Long parentId);

  boolean checkFileNamesForDuplicates(Long configId);

}
