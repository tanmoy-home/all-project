package com.rssoftware.ou.dao.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.bbps.schema.AgentType;
import org.bbps.schema.AmountType;
import org.bbps.schema.AmtType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillerType;
import org.bbps.schema.DeviceTagNameType;
import org.bbps.schema.DeviceType;
import org.bbps.schema.DeviceType.Tag;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.QckPayType;
import org.bbps.schema.SpltPayType;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.ClearingFeeObj;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.FeeCalculationHelper;
import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.common.MTI;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.ServiceTaxFeeTypes;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.global.TenantDatasource;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.RAWFile;
import com.rssoftware.ou.database.entity.tenant.SettlementFile;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.CertificateView;
import com.rssoftware.ou.model.tenant.ComplaintDispositionView;
import com.rssoftware.ou.model.tenant.ComplaintserviceReasonsView;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.ServiceTaxConfView;
import com.rssoftware.ou.model.tenant.ServiceTaxView;
import com.rssoftware.ou.model.tenant.SettlementFileView;
import com.rssoftware.ou.service.ComplaintService;
import com.rssoftware.ou.service.TenantDataSourceManager;
import com.rssoftware.ou.service.TenantDatasourceService;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CertificateService;
import com.rssoftware.ou.tenant.service.ComplaintDispositionService;
import com.rssoftware.ou.tenant.service.ComplaintserviceReasonsService;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;
import com.rssoftware.ou.tenant.service.PGService;
import com.rssoftware.ou.tenant.service.RawDataService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.ServiceTaxConfService;
import com.rssoftware.ou.tenant.service.ServiceTaxService;
import com.rssoftware.ou.tenant.service.SettlementFileService;
import com.rssoftware.ou.tenant.service.TenantParamService;
import com.rssoftware.ou.tenant.service.TenantTemplateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@EnableRetry
public class StaticDataInputService {
	@Autowired
	private TenantParamService tenantParamService;

	@Autowired
	private TenantTemplateService tenantTemplateService;

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private SettlementFileService settlementFileService;

	@Autowired
	private BillerService billerService;
	
	@Autowired
	private InterchangeFeeConfService interchangeFeeConfService;

	@Autowired
	private InterchangeFeeService interchangeFeeService;
	
	@Autowired
	private ServiceTaxConfService serviceTaxConfService;
	
	@Autowired
	private ServiceTaxService serviceTaxService;
	
	@Autowired
	private ResponseCodeService responseCodeService;

	@Autowired
	private PGService pgService;

	private String homeDirectory = System.getenv("OU_HOME");

	@Autowired
	private TenantDatasourceService tenantDatasourceService;

	@Autowired
	private TenantDetailService tenantDetailService;
	
	@Autowired
	private RawDataService rawDataService;
	
	@Autowired
	private ApplicationConfigService applicationConfigService;
	
	@Autowired
	private TenantDataSourceManager tenantDataSourceManager;
	
	@Autowired
	ComplaintService complaintService;
	
	@Autowired
	ComplaintserviceReasonsService complaintserviceReasonsService;
	
	@Autowired
	ComplaintDispositionService complaintDispositionService;

	@Test
	@Ignore
	public void insertTenantDataSource() {

		TenantDatasource tds = new TenantDatasource();
		tds.setTenantId(System.getProperty("tenantId"));
		tds.setJndiName("jdbc/OU22");
		tds.setMaxActive(BigDecimal.TEN);
		tds.setMaxIdle(BigDecimal.valueOf(2l));
		tds.setRemoveAbandoned("true");
		tds.setRemoveAbandonedTimeout(BigDecimal.valueOf(30L));
		tds.setTestOnBorrow("true");
		tds.setUrl("jdbc:postgresql://localhost:5432/bbps_ou?currentSchema=tenant_OU22");
		tds.setUserName("postgres");
		tds.setUserPassword("postgres");
		tds.setValidationQuery("SELECT 1");
		tds.setDriverClassName("org.postgresql.Driver");

		tenantDatasourceService.create(tds);
	}

