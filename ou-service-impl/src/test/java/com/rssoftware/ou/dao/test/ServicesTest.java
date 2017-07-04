package com.rssoftware.ou.dao.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.DeviceTagNameType;
import org.bbps.schema.DeviceType.Tag;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.database.entity.global.TenantDatasource;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.ParamConfig.DataType;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.domain.BillerResponseParams.AmountOption;
import com.rssoftware.ou.helper.CBSISOHelper;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.service.TenantDataSourceManager;
import com.rssoftware.ou.service.TenantDatasourceService;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.TemplateService;
import com.rssoftware.ou.tenant.service.TenantParamService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@EnableRetry
public class ServicesTest {
	@Autowired
	private ParamService paramService; 

	@Autowired
	private TenantDetailService tenantDetailService; 

	@Autowired
	private TenantDatasourceService tenantDatasourceService; 

	@Autowired
	private TenantDataSourceManager tenantDataSourceManager; 

	@Autowired
	private TenantParamService tenantParamService; 
	
	@Autowired
	private ResponseCodeService responseCodeService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private BillerService billerService; 
	
	@Autowired
	private IDGeneratorService idGeneratorService; 
	
	/*@Autowired
	CBSISOHelper cBSISOHelper;*/

	@Test
	@Ignore
	public void generateID(){
		TransactionContext.putTenantId("OU13");
		String id = idGeneratorService.getUniqueID(35,"OU13");
		System.out.println(id);
	}

	
	
	@Test
	@Ignore
	public void insertTenant(){
		TenantDetail td = new TenantDetail();
		td.setTenantId("OU13");
		td.setOuName("OU13");
		String retu = tenantDetailService.create(td);
		System.out.println(retu);
		System.out.println(tenantDetailService.fetchAll().size());
	}

	@Test
	@Ignore
	public void insertTenantDatasource(){
		TenantDatasource td = new TenantDatasource();
		td.setTenantId("OU13");
		td.setUrl("jdbc:oracle:thin:@172.18.17.54:1521:XE");
		td.setUserName("tenant_ou13");
		td.setUserPassword("Ou13tenant123");
		td.setDriverClassName("oracle.jdbc.OracleDriver");
		td.setMaxActive(new BigDecimal(10));
		td.setMaxIdle(new BigDecimal(2));
		td.setRemoveAbandoned("true");
		td.setRemoveAbandonedTimeout(new BigDecimal(30));
		td.setTestOnBorrow("true");
		td.setValidationQuery("SELECT 1 from dual");

		String retu = tenantDatasourceService.create(td);
		System.out.println(retu);
	}

	@Test
	@Ignore
	public void fetchTenantDatasource(){
		System.out.println(tenantDatasourceService.fetchAll().size());
		System.out.println(tenantDatasourceService.fetchByTenantId("OU13").getDriverClassName());
	}

	@Test
	@Ignore
	public void registerTenantDatasource(){
		tenantDataSourceManager.prepareDatasourceForTenant("OU13");
		DataSource ds = BeanLocator.getBean(TransactionContext.BEAN_DATA_SOURCE_PREFIX+"OU13", DataSource.class);
		HibernateTransactionManager htm = BeanLocator.getBean(TransactionContext.BEAN_HIBERNATE_TRANSACTION_MANAGER_PREFIX+"OU13", HibernateTransactionManager.class);
		System.out.println(ds);
		System.out.println(htm);
	}


	@Test
	@Ignore
	public void insertResponseCodes(){
		TransactionContext.putTenantId("OU13");
		
		responseCodeService.save("000", RequestType.FETCH, CommonConstants.RESP_SUCCESS_MSG, false, null);
		responseCodeService.save("000", RequestType.PAYMENT, CommonConstants.RESP_SUCCESS_MSG, false, null);
		responseCodeService.save("000", RequestType.PAYMENT, CommonConstants.RESP_SUCCESS_MSG, true, null);
		responseCodeService.save("001", RequestType.FETCH, CommonConstants.RESP_FAILURE_MSG, false, "Fail 1");
		responseCodeService.save("001", RequestType.PAYMENT, CommonConstants.RESP_FAILURE_MSG, false, "Fail 2");
		responseCodeService.save("001", RequestType.PAYMENT, CommonConstants.RESP_FAILURE_MSG, true, "Fail 3");

	}
	
