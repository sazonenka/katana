package by.sazonenka.katana.xml.domain;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Represents a model of an output file in <code>test_fields_upon_constraints.xml</code> file.
 * <p>
 * The <b>output-file</b> tag defines a list of fields that will be validated.
 * The fields can be directly listed with the <b>field</b> tags,
 * or inherited if the <b>extends</b> tag is used.
 * <ol>
 *  <li>The <b>name</b> attribute points to an existing file in the output directory,
 *      and should be in the <code>name.ext</code> format.
 *  <li>The <b>extends</b> element can be used for fields inheritance.
 *  <li>The <b>field</b> elements can be used for specifying
 *      mappings between fields and validation rules.
 * </ol>
 *
 * @author Aliaksandr Sazonenka
 * @see ConstraintConfigXml
 * @see OutputFieldXml
 * @see ExtendsXml
 */
@Root(name = "output-file")
public final class OutputFileXml {

  @Attribute
  private String name;

  @Element(name = "extends", required = false)
  private ExtendsXml extendsFile;

  @ElementList(inline = true, required = false)
  private ArrayList<OutputFieldXml> fields;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    OutputFileXml that = (OutputFileXml) obj;
    return Objects.equal(name, that.name)
        && Objects.equal(extendsFile, that.extendsFile)
        && Objects.equal(fields, that.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, extendsFile, fields);
  }

  /** Returns the value of the <b>name</b> attribute. */
  public String getName() { return name; }
  /** Sets the value of the <b>name</b> attribute. */
  public void setName(String name) { this.name = name; }

  /**
   * Returns the {@link ExtendsXml} if the <b>extends</b> element is present,
   * or <code>null</code> if this file doesn't extend any other file.
   */
  public ExtendsXml getExtendsFile() { return extendsFile; }
  /** Sets the {@link ExtendsXml} this file is associated with. */
  public void setExtendsFile(ExtendsXml extendsFile) { this.extendsFile = extendsFile; }

  /** Returns content of the <b>fields</b> inline list. */
  public ArrayList<OutputFieldXml> getFields() {
    return fields != null ? Lists.newArrayList(fields) : null;
  }
  /** Sets content of the <b>fields</b> inline list. */
  public void setFields(ArrayList<OutputFieldXml> fields) {
    this.fields = fields != null ? Lists.newArrayList(fields) : null;
  }

}
