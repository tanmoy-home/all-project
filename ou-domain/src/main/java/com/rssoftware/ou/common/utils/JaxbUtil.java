package com.rssoftware.ou.common.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxbUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(JaxbUtil.class);

	public static String stringifyJaxb(Object jaxbObject) {
		if(jaxbObject == null)
			return null;
		StringWriter writer = null;
		try
		{
			writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(jaxbObject.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(jaxbObject, writer);
			
			return writer.toString();
		}
		catch(Exception ex)
		{
			logger.error( ex.getMessage(), ex);
	          logger.info("In Excp : " + ex.getMessage());	
	          return null;
		}
	}
		
	public static <T> T jaxbifyString(String data, Class<T> type) {
		StringReader reader = new StringReader(data);
		try
		{
			return (T) JAXB.unmarshal(reader, type);
		}
		catch(Exception ex)
		{
			logger.error( ex.getMessage(), ex);
	        logger.info("In Excp : " + ex.getMessage());	
			return null;
		}
	}
	
}
