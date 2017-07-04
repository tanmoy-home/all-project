package com.rssoftware.ou.common.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.ReqDiagnostic;
import org.bbps.schema.ResDiagnostic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.RequestStatus;

public class ConverterUtils {

	private final static Logger logger = LoggerFactory.getLogger(ConverterUtils.class);

	static JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(BillPaymentRequest.class,
					BillPaymentResponse.class, Ack.class, ReqDiagnostic.class,
					ResDiagnostic.class, BillFetchRequest.class,
					BillFetchResponse.class);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
			jaxbContext = null;
		}
	}

	public static RequestStatus getStatus(BillFetchResponse billFetchResponse) {
		// TODO: look into the object and find the exact status
		return RequestStatus.RESPONSE_SUCCESS;
	}

	public static RequestStatus getPaymentStatus(
			BillPaymentResponse billPaymentResponse) {
		// TODO: look into the object and find the exact status
		return RequestStatus.RESPONSE_SUCCESS;
	}

	public static BillFetchRequest jaxbXMLToBillFetchRequest(String request) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(request);
			BillFetchRequest req = (BillFetchRequest) unmarshaller
					.unmarshal(reader);
			return req;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static BillFetchResponse jaxbXMLToBillFetchResponse(String resp) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(resp);
			BillFetchResponse res = (BillFetchResponse) unmarshaller
					.unmarshal(reader);
			return res;
		} catch (JAXBException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static BillPaymentRequest jaxbXMLToBillPaymentRequest(String request) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(request);
			BillPaymentRequest req = (BillPaymentRequest) unmarshaller
					.unmarshal(reader);
			return req;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static BillPaymentResponse jaxbXMLToBillPaymentResponse(String resp) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(resp);
			BillPaymentResponse res = (BillPaymentResponse) unmarshaller
					.unmarshal(reader);
			return res;
		} catch (JAXBException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static String jaxbAckToString(Ack ack) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(ack, writer);
			return writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static Ack jaxbStringToAck(String resp) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(resp);
			Ack ack = (Ack) unmarshaller.unmarshal(reader);
			return ack;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static String jaxbResDiagnosticToString(ResDiagnostic res) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(res, writer);
			return writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static String jaxbBillFetchRequestToString(
			BillFetchRequest billFetchRequest) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(billFetchRequest, writer);
			return writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static String jaxbBillFetchResponseToString(
			BillFetchResponse billFetchResponse) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(billFetchResponse, writer);
			return writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static String jaxbBillPaymentRequestToString(
			BillPaymentRequest billPaymentRequest) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(billPaymentRequest, writer);
			return writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	public static String jaxbBillPaymentResponseToString(
			BillPaymentResponse billPaymentResponse) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(billPaymentResponse, writer);
			return writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}
}