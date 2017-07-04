package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.common.BillStatus;
import com.rssoftware.ou.common.BillerMode;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.database.entity.tenant.MyBillerDetail;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.MyBillerView;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.dao.BillDetailsDao;
import com.rssoftware.ou.tenant.dao.BillFileConfigDao;
import com.rssoftware.ou.tenant.dao.BillMappingConfigDao;
import com.rssoftware.ou.tenant.dao.MyBillerDetailDao;
import com.rssoftware.ou.tenant.service.BillerOUtransactionDataService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.MyBillerDetailService;
@Service
public class MyBillerDetailServiceImpl implements MyBillerDetailService{
	@Autowired
	BillFileConfigDao billFileConfigDao;
	
	@Autowired
    BillMappingConfigDao billMappingConfigDao;
	
	@Autowired
	MyBillerDetailDao myBillerDetailDao;
	
	@Autowired
	private BillerOUtransactionDataService billerOUtransactionDataService;
	
	@Autowired
	private AsyncProcessor asyncProcessor;
	
	@Autowired
	private TenantDetailService tenantDetailService;

	@Autowired
	BillDetailsDao billDetailsDao;
	
	@Value("${BASIC_AUTH_CREDENTIAL}")
    private String BASIC_AUTH_CREDENTIAL;
	
	@Autowired
	private IDGeneratorService idGenetarorService;
	
	private static ObjectMapper mapper = new ObjectMapper();
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void save(MyBillerView billerView) {
		MyBillerDetail billerDetail = mapTo(billerView);
		myBillerDetailDao.createOrUpdate(billerDetail);
		/*BillFileConfig billFileConfig = mapToBillFileConfig(billerView);
		billFileConfigDao.createOrUpdate(billFileConfig);
		for (BillMappingConfigView bmc:billerView.getBillMappingConfigList()){
			BillMappingConfig billMappingConfig=mapToBillMappingConfig(bmc);
			billMappingConfig.setBlrId(billerView.getBlrId());
			billMappingConfigDao.createOrUpdate(billMappingConfig);
		}*/
	}

	/*private BillMappingConfig mapToBillMappingConfig(BillMappingConfigView bmcView) {
		BillMappingConfig bmc=new BillMappingConfig();
		bmc.setStartPosition(bmcView.getStartPosition());
		bmc.setEndPosition(bmcView.getEndPosition());
		bmc.setFieldDataType(bmcView.getFieldDataType());
		bmc.setFieldFormat(bmcView.getFieldFormat());
		bmc.setFieldQualifier(bmcView.getFieldQualifier());
		bmc.setSequenceNo(bmcView.getSequenceNo());
		return bmc;
	}

	private BillFileConfig mapToBillFileConfig(MyBillerView billerView) {
		BillFileConfig billFileConfig=billFileConfigDao.get(billerView.getBlrId());
		if(billFileConfig==null){
			billFileConfig=new BillFileConfig();
		}
		billFileConfig.setBlrId(billerView.getBlrId());
		billFileConfig.setDateFormat(billerView.getDateFormat());
		billFileConfig.setDelimiter(billerView.getDelimiter());
		billFileConfig.setFileType(billerView.getFileType());
		billFileConfig.setRootElement(billerView.getRootElement());
		billFileConfig.setTargetClassName(billerView.getTargetClassName());
		return billFileConfig;
	}*/

