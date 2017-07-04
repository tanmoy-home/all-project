package org.nulleins.formats.iso8583.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.base.Preconditions;

/** The standard ISO8583 field types: represented as constant
  * strings rather than an enum as the set is user-extensible
  * @author phillipsr */
public final class FieldType {

	private static final Logger log = LoggerFactory.getLogger(FieldType.class);
  public static final String ALPHA = "a";
  public static final String NUMERIC = "n";
  public static final String ALPHANUM = "an";
  public static final String ALPHASYMBOL = "as";
  public static final String ALPHANUMPAD = "anp";
  public static final String ALPHANUMSYMBOL = "ans";
  public static final String NUMSYMBOL = "ns";
  public static final String NUMSIGNED = "xn";
  public static final String DATE = "date";
  public static final String TIME = "time";
  public static final String EXDATE = "exdate";
  public static final String TRACKDATA = "z";

  private final String typeCode;
  public FieldType(final String code) {
    Preconditions.checkNotNull(code);
    Preconditions.checkArgument(!code.isEmpty());
    this.typeCode = code;
  }

  public String getCode() { return typeCode; }
}
