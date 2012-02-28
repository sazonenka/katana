package by.sazonenka.katana.xml.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import com.google.common.base.Objects;

/**
 * Represents a model of a file extention in <code>test_fields_upon_constraints.xml</code> file.
 * <p>
 * Mappings between fields and validation rules can be shared between files.
 * The <b>extends</b> tag specifies what file to inherit.
 * <ol>
 *  <li>The <b>file</b> attribute specifies the name of the file to inherit.
 *  <li>Note that the file cannot inherit more than one file.
 *  <li>Note that the file cannot inherit a file that, in turn, is inherited by others.
 * </ol>
 *
 * @author Aliaksandr Sazonenka
 * @see OutputFileXml
 */
@Root
public final class ExtendsXml {

  @Attribute
  private String file;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ExtendsXml that = (ExtendsXml) obj;
    return Objects.equal(file, that.file);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(file);
  }

  /** Returns the value of the <b>file</b> attribute. */
  public String getFile() { return file; }
  /** Sets the value of the <b>file</b> attribute. */
  public void setFile(String file) { this.file = file; }

}
