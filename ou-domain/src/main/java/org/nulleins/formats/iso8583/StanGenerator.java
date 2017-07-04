package org.nulleins.formats.iso8583;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author phillipsr
 */
public class StanGenerator implements AutoGenerator<Integer> {

	private static final Logger log = LoggerFactory.getLogger(StanGenerator.class);
  private final Integer floor;
  private final Integer ceiling;
  private static final ThreadLocal<Integer> next = new ThreadLocal<>();

  public StanGenerator(final Integer floor, final Integer ceiling) {
    this.floor = floor;
    this.ceiling = ceiling;
    next.set(floor);
  }

  /** {@inheritDoc} */
  @Override
  public Integer generate(final String autogen, final FieldTemplate field) {
    final Integer result = next.get();
    next.set(result + 1 > ceiling ? floor : result + 1);
    return result;
  }

}
