package by.sazonenka.katana.xml.domain;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Represents a model of a constraint config in <code>test_fields_upon_constraints.xml</code> file.
 * <p>
 * The <b>dataset-constraints</b> is a root tag of a
 * <code>test_fields_upon_constraints.xml</code> document.
 * <ol>
 *  <li>The constraint config should have a unique identifier (<b>id</b> attribute).
 *  <li>The <b>validation-rules</b> section hosts constraint definitions.
 *  <li>The <b>output-files</b> section hosts the mappings between fields and constraints.
 * </ol>
 *
 * @author Aliaksandr Sazonenka
 * @see ValidationRuleXml
 * @see OutputFileXml
 */
@Root(name = "dataset-constraints")
public final class ConstraintConfigXml {

  @Attribute
  private String id;

  @ElementList(name = "validation-rules")
  private ArrayList<ValidationRuleXml> validationRules;

  @ElementList(name = "output-files")
  private ArrayList<OutputFileXml> outputFiles;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ConstraintConfigXml that = (ConstraintConfigXml) obj;
    return Objects.equal(id, that.id)
        && Objects.equal(validationRules, that.validationRules)
        && Objects.equal(outputFiles, that.outputFiles);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, validationRules, outputFiles);
  }

  /** Returns the value of the <b>id</b> attribute. */
  public String getId() { return id; }
  /** Sets the value of the <b>id</b> attribute. */
  public void setId(String id) { this.id = id; }

  /** Returns content of the <b>validation-rules</b> section. */
  public ArrayList<ValidationRuleXml> getValidationRules() {
    return validationRules != null ? Lists.newArrayList(validationRules) : null;
  }
  /** Sets content of the <b>validation-rules</b> section. */
  public void setValidationRules(ArrayList<ValidationRuleXml> validationRules) {
    this.validationRules = validationRules != null ? Lists.newArrayList(validationRules) : null;
  }

  /** Returns content of the <b>output-files</b> section. */
  public ArrayList<OutputFileXml> getOutputFiles() {
    return outputFiles != null ? Lists.newArrayList(outputFiles) : null;
  }
  /** Sets content of the <b>output-files</b> section. */
  public void setOutputFiles(ArrayList<OutputFileXml> outputFiles) {
    this.outputFiles = outputFiles != null ? Lists.newArrayList(outputFiles) : null;
  }

}
