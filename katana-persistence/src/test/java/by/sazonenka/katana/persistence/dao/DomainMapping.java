package by.sazonenka.katana.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

/**
 * @author Aliaksandr Sazonenka
 */
enum DomainMapping {
  CONSTRAINT_CONFIG("CONSTRAINT_CONFIG", new RowMapper<ConstraintConfig>() {
    @Override
    public ConstraintConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
      ConstraintConfig config = new ConstraintConfig();

      config.setId(rs.getLong("ID"));
      config.setName(rs.getString("NAME"));
      config.setAuthor(rs.getString("AUTHOR"));
      config.setModified(rs.getDate("MODIFIED"));
      config.setVersion(rs.getInt("VERSION"));

      return config;
    }
  }),
  VALIDATION_RULE("VALIDATION_RULE", new RowMapper<ValidationRule>() {
    @Override
    public ValidationRule mapRow(ResultSet rs, int rowNum) throws SQLException {
      ValidationRule rule = new ValidationRule();

      rule.setId(rs.getLong("ID"));
      rule.setName(rs.getString("NAME"));
      rule.setNullable(rs.getBoolean("NULLABLE"));
      rule.setRegexp(rs.getString("REG_EXP"));
      rule.setDescription(rs.getString("DESCRIPTION"));
      rule.setOrderInConfig(rs.getInt("ORDER_IN_CONFIG"));
      rule.setVersion(rs.getInt("VERSION"));

      ConstraintConfig config = new ConstraintConfig();
      config.setId(rs.getLong("CONFIG_ID"));
      rule.setConfig(config);

      return rule;
    }
  }),
  OUTPUT_FILE("OUTPUT_FILE", new RowMapper<OutputFile>() {
    @Override
    public OutputFile mapRow(ResultSet rs, int rowNum) throws SQLException {
      OutputFile file = new OutputFile();

      file.setId(rs.getLong("ID"));
      file.setName(rs.getString("NAME"));
      file.setOrderInConfig(rs.getInt("ORDER_IN_CONFIG"));
      file.setVersion(rs.getInt("VERSION"));

      ConstraintConfig config = new ConstraintConfig();
      config.setId(rs.getLong("CONFIG_ID"));
      file.setConfig(config);

      Long parentId = rs.getLong("PARENT_ID");
      if (parentId > 0) {
        OutputFile parent = new OutputFile();
        parent.setId(parentId);
        file.setParent(parent);
      }

      return file;
    }
  }),
  OUTPUT_FIELD("OUTPUT_FIELD", new RowMapper<OutputField>() {
    @Override
    public OutputField mapRow(ResultSet rs, int rowNum) throws SQLException {
      OutputField field = new OutputField();

      field.setId(rs.getLong("ID"));
      field.setName(rs.getString("NAME"));
      field.setOrderInFile(rs.getInt("ORDER_IN_FILE"));
      field.setVersion(rs.getInt("VERSION"));

      OutputFile file = new OutputFile();
      file.setId(rs.getLong("FILE_ID"));
      field.setFile(file);

      Long ruleId = rs.getLong("RULE_ID");
      if (ruleId > 0) {
        ValidationRule rule = new ValidationRule();
        rule.setId(ruleId);
        field.setRule(rule);
      }

      return field;
    }
  });

  final String tableName;
  final RowMapper<?> mapper;

  DomainMapping(String tableName, RowMapper<?> mapper) {
    this.tableName = tableName;
    this.mapper = mapper;
  }

}
