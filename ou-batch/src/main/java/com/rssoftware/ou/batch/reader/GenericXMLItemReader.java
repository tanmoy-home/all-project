package com.rssoftware.ou.batch.reader;

import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.rssoftware.ou.batch.common.Dictionary;

@SuppressWarnings("rawtypes")
public class GenericXMLItemReader extends StaxEventItemReader{
	private Dictionary dictionary;
	
	private String inputFile;

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception{
		//System.out.println("Model Name: "+dictionary.getTargetBeanClass());
		System.out.println("input file: "+inputFile);
		
		Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
		//unmarshaller.setClassesToBeBound(Class.forName(dictionary.getTargetBeanClass()));
		
		setResource(new FileSystemResource(inputFile));
		//setFragmentRootElementName(dictionary.getFragmentRootElementName());
		setUnmarshaller(unmarshaller);
		super.afterPropertiesSet();
	}
	
}