	@Test
	@Ignore
	public void insertTenantDetail() {

		TenantDetail td = new TenantDetail();
		td.setTenantId(System.getProperty("tenantId"));
		td.setOuName("OU22");

		tenantDetailService.create(td);
	}
	
	@Test
	@Ignore
	public void chk() {
		List<ComplaintserviceReasonsView> list=complaintserviceReasonsService.fetchServiceReasonsByAgent();
		for(ComplaintserviceReasonsView csr:list){
			System.out.println("------------------------------------Agent----------------------------------------"+csr);
		}
		List<ComplaintserviceReasonsView> list1=complaintserviceReasonsService.fetchServiceReasonsByBiller();
		for(ComplaintserviceReasonsView csr:list1){
			System.out.println("------------------------------------Biller----------------------------------------"+csr);
		}
		List<ComplaintserviceReasonsView> list2=complaintserviceReasonsService.fetchServiceReasonsBySystem();
		for(ComplaintserviceReasonsView csr:list2){
			System.out.println("------------------------------------System----------------------------------------"+csr);
		}
	}
	@Test
	@Ignore
	public void insertPGFields() throws IOException {
		/*
		 * <input type=hidden id="order_id" name="order_id" value=""/> <input
		 * type=hidden id="transaction_ref_id" name="transaction_ref_id"
		 * value=""/> <input type=hidden id="merchant_id" name="merchant_id"
		 * value="1234"/> <input type=hidden id="payment_method_type"
		 * name="payment_method_type" value="netbanking"/> <input type=hidden
		 * id="payment_method" name="payment_method" value="netBanking_ICICI"/>
		 * <input type=hidden id="amount" name="amount" value=""/> <input
		 * type=hidden id="redirect_after_payment" name="redirect_after_payment"
		 * value="[${returnURI}]"/> <input type=hidden id="format" name="format"
		 * value=""/>
		 */

		/*
		 * pgService.savePGIntegrationField("order_id",
		 * PGIntegrationFieldType.REQ,
		 * "[J${com.rssoftware.ou.common.utils.CommonUtils.extractValueFromContext("
		 * + CommonConstants.REF_ID + ")}J]");
		 * pgService.savePGIntegrationField("transaction_ref_id",
		 * PGIntegrationFieldType.REQ,
		 * "[J${com.rssoftware.ou.common.utils.CommonUtils.extractValueFromContext("
		 * + CommonConstants.TXN_REF_ID + ")}J]");
		 * pgService.savePGIntegrationField("merchant_id",
		 * PGIntegrationFieldType.REQ, "1234567890");
		 * pgService.savePGIntegrationField("payment_method_type",
		 * PGIntegrationFieldType.REQ, "netbanking");
		 * pgService.savePGIntegrationField("payment_method",
		 * PGIntegrationFieldType.REQ, "netBanking_ICICI");
		 * pgService.savePGIntegrationField("amount",
		 * PGIntegrationFieldType.REQ,
		 * "[J${com.rssoftware.ou.common.utils.CommonUtils.extractValueFromContext("
		 * + CommonConstants.PAYMENT_AMOUNT + ")}J]");
		 * pgService.savePGIntegrationField("redirect_after_payment",
		 * PGIntegrationFieldType.REQ,
		 * "https://localhost:9000/resource/receive-pg-response/"+ System.getProperty("tenantId"));
		 * pgService.savePGIntegrationField("format",
		 * PGIntegrationFieldType.REQ, "form-submit");
		 */
		// PG Data for DUMMY

		pgService.savePGIntegrationField("order_id_return", PGIntegrationFieldType.REQ,
				"[L${" + CommonConstants.ORDER_ID + "}L]");
		pgService.savePGIntegrationField("transaction_ref_id_return", PGIntegrationFieldType.REQ,
				"[L${merchantTxnId)}L]");
		pgService.savePGIntegrationField("merchant_id", PGIntegrationFieldType.REQ, "1234567890");
		pgService.savePGIntegrationField("payment_method_type", PGIntegrationFieldType.REQ, "netbanking");
		pgService.savePGIntegrationField("payment_method", PGIntegrationFieldType.REQ, "netBanking_ICICI");
		pgService.savePGIntegrationField("amount_return", PGIntegrationFieldType.REQ,
				"[J${com.rssoftware.ou.common.utils.CommonUtils.extractValueFromContext("
						+ CommonConstants.PAYMENT_AMOUNT + ")}J]");
		pgService.savePGIntegrationField("redirect_after_payment", PGIntegrationFieldType.REQ,
				"https://localhost:9000/resource/pg-response/" + System.getProperty("tenantId"));
		pgService.savePGIntegrationField("format", PGIntegrationFieldType.REQ, "form-submit");

		// PG Data for CITRUS

		/*
		 * pgService.savePGIntegrationField("merchantTxnId",
		 * PGIntegrationFieldType.REQ, "[L${merchantTxnId)}L]");
		 * pgService.savePGIntegrationField("currency",
		 * PGIntegrationFieldType.REQ, "INR");
		 * pgService.savePGIntegrationField("orderAmount",
		 * PGIntegrationFieldType.REQ, "[L${orderAmount}L]");
		 * pgService.savePGIntegrationField("returnUrl",
		 * PGIntegrationFieldType.REQ,
		 * "https://bbpoutestpay.rssoftware.co.in/resource/pg-response/" +
		 * System.getProperty("tenantId")); pgService.savePGIntegrationField("secSignature",
		 * PGIntegrationFieldType.REQ, "[L${secSignature}L]");
		 * pgService.savePGIntegrationField("notifyUrl",
		 * PGIntegrationFieldType.REQ,
		 * "https://bbpoutestpay.rssoftware.co.in/resource/pg-response/"+
		 * System.getProperty("tenantId")); pgService.savePGIntegrationField("format",
		 * PGIntegrationFieldType.REQ, "form-submit");
		 */
	}

