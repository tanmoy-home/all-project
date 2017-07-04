package org.nulleins.formats.iso8583.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class Iso8583NamespaceHandler extends NamespaceHandlerSupport {

	private static final Logger log = LoggerFactory.getLogger(Iso8583NamespaceHandler.class);
  @Override
  public void init() {
    registerBeanDefinitionParser("schema",
        new SchemaDefinitionParser());
  }

}
