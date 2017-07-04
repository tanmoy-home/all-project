package org.nulleins.formats.iso8583;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author phillipsr
 */
public interface AutoGenerator<V> {

  /** @return an auto-generated value for the <code>field</code> supplied,
    * using the named <code>autogen</code> */
  V generate(String autogen, FieldTemplate field);

}