	@Test
	@Ignore
	public void insertExpressions() throws IOException {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		pgService.saveExpression(CommonConstants.EXPRESSION_IS_PAYMENT_SUCCESS, "'SUCCESS'.equals(o.get('status'))");
		pgService.saveExpression(CommonConstants.EXPRESSION_GET_PAYMENT_INSTRUMENT,
				"o.get('ifsc') + '|' + o.get('ac_num')");
		pgService.saveExpression(CommonConstants.EXPRESSION_GET_AUTH_CODE, "o.get('auth_code')");
		pgService.saveExpression(CommonConstants.EXPRESSION_GET_TRAN_REF_ID, "o.get('transactionRefId')");
	}

	@Test
	@Ignore
	public void insertTemplate() throws IOException {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		tenantTemplateService.saveTenantTemplate("tenantHeader",
				new String(Files.readAllBytes(Paths.get(homeDirectory + "/Misc/Files/tenantHeader"))));
		tenantTemplateService.saveTenantTemplate("tenantBillPayHTML",
				new String(Files.readAllBytes(Paths.get(homeDirectory + "/Misc/Files/tenantBillPayHTML"))));
		tenantTemplateService.saveTenantTemplate("BBPOUCSS",
				new String(Files.readAllBytes(Paths.get(homeDirectory + "/Misc/Files/BBPOUCSS"))));
		tenantTemplateService.saveTenantTemplate("BBPOULOGO",
				Files.readAllBytes(Paths.get(homeDirectory + "/Misc/Files/BBPOULOGO")));
		tenantTemplateService.saveTenantTemplate("sideMenuSwitchImage",
				Files.readAllBytes(Paths.get(homeDirectory + "/Misc/Files/sideMenuSwitchImage")));
		// tenantTemplateService.saveTenantTemplate("tenantNBPayForm",
		// Files.readAllBytes(Paths.get(homeDirectory+"/Misc/Files/tenantNBPayForm")));

	}

