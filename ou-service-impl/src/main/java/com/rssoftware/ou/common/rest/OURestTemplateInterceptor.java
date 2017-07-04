package com.rssoftware.ou.common.rest;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.tenant.service.CertificateService;

public class OURestTemplateInterceptor implements ClientHttpRequestInterceptor {
	
	private static Log logger = LogFactory.getLog(OURestTemplateInterceptor.class);
	
	private static CertificateService certificateService = null;
	

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		try {
			body = signXML(request, body);

			int contentLength = body.length;
			if (request.getHeaders().containsKey("Content-Length")) {
				request.getHeaders().remove("Content-Length");
			}
			request.getHeaders().add("Content-Length",
					String.valueOf(contentLength));
			
			logger.info("Outgoing Signed XML : " + new String(body));
			
			ClientHttpResponse response = execution.execute(request, body);
			return response;

		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
				| SAXException | ParserConfigurationException
				| MarshalException | XMLSignatureException
				| TransformerException e) {
			throw new IOException(e);
		}
	}

	private byte[] signXML(HttpRequest request, byte[] body)
			throws IOException, SAXException, ParserConfigurationException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			MarshalException, XMLSignatureException, TransformerException {
		Document doc = BbpsSignatureUtil.getXmlDocument(new String(body));
		
		if (certificateService == null){
			certificateService = BeanLocator.getBean(CertificateService.class);
		}
		Key[] keys = certificateService.getOUSignerPrivatePublicKey();
		BbpsSignatureUtil.generateXMLDigitalSignature(doc, (PrivateKey)keys[0],
				(PublicKey)keys[1]);

		return BbpsSignatureUtil.convert(doc).getBytes();
	}

}
