package by.sazonenka.katana.persistence.dao;

import static by.sazonenka.katana.persistence.dao.DaoTestData.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFieldDaoTest extends GenericDaoTest {

  @Test
  public void findFieldsByFileSize() {
    int expectedFieldCount = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();

    OutputFile file = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    List<OutputField> fieldsByFile = fieldDao.findByFile(file);

    assertThat("Got unexpected null while executing the query for OutputFields fetching.",
        fieldsByFile, notNullValue());
    assertThat("Got unexpected size of a list while executing the query "
        + "for OutputFields fetching.",
        fieldsByFile.size(), is(expectedFieldCount));
  }

  @Test
  public void findFieldsByFileSortedByOrder() {
    OutputFile file = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    List<OutputField> fieldsByFile = fieldDao.findByFile(file);

    List<OutputField> sortedFields = Lists.newArrayList(fieldsByFile);
    Collections.sort(sortedFields, new Comparator<OutputField>() {
      @Override
      public int compare(OutputField o1, OutputField o2) {
        return o1.getOrderInFile() - o2.getOrderInFile();
      }
    });

    assertThat("Got unexpected sorting while executing the query for OutputFields fetching.",
        fieldsByFile, is(sortedFields));
  }

  @Test
  public void getCountByFile() {
    int expectedFieldCount = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();

    OutputFile file = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    long count = fieldDao.getCountByFile(file);

    assertThat("Got unexpected count of OutputFields.",
        count, is((long) expectedFieldCount));
  }

  @Test
  public void findFieldsByRuleSize() {
    int expectedFieldCount = findByRule(DomainMapping.OUTPUT_FIELD, RULE_EXIST_ID).size();

    ValidationRule rule = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);
    List<OutputField> fieldsByRule = fieldDao.findByRule(rule);

    assertThat("Got unexpected null while executing the query for OutputFields fetching.",
        fieldsByRule, notNullValue());
    assertThat("Got unexpected size of a list while executing the query "
        + "for OutputFields fetching.",
        fieldsByRule.size(), is(expectedFieldCount));
  }

  @Test
  public void findFieldByNameAndFile() {
    OutputFile file = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);

    OutputField existingField = fieldDao.findByNameAndFile(FIELD_EXIST_NAME, file);

    assertThat("Got unexpected null while executing the query for OutputField fetching.",
        existingField, notNullValue());
    assertThat("Got unexpected id of a OutputField found by name.",
        existingField.getId(), is(FIELD_EXIST_ID));

    OutputField nonExistingField = fieldDao.findByNameAndFile(FIELD_NONEXIST_NAME, file);

    assertThat("Got unexpected not-null while executing the query for OutputField fetching.",
        nonExistingField, nullValue());
  }

  @Test
  public void getExistingField() {
    OutputField existingField = fieldDao.get(FIELD_EXIST_ID);

    assertThat("Got unexpected null while loading the existing instance.",
        existingField, notNullValue());
    assertThat("Got unexpected 'id' value while loading the existing instance.",
        existingField.getId(), is(FIELD_EXIST_ID));
    assertThat("Got unexpected 'name' value while loading the existing instance.",
        existingField.getName(), is(FIELD_EXIST_NAME));
    assertThat("Got unexpected 'orderInFile' value while loading the existing instance.",
        existingField.getOrderInFile(), is(FIELD_EXIST_ORDER));
    assertThat("Got unexpected 'file.id' value while loading the existing instance.",
        existingField.getFile().getId(), is(FILE_EXIST_ID));
    assertThat("Got unexpected 'rule.id' value while loading the existing instance.",
        existingField.getRule().getId(), is(RULE_EXIST_ID));
  }

  @Test
  public void getNonExistingField() {
    OutputField nonExistingField = fieldDao.get(FIELD_NONEXIST_ID);

    assertThat("Got unexpected not-null value while loading a non-existing instance.",
        nonExistingField, nullValue());
  }

  @Test
  public void insertField() {
    OutputFile file = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    ValidationRule rule = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);
    OutputField fieldToInsert = new OutputField(FIELD_NONEXIST_NAME, FIELD_NONEXIST_ORDER,
        file, rule);

    int sizeByFileBeforeInsert = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();
    fieldDao.save(fieldToInsert);
    fieldDao.flush();
    int sizeByFileAfterInsert = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to increment by one after an insert operation.",
        sizeByFileAfterInsert, is(sizeByFileBeforeInsert + 1));

    OutputField insertedField = get(DomainMapping.OUTPUT_FIELD, fieldToInsert.getId());

    assertThat("Got unexpected null while loading the inserted instance.",
        insertedField, notNullValue());
    assertThat("Got unexpected 'name' value while loading the inserted instance.",
        insertedField.getName(), is(FIELD_NONEXIST_NAME));
    assertThat("Got unexpected 'orderInFile' value while loading the inserted instance.",
        insertedField.getOrderInFile(), is(FIELD_NONEXIST_ORDER));
    assertThat("Got unexpected 'file.id' value while loading the existing instance.",
        insertedField.getFile().getId(), is(FILE_EXIST_ID));
    assertThat("Got unexpected 'rule.id' value while loading the existing instance.",
        insertedField.getRule().getId(), is(RULE_EXIST_ID));
  }

  @Test
  public void updateField() {
    OutputField fieldToUpdate = get(DomainMapping.OUTPUT_FIELD, FIELD_EXIST_ID);
    fieldToUpdate.setName(FIELD_NONEXIST_NAME);
    fieldToUpdate.setOrderInFile(FIELD_NONEXIST_ORDER);

    int sizeByFileBeforeUpdate = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();
    fieldDao.save(fieldToUpdate);
    fieldDao.flush();
    int sizeByFileAfterUpdate = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to remain the same after an update operation.",
        sizeByFileAfterUpdate, is(sizeByFileBeforeUpdate));

    OutputField updatedField = get(DomainMapping.OUTPUT_FIELD, FIELD_EXIST_ID);

    assertThat("Got unexpected null while loading the updated instance.",
        updatedField, notNullValue());
    assertThat("Got unexpected 'name' value while loading the updated instance.",
        updatedField.getName(), is(FIELD_NONEXIST_NAME));
    assertThat("Got unexpected 'orderInFile' value while loading the updated instance.",
        updatedField.getOrderInFile(), is(FIELD_NONEXIST_ORDER));
  }

  @Test
  public void incrementVersion() {
    OutputField fieldToUpdate = get(DomainMapping.OUTPUT_FIELD, FIELD_EXIST_ID);
    fieldToUpdate.setName(FIELD_NONEXIST_NAME);

    Integer versionBeforeUpdate = fieldToUpdate.getVersion();

    fieldDao.save(fieldToUpdate);
    fieldDao.flush();

    OutputField updatedField = get(DomainMapping.OUTPUT_FIELD, FIELD_EXIST_ID);
    Integer versionAfterUpdate = updatedField.getVersion();

    assertThat("A version of persistent instances is supposed "
        + "to increment by one after an update operation.",
        versionAfterUpdate, is(versionBeforeUpdate + 1));
  }

  @Test
  public void deleteField() {
    OutputField fieldToDelete = fieldDao.get(FIELD_EXIST_ID);

    int sizeByFileBeforeDelete = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();
    fieldDao.delete(fieldToDelete);
    fieldDao.flush();
    int sizeByFileAfterDelete = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to decrement by one after a delete operation.",
        sizeByFileAfterDelete, is(sizeByFileBeforeDelete - 1));

    OutputField deletedField = fieldDao.get(FIELD_EXIST_ID);

    assertThat("Got unexpected not-null value while loading the deleted instance.",
        deletedField, nullValue());
  }

}