	@Test
	@Ignore
	public void getResponseCodeDesc(){
		TransactionContext.putTenantId("OU13");
		
		String description = responseCodeService.getDescription("001", RequestType.PAYMENT, CommonConstants.RESP_FAILURE_MSG, false);
		System.out.println(description);
	}

	@Test
	@Ignore
	public void insertTemplate(){
		TransactionContext.putTenantId("OU13");
		
		templateService.saveGlobalTemplate("testGlobal", "testGlobal");
		templateService.saveTenantTemplate("testTenant", "testTenant");
	}

	@Test
	@Ignore
	public void insertImageTemplate() throws IOException{
		TransactionContext.putTenantId("OU13");
		byte[] pic = Files.readAllBytes(Paths.get("D:\\Prasenjit\\download.png"));
		templateService.saveGlobalTemplate("Yes_Bank_Logo", pic);
	}

	@Test
	@Ignore
	public void insertJPEGImageTemplate() throws IOException{
		TransactionContext.putTenantId("OU13");
		byte[] pic = Files.readAllBytes(Paths.get("/home/rsdpp/Pictures/apple.jpeg"));
		templateService.saveTenantTemplate("apple", pic);
	}
	
	@Test
	@Ignore
	public void getTemplate(){
		TransactionContext.putTenantId("OU13");
		
		String temp1 = templateService.retrieveStringTemplateByName("testGlobal");
		System.out.println(temp1);
		
		String temp2 = templateService.retrieveStringTemplateByName("testTenant");
		System.out.println(temp2);
	}

	@Test
	@Ignore
	public void insertParam(){
		TransactionContext.putTenantId("OU13");
		
		paramService.saveGlobalParam("testGlobal", "testGlobal");
		paramService.saveTenantParam("testTenant", "testTenant");
	}
	
	@Test
	@Ignore
	public void getParam(){
		TransactionContext.putTenantId("OU13");
		
		String temp1 = paramService.retrieveStringParamByName("testGlobal");
		System.out.println(temp1);
		
		String temp2 = paramService.retrieveStringParamByName("testTenant");
		System.out.println(temp2);
	}

	@Test
	@Ignore
	public void insertCSSTemplate() throws IOException{
		TransactionContext.putTenantId("OU13");
		byte[] css = Files.readAllBytes(Paths.get("D:\\Prasenjit\\Projects\\NPCI\\BBPOU\\Design\\css\\bbpou.css"));
		templateService.saveTenantTemplate("BBPOUCSS", new String(css));
	}

	@Test
	@Ignore
	public void insertJavascriptTemplate() throws IOException{
		TransactionContext.putTenantId("OU13");
		String testJS = "function testJS(){ \n"
				+ " alert('Hi'); \n"
				+ "} \n";
	
		templateService.saveTenantTemplate("testJS", testJS);
	}

	@Test
	@Ignore
	public void insertTestParams(){
		TransactionContext.putTenantId("OU13");
		
		paramService.saveTenantParam("testJS", "/resource/javascript/OU13/testJS.js");
		paramService.saveTenantParam("BBPOUCSS", "/resource/css/OU13/testCSS.css");
		paramService.saveTenantParam("Yes_Bank_Logo", "/resource/image-png/OU13/download");
	}
	
