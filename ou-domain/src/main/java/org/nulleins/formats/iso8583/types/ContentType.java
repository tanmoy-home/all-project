package org.nulleins.formats.iso8583.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enumeration of the content types allowed for an ISO8583 message,
 * Binary Coded Decimal (for numeric fieldlist) and ASCII or EBCDIC for
 * text fieldlist
 * @author phillipsr
 */
public enum ContentType {
  BCD, TEXT
}
