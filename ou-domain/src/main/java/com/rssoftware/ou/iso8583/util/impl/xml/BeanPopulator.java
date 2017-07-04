package com.rssoftware.ou.iso8583.util.impl.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rssoftware.ou.iso8583.util.impl.IsoMapElement;
import com.rssoftware.ou.iso8583.util.impl.IsoMapSubElement;



public class BeanPopulator {

	private static final Logger log = LoggerFactory.getLogger(BeanPopulator.class);
	
	private Map<Integer, IsoMapElement> isoMap;
	//String is the Tag Value
	private Map<String, IsoMapSubElement> isoSubElementMap;

	public Map<Integer, IsoMapElement> getIsoMap() {
		return isoMap;
	}

	public void setIsoMap(Map<Integer, IsoMapElement> isoMap) {
		this.isoMap = isoMap;
	}

	public BeanPopulator() {
		try {
			init();
		} catch (ParserConfigurationException e) {
			
			log.error(e.getMessage(), e);
		} catch (SAXException e) {
			
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			
			log.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			
			log.error(e.getMessage(), e);
		}
		catch (Exception e) {
			
			log.error(e.getMessage(), e);
		}
	}

	public void init() throws ParserConfigurationException, SAXException,
			IOException, ClassNotFoundException {
		ResourceBundle bundle = ResourceBundle.getBundle("docfile");
		String filePath = bundle.getString("filename");
		//File fXmlFile = new File(filePath);
		InputStream fis = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fis);
		NodeList nList = doc.getElementsByTagName("isomapelement");
		isoMap = new TreeMap<Integer, IsoMapElement>();
		isoSubElementMap = new TreeMap<String, IsoMapSubElement>();
		for (int i = 0; i < nList.getLength(); i++) {
			Element node = (Element) nList.item(i);
			IsoMapElement element = new IsoMapElement();
			
			element.setName(node.getAttribute("name"));
			int pos = Integer.parseInt(node.getAttribute("position"));
			element.setPos(pos);
			element.setType(Class.forName(node.getAttribute("type")));
			element.setLength(Integer.parseInt(node.getAttribute("length")));
			element.setVarlength(Integer.parseInt(node.getAttribute("varlength")));
			
			//Processing the sub element,This should be generic.
		  if(node.getAttribute("tlv").equals("true"))
			{				
				NodeList subList = node.getElementsByTagName("isosubelement");
				for (int j = 0; j < subList.getLength(); j++) {
					Element sNode = (Element) subList.item(j);
					IsoMapSubElement subElement = new IsoMapSubElement();
					//System.out.println("~~~~~Found Sub Node~~~~~"+ sNode.getAttribute("position")+ ",pos==" + element.getPos());
					//System.out.println("~~~~~Found Sub Node~~~~~"+ sNode.getAttribute("position")+ ",tagValue==" + sNode.getAttribute("tagValue"));
					
					subElement.setName(sNode.getAttribute("name"));
					int subPos = Integer.parseInt(sNode.getAttribute("position"));
					subElement.setPos(subPos);
					subElement.setType(Class.forName(sNode.getAttribute("type")));
					subElement.setLength(Integer.parseInt(sNode.getAttribute("length")));
					subElement.setVarlength(Integer.parseInt(sNode.getAttribute("varlength")));
					String tagValue = sNode.getAttribute("tagValue");
					subElement.setTagValue(tagValue);
					
					//Add to the subMap
					isoSubElementMap.put(tagValue, subElement);
				}
				//If true then set the SubElement Map
				element.setTLV(true);
				element.setSubElementMap(isoSubElementMap);
			}
			
			isoMap.put(pos, element);
		}
	}

	
	//Util Method to return value based on Key Passed.
	public IsoMapElement getByPosKey(Integer pos)
	{
		return isoMap.get(pos);
	}
	
	public static void main(String[] args) {
		BeanPopulator pop = new BeanPopulator();
	}
}