	@Test
	@Ignore
	public void insertParam() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		tenantParamService.saveTenantParam("BBPOUCSS", "/resource/css/" + System.getProperty("tenantId") + "/BBPOUCSS.css");
		tenantParamService.saveTenantParam("BBPOULOGO", "/resource/image-png/" + System.getProperty("tenantId") + "/BBPOULOGO");
		tenantParamService.saveTenantParam("OU_NAME", "One Bank Ltd");
		tenantParamService.saveTenantParam("SideMenuSwitch",
				"/resource/image-png/" + System.getProperty("tenantId") + "/sideMenuSwitchImage");
		// tenantParamService.saveTenantParam(CommonConstants.CU_DOMAIN,
		// "https://172.18.2.32:443");
		tenantParamService.saveTenantParam(CommonConstants.CU_DOMAIN, "https://localhost:443");
		// tenantParamService.saveTenantParam(CommonConstants.DEFAULT_AGENT,
		// "OU01DP01525818209130");
		tenantParamService.saveTenantParam(CommonConstants.DEFAULT_AGENT, "OU12OXYG000001123458");
		tenantParamService.saveTenantParam(CommonConstants.DEFAULT_MAC_ADDRESS, "08:00:27:ad:97:1e");
		// tenantParamService.saveTenantParam("returnURI",
		// "https://172.25.4.151:9000/resource/NBResponse/");
		tenantParamService.saveTenantParam(CommonConstants.PAYMENT_GATEWAY_URL_PARAM_KEY,
				"https://localhost:9000/resource/dummyPG");
		tenantParamService.saveTenantParam("PG_NAME", "DUMMY");
		tenantParamService.saveTenantParam("RECON_MAX_TRY", "1");
	}

	@Test
