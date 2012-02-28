package by.sazonenka.katana.persistence.dao;

import java.util.List;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputFile;

/**
 * Interface that specifies a set of operations which can be performed on
 * {@link OutputFile} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericDao
 */
public interface OutputFileDao extends GenericDao<OutputFile, Long> {

  /**
   * Returns a list of all {@link OutputFile}s
   * associated with the given {@link ConstraintConfig}.
   */
  List<OutputFile> findByConfig(ConstraintConfig config);

  long getCountByConfig(ConstraintConfig config);

  /**
   * Returns a list of all {@link OutputFile}s
   * associated with the given parent {@link OutputFile}.
   */
  List<OutputFile> findByParent(OutputFile parent);

  OutputFile findByNameAndConfig(String name, ConstraintConfig config);

}
