package org.npci.bbps.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/*
 * re-implementation of ClientHttpRequestInterceptor to intercept request and response to validate the XML Signature
 * 
 *  @author Avinash Roy
 */
public class DataPumpRestTemplateInterceptor implements ClientHttpRequestInterceptor {
	public static PrivateKey privateKey = null;
	public static PublicKey publicKey = null;
	private static String TRANSACTION_URN = "urn:referenceId:";
	private static String TRANSACTION_ID_HEADER = "Txn-Id";
	private static String CONTENT_LENGTH_HEADER = "Content-Length";
//	private static String PATH_TO_WS=System.getProperty("path.to.ws");
	private static String PATH_TO_WS=System.getenv("OU_HOME");
	static {
        try {
			KeyStore keystore = KeyStore.getInstance("PKCS12");

			/*Information for certificate to be generated */ 
			String password = "npciupi";
			String alias = "1";
			
			InputStream is = new FileInputStream(new File(PATH_TO_WS+"/config/certificate/OU/ousigner.p12"));
			InputStream caInput = new BufferedInputStream(is);
			
			/* getting the key*/
			keystore.load(caInput, password.toCharArray());
			PrivateKey key = (PrivateKey)keystore.getKey(alias, password.toCharArray());
			
			privateKey = key;

			java.security.cert.Certificate cert = keystore.getCertificate(alias); 
			publicKey = cert.getPublicKey();
		} catch (UnrecoverableKeyException | KeyStoreException
				| NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.http.client.ClientHttpRequestInterceptor#intercept
	 * (org.springframework.http.HttpRequest, byte[],
	 * org.springframework.http.client.ClientHttpRequestExecution)
	 * 
	 * This method is fired before communicating the external url. It signs
	 * every outgoing XML and validates the signature of every incoming XML
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {

		// Sign XML
		try {
			body = signXML(request, body);
			
//			System.out.println("Signed xml:");
//			System.out.println(new String(body));
			
			int contentLength = body.length;
			if (request.getHeaders().containsKey(CONTENT_LENGTH_HEADER)){
				request.getHeaders().remove(CONTENT_LENGTH_HEADER);
			}
			request.getHeaders().add(CONTENT_LENGTH_HEADER, String.valueOf(contentLength));

			String txnId = request.getURI().getPath().substring(request.getURI().getPath().lastIndexOf(TRANSACTION_URN)+TRANSACTION_URN.length());
			if (request.getHeaders().containsKey(TRANSACTION_ID_HEADER)){
				request.getHeaders().remove(TRANSACTION_ID_HEADER);
			}
			request.getHeaders().add(TRANSACTION_ID_HEADER, txnId);
			
			// Send request and get response
			ClientHttpResponse response = execution.execute(request, body);

			
			// validate the XML signature
//			boolean isValidResponse = validateXMLSignature(response);
//			if (!isValidResponse) {
//				// TODO handle invalid response
//			}
			

			
			/*
			 * c.setRequestProperty("Content-Length",(cotentLength+1));
c.setFixedLengthStreamingMode(contentLength+1);
c.setRequestProperty("Content-Type","appplication/zip");
c.setRequestMethod("POST");
c.setRequestProperty("Content-Range", "bytes "+startRange+"-"+endRange+"/"+filesize);
			 * 
			 */
			
			
			// return
			return response;

		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
				| SAXException | ParserConfigurationException
				| MarshalException | XMLSignatureException
				| TransformerException e) {
			throw new IOException(e);
		}
	}

	/*
	 * Method to sign the XML
	 * 
	 * @param request the entire request
	 * 
	 * @param body the request body
	 */
	private byte[] signXML(HttpRequest request, byte[] body) throws IOException, SAXException, ParserConfigurationException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, XMLSignatureException, TransformerException {
		Document doc = DataPumpSignatureUtil.getXmlDocument(new String(body));
		DataPumpSignatureUtil.generateXMLDigitalSignature(doc, privateKey, publicKey);
		
		return DataPumpSignatureUtil.convert(doc).getBytes();
	}

	/*
	 * validates the XML Signature
	 * 
	 * @param the client response object
	 */
//	private boolean validateXMLSignature(ClientHttpResponse response)
//			throws IOException {
//		boolean isValidResponse = false;
//		// convert actual response to wrapper response object so that tit can be
//		// read more than once.
//		BufferingClientHttpResponseWrapper responseWrapper = new BufferingClientHttpResponseWrapper(
//				response);
//
//		if (this.isUPICall) {
//			// fetch the Digital Signature from response header
//			String inSignedData = responseWrapper.getHeaders()
//					.get(CommonConstant.HEADER_NAME_DIGITAL_SIGNATURE).get(0);
//			// String genDigest = UPIChechsum.generateSHA2(new
//			// String(StreamUtils.copyToByteArray(responseWrapper.getBody())));
//			InputStream is = responseWrapper.getBody();
//			byte[] bytePayLoad = new byte[is.available()];
//			is.read(bytePayLoad);
//			
//			isValidResponse = UPICipher.validateSignature(inSignedData, bytePayLoad, "upi");
//			
//			// if the incoming digest and generated digest matches then the XML
//			// is
//			// authentic otherwise it has been tempered in the path
//			/*
//			 * if (inDigest.equals(genDigest)) {
//			 * logger.info("Authentic Response"); isValidResponse = true; } else
//			 * { logger.fatal("Invalid Response."); isValidResponse = false; }
//			 */
//		} else if (this.isCMCall) {
//			String cmIdentifierKeyReceived = responseWrapper.getHeaders()
//					.get(CommonConstant.CM_IDENTIFIER_KEY_HEADER_NAME).get(0);
//			if (cmIdentifierKeyReceived.equals(GatewayConfiguration
//					.getInstance().getCmIdentifierKey())) {
//				logger.info("Authentic Response");
//				isValidResponse = true;
//			} else {
//				logger.fatal("Invalid Response from Central Mapper.");
//				isValidResponse = false;
//			}
//		} else {
//			// No validation required
//			isValidResponse = true;
//		}
//
//		return isValidResponse;
//	}
	
	/*public static void main(String []args) throws Exception {
	    Document doc = null;
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
	    doc = dbf.newDocumentBuilder().parse(new InputSource(new FileReader("C:/BBPS/BBPS_ProjectGitView/bbps/DataPump/src/main/resources/BatchBillPaymentRequest.xml")));
	    DataPumpSignatureUtil.generateXMLDigitalSignature(doc, privateKey, publicKey);
	    DataPumpRestTemplateInterceptor interceptor = new DataPumpRestTemplateInterceptor();
	    System.out.println(interceptor.isXmlDigitalSignatureValid(doc,publicKey));
	}*/
}
