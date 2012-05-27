package by.sazonenka.katana.persistence.dao;

import static by.sazonenka.katana.persistence.dao.DaoTestData.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class ConstraintConfigDaoTest extends GenericDaoTest {

  @Test
  public void findAllConfigsListSize() {
    int expectedConfigCount = count(DomainMapping.CONSTRAINT_CONFIG);

    List<ConstraintConfig> allConfigs = configDao.findAll();

    assertThat("Got unexpected null while executing the query for ConstraintConfig fetching.",
        allConfigs, notNullValue());
    assertThat("Got unexpected size of a list while executing the query "
        + "for ConstraintConfig fetching.",
        allConfigs.size(), is(expectedConfigCount));
  }

  @Test
  public void findAllConfigsListSortedByModified() {
    List<ConstraintConfig> allConfigs = configDao.findAll();

    List<ConstraintConfig> sortedConfigs = Lists.newArrayList(allConfigs);
    Collections.sort(sortedConfigs, new Comparator<ConstraintConfig>() {
      @Override
      public int compare(ConstraintConfig c1, ConstraintConfig c2) {
        return c2.getModified().compareTo(c1.getModified());
      }
    });

    assertThat("Got unexpected sorting while executing the query for ConstraintConfig fetching.",
        allConfigs, is(sortedConfigs));
  }

  @Test
  public void getCount() {
    int expectedConfigCount = count(DomainMapping.CONSTRAINT_CONFIG);

    long count = configDao.getCount();

    assertThat("Got unexpected count of ConstraintConfigs.",
        count, is((long) expectedConfigCount));
  }

  @Test
  public void getExistingConfig() {
    ConstraintConfig existingConfig = configDao.get(CONFIG_EXIST_ID);

    assertThat("Got unexpected null while loading the existing instance.",
        existingConfig, notNullValue());
    assertThat("Got unexpected 'id' value while loading the existing instance.",
        existingConfig.getId(), is(CONFIG_EXIST_ID));
    assertThat("Got unexpected 'name' value while loading the existing instance.",
        existingConfig.getName(), is(CONFIG_EXIST_NAME));
    assertThat("Got unexpected 'author' value while loading the existing instance.",
        existingConfig.getAuthor(), is(CONFIG_EXIST_AUTHOR));
    assertThat("Got unexpected 'modified' value while loading the existing instance.",
        existingConfig.getModified(), is(CONFIG_EXIST_MODIFIED));
  }

  @Test
  public void getNonExistingConfig() {
    ConstraintConfig nonExistingConfig = configDao.get(CONFIG_NONEXIST_ID);

    assertThat("Got unexpected not-null value while loading a non-existing instance.",
        nonExistingConfig, nullValue());
  }

  @Test
  public void insertConfig() {
    ConstraintConfig configToInsert = new ConstraintConfig(CONFIG_NONEXIST_NAME,
        CONFIG_NONEXIST_AUTHOR, CONFIG_NONEXIST_MODIFIED);

    int sizeBeforeInsert = count(DomainMapping.CONSTRAINT_CONFIG);
    configDao.save(configToInsert);
    configDao.flush();
    int sizeAfterInsert = count(DomainMapping.CONSTRAINT_CONFIG);

    assertThat("A number of persistent instances is supposed "
        + "to increment by one after an insert operation.",
        sizeAfterInsert, is(sizeBeforeInsert + 1));

    ConstraintConfig insertedConfig = get(DomainMapping.CONSTRAINT_CONFIG, configToInsert.getId());

    assertThat("Got unexpected null while loading the inserted instance.",
        insertedConfig, notNullValue());
    assertThat("Got unexpected 'name' value while loading the inserted instance.",
        insertedConfig.getName(), is(CONFIG_NONEXIST_NAME));
    assertThat("Got unexpected 'author' value while loading the inserted instance.",
        insertedConfig.getAuthor(), is(CONFIG_NONEXIST_AUTHOR));
    assertThat("Got unexpected 'modified' value while loading the inserted instance.",
        insertedConfig.getModified(), is(CONFIG_NONEXIST_MODIFIED));
  }

  @Test
  public void updateConfig() {
    ConstraintConfig configToUpdate = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    configToUpdate.setName(CONFIG_NONEXIST_NAME);
    configToUpdate.setAuthor(CONFIG_NONEXIST_AUTHOR);
    configToUpdate.setModified(CONFIG_NONEXIST_MODIFIED);

    int sizeBeforeUpdate = count(DomainMapping.CONSTRAINT_CONFIG);
    configDao.save(configToUpdate);
    configDao.flush();
    int sizeAfterUpdate = count(DomainMapping.CONSTRAINT_CONFIG);

    assertThat("A number of persistent instances is supposed "
        + "to remain the same after an update operation.",
        sizeAfterUpdate, is(sizeBeforeUpdate));

    ConstraintConfig updatedConfig = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);

    assertThat("Got unexpected null while loading the updated instance.",
        updatedConfig, notNullValue());
    assertThat("Got unexpected 'name' value while loading the updated instance.",
        updatedConfig.getName(), is(CONFIG_NONEXIST_NAME));
    assertThat("Got unexpected 'author' value while loading the updated instance.",
        updatedConfig.getAuthor(), is(CONFIG_NONEXIST_AUTHOR));
    assertThat("Got unexpected 'modified' value while loading the updated instance.",
        updatedConfig.getModified(), is(CONFIG_NONEXIST_MODIFIED));
  }

  @Test
  public void incrementVersion() {
    ConstraintConfig configToUpdate = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    configToUpdate.setName(CONFIG_NONEXIST_NAME);

    Integer versionBeforeUpdate = configToUpdate.getVersion();

    configDao.save(configToUpdate);
    configDao.flush();

    ConstraintConfig updatedConfig = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    Integer versionAfterUpdate = updatedConfig.getVersion();

    assertThat("A version of persistent instances is supposed "
        + "to increment by one after an update operation.",
        versionAfterUpdate, is(versionBeforeUpdate + 1));
  }

  @Test
  public void deleteConfig() {
    ConstraintConfig configToDelete = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);

    int sizeBeforeDelete = count(DomainMapping.CONSTRAINT_CONFIG);
    configDao.delete(configToDelete);
    configDao.flush();
    int sizeAfterDelete = count(DomainMapping.CONSTRAINT_CONFIG);

    assertThat("A number of persistent instances is supposed "
        + "to decrement by one after a delete operation.",
        sizeAfterDelete, is(sizeBeforeDelete - 1));

    ConstraintConfig deletedConfig = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);

    assertThat("Got unexpected not-null value while loading the deleted instance.",
        deletedConfig, nullValue());
  }

  @Test
  public void deleteConfigCascadeToRules() {
    ConstraintConfig configToDelete = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);

    List<ValidationRule> rulesBeforeDelete = findByConfig(DomainMapping.VALIDATION_RULE,
        CONFIG_EXIST_ID);

    assertThat("Got unexpected empty list while executing the query "
        + "for fetching ValidationRules by the existing ConstraintConfig.",
        rulesBeforeDelete.size(), not(0));

    configDao.delete(configToDelete);
    configDao.flush();

    for (ValidationRule ruleBeforeDelete : rulesBeforeDelete) {
      ValidationRule ruleAfterDelete = get(DomainMapping.VALIDATION_RULE, ruleBeforeDelete.getId());

      assertThat("Haven't got the expected cascade deleting of the ValidationRule "
          + "associated with the deleted ConstraintConfig.",
          ruleAfterDelete, nullValue());
    }
  }

  @Test
  public void deleteConfigCascadeToFiles() {
    ConstraintConfig configToDelete = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);

    List<OutputFile> filesBeforeDelete = findByConfig(DomainMapping.OUTPUT_FILE,
        CONFIG_EXIST_ID);

    assertThat("Got unexpected empty list while executing the query "
        + "for fetching OutputFiles by the existing ConstraintConfig.",
        filesBeforeDelete.size(), not(0));

    configDao.delete(configToDelete);
    configDao.flush();

    for (OutputFile fileBeforeDelete : filesBeforeDelete) {
      OutputFile fileAfterDelete = get(DomainMapping.OUTPUT_FILE, fileBeforeDelete.getId());

      assertThat("Haven't got the expected cascade deleting of the OutputFile "
          + "associated with the deleted ConstraintConfig.",
          fileAfterDelete, nullValue());
    }
  }

}
