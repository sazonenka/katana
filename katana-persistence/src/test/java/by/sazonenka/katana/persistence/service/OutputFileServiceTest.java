package by.sazonenka.katana.persistence.service;

import static by.sazonenka.katana.persistence.service.ServiceTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputFile;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFileServiceTest extends GenericServiceTest {

  @Test
  public void testGet() {
    // Given
    OutputFile file = getFile1();
    // Expect
    when(fileDao.get(FILE_1_ID)).thenReturn(file);
    // Run
    OutputFile actualFile = fileService.get(FILE_1_ID);
    // Verify
    verify(fileDao).get(FILE_1_ID);
    // Assert
    assertThat(actualFile, is(file));
  }

  @Test
  public void testSaveNewFile() {
    // Given
    OutputFile file = getFile1();
    ConstraintConfig config = file.getConfig();
    config.setId(CONFIG_1_ID);
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(fileDao.getCountByConfig(config)).thenReturn(FILES_IN_CONFIG_1);
    when(fileDao.save(file)).thenReturn(file);
    // Run
    OutputFile savedFile = fileService.save(file);
    // Verify
    InOrder inOrder = inOrder(configDao, fileDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(fileDao).getCountByConfig(config);
    inOrder.verify(fileDao).save(file);
    // Assert
    assertThat(savedFile, is(file));
    assertThat(savedFile.getOrderInConfig(), is(FILES_IN_CONFIG_1.intValue()));
  }

  @Test
  public void testSaveExistingFile() {
    // Given
    OutputFile file = getFile1();
    file.setId(FILE_1_ID);
    // Expect
    when(fileDao.save(file)).thenReturn(file);
    // Run
    OutputFile savedFile = fileService.save(file);
    // Verify
    InOrder inOrder = inOrder(configDao, fileDao);
    inOrder.verify(configDao, never()).get(any(Long.class));
    inOrder.verify(fileDao, never()).getCountByConfig(any(ConstraintConfig.class));
    inOrder.verify(fileDao).save(file);
    // Assert
    assertThat(savedFile, is(file));
  }

  @Test
  public void testDelete() {
    // Given
    OutputFile fileToDelete = getFile1();
    ConstraintConfig config = fileToDelete.getConfig();

    OutputFile file1 = getFile1();
    file1.setOrderInConfig(0);
    OutputFile file2 = getFile1();
    file2.setOrderInConfig(2);
    List<OutputFile> files = Lists.newArrayList(file1, file2);
    // Expect
    when(fileDao.get(FILE_1_ID)).thenReturn(fileToDelete);
    when(fileDao.findByConfig(config)).thenReturn(files);
    // Run
    fileService.delete(FILE_1_ID);
    // Verify
    InOrder inOrder = inOrder(fileDao);
    inOrder.verify(fileDao).get(FILE_1_ID);
    inOrder.verify(fileDao).delete(fileToDelete);

    inOrder.verify(fileDao).findByConfig(config);
    inOrder.verify(fileDao).save(file2);
    // Assert
    assertThat(file2.getOrderInConfig(), is(1));
  }

  @Test
  public void testFindByConfig() {
    // Given
    ConstraintConfig config = getConfig1();
    List<OutputFile> files = Lists.newArrayList(getFile1(), getFile1(), getFile1());
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(fileDao.findByConfig(config)).thenReturn(files);
    // Run
    List<OutputFile> actualFiles = fileService.findByConfig(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, fileDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(fileDao).findByConfig(config);
    // Assert
    assertThat(actualFiles, is(files));
  }

  @Test
  public void testFindByParent() {
    // Given
    OutputFile parent = getFile1();
    List<OutputFile> files = Lists.newArrayList(getFile1(), getFile1(), getFile1());
    // Expect
    when(fileDao.get(FILE_1_ID)).thenReturn(parent);
    when(fileDao.findByParent(parent)).thenReturn(files);
    // Run
    List<OutputFile> actualFiles = fileService.findByParent(FILE_1_ID);
    // Verify
    InOrder inOrder = inOrder(fileDao);
    inOrder.verify(fileDao).get(FILE_1_ID);
    inOrder.verify(fileDao).findByParent(parent);
    // Assert
    assertThat(actualFiles, is(files));
  }

  @Test
  public void testNameDuplicatesFound() {
    // Given
    ConstraintConfig config = getConfig1();
    List<OutputFile> files = Lists.newArrayList(getFile1(), getFile1());
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(fileDao.findByConfig(config)).thenReturn(files);
    // Run
    boolean duplicatesFound = fileService.checkFileNamesForDuplicates(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, fileDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(fileDao).findByConfig(config);
    // Assert
    assertThat(duplicatesFound, is(true));
  }

  @Test
  public void testNameDuplicatesNotFound() {
    // Given
    ConstraintConfig config = getConfig1();

    OutputFile file1 = getFile1();
    OutputFile file2 = getFile1();
    file2.setName(file2.getName() + " ");
    List<OutputFile> rules = Lists.newArrayList(file1, file2);
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(fileDao.findByConfig(config)).thenReturn(rules);
    // Run
    boolean duplicatesFound = fileService.checkFileNamesForDuplicates(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, fileDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(fileDao).findByConfig(config);
    // Assert
    assertThat(duplicatesFound, is(false));
  }

}
