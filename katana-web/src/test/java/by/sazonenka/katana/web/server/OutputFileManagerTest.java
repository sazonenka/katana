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

import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.web.model.OutputFileModel;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFileManagerTest extends GenericManagerTest {

  @Test
  public void findByConfig() throws Exception {
    // Given
    List<OutputFile> expectedFiles = Lists.newArrayList(getFile1(), getFile2(), getFile3());
    List<OutputFileModel> expectedFileModels = Lists.newArrayList(getFileModel1(),
        getFileModel2(), getFileModel3());
    // Expect
    when(fileService.findByConfig(CONFIG_1_ID)).thenReturn(expectedFiles);
    // Run
    List<OutputFileModel> actualFileModels = fileManager.findByConfig(CONFIG_1_ID);
    // Verify
    verify(fileService).findByConfig(CONFIG_1_ID);
    // Assert
    assertThat(actualFileModels, is(expectedFileModels));
  }

  @Test
  public void saveNewFile() throws Exception {
    // Given
    OutputFile newFile = getNewFile1();
    OutputFile savedFile = getFile1();

    OutputFileModel newFileModel = getNewFileModel1();
    OutputFileModel expectedFileModel = getFileModel1();
    // Expect
    when(fileService.save(newFile)).thenReturn(savedFile);
    // Run
    OutputFileModel actualFileModel = fileManager.save(newFileModel);
    // Verify
    verify(fileService).save(newFile);
    // Assert
    assertThat(actualFileModel, is(expectedFileModel));
  }

  @Test
  public void updateFile() throws Exception {
    // Given
    OutputFile file = getFile1();

    OutputFileModel fileModel = getFileModel1();
    String expectedFileName = fileModel.getName() + "_updated";
    fileModel.setName(expectedFileName);
    // Expect
    when(fileService.get(FILE_1_ID)).thenReturn(file);
    when(fileService.save(file)).thenReturn(file);
    // Run
    OutputFileModel actualFileModel = fileManager.save(fileModel);
    // Verify
    verify(fileService).get(FILE_1_ID);
    verify(fileService).save(file);
    // Assert
    assertThat(actualFileModel, is(fileModel));
    assertThat(actualFileModel.getName(), is(expectedFileName));
  }

  @Test
  public void delete() throws Exception {
    // Run
    fileManager.delete(Lists.newArrayList(FILE_1_ID, FILE_2_ID, FILE_3_ID));
    // Verify
    verify(fileService).delete(FILE_1_ID);
    verify(fileService).delete(FILE_2_ID);
    verify(fileService).delete(FILE_3_ID);
  }

  @Test
  public void reorder() throws Exception {
    // Given
    OutputFile file1 = getFile1();
    int file1Order = file1.getOrderInConfig();
    OutputFile file2 = getFile2();
    int file2Order = file2.getOrderInConfig();
    OutputFile file3 = getFile3();
    int file3Order = file3.getOrderInConfig();
    // Expect
    when(fileService.get(FILE_1_ID)).thenReturn(file1);
    when(fileService.get(FILE_2_ID)).thenReturn(file2);
    when(fileService.get(FILE_3_ID)).thenReturn(file3);
    // Run
    fileManager.reorder(Lists.newArrayList(FILE_3_ID, FILE_2_ID, FILE_1_ID));
    // Verify
    ArgumentCaptor<OutputFile> captor = ArgumentCaptor.forClass(OutputFile.class);
    verify(fileService, times(3)).save(captor.capture());
    // Assert
    List<OutputFile> savedFiles = captor.getAllValues();
    assertThat(savedFiles.get(0).getId(), is(FILE_3_ID));
    assertThat(savedFiles.get(0).getOrderInConfig(), is(file1Order));
    assertThat(savedFiles.get(1).getId(), is(FILE_2_ID));
    assertThat(savedFiles.get(1).getOrderInConfig(), is(file2Order));
    assertThat(savedFiles.get(2).getId(), is(FILE_1_ID));
    assertThat(savedFiles.get(2).getOrderInConfig(), is(file3Order));
  }

  @Test
  public void mapSuccess() throws Exception {
    // Given
    OutputFile file1 = getFile1();
    OutputFile file2 = getFile2();
    OutputFile file3 = getFile3();
    // Expect
    when(fileService.get(FILE_1_ID)).thenReturn(file1);
    when(fileService.get(FILE_2_ID)).thenReturn(file2);
    when(fileService.get(FILE_3_ID)).thenReturn(file3);

    when(fileService.findByParent(FILE_2_ID)).thenReturn(Lists.<OutputFile>newArrayList());
    when(fileService.findByParent(FILE_3_ID)).thenReturn(Lists.<OutputFile>newArrayList());
    // Run
    boolean success = fileManager.map(Lists.newArrayList(FILE_2_ID, FILE_3_ID), FILE_1_ID);
    // Verify
    ArgumentCaptor<OutputFile> captor = ArgumentCaptor.forClass(OutputFile.class);
    verify(fileService, times(2)).save(captor.capture());
    // Assert
    List<OutputFile> savedFiles = captor.getAllValues();
    for (OutputFile savedFile : savedFiles) {
      assertThat(savedFile.getParent(), is(file1));
    }

    assertThat(success, is(true));
  }

  @Test
  public void mapFailure() throws Exception {
    // Given
    OutputFile file1 = getFile1();
    OutputFile file2 = getFile2();
    OutputFile file3 = getFile3();
    // Expect
    when(fileService.get(FILE_1_ID)).thenReturn(file1);
    when(fileService.get(FILE_2_ID)).thenReturn(file2);
    when(fileService.get(FILE_3_ID)).thenReturn(file3);

    when(fileService.findByParent(FILE_2_ID)).thenReturn(Lists.<OutputFile>newArrayList());
    when(fileService.findByParent(FILE_3_ID)).thenReturn(Lists.newArrayList(file2));
    // Run
    boolean success = fileManager.map(Lists.newArrayList(FILE_2_ID, FILE_3_ID), FILE_1_ID);
    // Verify
    verify(fileService, never()).save(any(OutputFile.class));
    // Assert
    assertThat(success, is(false));
  }

  @Test
  public void unmap() throws Exception {
    // Given
    OutputFile file1 = getFile1();
    OutputFile file2 = getFile2();
    OutputFile file3 = getFile3();
    // Expect
    when(fileService.get(FILE_1_ID)).thenReturn(file1);
    when(fileService.get(FILE_2_ID)).thenReturn(file2);
    when(fileService.get(FILE_3_ID)).thenReturn(file3);
    // Run
    fileManager.unmap(Lists.newArrayList(FILE_1_ID, FILE_2_ID, FILE_3_ID));
    // Verify
    ArgumentCaptor<OutputFile> captor = ArgumentCaptor.forClass(OutputFile.class);
    verify(fileService, times(3)).save(captor.capture());
    // Assert
    List<OutputFile> savedFiles = captor.getAllValues();
    for (OutputFile savedFile : savedFiles) {
      assertThat(savedFile.getParent(), nullValue());
    }
  }

  @Test
  public void refresh() throws Exception {
    // Given
    OutputFile file1 = getFile1();
    OutputFile file2 = getFile2();
    OutputFile file3 = getFile3();
    List<OutputFileModel> expectedFileModels = Lists.newArrayList(getFileModel1(),
        getFileModel2(), getFileModel3());
    // Expect
    when(fileService.get(FILE_1_ID)).thenReturn(file1);
    when(fileService.get(FILE_2_ID)).thenReturn(file2);
    when(fileService.get(FILE_3_ID)).thenReturn(file3);
    // Run
    List<OutputFileModel> actualFileModels = fileManager.refresh(Lists.newArrayList(FILE_1_ID,
        FILE_2_ID, FILE_3_ID));
    // Verify
    verify(fileService).get(FILE_1_ID);
    verify(fileService).get(FILE_2_ID);
    verify(fileService).get(FILE_3_ID);
    // Assert
    assertThat(actualFileModels, is(expectedFileModels));
  }

}