//	@Ignore
	public void insertCertificates() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));

		CertificateView ce = new CertificateView();
		ce.setOrgId(CommonConstants.BBPS_SYSTEM_NAME);
		ce.setCertType(CertificateView.CertType.SIGNER);
		ce.setCrtCerFile(convert(homeDirectory + "/config/certificate/CU/signer.crt"));
		ce.setCrtCerAlias("1");
		populateFromAndToDates(ce);
		certificateService.save(ce);

		ce = new CertificateView();
		ce.setOrgId(CommonConstants.BBPS_SYSTEM_NAME);
		ce.setCertType(CertificateView.CertType.SSL);
		ce.setCrtCerFile(convert(homeDirectory + "/config/certificate/CU/haproxy.crt"));
		ce.setCrtCerAlias("1");
		populateFromAndToDates(ce);
		certificateService.save(ce);

		ce = new CertificateView();
		ce.setOrgId(TransactionContext.getTenantId());
		ce.setCertType(CertificateView.CertType.SIGNER);
		ce.setKeyFile(convert(homeDirectory + "/config1/certificate/OU/key_file"));
		// ce.setPemFile(pemFile);
		ce.setP12File(convert(homeDirectory + "/config1/certificate/OU/p12_file"));
		ce.setKeyAlias("1");
		ce.setKeyPwd("npciupi");
		ce.setCsrFile(convert(homeDirectory + "/config1/certificate/OU/csr_file"));
		ce.setCrtCerFile(convert(homeDirectory + "/config1/certificate/OU/crt_csr_file"));
		ce.setCrtCerAlias("1");
		populateFromAndToDates(ce);
		certificateService.save(ce);

	}

	@Test
	@Ignore
	public void insertSettlementFinRawFile() throws IOException {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		SettlementFileView sFileView = new SettlementFileView();
		sFileView.setTenantId(System.getProperty("tenantId"));
		sFileView.setFileName("00003OU132016071800.xml");
		sFileView.setFileType(SettlementFile.FileType.FINRAW);
		Path path = Paths.get(homeDirectory + "/Misc/Files/00003OU132016071800.xml");		
		sFileView.setFile(Files.readAllBytes(path));
		settlementFileService.insert(sFileView);
	}
	
	@Test
	@Ignore
	public void insertRawData() throws IOException {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		RawDataView rawDataView = new RawDataView();
		rawDataView.setTenantId(System.getProperty("tenantId"));
		rawDataView.setSettlementCycleId("00320160718");
		rawDataView.setRefId("OU130000000000000000000000000001006");
		rawDataView.setTxnType(RequestType.PAYMENT);
		rawDataView.setSplitPay(false);
		rawDataView.setReversal(false);
		rawDataView.setDecline(false);
		rawDataView.setCasProcessed(false);
		
		rawDataService.insert(rawDataView);
	}

	@Test
	@Ignore
	public void insertBillerDetails() throws IOException{
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		
		BillerView billerView = new BillerView();
		billerView.setBlrId("VODAACBI010079");
		billerView.setBlrName("ACBI");
		billerView.setParentBlr(false);
		billerView.setBlrAcceptsAdhoc(false);
		billerView.setEntityStatus(EntityStatus.ACTIVE);
		billerView.setBlrEffctvFrom("20160628");
		billerView.setBlrEffctvTo("20161031");

		InterchangeFeeConfView confView = new InterchangeFeeConfView();
		confView.setBlrId("VODAACBI010079");
		confView.setMti(MTI.PAYMENT);
		confView.setPaymentMode(PaymentMode.Credit_Card);
		confView.setPaymentChannel(PaymentChannel.Mobile);
		confView.setResponseCode("000");
		confView.setEffectiveFrom("20160628");
		confView.setEffectiveTo("20161031");

		List<String> fees = new ArrayList<>();
		fees.add("CCF1");fees.add("CCF2");fees.add("CCF3");
		confView.setFees(fees);
		confView.setMti(MTI.PAYMENT);

		List<InterchangeFeeConfView> interchangeConfViewList = new ArrayList<>();
		interchangeConfViewList.add(confView);
		billerView.setInterchangeFeeConfView(interchangeConfViewList);

		TransactionContext.putTenantId(System.getProperty("tenantId"));
		InterchangeFeeView ifview = new InterchangeFeeView();
		ifview.setFeeId(UUID.randomUUID().getLeastSignificantBits());
		ifview.setBillerId("VODAAIRTE26999");
		ifview.setFeeCode("CCF2");
		ifview.setFeeDesc("Customer Convenience Fee 2");
		ifview.setFeeDirection(InterchangeFeeDirectionType.C2B);
		ifview.setFlatFee(new BigDecimal("20.00"));
		ifview.setPercentFee(new BigDecimal("5.69"));
		ifview.setTranAmtRangeMax(1000000l);
		ifview.setTranAmtRangeMin(0l);
		ifview.setEffctvFrom("20160603");
		ifview.setEffctvTo("20161103");

		List<InterchangeFeeView> interchangeFeeView = new ArrayList<>();
		interchangeFeeView.add(ifview);
		billerView.setInterchangeFeeView(interchangeFeeView);

		List<BillerView> biilerViewList = new ArrayList<>();
		biilerViewList.add(billerView);

		billerService.refreshBillers(biilerViewList);

	}

	@Test
	@Ignore
	public void insertInterchangeFeeConf() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		InterchangeFeeConfView iFeeConfView = new InterchangeFeeConfView();
		iFeeConfView.setBlrId("VODA00000MUM03");
		iFeeConfView.setFees(Arrays.asList("CCF1,CCF2,CCF3".split(",")));
		iFeeConfView.setMti(MTI.PAYMENT);
		iFeeConfView.setPaymentMode(PaymentMode.Cash);
		iFeeConfView.setPaymentChannel(PaymentChannel.ATM);
		iFeeConfView.setResponseCode("000");
		iFeeConfView.setEffectiveFrom("20160628");
		iFeeConfView.setEffectiveTo("20161031");
		interchangeFeeConfService.save(iFeeConfView);
		System.out.println(interchangeFeeConfService.fetchAllInterchangeFeeConfByBillerId("VODA00000MUM03").size());
		System.out.println(interchangeFeeConfService.deleteAllInterchangeFeeConfByBillerId("VODA00000MUM03"));
		System.out.println(interchangeFeeConfService.fetchAllInterchangeFeeConfByBillerId("VODA00000MUM03").size());
	}

	@Test
	@Ignore
	public void insertInterchangeFee() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		InterchangeFeeView ifview = new InterchangeFeeView();
		ifview.setBillerId("VODA00000MUM03");
		ifview.setFeeCode("CCF1");
		ifview.setFeeDesc("Customer Convenience Fee 1");
		ifview.setFeeDirection(InterchangeFeeDirectionType.C2B);
		ifview.setFlatFee(new BigDecimal("20.00"));
		ifview.setPercentFee(new BigDecimal("5.69"));
		ifview.setTranAmtRangeMax(10000000l);
		ifview.setTranAmtRangeMin(0l);
		ifview.setEffctvFrom("20160603");
		ifview.setEffctvTo("20161103");
		interchangeFeeService.save(ifview);
		System.out.println(interchangeFeeService.fetchAllInterchangeFeeByBillerId("VODA00000MUM03").size());
		System.out.println(interchangeFeeService.deleteAllInterchangeFeeByBillerId("VODA00000MUM03"));
		System.out.println(interchangeFeeService.fetchAllInterchangeFeeByBillerId("VODA00000MUM03").size());
	}
	
	@Test
	@Ignore
	public void insertServiceTaxConf() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		ServiceTaxConfView sTaxConfView = new ServiceTaxConfView();
		sTaxConfView.setBillerCategory("Utility");
		sTaxConfView.setCodes(Arrays.asList("STAX,SBC,KKC".split(",")));
		sTaxConfView.setFeeType(ServiceTaxFeeTypes.INTERCHANGE);
		sTaxConfView.setFeeSubType("C2B");
		sTaxConfView.setEffctvFrom("20160101");
		sTaxConfView.setEffctvTo("20200101");
		serviceTaxConfService.save(sTaxConfView);
		
		System.out.println("#################### Service Tax Conf List Size "+serviceTaxConfService.fetchAllActiveList().size());		
	}
	
	@Test
	@Ignore
	public void insertServiceTax() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		ServiceTaxView sTaxView = new ServiceTaxView();
		sTaxView.setCode("STAX");
		sTaxView.setDescription("Service Tax @14.0%");
		sTaxView.setPercentTax(new BigDecimal("14"));
		sTaxView.setEffectiveFrom("20160101");
		sTaxView.setEffectiveTo("20200101");
		serviceTaxService.save(sTaxView);

		sTaxView = new ServiceTaxView();
		sTaxView.setCode("SBC");
		sTaxView.setDescription("Swachh Bharat Cess @0.5%");
		sTaxView.setPercentTax(new BigDecimal("0.5"));
		sTaxView.setEffectiveFrom("20160101");
		sTaxView.setEffectiveTo("20200101");
		serviceTaxService.save(sTaxView);
		
		sTaxView = new ServiceTaxView();
		sTaxView.setCode("KKC");
		sTaxView.setDescription("Krishi Kalyan Cess @0.5%");
		sTaxView.setPercentTax(new BigDecimal("0.5"));
		sTaxView.setEffectiveFrom("20160101");
		sTaxView.setEffectiveTo("20200101");
		serviceTaxService.save(sTaxView);
		
		System.out.println("#################### Service Tax List Size "+serviceTaxService.fetchAllActiveList().size());
	}
	
	@Test
	@Ignore
	public void checkResponseCode() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));	
		responseCodeService.refresh();
		System.out.println("#################### Payment Successful response code " + responseCodeService.getSuccessfulResponseCode(RequestType.PAYMENT, false));
	}
	
	@Test
	@Ignore
	public void calculateCCF() {
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
		AmountType amount = new AmountType();
		AmtType amt = new AmtType();
		amt.setAmount("999");
		amount.setAmt(amt);
		billPaymentRequest.setAmount(amount);
		
		String billerId = "VODA00000MUM03";
		String billerCategory = "Utility";
		String DEFAULT_SUCCESS_RESPONSE_CODE = "000";
		
		BillDetailsType bilLDetailsType = new BillDetailsType();
		BillerType billerType = new BillerType();
		billerType.setId(billerId);
		bilLDetailsType.setBiller(billerType);
		billPaymentRequest.setBillDetails(bilLDetailsType);
		
		AgentType agentType = new AgentType();
		agentType.setId("OU131234567890");
		DeviceType deviceType = new DeviceType();
		Tag tag = new Tag();
		tag.setName(DeviceTagNameType.INITIATING_CHANNEL);
		tag.setValue("ATM");
		deviceType.getTags().add(tag);
		agentType.setDevice(deviceType);
		billPaymentRequest.setAgent(agentType);
		
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode("Cash");
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		billPaymentRequest.setPaymentMethod(pmtMtdType);
		
		List<InterchangeFeeConfView> icFeeConfs = interchangeFeeConfService.fetchAllInterchangeFeeConfByBillerId(billerId);
		List<InterchangeFeeView> icFees = interchangeFeeService.fetchAllInterchangeFeeByBillerId(billerId);
		
		List<InterchangeFeeConfView> nonDefaultICConfs = new ArrayList<>(2);
		List<InterchangeFeeConfView> defaultICConfs = new ArrayList<>(2);
		
		for (InterchangeFeeConfView icConf:icFeeConfs){
			if (icConf != null){
				if (icConf.isDefault()){
					defaultICConfs.add(icConf);
				}
				else {
					nonDefaultICConfs.add(icConf);
				}
			}
		}
		
		Map<String, List<InterchangeFeeView>> iFeeMap = new HashMap<String, List<InterchangeFeeView>>();
		for (InterchangeFeeView fee:icFees){
			if (fee != null && fee.getFeeCode() != null){
				if (iFeeMap.get(fee.getFeeCode()) == null){
					iFeeMap.put(fee.getFeeCode(), new ArrayList<InterchangeFeeView>(2));
				}
				iFeeMap.get(fee.getFeeCode()).add(fee);
			}
		}
		
		List<ServiceTaxConfView> taxConfs = serviceTaxConfService.fetchAllActiveList();
		List<ServiceTaxView> taxes = serviceTaxService.fetchAllActiveList();
		
		Map<String, List<ServiceTaxView>> serviceTaxMap = new HashMap<>();
		
		if (taxes != null){
			for (ServiceTaxView serviceTax:taxes){
				if (serviceTax != null && serviceTax.getCode() != null){
					if (serviceTaxMap.get(serviceTax.getCode()) == null){
						serviceTaxMap.put(serviceTax.getCode(), new ArrayList<ServiceTaxView>());
					}
					
					serviceTaxMap.get(serviceTax.getCode()).add(serviceTax);	
				}
			}
		}
		
		ClearingFeeObj cfo = FeeCalculationHelper.calculateCustomerConvenienceFee(billPaymentRequest, DEFAULT_SUCCESS_RESPONSE_CODE, nonDefaultICConfs, iFeeMap);	
		
		if (cfo.getBouInterchangeFees() != null && !cfo.getBouInterchangeFees().isEmpty()
				&& cfo.getCouInterchangeFees() != null && !cfo.getCouInterchangeFees().isEmpty()){
			// nothing to do int fees are already calculated
		}
		else { // try with the default fees
			cfo = FeeCalculationHelper.calculateCustomerConvenienceFee(billPaymentRequest, DEFAULT_SUCCESS_RESPONSE_CODE , defaultICConfs, iFeeMap);
		}
		
		FeeCalculationHelper.calculateInterchangeFeeTaxes(cfo, billerCategory, InterchangeFeeDirectionType.C2B, taxConfs, serviceTaxMap);
		System.out.println("############# CCF Plus Tax "+cfo.getBOUTotalCCFPlusTax().toPlainString());
	}
	
	@Test
	@Ignore
	public void checkSettlementFileProcessor() throws IOException {
		TransactionContext.putTenantId(System.getProperty("tenantId"));

		SettlementFileView sFileView = new SettlementFileView();
		sFileView.setTenantId(System.getProperty("tenantId"));
		sFileView.setFileName("00003OU132016071800.xml");
		sFileView.setFileType(SettlementFile.FileType.FINRAW);
		Path path = Paths.get(homeDirectory + "/Misc/Files/00003OU132016071800.xml");		
		sFileView.setFile(Files.readAllBytes(path));		
		settlementFileService.insert(sFileView);
		
		if(SettlementFile.FileType.FINRAW == sFileView.getFileType()) {
			try {
				Unmarshaller unmarshaller = JAXBContext.newInstance(RAWFile.class).createUnmarshaller();
				RAWFile rawFile = (RAWFile)unmarshaller.unmarshal(new ByteArrayInputStream(sFileView.getFile()));
				rawFile.getTransactions().forEach( rawFileTransation -> {
					RawDataView rawDataView = new RawDataView();
					rawDataView.setRefId(rawFileTransation.getRefId());
					rawDataView.setTxnType(
							RequestType.valueOf(rawFileTransation.getTxnType()) != null?
									RequestType.valueOf(rawFileTransation.getTxnType()) :
										RequestType.FETCH);
					rawDataView.setCasProcessed(
							rawFileTransation.isCasProcessed() != null && rawFileTransation.isCasProcessed().booleanValue() == true ?
									true : false);
					rawDataView.setDecline(
							rawFileTransation.isDecline() != null && rawFileTransation.isDecline().booleanValue() == true ?
									true : false);
					rawDataView.setReversal(				
							rawFileTransation.isReversal() != null && rawFileTransation.isReversal().booleanValue() == true ?
									true : false);
					rawDataView.setSplitPay(
							rawFileTransation.isSplitPay() != null && rawFileTransation.isSplitPay().booleanValue() == true ?
									true : false);
					try {
						rawDataService.insert(rawDataView);
					} catch (Exception e) {
						e.printStackTrace();
					}				
				});				
			} catch (JAXBException e) {
				e.printStackTrace();
			}			
		}
	}
	
	@Test
	@Ignore
	public void checkApplicationConfigService() throws IOException {
		//tenantDataSourceManager.initializeTenantDatasources();
		
		for (TenantDetail td:tenantDetailService.fetchAll()){
			TransactionContext.putTenantId(td.getTenantId());
			try {
				applicationConfigService.refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		TransactionContext.putTenantId(System.getProperty("tenantId"));
		System.out.println("########################################################################"); 
		System.out.println(applicationConfigService.getPaymentModeChannelMap().get(PaymentMode.Cash.getExpandedForm()));
		System.out.println(applicationConfigService.getPaymentInfoMap().get(PaymentMode.Cash.getExpandedForm()));
		System.out.println(applicationConfigService.getAgentDevMap().get(PaymentChannel.ATM.getExpandedForm()));
		System.out.println("########################################################################");
	}
	
	private static byte[] convert(String file) {
		try {
			byte[] data = Files.readAllBytes((new File(file)).toPath());
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void populateFromAndToDates(CertificateView cv) {
		if (cv != null) {
			try {
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				InputStream is = new ByteArrayInputStream(cv.getCrtCerFile());
				InputStream caInput = new BufferedInputStream(is);
				Certificate ca;
				try {
					ca = cf.generateCertificate(caInput);

					if (ca instanceof X509Certificate) {
						X509Certificate xca = (X509Certificate) ca;
						cv.setValidFrom(CommonUtils.getFormattedDateyyyyMMdd(xca.getNotBefore()));
						cv.setValidUpto(CommonUtils.getFormattedDateyyyyMMdd(xca.getNotAfter()));
					}

				} finally {
					try {
						caInput.close();
					} catch (IOException e) {
					}
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			} catch (CertificateException e) {
				e.printStackTrace();
			}
		}
	}
}
