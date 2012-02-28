package by.sazonenka.katana.persistence.dao;

import java.util.List;

import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

/**
 * Interface that specifies a set of operations which can be performed on
 * {@link OutputField} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericDao
 */
public interface OutputFieldDao extends GenericDao<OutputField, Long> {

  /**
   * Returns a list of all {@link OutputField}s
   * associated with the given {@link OutputFile}.
   */
  List<OutputField> findByFile(OutputFile file);

  long getCountByFile(OutputFile file);

  /**
   * Returns a list of all {@link OutputField}s
   * associated with the given {@link ValidationRule}.
   */
  List<OutputField> findByRule(ValidationRule rule);

  OutputField findByNameAndFile(String name, OutputFile file);

}