	@Test
	@Ignore
	public void insertHTMLTemplate() throws IOException{
		TransactionContext.putTenantId("OU13");
		templateService.saveTenantTemplate("testHTML", testHTML);
	}

	
	String testHTML = 
			"<HTML>"+
			"<HEAD>"+
			"<TITLE>Your Title Here</TITLE>"+
			"<link rel=\"stylesheet\" href=\"[${testCSS}]\" />"+
			"</HEAD>"+
			"<script language=\"JavaScript\" src=\"[${testJS}]\"></script>"+
			"<BODY BGCOLOR=\"FFFFFF\">"+
			"<CENTER><IMG SRC=\"[${testImage}]\" ALIGN=\"BOTTOM\" onclick=\"testJS()\"> </CENTER>"+
			"<HR>"+
			"<a href=\"http://somegreatsite.com\">Link Name</a>"+
			"s a link to another nifty site"+
			"<H1>This is a Header</H1>"+
			"<H2>This is a Medium Header</H2>"+
			"Send me mail at <a href=\"mailto:support@yourcompany.com\">"+
			"support@yourcompany.com</a>."+
			"<P> This is a new paragraph!"+
			"<P> <B>This is a new paragraph!</B>"+
			"<BR> <B><I>This is a new sentence without a paragraph break, in bold italics.</I></B>"+
			"<HR>"+
			"</BODY>"+
			"</HTML> ";

	
	@Test
	@Ignore
	public void getBillerCategories() throws IOException{
		TransactionContext.putTenantId("OU13");
		
		String[] billerCategories = billerService.getBillerCategories(new String[]{PaymentChannel.Internet_Banking.name()}, new String[]{PaymentMode.Internet_Banking.name()});
		System.out.println(Arrays.toString(billerCategories));
	}
	
