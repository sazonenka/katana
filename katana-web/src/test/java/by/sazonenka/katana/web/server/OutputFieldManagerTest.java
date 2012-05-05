package by.sazonenka.katana.web.server;

import static by.sazonenka.katana.web.server.ManagerTestData.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;

import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.web.model.OutputFieldModel;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFieldManagerTest extends GenericManagerTest {

  @Test
  public void findByFile() throws Exception {
    // Given
    List<OutputField> expectedFields = Lists.newArrayList(getField1(), getField2());
    List<OutputFieldModel> expectedFieldModels = Lists.newArrayList(getFieldModel1(),
        getFieldModel2());
    // Expect
    when(fieldService.findByFile(FILE_1_ID)).thenReturn(expectedFields);
    // Run
    List<OutputFieldModel> actualFieldModels = fieldManager.findByFile(FILE_1_ID);
    // Verify
    verify(fieldService).findByFile(FILE_1_ID);
    // Assert
    assertThat(actualFieldModels, is(expectedFieldModels));
  }

  @Test
  public void saveNewField() throws Exception {
    // Given
    OutputField newField = getNewField1();
    OutputField savedField = getField1();

    OutputFieldModel newFieldModel = getNewFieldModel1();
    OutputFieldModel expectedFieldModel = getFieldModel1();
    // Expect
    when(fieldService.save(newField)).thenReturn(savedField);
    // Run
    OutputFieldModel actualFieldModel = fieldManager.save(newFieldModel);
    // Verify
    verify(fieldService).save(newField);
    // Assert
    assertThat(actualFieldModel, is(expectedFieldModel));
  }

  @Test
  public void updateField() throws Exception {
    // Given
    OutputField field = getField1();

    OutputFieldModel fieldModel = getFieldModel1();
    String expectedFieldName = fieldModel.getName() + "_updated";
    fieldModel.setName(expectedFieldName);
    // Expect
    when(fieldService.get(FIELD_1_ID)).thenReturn(field);
    when(fieldService.save(field)).thenReturn(field);
    // Run
    OutputFieldModel actualFieldModel = fieldManager.save(fieldModel);
    // Verify
    verify(fieldService).get(FIELD_1_ID);
    verify(fieldService).save(field);
    // Assert
    assertThat(actualFieldModel, is(fieldModel));
    assertThat(actualFieldModel.getName(), is(expectedFieldName));
  }

  @Test
  public void delete() throws Exception {
    // Run
    fieldManager.delete(Lists.newArrayList(FIELD_1_ID, FIELD_2_ID));
    // Verify
    verify(fieldService).delete(FIELD_1_ID);
    verify(fieldService).delete(FIELD_2_ID);
  }

  @Test
  public void reorder() throws Exception {
    // Given
    OutputField field1 = getField1();
    int field1Order = field1.getOrderInFile();
    OutputField field2 = getField2();
    int field2Order = field2.getOrderInFile();
    // Expect
    when(fieldService.get(FIELD_1_ID)).thenReturn(field1);
    when(fieldService.get(FIELD_2_ID)).thenReturn(field2);
    // Run
    fieldManager.reorder(Lists.newArrayList(FIELD_2_ID, FIELD_1_ID));
    // Verify
    ArgumentCaptor<OutputField> captor = ArgumentCaptor.forClass(OutputField.class);
    verify(fieldService, times(2)).save(captor.capture());
    // Assert
    List<OutputField> savedFields = captor.getAllValues();
    assertThat(savedFields.get(0).getId(), is(FIELD_2_ID));
    assertThat(savedFields.get(0).getOrderInFile(), is(field1Order));
    assertThat(savedFields.get(1).getId(), is(FIELD_1_ID));
    assertThat(savedFields.get(1).getOrderInFile(), is(field2Order));
  }

  @Test
  public void map() throws Exception {
    // Given
    ValidationRule rule = getRule1();
    OutputField field1 = getField1();
    OutputField field2 = getField2();
    // Expect
    when(ruleService.get(RULE_1_ID)).thenReturn(rule);
    when(fieldService.get(FIELD_1_ID)).thenReturn(field1);
    when(fieldService.get(FIELD_2_ID)).thenReturn(field2);
    // Run
    fieldManager.map(Lists.newArrayList(FIELD_1_ID, FIELD_2_ID), RULE_1_ID);
    // Verify
    ArgumentCaptor<OutputField> captor = ArgumentCaptor.forClass(OutputField.class);
    verify(fieldService, times(2)).save(captor.capture());
    // Assert
    List<OutputField> savedFields = captor.getAllValues();
    for (OutputField savedField : savedFields) {
      assertThat(savedField.getRule(), is(rule));
    }
  }

  @Test
  public void unmap() throws Exception {
    // Given
    OutputField field1 = getField1();
    OutputField field2 = getField2();
    // Expect
    when(fieldService.get(FIELD_1_ID)).thenReturn(field1);
    when(fieldService.get(FIELD_2_ID)).thenReturn(field2);
    // Run
    fieldManager.unmap(Lists.newArrayList(FIELD_1_ID, FIELD_2_ID));
    // Verify
    ArgumentCaptor<OutputField> captor = ArgumentCaptor.forClass(OutputField.class);
    verify(fieldService, times(2)).save(captor.capture());
    // Assert
    List<OutputField> savedFields = captor.getAllValues();
    for (OutputField savedField : savedFields) {
      assertThat(savedField.getRule(), nullValue());
    }
  }

  @Test
  public void refresh() throws Exception {
    // Given
    OutputField field1 = getField1();
    OutputField field2 = getField2();
    List<OutputFieldModel> expectedFieldModels = Lists.newArrayList(getFieldModel1(),
        getFieldModel2());
    // Expect
    when(fieldService.get(FIELD_1_ID)).thenReturn(field1);
    when(fieldService.get(FIELD_2_ID)).thenReturn(field2);
    // Run
    List<OutputFieldModel> actualFieldModels = fieldManager.refresh(Lists.newArrayList(FIELD_1_ID,
        FIELD_2_ID));
    // Verify
    verify(fieldService).get(FIELD_1_ID);
    verify(fieldService).get(FIELD_2_ID);
    // Assert
    assertThat(actualFieldModels, is(expectedFieldModels));
  }

}
