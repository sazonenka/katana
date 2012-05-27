package by.sazonenka.katana.persistence.dao;

import java.util.List;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aliaksandr Sazonenka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-dao.xml", "/spring/spring-dao-test.xml"})
@Transactional
public abstract class GenericDaoTest {

  @Inject protected ConstraintConfigDao configDao;
  @Inject protected ValidationRuleDao ruleDao;
  @Inject protected OutputFileDao fileDao;
  @Inject protected OutputFieldDao fieldDao;

  @Inject protected SimpleJdbcTemplate jdbcTemplate;

  protected int count(DomainMapping mapping) {
    return SimpleJdbcTestUtils.countRowsInTable(jdbcTemplate, mapping.tableName);
  }

  protected <T> T get(DomainMapping mapping, Object id) {
    List<T> results = queryForList(mapping, "ID", id);
    return DataAccessUtils.singleResult(results);
  }

  protected <T> List<T> findByConfig(DomainMapping mapping, Object configId) {
    return queryForList(mapping, "CONFIG_ID", configId);
  }

  protected <T> List<T> findByFile(DomainMapping mapping, Object fileId) {
    return queryForList(mapping, "FILE_ID", fileId);
  }

  protected <T> List<T> findByParent(DomainMapping mapping, Object fileId) {
    return queryForList(mapping, "PARENT_ID", fileId);
  }

  protected <T> List<T> findByRule(DomainMapping mapping, Object ruleId) {
    return queryForList(mapping, "RULE_ID", ruleId);
  }

  @SuppressWarnings("unchecked")
  private <T> List<T> queryForList(DomainMapping mapping, String paramColumn, Object paramValue) {
    String sql = String.format("SELECT * FROM %s WHERE %s = ?", mapping.tableName, paramColumn);
    return (List<T>) jdbcTemplate.query(sql, mapping.mapper, paramValue);
  }

}
