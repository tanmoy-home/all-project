package org.nulleins.formats.iso8583.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by royp on 23/11/14.
 */
public class SpringMessageTemplate {

	private static final Logger log = LoggerFactory.getLogger(SpringMessageTemplate.class);
  private String type;
  private String name;

  public List<SpringFieldTemplate> getFields () {
    return fields;
  }

  public void setFields (List<SpringFieldTemplate> fields) {
    this.fields = fields;
  }

  private List<SpringFieldTemplate> fields;

  public String getType () {
    return type;
  }

  public void setType (String type) {
    this.type = type;
  }

  public String getName () {
    return name;
  }

  public void setName (String name) {
    this.name = name;
  }
}