	@Test
	@Ignore
	public void insertBiller(){
		TransactionContext.putTenantId("OU13");
		
		BillerView billerView = new BillerView();
		billerView.setBlrId("VODA0TESTMUM03");
		billerView.setBlrName("VODAFONE");
		billerView.setParentBlr(false);
		billerView.setBlrAcceptsAdhoc(false);
		billerView.setBlrCategoryName("Telecom");
		billerView.setBlrCommumicationAdrline("testAdr");
		billerView.setBlrCommumicationCity("test city");
		billerView.setBlrCommumicationState("test state");
		billerView.setBlrCommumicationCountry("India");
		billerView.setBlrCommumicationCountry("700001");

		billerView.setBlrRegisteredAdrline("testAdr");
		billerView.setBlrRegisteredCity("test city");
		billerView.setBlrRegisteredState("test state");
		billerView.setBlrRegisteredCountry("India");
		billerView.setBlrRegisteredCountry("700001");

		
		List<PaymentModeLimit> paymentModeList = new ArrayList<PaymentModeLimit>();
		PaymentModeLimit pml = new PaymentModeLimit();
		pml.setPaymentMode(PaymentMode.Internet_Banking);
		paymentModeList.add(pml);
		billerView.setBillerPaymentModes(paymentModeList);
		
		List<PaymentChannelLimit> paymentChannelLimit = new ArrayList<PaymentChannelLimit>();
		PaymentChannelLimit pcl = new PaymentChannelLimit();
		pcl.setPaymentChannel(PaymentChannel.ATM);
		paymentChannelLimit.add(pcl);
		
		pcl = new PaymentChannelLimit();
		pcl.setPaymentChannel(PaymentChannel.Bank_Branch);
		paymentChannelLimit.add(pcl);
		
		pcl = new PaymentChannelLimit();
		pcl.setPaymentChannel(PaymentChannel.Internet_Banking);
		paymentChannelLimit.add(pcl);
		
		pcl = new PaymentChannelLimit();
		pcl.setPaymentChannel(PaymentChannel.Kiosk);
		paymentChannelLimit.add(pcl);
		
		pcl = new PaymentChannelLimit();
		pcl.setPaymentChannel(PaymentChannel.Mobile);
		paymentChannelLimit.add(pcl);
		
		pcl = new PaymentChannelLimit();
		pcl.setPaymentChannel(PaymentChannel.MPOS);
		paymentChannelLimit.add(pcl);
		
		pcl = new PaymentChannelLimit();
		pcl.setPaymentChannel(PaymentChannel.POS);
		paymentChannelLimit.add(pcl);
		
		billerView.setBillerPaymentChannels(paymentChannelLimit);
		
		List<ParamConfig> billerCustParamsList = new ArrayList<ParamConfig>();
		ParamConfig pc = new ParamConfig();
		pc.setParamName("test cust param");
		pc.setDataType(DataType.ALPHANUMERIC);
		pc.setOptional(false);
		billerCustParamsList.add(pc);
		
		pc = new ParamConfig();
		pc.setParamName("test cust param2");
		pc.setDataType(DataType.NUMERIC);
		pc.setOptional(false);
		billerCustParamsList.add(pc);
		
		pc = new ParamConfig();
		pc.setParamName("test cust param3");
		pc.setDataType(DataType.ALPHANUMERIC);
		pc.setOptional(true);
		billerCustParamsList.add(pc);
		
		billerView.setBillerCustomerParams(billerCustParamsList);
		
		List<ParamConfig> billerAddnlInfoList = new ArrayList<ParamConfig>();
		pc = new ParamConfig();
		pc.setParamName("test addl param");
		pc.setDataType(DataType.ALPHANUMERIC);
		pc.setOptional(false);
		billerAddnlInfoList.add(pc);
		
		pc = new ParamConfig();
		pc.setParamName("test addl param2");
		pc.setDataType(DataType.NUMERIC);
		pc.setOptional(false);
		billerAddnlInfoList.add(pc);
		
		pc = new ParamConfig();
		pc.setParamName("test addl param3");
		pc.setDataType(DataType.ALPHANUMERIC);
		pc.setOptional(true);
		billerAddnlInfoList.add(pc);

		billerView.setBillerAdditionalInfo(billerAddnlInfoList);

		BillerResponseParams brp = new BillerResponseParams();
		pc = new ParamConfig();
		pc.setParamName("test resp param");
		pc.setDataType(DataType.ALPHANUMERIC);
		pc.setOptional(false);
		brp.getParams().add(pc);
		
		pc = new ParamConfig();
		pc.setParamName("test resp param2");
		pc.setDataType(DataType.NUMERIC);
		pc.setOptional(false);
		brp.getParams().add(pc);
		
		pc = new ParamConfig();
		pc.setParamName("test resp param3");
		pc.setDataType(DataType.ALPHANUMERIC);
		pc.setOptional(true);
		brp.getParams().add(pc);

		BillerResponseParams.AmountOption ao = new BillerResponseParams.AmountOption();
		List<String> amountBreakups = new ArrayList<String>();
		amountBreakups.add(BillerResponseParams.BASE_BILL_AMOUNT);
		ao.setAmountBreakups(amountBreakups);
		brp.getAmountOptions().add(ao);
		billerView.setBillerResponseParams(brp);
		
		billerView.setEntityStatus(EntityStatus.ACTIVE);
		billerView.setBlrEffctvFrom(null);
		billerView.setBlrEffctvTo(null);

		billerService.save(billerView);
		
		billerView.setBlrId("VODA0TESTMUM01");
		billerView.setBlrName("VODAFONE_Jaali1");
		billerService.save(billerView);
		
		billerView.setBlrId("VODA0TESTMUM02");
		billerView.setBlrName("VODAFONE_Jaali2");
		billerService.save(billerView);

	}	
	
	
	/*@Test
	//@Ignore
	public void getProperties() {
		CBSISOHelper cBSISOHelper= new CBSISOHelper();
		
		String value=cBSISOHelper.getPropretyFiles("IDBI_URL");
		System.out.println("Prop Files..."+value);
		
	}*/
}