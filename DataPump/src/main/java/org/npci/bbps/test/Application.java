package org.npci.bbps.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
public class Application implements CommandLineRunner{

	static int COUNT = 1;
	static int MINS = 1;
	static ApplicationContext context;
	static boolean SHOW_RESPONSE = true;
	static int MAX_RETRY_COUNT = 3;
	
	@Value("${local.ou.domain}")
	public String localDomain;
	
	@Value("${qa.ou.domain}")
	public String qaDomain;
	

	public static void main(String[] args) throws Exception{
		SpringApplication.run(Application.class, args);
	}
	
	static AtomicLong cntr = new AtomicLong(0);
	
	static String thisTime = ""+System.currentTimeMillis();

	static DataPumpRestTemplate restTemplate = DataPumpRestTemplate.createObject();
	static BBPSBatchRestTemplate batchRestTemplate = BBPSBatchRestTemplate.createObject();

	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	private void test() throws Exception{
	
		boolean isRunningOnQa = 
				(System.getProperty("run.on.qa") != null && 
				"Y".equalsIgnoreCase(System.getProperty("run.on.qa")))? true : false;
				
		RequestType requestType =
				System.getProperty("request.type") != null ? RequestType.valueOf(System.getProperty("request.type")) : null;
		
		long startTime = System.currentTimeMillis();
		for (int i=0;i<1;i++){
			if ((System.currentTimeMillis() - startTime) > MINS*60000){
				break;
			}
			
			Sender sender = new Sender(isRunningOnQa, this, requestType);
			executor.submit(sender);
			
		}	
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
		  
		}
		
		System.exit(0);

	}

	@Override
	public void run(String... args) throws Exception {
		try {
			if (args != null && args.length >= 2){
				COUNT = Integer.parseInt(args[0]);
				MINS = Integer.parseInt(args[1]);
			}
			this.test();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public static Object makeCopy(Object obj){
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			Object object = (Object) new ObjectInputStream(bais).readObject();
			return object;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String signIndividualEntry(String xmlDocument) {
			try {
				Document doc = DataPumpSignatureUtil.getXmlDocument(xmlDocument);
				DataPumpSignatureUtil.generateXMLDigitalSignature(doc, DataPumpRestTemplateInterceptor.privateKey, DataPumpRestTemplateInterceptor.publicKey);
				return DataPumpSignatureUtil.convert(doc);
				//signedString.replaceAll(System.lineSeparator(), "");
			} catch (SAXException | 
						IOException | 
							ParserConfigurationException | 
								NoSuchAlgorithmException | 
									InvalidAlgorithmParameterException | 
										MarshalException | 
											XMLSignatureException | 
												TransformerException e) {
				e.printStackTrace();
			}
		return xmlDocument;
	}
}