	private MyBillerDetail mapTo(MyBillerView billerView) {
		MyBillerDetail billerDetail = myBillerDetailDao.get(billerView.getBlrId());
		if (null == billerDetail) {
			billerDetail = new MyBillerDetail();
			billerDetail.setCrtnTs(new Timestamp(new Date().getTime()));
			billerDetail.setBlrId(billerView.getBlrId());
		} else {
			billerDetail.setUpdtTs(new Timestamp(new Date().getTime()));
		}
		billerDetail.setBlrName(billerView.getBlrName());
		billerDetail.setBlrAliasName(billerView.getBlrAliasName());
		billerDetail.setParentBlrId(billerView.getParentBlrId());
		billerDetail.setBlrAcceptsAdhoc(billerView.isBlrAcceptsAdhoc());
		billerDetail.setBlrCategoryName(billerView.getBlrCategoryName());

		billerDetail.setBlrRegisteredAdrline(billerView.getBlrRegisteredAdrline());
		billerDetail.setBlrRegisteredCity(billerView.getBlrRegisteredCity());
		billerDetail.setBlrRegisteredState(billerView.getBlrRegisteredState());
		billerDetail.setBlrRegisteredCountry(billerView.getBlrRegisteredCountry());
		billerDetail.setBlrRegisteredPinCode(billerView.getBlrRegisteredPinCode());

		billerDetail.setBlrCommumicationAdrline(billerView.getBlrCommumicationAdrline());
		billerDetail.setBlrCommumicationCity(billerView.getBlrCommumicationCity());
		billerDetail.setBlrCommumicationState(billerView.getBlrCommumicationState());
		billerDetail.setBlrCommumicationCountry(billerView.getBlrCommumicationCountry());
		billerDetail.setBlrCommumicationPinCode(billerView.getBlrCommumicationPinCode());
		
		billerDetail.setBillerAuthLetter1(billerView.getBillerAuthLetter1());
		billerDetail.setBillerAuthLetter1Name(billerView.getBillerAuthLetter1Name());
		billerDetail.setBillerAuthLetter2(billerView.getBillerAuthLetter2());
		billerDetail.setBillerAuthLetter2Name(billerView.getBillerAuthLetter2Name());
		billerDetail.setBillerAuthLetter3(billerView.getBillerAuthLetter3());
		billerDetail.setBillerAuthLetter3Name(billerView.getBillerAuthLetter3Name());
		try {
			billerDetail.setBlrPaymentChannels(mapper.writeValueAsString(billerView.getBillerPaymentChannels()));
			billerDetail.setBlrPaymentModes(mapper.writeValueAsString(billerView.getBillerPaymentModes()));
			billerDetail.setBlrCustomerParams(mapper.writeValueAsString(billerView.getBillerCustomerParams()));
			billerDetail.setBlrAdditionalInfo(mapper.writeValueAsString(billerView.getBillerAdditionalInfo()));
			billerDetail.setBlrResponseParams(mapper.writeValueAsString(billerView.getBillerResponseParams()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new RuntimeException(e);
		}
		billerDetail.setEntityStatus(billerView.getEntityStatus().name());
		billerDetail.setBlrEffctvFrom(billerView.getBlrEffctvFrom());
		billerDetail.setBlrEffctvTo(billerView.getBlrEffctvTo());
		billerDetail.setEndpointURL(billerView.getBlrEndpointURL());
		billerDetail.setBlrMode(billerView.getBlrMode().name());
		billerDetail.setFetchRequirement(billerView.getFetchRequirement());
		return billerDetail;
	}

	@Override
	public void delete(String billerId) {
		MyBillerDetail biller = myBillerDetailDao.get(billerId);
		myBillerDetailDao.delete(biller);
		/*biller.setEntityStatus(EntityStatus.DELETED.name());
		biller.setDeletedTs(new Timestamp(new Date().getTime()));
		biller.setDeletedUserId(CommonUtils.getLoggedInUser());
		myBillerDetailDao.update(biller);*/
	}
	@Override
	public void reject(String billerId) {
		MyBillerDetail biller = myBillerDetailDao.get(billerId);
		biller.setEntityStatus(EntityStatus.REJECTED.name());
		biller.setRejectedTs(new Timestamp(new Date().getTime()));
		biller.setRejectedUserId(CommonUtils.getLoggedInUser());
		myBillerDetailDao.update(biller);
	}
	@Override
	public void approve(String billerId) {
		MyBillerDetail biller = myBillerDetailDao.get(billerId);
		biller.setEntityStatus(EntityStatus.APPROVED.name());
		biller.setApprovedTs(new Timestamp(new Date().getTime()));
		biller.setApprovedUserId(CommonUtils.getLoggedInUser());
		myBillerDetailDao.update(biller);
	}
	@Override
	public MyBillerView getBillerById(String billerId) throws IOException{
		MyBillerDetail billerDetail = myBillerDetailDao.get(billerId);
		return mapFrom(billerDetail);
	}
	@Override
	public List<MyBillerView> getActiveBillers() throws IOException{
		List<MyBillerDetail> activeBillers = myBillerDetailDao.getActiveBillers();
		List<MyBillerView> billers = new ArrayList<>();
		for (MyBillerDetail activeBiller : activeBillers) {
			billers.add(mapFrom(activeBiller));
		}
		return billers;
	}
	private static MyBillerView mapFrom(MyBillerDetail billerDetail) throws IOException{
		if (null == billerDetail) {
			return null;
		}
		MyBillerView billerView = new MyBillerView();
		billerView.setBlrId(billerDetail.getBlrId());
		billerView.setBlrName(billerDetail.getBlrName());
		billerView.setBlrAliasName(billerDetail.getBlrAliasName());
		billerView.setBlrAcceptsAdhoc(billerDetail.getBlrAcceptsAdhoc()!=null?billerDetail.getBlrAcceptsAdhoc():false);
		billerView.setBlrCategoryName(billerDetail.getBlrCategoryName());
		billerView.setParentBlrId(billerDetail.getParentBlrId());

		billerView.setBlrCommumicationAdrline(billerDetail.getBlrCommumicationAdrline());
		billerView.setBlrCommumicationCity(billerDetail.getBlrCommumicationCity());
		billerView.setBlrCommumicationState(billerDetail.getBlrCommumicationState());
		billerView.setBlrCommumicationPinCode(billerDetail.getBlrCommumicationPinCode());
		billerView.setBlrCommumicationCountry(billerDetail.getBlrCommumicationCountry());

		billerView.setBlrRegisteredAdrline(billerDetail.getBlrRegisteredAdrline());
		billerView.setBlrRegisteredCity(billerDetail.getBlrRegisteredCity());
		billerView.setBlrRegisteredState(billerDetail.getBlrRegisteredState());
		billerView.setBlrRegisteredPinCode(billerDetail.getBlrRegisteredPinCode());
		billerView.setBlrRegisteredCountry(billerDetail.getBlrRegisteredCountry());

		try {
			if (StringUtils.isNotBlank(billerDetail.getBlrPaymentModes())) {
				List<PaymentModeLimit> paymentModeList = mapper.readValue(billerDetail.getBlrPaymentModes(),
						TypeFactory.defaultInstance().constructCollectionType(List.class, PaymentModeLimit.class));
				billerView.setBillerPaymentModes(paymentModeList);
			}
			if (StringUtils.isNotBlank(billerDetail.getBlrPaymentChannels())) {
				List<PaymentChannelLimit> paymentChannelLimit = mapper.readValue(billerDetail.getBlrPaymentChannels(),
						TypeFactory.defaultInstance().constructCollectionType(List.class, PaymentChannelLimit.class));
				billerView.setBillerPaymentChannels(paymentChannelLimit);
			}
			if (StringUtils.isNotBlank(billerDetail.getBlrCustomerParams())) {
				List<ParamConfig> billerCustParamsList = mapper.readValue(billerDetail.getBlrCustomerParams(),
						TypeFactory.defaultInstance().constructCollectionType(List.class, ParamConfig.class));
				billerView.setBillerCustomerParams(billerCustParamsList);
			}
			if (StringUtils.isNotBlank(billerDetail.getBlrAdditionalInfo())) {
				List<ParamConfig> billerAddnlInfoList = mapper.readValue(billerDetail.getBlrAdditionalInfo(),
						TypeFactory.defaultInstance().constructCollectionType(List.class, ParamConfig.class));
				billerView.setBillerAdditionalInfo(billerAddnlInfoList);
			}
			if (StringUtils.isNotBlank(billerDetail.getBlrResponseParams())) {
				billerView.setBillerResponseParams(mapper.readValue(billerDetail.getBlrResponseParams(),
						TypeFactory.defaultInstance().constructType(BillerResponseParams.class)));
			}

			/* Added for incorporating the addition of new fields in XSD */
			/*
			 * if
			 * (StringUtils.isNotBlank(billerDetail.getInterchangeFeeConf())){
			 * billerView.setInterchangeFeeConf(mapper.readValue(billerDetail.
			 * getInterchangeFeeConf(),
			 * TypeFactory.defaultInstance().constructType(InterchangeFeeConf.
			 * class))); } if
			 * (StringUtils.isNotBlank(billerDetail.getInterchangeFee())){
			 * billerView.setInterchangeFee(mapper.readValue(billerDetail.
			 * getInterchangeFee(),
			 * TypeFactory.defaultInstance().constructType(InterchangeFee.class)
			 * )); }
			 */
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		billerView.setEntityStatus(EntityStatus.valueOf(billerDetail.getEntityStatus()));
		billerView.setBlrEffctvFrom(billerDetail.getBlrEffctvFrom());
		billerView.setBlrEffctvTo(billerDetail.getBlrEffctvTo());

		/* Added for incorporating the addition of new fields in XSD */
		billerView.setBlrOwnerShp(billerDetail.getBlrOwnership());
		billerView.setBlrCoverage(billerDetail.getBlrCoverage());
		billerView.setBlrEndpointURL(billerDetail.getEndpointURL());
		billerView.setBlrMode(billerDetail.getBlrMode()!=null?BillerMode.valueOf(billerDetail.getBlrMode()):null);		billerView.setFetchRequirement(billerDetail.getFetchRequirement());
		return billerView;
	}
	
@Override
public void updateBillDetails(String billNo, BillStatus status) {
   	BillDetails bill=billDetailsDao.get(billNo);
	bill.setStatus(status.name());
	bill.setUpdtTs(new Timestamp(System.currentTimeMillis()));
	try {
		billDetailsDao.createOrUpdate(bill);
	} catch (Exception e) {
		e.printStackTrace();
	}
}


@Override
public List<String> getAllCurrentBillerIds() {
	return myBillerDetailDao.getAllCurrentBillerIds();
}



}