package by.sazonenka.katana.persistence.domain;

import java.util.Date;

/**
 * @author Aliaksandr Sazonenka
 */
public final class DomainTestData {

  public static final String NAME_1 = "name-1";
  public static final String NAME_2 = "name-2";

  public static final String AUTHOR_1 = "author-1";
  public static final String AUTHOR_2 = "author-2";

  public static final String REGEXP_1 = "regexp-1";
  public static final String REGEXP_2 = "regexp-2";

  public static final String DESCRIPTION_1 = "description-1";
  public static final String DESCRIPTION_2 = "description-2";

  public static final boolean NULLABLE_1 = true;
  public static final boolean NULLABLE_2 = false;

  public static final Date MODIFIED_1 = new Date(1L);
  public static final Date MODIFIED_2 = new Date(2L);

  public static final int ORDER_1 = 0;
  public static final int ORDER_2 = 1;

  private DomainTestData() {
    /* Ensure non-instanciability. */
  }

}
