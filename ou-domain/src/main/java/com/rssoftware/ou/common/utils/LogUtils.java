package com.rssoftware.ou.common.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerFetchRequest;
import org.bbps.schema.BillerFetchResponse;
import org.bbps.schema.ReqDiagnostic;
import org.bbps.schema.ResDiagnostic;
import org.bbps.schema.TxnStatusComplainRequest;
import org.bbps.schema.TxnStatusComplainResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.domain.FinTransactionDetails;

import in.co.rssoftware.bbps.schema.PaymentReceipt;
import in.co.rssoftware.bbps.schema.PaymentRequest;

public class LogUtils {

	private final static Logger logger = LoggerFactory.getLogger(LogUtils.class);

	static String errorCd = "";
	static JAXBContext jaxbContext;

	static {
		try {
			jaxbContext = JAXBContext.newInstance(Ack.class, BillFetchRequest.class, BillFetchResponse.class,
					BillPaymentRequest.class, BillPaymentResponse.class, ReqDiagnostic.class, ResDiagnostic.class,
					TxnStatusComplainRequest.class, TxnStatusComplainResponse.class, PaymentRequest.class,
					FinTransactionData.class, BillerFetchResponse.class, PaymentReceipt.class,BillerFetchRequest.class);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
			jaxbContext = null;
		}
	}

	public static void logReqRespMessage(Object request, String refId, Action action) {
		if (null != action){
			//System.out.println(action.alias());
		}
		
		try {
			if (refId == null)
				refId = "";

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter actionRequest = new StringWriter();
			jaxbMarshaller.marshal(request, actionRequest);
			String payXml = actionRequest.toString();
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// jaxbMarshaller.marshal(request, baos);
			logger.info(
					"\n------------------------------------------------------------------------------------------------------------------------------------\n"
							+ " \n" + payXml
							+ "\n------------------------------------------------------------------------------------------------------------------------------------\n\n");

		} catch (Exception ex) {
			logger.info("Got JAXB Ecpetion in ProcessBBPSReqResController while logging message");
		}
	}

}
