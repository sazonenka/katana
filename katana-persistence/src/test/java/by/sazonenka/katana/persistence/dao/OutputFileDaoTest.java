package by.sazonenka.katana.persistence.dao;

import static by.sazonenka.katana.persistence.dao.DaoTestData.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFileDaoTest extends GenericDaoTest {

  @Test
  public void findFilesByConfigSize() {
    int expectedFileCount = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();

    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    List<OutputFile> filesByConfig = fileDao.findByConfig(config);

    assertThat("Got unexpected null while executing the query for OutputFiles fetching.",
        filesByConfig, notNullValue());
    assertThat("Got unexpected size of a list while executing the query "
        + "for OutputFiles fetching.",
        filesByConfig.size(), is(expectedFileCount));
  }

  @Test
  public void findFilesByConfigSortedByOrder() {
    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    List<OutputFile> filesByConfig = fileDao.findByConfig(config);

    List<OutputFile> sortedFiles = Lists.newArrayList(filesByConfig);
    Collections.sort(sortedFiles, new Comparator<OutputFile>() {
      @Override
      public int compare(OutputFile o1, OutputFile o2) {
        return o1.getOrderInConfig() - o2.getOrderInConfig();
      }
    });

    assertThat("Got unexpected sorting while executing the query for OutputFiles fetching.",
        filesByConfig, is(sortedFiles));
  }

  @Test
  public void getCountByConfig() {
    int expectedFileCount = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();

    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    long count = fileDao.getCountByConfig(config);

    assertThat("Got unexpected count of OutputFiles.",
        count, is((long) expectedFileCount));
  }

  @Test
  public void findFilesByParentSize() {
    int expectedFileCount = findByParent(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID).size();

    OutputFile parent = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    List<OutputFile> filesByParent = fileDao.findByParent(parent);

    assertThat("Got unexpected null while executing the query for child OutputFiles fetching.",
        filesByParent, notNullValue());
    assertThat("Got unexpected size of a list while executing the query "
        + "for child OutputFiles fetching.",
        filesByParent.size(), is(expectedFileCount));
  }

  @Test
  public void findFilesByParentSortedByOrder() {
    OutputFile parent = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    List<OutputFile> filesByParent = fileDao.findByParent(parent);

    List<OutputFile> sortedFiles = Lists.newArrayList(filesByParent);
    Collections.sort(sortedFiles, new Comparator<OutputFile>() {
      @Override
      public int compare(OutputFile o1, OutputFile o2) {
        return o1.getOrderInConfig() - o2.getOrderInConfig();
      }
    });

    assertThat("Got unexpected sorting while executing the query for child OutputFiles fetching.",
        filesByParent, is(sortedFiles));
  }

  @Test
  public void findFileByNameAndConfig() {
    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);

    OutputFile existingFile = fileDao.findByNameAndConfig(FILE_EXIST_NAME, config);

    assertThat("Got unexpected null while executing the query for OutputFile fetching.",
        existingFile, notNullValue());
    assertThat("Got unexpected id of a OutputFile found by name.",
        existingFile.getId(), is(FILE_EXIST_ID));

    OutputFile nonExistingFile = fileDao.findByNameAndConfig(FILE_NONEXIST_NAME, config);

    assertThat("Got unexpected not-null while executing the query for OutputFile fetching.",
        nonExistingFile, nullValue());
  }

  @Test
  public void getExistingFile() {
    OutputFile existingFile = fileDao.get(FILE_EXIST_ID);

    assertThat("Got unexpected null while loading the existing instance.",
        existingFile, notNullValue());
    assertThat("Got unexpected 'id' value while loading the existing instance.",
        existingFile.getId(), is(FILE_EXIST_ID));
    assertThat("Got unexpected 'name' value while loading the existing instance.",
        existingFile.getName(), is(FILE_EXIST_NAME));
    assertThat("Got unexpected 'orderInConfig' value while loading the existing instance.",
        existingFile.getOrderInConfig(), is(FILE_EXIST_ORDER));
    assertThat("Got unexpected 'parent' value while loading the existing instance.",
        existingFile.getParent(), nullValue());
    assertThat("Got unexpected 'config.id' value while loading the existing instance.",
        existingFile.getConfig().getId(), is(CONFIG_EXIST_ID));
  }

  @Test
  public void getNonExistingFile() {
    OutputFile nonExistingFile = fileDao.get(FILE_NONEXIST_ID);

    assertThat("Got unexpected not-null value while loading a non-existing instance.",
        nonExistingFile, nullValue());
  }

  @Test
  public void insertFile() {
    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    OutputFile fileToInsert = new OutputFile(FILE_NONEXIST_NAME, FILE_NONEXIST_ORDER, null, config);

    int sizeBeforeInsert = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();
    fileDao.save(fileToInsert);
    fileDao.flush();
    int sizeAfterInsert = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to increment by one after an insert operation.",
        sizeAfterInsert, is(sizeBeforeInsert + 1));

    OutputFile insertedFile = get(DomainMapping.OUTPUT_FILE, fileToInsert.getId());

    assertThat("Got unexpected null while loading the inserted instance.",
        insertedFile, notNullValue());
    assertThat("Got unexpected 'name' value while loading the inserted instance.",
        insertedFile.getName(), is(FILE_NONEXIST_NAME));
    assertThat("Got unexpected 'orderInConfig' value while loading the inserted instance.",
        insertedFile.getOrderInConfig(), is(FILE_NONEXIST_ORDER));
    assertThat("Got unexpected 'parent' value while loading the inserted instance.",
        insertedFile.getParent(), nullValue());
    assertThat("Got unexpected 'config.id' value while loading the inserted instance.",
        insertedFile.getConfig().getId(), is(CONFIG_EXIST_ID));
  }

  @Test
  public void updateFile() {
    OutputFile fileToUpdate = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    fileToUpdate.setName(FILE_NONEXIST_NAME);
    fileToUpdate.setOrderInConfig(FILE_NONEXIST_ORDER);

    int sizeBeforeUpdate = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();
    fileDao.save(fileToUpdate);
    fileDao.flush();
    int sizeAfterUpdate = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to remain the same after an update operation.",
        sizeAfterUpdate, is(sizeBeforeUpdate));

    OutputFile updatedFile = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);

    assertThat("Got unexpected null while loading the updated instance.",
        updatedFile, notNullValue());
    assertThat("Got unexpected 'name' value while loading the updated instance.",
        updatedFile.getName(), is(FILE_NONEXIST_NAME));
    assertThat("Got unexpected 'orderInConfig' value while loading the updated instance.",
        updatedFile.getOrderInConfig(), is(FILE_NONEXIST_ORDER));
  }

  @Test
  public void incrementVersion() {
    OutputFile fileToUpdate = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    fileToUpdate.setName(FILE_NONEXIST_NAME);

    Integer versionBeforeUpdate = fileToUpdate.getVersion();

    fileDao.save(fileToUpdate);
    fileDao.flush();

    OutputFile updatedFile = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);
    Integer versionAfterUpdate = updatedFile.getVersion();

    assertThat("A version of persistent instances is supposed "
        + "to increment by one after an update operation.",
        versionAfterUpdate, is(versionBeforeUpdate + 1));
  }

  @Test
  public void deleteFile() {
    OutputFile fileToDelete = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);

    int sizeBeforeDelete = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();
    fileDao.delete(fileToDelete);
    fileDao.flush();
    int sizeAfterDelete = findByConfig(DomainMapping.OUTPUT_FILE, CONFIG_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to decrement by one after a delete operation.",
        sizeAfterDelete, is(sizeBeforeDelete - 1));

    OutputFile deletedFile = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);

    assertThat("Got unexpected not-null value while loading the deleted instance.",
        deletedFile, nullValue());
  }

  @Test
  public void deleteFileSetNullOnChildren() {
    OutputFile fileToDelete = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);

    List<OutputFile> childrenBeforeDelete = findByParent(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);

    assertThat("Got unexpected empty list while executing the query "
        + "for fetching child OutputFiles by the existing parent OutputFile.",
        childrenBeforeDelete.size(), not(0));

    fileDao.delete(fileToDelete);
    fileDao.flush();

    for (OutputFile childBeforeDelete : childrenBeforeDelete) {
      OutputFile childAfterDelete = get(DomainMapping.OUTPUT_FILE, childBeforeDelete.getId());

      assertThat("Got unexpected cascade deleting of the child OutputFile "
          + "associated with the deleted parent OutputFile.",
          childAfterDelete, notNullValue());
      assertThat("Got unexpected not-null association "
          + "between the child OutputFile and the deleted parent OutputFile.",
          childAfterDelete.getParent(), nullValue());
    }
  }

  @Test
  public void deleteFileCascadeToFields() {
    OutputFile fileToDelete = get(DomainMapping.OUTPUT_FILE, FILE_EXIST_ID);

    List<OutputField> fieldsBeforeDelete = findByFile(DomainMapping.OUTPUT_FIELD, FILE_EXIST_ID);

    assertThat("Got unexpected empty list while executing the query "
        + "for fetching OutputFields by the existing OutputFile.",
        fieldsBeforeDelete.size(), not(0));

    fileDao.delete(fileToDelete);
    fileDao.flush();

    for (OutputField fieldBeforeDelete : fieldsBeforeDelete) {
      OutputField fieldAfterDelete = get(DomainMapping.OUTPUT_FIELD, fieldBeforeDelete.getId());

      assertThat("Haven't got the expected cascade deleting of the OutputField "
          + "associated with the deleted OutputFile.",
          fieldAfterDelete, nullValue());
    }
  }

}
