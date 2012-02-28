package by.sazonenka.katana.persistence.service;

import java.util.List;

import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

/**
 * Interface that specifies a set of business operations
 * which can be performed on {@link OutputField} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericService
 */
public interface OutputFieldService extends GenericService<OutputField, Long> {

  /**
   * Returns a list of all {@link OutputField}s
   * associated with a {@link OutputFile} with the given <code>fileId</code>.
   */
  List<OutputField> findByFile(Long fileId);

  /**
   * Returns a list of all {@link OutputField}s
   * associated with a {@link ValidationRule} with the given <code>ruleId</code>.
   */
  List<OutputField> findByRule(Long ruleId);

  boolean checkFieldNamesForDuplicates(Long configId);

}
