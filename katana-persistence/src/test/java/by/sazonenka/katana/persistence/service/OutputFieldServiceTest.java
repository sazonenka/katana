package by.sazonenka.katana.persistence.service;

import static by.sazonenka.katana.persistence.service.ServiceTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFieldServiceTest extends GenericServiceTest {

  @Test
  public void testGet() {
    // Given
    OutputField field = getField1();
    // Expect
    when(fieldDao.get(FIELD_1_ID)).thenReturn(field);
    // Run
    OutputField actualField = fieldService.get(FIELD_1_ID);
    // Verify
    verify(fieldDao).get(FIELD_1_ID);
    // Assert
    assertThat(actualField, is(field));
  }

  @Test
  public void testSaveNewField() {
    // Given
    OutputField field = getField1();
    OutputFile file = field.getFile();
    file.setId(FILE_1_ID);
    // Expect
    when(fileDao.get(FILE_1_ID)).thenReturn(file);
    when(fieldDao.getCountByFile(file)).thenReturn(FIELDS_IN_FILE_1);
    when(fieldDao.save(field)).thenReturn(field);
    // Run
    OutputField savedField = fieldService.save(field);
    // Verify
    InOrder inOrder = inOrder(fileDao, fieldDao);
    inOrder.verify(fileDao).get(FILE_1_ID);
    inOrder.verify(fieldDao).getCountByFile(file);
    inOrder.verify(fieldDao).save(field);
    // Assert
    assertThat(savedField, is(field));
    assertThat(savedField.getOrderInFile(), is(FIELDS_IN_FILE_1.intValue()));
  }

  @Test
  public void testSaveExistingField() {
    // Given
    OutputField field = getField1();
    field.setId(FIELD_1_ID);
    // Expect
    when(fieldDao.save(field)).thenReturn(field);
    // Run
    OutputField savedField = fieldService.save(field);
    // Verify
    InOrder inOrder = inOrder(fileDao, fieldDao);
    inOrder.verify(fileDao, never()).get(any(Long.class));
    inOrder.verify(fieldDao, never()).getCountByFile(any(OutputFile.class));
    inOrder.verify(fieldDao).save(field);
    // Assert
    assertThat(savedField, is(field));
  }

  @Test
  public void testDelete() {
    // Given
    OutputField fieldToDelete = getField1();
    OutputFile file = fieldToDelete.getFile();

    OutputField field1 = getField1();
    field1.setOrderInFile(0);
    OutputField field2 = getField1();
    field2.setOrderInFile(2);
    List<OutputField> fields = Lists.newArrayList(field1, field2);
    // Expect
    when(fieldDao.get(FIELD_1_ID)).thenReturn(fieldToDelete);
    when(fieldDao.findByFile(file)).thenReturn(fields);
    // Run
    fieldService.delete(FIELD_1_ID);
    // Verify
    InOrder inOrder = inOrder(fieldDao);
    inOrder.verify(fieldDao).get(FIELD_1_ID);
    inOrder.verify(fieldDao).delete(fieldToDelete);

    inOrder.verify(fieldDao).findByFile(file);
    inOrder.verify(fieldDao).save(field2);
    // Assert
    assertThat(field2.getOrderInFile(), is(1));
  }

  @Test
  public void testFindByFile() {
    // Given
    OutputFile file = getFile1();
    List<OutputField> fields = Lists.newArrayList(getField1(), getField1(), getField1());
    // Expect
    when(fileDao.get(FILE_1_ID)).thenReturn(file);
    when(fieldDao.findByFile(file)).thenReturn(fields);
    // Run
    List<OutputField> actualFields = fieldService.findByFile(FILE_1_ID);
    // Verify
    InOrder inOrder = inOrder(fileDao, fieldDao);
    inOrder.verify(fileDao).get(FILE_1_ID);
    inOrder.verify(fieldDao).findByFile(file);
    // Assert
    assertThat(actualFields, is(fields));
  }

  @Test
  public void testFindByRule() {
    // Given
    ValidationRule rule = getRule1();
    List<OutputField> fields = Lists.newArrayList(getField1(), getField1(), getField1());
    // Expect
    when(ruleDao.get(RULE_1_ID)).thenReturn(rule);
    when(fieldDao.findByRule(rule)).thenReturn(fields);
    // Run
    List<OutputField> actualFields = fieldService.findByRule(RULE_1_ID);
    // Verify
    InOrder inOrder = inOrder(ruleDao, fieldDao);
    inOrder.verify(ruleDao).get(RULE_1_ID);
    inOrder.verify(fieldDao).findByRule(rule);
    // Assert
    assertThat(actualFields, is(fields));
  }

  @Test
  public void testNameDuplicatesFound() {
    // Given
    OutputFile file = getFile1();
    ConstraintConfig config = file.getConfig();
    List<OutputFile> files = Lists.newArrayList(file);

    List<OutputField> fields = Lists.newArrayList(getField1(), getField1());
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(fileDao.findByConfig(config)).thenReturn(files);
    when(fieldDao.findByFile(file)).thenReturn(fields);
    // Run
    boolean duplicatesFound = fieldService.checkFieldNamesForDuplicates(FILE_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, fileDao, fieldDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(fileDao).findByConfig(config);
    inOrder.verify(fieldDao).findByFile(file);
    // Assert
    assertThat(duplicatesFound, is(true));
  }

  @Test
  public void testNameDuplicatesNotFound() {
    // Given
    OutputFile file = getFile1();
    ConstraintConfig config = file.getConfig();
    List<OutputFile> files = Lists.newArrayList(file);

    OutputField field1 = getField1();
    OutputField field2 = getField1();
    field2.setName(field2.getName() + " ");
    List<OutputField> fields = Lists.newArrayList(field1, field2);
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(fileDao.findByConfig(config)).thenReturn(files);
    when(fieldDao.findByFile(file)).thenReturn(fields);
    // Run
    boolean duplicatesFound = fieldService.checkFieldNamesForDuplicates(FILE_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, fileDao, fieldDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(fileDao).findByConfig(config);
    inOrder.verify(fieldDao).findByFile(file);
    // Assert
    assertThat(duplicatesFound, is(false));
  }

}
