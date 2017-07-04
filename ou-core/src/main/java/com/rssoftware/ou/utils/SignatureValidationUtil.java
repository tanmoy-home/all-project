package com.rssoftware.ou.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.cert.Certificate;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.ParserConfigurationException;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerFetchRequest;
import org.bbps.schema.BillerFetchResponse;
import org.bbps.schema.ReqDiagnostic;
import org.bbps.schema.TxnStatusComplainRequest;
import org.bbps.schema.TxnStatusComplainResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.rest.BbpsSignatureUtil;
import com.rssoftware.ou.tenant.service.CertificateService;

public class SignatureValidationUtil {
	private static CertificateService certificateService;
	
	private final static Logger log = LoggerFactory.getLogger(SignatureValidationUtil.class);
	
	static JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(Ack.class, BillFetchRequest.class, BillFetchResponse.class,
					BillPaymentRequest.class, BillPaymentResponse.class, ReqDiagnostic.class, 
					TxnStatusComplainRequest.class, TxnStatusComplainResponse.class, BillerFetchRequest.class, BillerFetchResponse.class);
		} catch (JAXBException e) {
			    log.error( e.getMessage(), e);
	            log.info("In Excp : " + e.getMessage());
			jaxbContext = null;
		}
	}

	private static Object validateXMLTag(String requestBody) throws IOException, JAXBException, ValidationException{
		
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		jaxbUnMarshaller.setEventHandler(new ValidationEventHandler() {
	        @Override
	        public boolean handleEvent(ValidationEvent event) {
                if (event.getMessage().indexOf(CommonConstants.SIGNATURE_NODE_PATTERN) >= 0){
                	return true;
                }
                return false;
	        }
        });
		
		return jaxbUnMarshaller.unmarshal(new StringReader(requestBody));
	}
	
	public static Object unmarshall(HttpServletRequest request)
			throws ValidationException {
		if (certificateService == null){
			certificateService = BeanLocator.getBean(CertificateService.class);
		}
		
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);

			String requestBody = bufferedReqest.getRequestBody();
			
			if (!checkSignature(requestBody)){
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.SIGNATURE_MISMATCH);
			}
			
			try {
				return validateXMLTag(requestBody);
			}
			catch (Exception e){
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_XML);
			}
			
		} catch (Exception e) {
			if (e instanceof ValidationException){
				throw (ValidationException)e;
			}
			
			log.error( e.getMessage(), e);
            log.info("In Excp : " + e.getMessage());
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.ISE);
		}

	}


	public static boolean checkSignature(String requestBody){
		Document doc;
		try {
			doc = BbpsSignatureUtil.getXmlDocument(requestBody);
		} catch (SAXException | ParserConfigurationException | IOException e1) {
			return false;
		}

		Certificate cert = certificateService.getCUSignerCertificate();

		boolean verifiedSign = false;
		try {
			verifiedSign = BbpsSignatureUtil.isXmlDigitalSignatureValid(doc, cert.getPublicKey());
		} catch (Exception e) {}

		return verifiedSign;
	}


	public static final class BufferedRequestWrapper extends HttpServletRequestWrapper {
		/*
		 * The object where the request will be stored
		 */
		private ByteArrayOutputStream baos = null;

		/*
		 * the object where the request is stored in byte array format.
		 */
		private byte[] buffer = null;

		/*
		 * Following initialization is done here <ul> <li>Get InputStream from
		 * request</li> <li>Copy the InputStream to ByteArrayInputStream using
		 * while loop</li> <li>Convert ByteArrayInputStream to byte array for
		 * further use in the class</li> </ul>
		 */
		public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
			super(req);
			// Read InputStream and store its content in a buffer.
			InputStream is = req.getInputStream();
			this.baos = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int letti;

			while ((letti = is.read(buf)) > 0) {
				this.baos.write(buf, 0, letti);
			}

			this.buffer = this.baos.toByteArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.servlet.ServletRequestWrapper#getInputStream() Return
		 * BufferedServletInputStream here so that the wrapper can be used for
		 * reading request more than once.
		 */
		@Override
		public ServletInputStream getInputStream() {
			return new BufferedServletInputStream(new ByteArrayInputStream(this.buffer));
		}

		/*
		 * @return the request body in String
		 */
		String getRequestBody() throws IOException {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
			StringBuilder inputBuffer = new StringBuilder();
			try {
				String line = null;

				do {
					line = reader.readLine();

					if (null != line) {
						inputBuffer.append(line.trim());
					}

				} while (line != null);
			} finally {
				reader.close();
			}

			return inputBuffer.toString().trim();
		}
	}

	private static final class BufferedServletInputStream extends ServletInputStream {
		private ByteArrayInputStream bais;

		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		@Override
		public int available() {
			return this.bais.available();
		}

		@Override
		public int read() {
			return this.bais.read();
		}

		@Override
		public int read(byte[] buf, int off, int len) {
			return this.bais.read(buf, off, len);
		}

		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setReadListener(ReadListener arg0) {
			// TODO Auto-generated method stub

		}

	}

}
