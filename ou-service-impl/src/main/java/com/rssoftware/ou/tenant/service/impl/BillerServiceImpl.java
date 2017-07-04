package com.rssoftware.ou.tenant.service.impl;

import in.co.rssoftware.bbps.schema.BillerList;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bbps.schema.Ack;
import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.CustomerParamsType.Tag;
import org.bbps.schema.HeadType;
import org.bbps.schema.ReasonType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.TxnType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.BillStatus;
import com.rssoftware.ou.common.BillerMode;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.ExcelUtil;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.StatusField;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.common.utils.RequestResponseGenerator;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
//import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.database.entity.tenant.BillFileConfig;
import com.rssoftware.ou.database.entity.tenant.BillMappingConfig;
import com.rssoftware.ou.database.entity.tenant.BillerDetail;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.domain.ResponseCode;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillMappingConfigView;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.dao.BillDetailsDao;
import com.rssoftware.ou.tenant.dao.BillFileConfigDao;
import com.rssoftware.ou.tenant.dao.BillMappingConfigDao;
import com.rssoftware.ou.tenant.dao.BillerDao;
import com.rssoftware.ou.tenant.service.BillerOUtransactionDataService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CUService;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;

@Service
public class BillerServiceImpl implements BillerService {

	private static Logger logger = LoggerFactory.getLogger(BillerServiceImpl.class);

	@Autowired
	BillerDao billerDao;

	@Autowired
	private TenantDetailService tenantDetailService;
	/* Added for new Biller Fetch XSD */
	/*
	 * @Autowired InterchangeFeeDao interchangeFeeDao;
	 * 
	 * @Autowired InterchangeFeeConfDao interchangeFeeConfDao;
	 * 
	 * @Autowired InterchangeFeeDetailsDao interchangeFeeDetailsDao;
	 */

	/*
	 * @Autowired BillerService billerService;
	 */

	@Autowired
	private CUService cuService;

	@Autowired
	private AsyncProcessor asyncProcessor;

	@Autowired
	BillFileConfigDao billFileConfigDao;

	@Autowired
	BillMappingConfigDao billMappingConfigDao;

	/*
	 * @Autowired TransactionDataService transactionDataService;
	 */

	@Autowired
	private InterchangeFeeService interchangeFeeService;

	@Autowired
	private InterchangeFeeConfService interchangeFeeConfService;

	@Autowired
	private BillerOUtransactionDataService billerOUtransactionDataService;

	@Autowired
	BillDetailsDao billDetailsDao;

	@Value("${BASIC_AUTH_CREDENTIAL}")
	private String BASIC_AUTH_CREDENTIAL;

	private static ObjectMapper mapper = new ObjectMapper();

	@Override
	public BillerView getBillerById(String billerId) {
		BillerDetail billerDetail = billerDao.get(billerId);
		return mapFrom(billerDetail);
	}

	@Override
	public String[] getBillerCategories(String[] paymentChannels, String[] paymentModes) throws IOException {
		List<BillerDetail> billerList = billerDao.getActiveBillers();
		Map<String, BillerView> billerMap = filterBillerListIncomingPaymentChannelMode(paymentChannels, paymentModes,
				billerList, false);
		Set<String> categorySet = new TreeSet<String>();
		for (String billerId : billerMap.keySet()) {
			categorySet.add(billerMap.get(billerId).getBlrCategoryName());
		}
		return categorySet.toArray(new String[categorySet.size()]);
	}

	private static Map<String, BillerView> filterBillerListIncomingPaymentChannelMode(String[] paymentChannels,
			String[] paymentModes, List<BillerDetail> billerList, boolean includeChildBillers) throws IOException {
		Set<String> incomingPaymentChannels = new HashSet<String>();
		Set<String> incomingPaymentModes = new HashSet<String>();

		Map<String, BillerView> billerMap = new HashMap<String, BillerView>();

		Map<String, BillerView> parentFilteredbillerMap = new HashMap<String, BillerView>();

		if (paymentChannels != null) {
			for (String pc : paymentChannels) {
				incomingPaymentChannels.add(pc);
			}
		}

		if (paymentModes != null) {
			for (String pm : paymentModes) {
				incomingPaymentModes.add(pm);
			}
		}

		for (BillerDetail billerDetail : billerList) {
			Set<String> billerPaymentChannels = new HashSet<String>();
			Set<String> billerPaymentModes = new HashSet<String>();
			BillerView bv = mapFrom(billerDetail);

			if (bv.getBillerPaymentChannels() != null) {
				for (PaymentChannelLimit pcl : bv.getBillerPaymentChannels()) {
					billerPaymentChannels.add(pcl.getPaymentChannel().name());
					billerPaymentChannels.add(pcl.getPaymentChannel().getExpandedForm());
				}
			}
			if (bv.getBillerPaymentModes() != null) {
				for (PaymentModeLimit pml : bv.getBillerPaymentModes()) {
					billerPaymentModes.add(pml.getPaymentMode().name());
					billerPaymentModes.add(pml.getPaymentMode().getExpandedForm());
				}
			}
			if ((billerDetail.getIsParentBlr() == Boolean.TRUE)
					|| (billerPaymentChannels.containsAll(incomingPaymentChannels)
							&& billerPaymentModes.containsAll(incomingPaymentModes))) {
				billerMap.put(bv.getBlrId(), bv);
			}
		}

		for (String billerId : billerMap.keySet()) {
			BillerView bv = billerMap.get(billerId);
			if (!bv.isParentBlr() && !CommonUtils.hasValue(bv.getParentBlrId())) {
				parentFilteredbillerMap.put(billerId, bv);
			} else if (!bv.isParentBlr() && CommonUtils.hasValue(bv.getParentBlrId())
					&& billerMap.get(bv.getParentBlrId()) != null) {
				if (includeChildBillers) {
					parentFilteredbillerMap.put(billerId, bv);
				}
				parentFilteredbillerMap.put(bv.getParentBlrId(), billerMap.get(bv.getParentBlrId()));
			}
		}

		return parentFilteredbillerMap;
	}

	@Override
	public BillerView[] getBillersByCategory(String billerCategory, String[] paymentChannels, String[] paymentModes)
			throws IOException {
		List<BillerDetail> billerList = billerDao.getActiveBillers();

		Map<String, BillerView> billerMap = filterBillerListIncomingPaymentChannelMode(paymentChannels, paymentModes,
				billerList, false);

		Set<BillerView> filteredBillers = new TreeSet<>();

		for (String billerId : billerMap.keySet()) {
			BillerView bv = billerMap.get(billerId);
			if (CommonUtils.hasValue(billerCategory) && billerCategory.equals(bv.getBlrCategoryName())) {
				filteredBillers.add(bv);
			} else if (!CommonUtils.hasValue(billerCategory)) {
				filteredBillers.add(bv);
			}
		}

		return filteredBillers.toArray(new BillerView[filteredBillers.size()]);
	}

	@Override
	public BillerView[] getSubBillersByParentBiller(String parentBillerId, String billerCategory,
			String[] paymentChannels, String[] paymentModes) throws IOException {
		List<BillerDetail> billerList = billerDao.getActiveBillers();

		Map<String, BillerView> billerMap = filterBillerListIncomingPaymentChannelMode(paymentChannels, paymentModes,
				billerList, true);

		Set<BillerView> filteredBillers = new TreeSet<>();

		for (String billerId : billerMap.keySet()) {
			BillerView bv = billerMap.get(billerId);
			if (!CommonUtils.hasValue(parentBillerId) || (parentBillerId.equals(bv.getParentBlrId()))) {
				if (CommonUtils.hasValue(billerCategory) && billerCategory.equals(bv.getBlrCategoryName())) {
					filteredBillers.add(bv);
				} else if (!CommonUtils.hasValue(billerCategory)) {
					filteredBillers.add(bv);
				}
			}
		}

		return filteredBillers.toArray(new BillerView[filteredBillers.size()]);
	}

	@Override
	public void save(BillerView billerView) {
		BillerDetail billerDetail = mapTo(billerView);
		billerDao.createOrUpdate(billerDetail);
		/*
		 * BillFileConfig billFileConfig = mapToBillFileConfig(billerView);
		 * billFileConfigDao.createOrUpdate(billFileConfig); for
		 * (BillMappingConfigView bmc:billerView.getBillMappingConfigList()){
		 * BillMappingConfig billMappingConfig=mapToBillMappingConfig(bmc);
		 * billMappingConfig.setBlrId(billerView.getBlrId());
		 * billMappingConfigDao.createOrUpdate(billMappingConfig); }
		 */
	}

	@Override
	public void delete(String billerId) {
		BillerDetail biller = billerDao.get(billerId);
		billerDao.delete(biller);
	}

	private static BillerView mapFrom(BillerDetail billerDetail) {
		if (null == billerDetail) {
			return null;
		}
		BillerView billerView = new BillerView();
		billerView.setBlrId(billerDetail.getBlrId());
		billerView.setBlrMode(billerDetail.getBlrMode() != null ? BillerMode.valueOf(billerDetail.getBlrMode()) : null);
		billerView.setFetchRequirement(billerDetail.getFetchRequirement());
		billerView.setBlrName(billerDetail.getBlrName());
		billerView.setParentBlr(billerDetail.getIsParentBlr());
		billerView.setBlrAcceptsAdhoc(billerDetail.getBlrAcceptsAdhoc());
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
		billerView.setBillerPaymentExactness(billerDetail.getBillerPaymentExactness());

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
			logger.debug(e.getMessage(), e);
		}
		billerView.setEntityStatus(EntityStatus.valueOf(billerDetail.getEntityStatus()));
		billerView.setBlrEffctvFrom(billerDetail.getBlrEffctvFrom());
		billerView.setBlrEffctvTo(billerDetail.getBlrEffctvTo());

		/* Added for incorporating the addition of new fields in XSD */
		billerView.setBlrOwnerShp(billerDetail.getBlrOwnership());
		billerView.setBlrCoverage(billerDetail.getBlrCoverage());
		billerView.setBlrEndpointURL(billerDetail.getEndpointURL());

		return billerView;
	}

	private BillMappingConfig mapToBillMappingConfig(BillMappingConfigView bmcView) {
		BillMappingConfig bmc = new BillMappingConfig();
		bmc.setStartPosition(bmcView.getStartPosition());
		bmc.setEndPosition(bmcView.getEndPosition());
		bmc.setFieldDataType(bmcView.getFieldDataType());
		bmc.setFieldFormat(bmcView.getFieldFormat());
		bmc.setFieldQualifier(bmcView.getFieldQualifier());
		bmc.setSequenceNo(bmcView.getSequenceNo());
		return bmc;
	}

	private BillFileConfig mapToBillFileConfig(BillerView billerView) {
		BillFileConfig billFileConfig = billFileConfigDao.get(billerView.getBlrId());
		if (billFileConfig == null) {
			billFileConfig = new BillFileConfig();
		}
		billFileConfig.setBlrId(billerView.getBlrId());
		billFileConfig.setDateFormat(billerView.getDateFormat());
		billFileConfig.setDelimiter(billerView.getDelimiter());
		billFileConfig.setFileType(billerView.getFileType());
		billFileConfig.setRootElement(billerView.getRootElement());
		billFileConfig.setTargetClassName(billerView.getTargetClassName());
		return billFileConfig;
	}

	private BillerDetail mapTo(BillerView billerView) {
		BillerDetail billerDetail = billerDao.get(billerView.getBlrId());
		if (null == billerDetail) {
			billerDetail = new BillerDetail();
			billerDetail.setCrtnTs(new Timestamp(new Date().getTime()));
			billerDetail.setBlrId(billerView.getBlrId());
		} else {
			billerDetail.setUpdtTs(new Timestamp(new Date().getTime()));
		}
		billerDetail.setBlrMode(billerView.getBlrMode() != null ? billerView.getBlrMode().name() : null);
		billerDetail.setFetchRequirement(billerView.getFetchRequirement());
		billerDetail.setBlrName(billerView.getBlrName());
		billerDetail.setBlrAliasName(billerView.getBlrAliasName());
		billerDetail.setIsParentBlr(billerView.isParentBlr());
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
		try {
			billerDetail.setBlrPaymentChannels(mapper.writeValueAsString(billerView.getBillerPaymentChannels()));
			billerDetail.setBlrPaymentModes(mapper.writeValueAsString(billerView.getBillerPaymentModes()));
			billerDetail.setBlrCustomerParams(mapper.writeValueAsString(billerView.getBillerCustomerParams()));
			billerDetail.setBlrAdditionalInfo(mapper.writeValueAsString(billerView.getBillerAdditionalInfo()));
			billerDetail.setBlrResponseParams(mapper.writeValueAsString(billerView.getBillerResponseParams()));
			/* Added for incorporating the addition of new fields in XSD */
			// billerDetail.setInterchangeFeeConf(billerView.getInterchangeFeeConf());
			// billerDetail.setInterchangeFee(billerView.getInterchangeFee());

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		billerDetail.setEntityStatus(billerView.getEntityStatus().name());
		billerDetail.setBlrEffctvFrom(billerView.getBlrEffctvFrom());
		billerDetail.setBlrEffctvTo(billerView.getBlrEffctvTo());
		billerDetail.setEndpointURL(billerView.getBlrEndpointURL());
		billerDetail.setBillerPaymentExactness(billerView.getBillerPaymentExactness());
		
		return billerDetail;
	}

	@Override
	public void refreshBillers(List<BillerView> billers) throws IOException {
		Map<String, BillerView> differentialBillersMap = new HashMap<>();

		List<BillerDetail> existingBillers = billerDao.getAll();
		if (existingBillers != null) {
			for (BillerDetail bd : existingBillers) {
				differentialBillersMap.put(bd.getBlrId(), mapFrom(bd));
			}
		}

		if (billers != null) {
			for (BillerView bv : billers) {
				billerDao.createOrUpdate(mapTo(bv));
				interchangeFeeConfService.deleteAllInterchangeFeeConfByBillerId(bv.getBlrId());
				interchangeFeeService.deleteAllInterchangeFeeByBillerId(bv.getBlrId());
				for (InterchangeFeeConfView icfc : bv.getInterchangeFeeConfView()) {
					interchangeFeeConfService.save(icfc);

					for (InterchangeFeeView icf : bv.getInterchangeFeeView()) {
						interchangeFeeService.save(icf);
						differentialBillersMap.remove(bv.getBlrId());
					}
				}

				/*
				 * for(InterchangeFeeView icf : bv.getInterchangeFeeView()){
				 * for(InterchangeFeeDetailsView ifdv :
				 * icf.getInterchangeFeeDetailsView()) {
				 * interchangeFeeDetailsService.save(ifdv); }
				 * interchangeFeeService.save(icf); }
				 * 
				 * for(InterchangeFeeView ifv:bv.getInterchangeFeeView()){
				 * for(InterchangeFeeDetailsView
				 * icfdv:ifv.getInterchangeFeeDetailsView()){
				 * interchangeFeeDetailsService.save(icfdv); }
				 * 
				 * differentialBillersMap.remove(bv.getBlrId()); }
				 */
			}

			for (String billerId : differentialBillersMap.keySet()) {
				BillerView bv = differentialBillersMap.get(billerId);
				if (bv != null) {
					bv.setEntityStatus(EntityStatus.DELETED);
					billerDao.update(mapTo(bv));
				}
			}
		}

	}

	@Override
	public List<String> getBillerCategoriesForAgent(List<String> paymentChannels, List<String> paymentModes,
			AgentView agent) throws IOException {
		List<BillerDetail> billerList = billerDao.getActiveBillers();

		Map<String, BillerView> billerMap = filterBillerListOnIncomingPaymentChannelAndMode(paymentChannels,
				paymentModes, billerList, false, agent);
		Set<String> categorySet = new TreeSet<String>();
		for (String billerId : billerMap.keySet()) {
			categorySet.add(billerMap.get(billerId).getBlrCategoryName());
		}

		return new ArrayList<String>(categorySet);
	}

	private Map<String, BillerView> filterBillerListOnIncomingPaymentChannelAndMode(List<String> paymentChannels,
			List<String> paymentModes, List<BillerDetail> billerList, boolean includeChildBillers, AgentView agent)
					throws IOException {

		Map<String, BillerView> billerMap = new HashMap<String, BillerView>();

		Map<String, BillerView> parentFilteredbillerMap = new HashMap<String, BillerView>();

		List<PaymentModeLimit> agentPaymentModes = agent.getAgentPaymentModes();

		if (CollectionUtils.isEmpty(paymentModes) && CollectionUtils.isNotEmpty(agentPaymentModes)) {
			for (PaymentModeLimit paymodeLimit : agentPaymentModes) {
				paymentModes.add(paymodeLimit.getPaymentMode().name());
				paymentModes.add(paymodeLimit.getPaymentMode().getExpandedForm());
			}
		}

		List<PaymentChannelLimit> agentPaymentChannels = agent.getAgentPaymentChannels();
		if (CollectionUtils.isEmpty(paymentChannels) && CollectionUtils.isNotEmpty(agentPaymentChannels)) {
			for (PaymentChannelLimit paymentChannelLimit : agentPaymentChannels) {
				paymentModes.add(paymentChannelLimit.getPaymentChannel().name());
				paymentModes.add(paymentChannelLimit.getPaymentChannel().getExpandedForm());

			}
		}

		for (BillerDetail billerDetail : billerList) {
			BillerView bv = mapFrom(billerDetail);
			if (!billerDetail.getIsParentBlr()) {
				Set<String> billerPaymentChannels = new HashSet<String>();
				Set<String> billerPaymentModes = new HashSet<String>();

				if (bv.getBillerPaymentChannels() != null) {

					CommonUtils.getPaymentChannelsAsStrings(bv.getBillerPaymentChannels(), billerPaymentChannels);
				}

				if (bv.getBillerPaymentModes() != null) {
					CommonUtils.getPaymentModesAsStrings(bv.getBillerPaymentModes(), billerPaymentModes);

					if (billerPaymentChannels.containsAll(paymentChannels)
							& billerPaymentModes.containsAll(paymentModes)) {
						billerMap.put(bv.getBlrId(), bv);
					}
				}
			} else {
				billerMap.put(bv.getBlrId(), bv);
			}
		}

		for (String billerId : billerMap.keySet()) {
			BillerView biller = billerMap.get(billerId);

			if (!biller.isParentBlr()) {
				if (!CommonUtils.hasValue(biller.getParentBlrId())) {
					parentFilteredbillerMap.put(billerId, biller);
				} else if (CommonUtils.hasValue(biller.getParentBlrId())
						&& billerMap.get(biller.getParentBlrId()) != null) {
					// if this is child biller, then put both parent and child
					// billers in map
					parentFilteredbillerMap.put(billerId, biller);
					parentFilteredbillerMap.put(biller.getParentBlrId(), billerMap.get(biller.getParentBlrId()));
				}

			}
		}
		return parentFilteredbillerMap;

	}

	@Override
	public List<BillerView> getBillersByCategoryAndAgent(String billerCategory, List<String> paymentChannels,
			List<String> paymentModes, AgentView agent) throws IOException {
		List<BillerDetail> billerList = billerDao.getActiveBillers();

		Map<String, BillerView> billerMap = filterBillerListOnIncomingPaymentChannelAndMode(paymentChannels,
				paymentModes, billerList, false, agent);

		Set<BillerView> filteredBillers = new TreeSet<>();

		for (String billerId : billerMap.keySet()) {
			BillerView bv = billerMap.get(billerId);
			if (CommonUtils.hasValue(billerCategory) && billerCategory.equals(bv.getBlrCategoryName())) {
				filteredBillers.add(bv);
			} else if (!CommonUtils.hasValue(billerCategory)) {
				filteredBillers.add(bv);
			}
		}

		return new ArrayList<BillerView>(filteredBillers);
	}

	@Override
	public List<BillerView> searchParentBiller() {

		List<BillerView> billerList = new ArrayList<BillerView>();

		BillerView biller1 = new BillerView();
		biller1.setBlrName("Mahanagar Store-Biller");
		biller1.setBlrId("MHGL00000MUM12");

		BillerView biller2 = new BillerView();
		biller1.setBlrName("Vodafone Kolkata");
		biller1.setBlrId("MHGL00000MUM15");

		billerList.add(biller1);
		billerList.add(biller2);

		return billerList;
	}

	@Override
	public List<BillerView> searchBiller(String loggedInUsersOuId) throws ValidationException, IOException {
		List<BillerDetail> billers = billerDao.searchBiller(loggedInUsersOuId);

		if (billers != null) {
			List<BillerView> avs = new ArrayList<BillerView>(billers.size());
			for (BillerDetail bl : billers) {
				avs.add(mapFrom(bl));
			}
			return avs;
		}
		return null;
	}

	@Override
	public List<BillerView> fetchFunctionallyActiveList(int pageNo, int pageSize)
			throws ValidationException, IOException {
		List<BillerDetail> billers = billerDao.fetchAll(pageNo * pageSize, pageSize, EntityStatus.ACTIVE,
				EntityStatus.PENDING_DELETE, EntityStatus.PENDING_DEACTIVATION);

		if (billers != null) {
			List<BillerView> billerviews = new ArrayList<BillerView>(billers.size());
			for (BillerDetail biller : billers) {
				billerviews.add(mapFrom(biller));
			}
			return billerviews;
		}
		return new ArrayList<BillerView>(1);
	}

	@Override
	public List<BillerView> fetchFunctionallyActiveListForUser(int pageNo, int pageSize, String loggedInUsersOuId)
			throws ValidationException, IOException {
		List<BillerDetail> billers = billerDao.fetchAllForUser(pageNo * pageSize, pageSize, loggedInUsersOuId,
				EntityStatus.ACTIVE, EntityStatus.PENDING_DELETE, EntityStatus.PENDING_DEACTIVATION);

		if (billers != null) {
			List<BillerView> billerviews = new ArrayList<BillerView>(billers.size());

			for (BillerDetail biller : billers) {
				billerviews.add(mapFrom(biller));
			}
			return billerviews;
		}
		return new ArrayList<BillerView>(1);
	}

	@Override
	public BillerView fetchWithIdAndStatus(String billerId, String status) throws ValidationException, IOException {
		// ActionHistory actioHistory= new ActionHistory();
		BillerView bv = new BillerView();
		List<BillerDetail> billers = billerDao.fetch(billerId, EntityStatus.valueOf(status));

		if (billers != null && !billers.isEmpty()) {
			// actioHistory=
			// GatewayCommonUtils.getActionHistoryService().fetch(billerId,
			// CommonConstants.ENTITY_BILLER);
			bv = mapFrom(billers.get(0));
			/*
			 * if(actioHistory!=null){
			 * bv.setUserComments(actioHistory.getComments()); }
			 */
			return bv;
		}

		return null;

	}

	@Override
	public BillerView fetchFunctionalActiveParent(String billerId) throws ValidationException, IOException {
		List<BillerDetail> billers = billerDao.fetch(billerId, EntityStatus.ACTIVE, EntityStatus.PENDING_DELETE,
				EntityStatus.PENDING_DEACTIVATION);
		if (billers != null && !billers.isEmpty()) {
			return mapFrom(billers.get(0));
		}

		return null;

	}

	/*
	 * private BillerDetail validateRetrieveForDraft(BillerView blrview) throws
	 * ValidationException { // TODO Auto-generated method stub if (blrview ==
	 * null || !CommonUtils.hasValue(blrview.getBlrId())){ throw new
	 * ValidationException(ErrorCode.NULL_ID.name()); }
	 * 
	 * List<BillerDetail> billers = billerDao.fetch(blrview.getBlrId());
	 * 
	 * BillerDetail ret = null;
	 * 
	 * if (billers != null){ for (BillerDetail b:billers){ if
	 * (b.getEntityStatus() != EntityStatus.DRAFT.name() && b.getEntityStatus()
	 * != EntityStatus.DELETED && b.getEntityStatus() != EntityStatus.REJECTED)
	 * { throw new ValidationException(ErrorCode.INVALID_STATE.name()); }
	 * 
	 * if (b.getEntityStatus() == EntityStatus.DRAFT || b.getEntityStatus() !=
	 * EntityStatus.REJECTED) { if (ret != null){ throw new
	 * ValidationException(ErrorCode.INVALID_STATE.name()); } ret = b; } } }
	 * 
	 * return ret; }
	 */
	private BillerDetail validateRetrieveForDraft(BillerView blrview) throws ValidationException {
		if (blrview == null || !CommonUtils.hasValue(blrview.getBlrId())) {
			throw ValidationException.getInstance(ValidationErrorReason.NULL, "Biller id is null");
		}

		List<BillerDetail> billers = billerDao.fetch(blrview.getBlrId());

		BillerDetail ret = null;

		if (billers != null) {
			for (BillerDetail b : billers) {
				if (!b.getEntityStatus().equals(EntityStatus.DRAFT.name())
						&& !b.getEntityStatus().equals(EntityStatus.DELETED.name())
						&& !b.getEntityStatus().equals(EntityStatus.REJECTED.name())) {
					throw ValidationException.getInstance(ValidationErrorReason.INVALID_STATE);
				}

				if (b.getEntityStatus().equals(EntityStatus.DRAFT.name())
						|| !b.getEntityStatus().equals(EntityStatus.REJECTED.name())) {
					if (ret != null) {
						throw ValidationException.getInstance(ValidationErrorReason.INVALID_STATE);
					}
					ret = b;
				}
			}
		}

		return ret;
	}

	private BillerDetail mapTo(BillerDetail blr, BillerView blrview) {
		if (blr == null) {
			blr = new BillerDetail();
		}
		// blr.setblrId(blrview.getBillerId());
		blr.setBlrId(blrview.getBlrId());
		blr.setBlrMode(blrview.getBlrMode() != null ? blrview.getBlrMode().name() : null);
		blr.setFetchRequirement(blrview.getFetchRequirement());
		blr.setBlrName(blrview.getBlrName());
		blr.setBlrAliasName(blrview.getBlrAliasName());
		blr.setBlrCategoryName(blrview.getBlrCategoryName());

		if (blrview.getBlrLinkedOuDefault() == null || blrview.getBlrLinkedOuDefault().equals("")) {
			blr.setBlrLinkedOuDefault(null);
		} else {
			blr.setBlrLinkedOuDefault(blrview.getBlrLinkedOuDefault());
		}

		if (blrview.getBillerLinkedOuBackup1() == null || blrview.getBillerLinkedOuBackup1().equals("")) {
			blr.setBlrLinkedOuBackup1(null);
		} else {
			blr.setBlrLinkedOuBackup1(blrview.getBillerLinkedOuBackup1());
		}

		if (blrview.getBillerLinkedOuBackup2() == null || blrview.getBillerLinkedOuBackup2().equals("")) {
			blr.setBlrLinkedOuBackup2(null);
		} else {
			blr.setBlrLinkedOuBackup2(blrview.getBillerLinkedOuBackup2());
		}

		if (blrview.getBillerLinkedOuBackup3() == null || blrview.getBillerLinkedOuBackup3().equals("")) {
			blr.setBlrLinkedOuBackup3(null);
		} else {
			blr.setBlrLinkedOuBackup3(blrview.getBillerLinkedOuBackup3());
		}

		if (blrview.getParentBlrId() == null || blrview.getParentBlrId().equals("")) {
			blrview.setParentBlrId(null);
		} else {
			blr.setParentBlrId(blrview.getParentBlrId());
		}

		blr.setIsParentBlr(blrview.isParentBlr());
		blr.setBlrRegisteredAdrline(blrview.getBlrRegisteredAdrline());
		blr.setBlrRegisteredCity(blrview.getBlrRegisteredCity());
		blr.setBlrRegisteredPinCode(blrview.getBlrRegisteredPinCode());
		blr.setBlrRegisteredState(blrview.getBlrRegisteredState());
		blr.setBlrRegisteredCountry(blrview.getBlrRegisteredCountry());
		blr.setBlrCommumicationAdrline(blrview.getBlrCommumicationAdrline());
		blr.setBlrCommumicationCity(blrview.getBlrCommumicationCity());
		blr.setBlrCommumicationPinCode(blrview.getBlrCommumicationPinCode());
		blr.setBlrCommumicationState(blrview.getBlrCommumicationState());
		blr.setBlrCommumicationCountry(blrview.getBlrCommumicationCountry());
		blr.setBlrAcceptsAdhoc(blrview.isBlrAcceptsAdhoc());
		if (blrview.getBlrOwnerShp() == null || blrview.getBlrOwnerShp().equals("")) {
			blrview.setBlrOwnerShp(null);
		}
		blr.setBlrOwnership(blrview.getBlrOwnerShp());
		// to be blr.setBillerAuthLetter1(blrview.getBillerAuthLetter1());
		if (blrview.getBillerAuthLetter1Name() == null || blrview.getBillerAuthLetter1Name().equals("")) {
			blrview.setBillerAuthLetter1Name(null);
		}
		// to be
		// blr.setBillerAuthLetter1Name(blrview.getBillerAuthLetter1Name());
		// to be blr.setBillerAuthLetter2(blrview.getBillerAuthLetter2());
		if (blrview.getBillerAuthLetter2Name() == null || blrview.getBillerAuthLetter2Name().equals("")) {
			blrview.setBillerAuthLetter2Name(null);
		}
		// to be
		// blr.setBillerAuthLetter2Name(blrview.getBillerAuthLetter2Name());
		// to be blr.setBillerAuthLetter3(blrview.getBillerAuthLetter3());
		if (blrview.getBillerAuthLetter3Name() == null || blrview.getBillerAuthLetter3Name().equals("")) {
			blrview.setBillerAuthLetter3Name(null);
		}
		// to be
		// blr.setBillerAuthLetter3Name(blrview.getBillerAuthLetter3Name());
		if (blrview.getBlrAvgTicketSize() == null || blrview.getBlrAvgTicketSize().equals("")) {
			blrview.setBlrAvgTicketSize(null);
		}
		blr.setBlrAvgTicketSize(blrview.getBlrAvgTicketSize());
		/*
		 * to be if(blrview.getBillerVolmPerDay()==null ||
		 * blrview.getBillerVolmPerDay().equals("")){
		 * blrview.setBillerVolmPerDay(null); }
		 * blr.setBillerVolmPerDay(blrview.getBillerVolmPerDay());
		 * if(blrview.getBillerCoverage()==null ||
		 * blrview.getBillerCoverage().equals("")){
		 * blrview.setBillerCoverage(null); }
		 * blr.setBillerCoverage(blrview.getBillerCoverage());
		 * blr.setBillerDeafaultOuChangeVrsn
		 * (blrview.getBillerDeafaultOuChangeVrsn());
		 * blr.setBillerPaymentModes(blrview.getBillerPaymentModes());
		 * blr.setBillerPaymentChannels(blrview.getBillerPaymentChannels());
		 * 
		 * blr.setBillerEffctvFrom(blrview.getBillerEffctvFrom());
		 * 
		 * if(blrview.getBillerEffctvTo()==null ||
		 * blrview.getBillerEffctvTo().equals("")){
		 * blrview.setBillerEffctvTo(null); }
		 * blr.setBillerEffctvTo(blrview.getBillerEffctvTo());
		 * blr.setBillerTan(blrview.getBillerTan());
		 * if(blrview.getBillerUaadhaar()==null ||
		 * blrview.getBillerUaadhaar().equals("")){
		 * blrview.setBillerUaadhaar(null); }
		 * blr.setBillerUaadhaar(blrview.getBillerUaadhaar());
		 * if(blrview.getBillerRocUin()==null ||
		 * blrview.getBillerRocUin().equals("")){ blrview.setBillerRocUin(null);
		 * } blr.setBillerRocUin(blrview.getBillerRocUin());
		 * blr.setBillerCustomerParams(blrview.getBillerCustomerParams());
		 * 
		 * if (blrview.getBillerResponseParamsAmtOpts() != null){
		 * blr.setBillerResponseParams(new BillerResponseParams()); for
		 * (BillerResponseParams.AmountOption
		 * amt:blrview.getBillerResponseParamsAmtOpts().getAmountOptions()){ if
		 * (amt != null){ BillerResponseParams.AmountOption amountOpt = new
		 * BillerResponseParams.AmountOption(); for (String
		 * breakup:amt.getAmountBreakupSet()){ if (breakup != null){
		 * amountOpt.getAmountBreakupSet().add(breakup); } }
		 * blr.getBillerResponseParams().getAmountOptions().add(amountOpt); } }
		 * 
		 * for (ParamConfig
		 * p:blrview.getBillerResponseParamsAmtOpts().getParams()){ if (p !=
		 * null && CommonUtils.hasValue(p.getParamName())){ ParamConfig pc = new
		 * ParamConfig(); pc.setParamName(p.getParamName());
		 * pc.setDataType(DataType.NUMERIC); pc.setOptional(p.getOptional());
		 * blr.getBillerResponseParams().getParams().add(pc); } }
		 * 
		 * } blr.setBillerAdditionalInfo(blrview.getBillerAdditionalInfo());
		 * if(blrview.getTmpDeactvStartDt()==null ||
		 * blrview.getTmpDeactvStartDt().equals("")){
		 * blrview.setTmpDeactvStartDt(null); }
		 * if(blrview.getTmpDeactvEndDt()==null ||
		 * blrview.getTmpDeactvEndDt().equals("")){
		 * blrview.setTmpDeactvEndDt(null); }
		 * blr.setTempDeactivationStartDt(blrview.getTmpDeactvStartDt());
		 * blr.setTempDeactivationEndDt(blrview.getTmpDeactvEndDt());
		 */
		return blr;
	}

	@Override
	public BillerView saveAsDraft(BillerView blrview) throws ValidationException, IOException {
		String userId = CommonUtils.getLoggedInUser();
		// ActionHistory actionHistory= new ActionHistory();
		BillerDetail blr = null;
		try {
			blr = validateRetrieveForDraft(blrview);
		} catch (Exception e) {
			throw e;
		}
		BillerView billerViewResult = null;
		boolean isExisting = true;
		EntityStatus prevEntityStatus = null;
		if (blr == null) {
			isExisting = false;
		} else {
			prevEntityStatus = EntityStatus.valueOf(blr.getEntityStatus());
		}

		blr = mapTo(blr, blrview);

		if (!isExisting) {
			blr.setCrtnTs(CommonUtils.currentTimestamp());
			blr.setCrtnUserId(userId);
		} else {
			blr.setUpdtTs(CommonUtils.currentTimestamp());
			blr.setUpdtUserId(userId);
		}
		blr.setEntityStatus(EntityStatus.DRAFT.name());

		if (!isExisting) {
			// need to insert
			billerViewResult = mapFrom(billerDao.createOrUpdate(blr));

			/*
			 * actionHistory.setActionId(UUID.randomUUID().
			 * getLeastSignificantBits ());
			 * actionHistory.setActionType(CommonConstants.
			 * ACTION_UPDATE_SAVEASDRAFT );
			 * actionHistory.setEntityType(CommonConstants.ENTITY_BILLER);
			 * actionHistory.setEntityId(blr.getBlrId());
			 * actionHistory.setLastUpdtBy(userId);
			 * actionHistory.setLastUpdtTs(CommonUtils.currentTimestamp());
			 * actionHistory.setComments(blrview.getUserComments());
			 */
			// GatewayCommonUtils.getActionHistoryService().insert(actionHistory);
		}
		return billerViewResult;
	}

	private BillerDetail validateRetrieveForSubmit(BillerView billerView, boolean isParent) throws ValidationException {
		if (billerView == null || !CommonUtils.hasValue(billerView.getBlrId())) {
			throw ValidationException.getInstance(ValidationErrorReason.NULL, "Biller id is null");
		}
		if (billerView == null || !CommonUtils.hasValue(billerView.getBlrName())) {
			throw ValidationException.getInstance(ValidationErrorReason.NULL, "Biller name is null");
		}
		if (billerView == null || !CommonUtils.hasValue(billerView.getBlrAliasName())) {
			throw ValidationException.getInstance(ValidationErrorReason.NULL, "Biller alias name is null");
		}
		if (billerView == null || !CommonUtils.hasValue(billerView.getBlrCategoryName())) {
			throw ValidationException.getInstance(ValidationErrorReason.NULL, "Biller category name is null");
		}
		if (!isParent) {
			if (billerView == null || !CommonUtils.hasValue(billerView.getBlrLinkedOuDefault())) {
				throw ValidationException.getInstance(ValidationErrorReason.OU_NOT_AVAILABLE);
			}
		}
		/*
		 * if (billerView == null ||
		 * !CommonUtils.hasValue(billerView.getBillerLinkedOuBackup1())){ throw
		 * new ValidationException(ErrorCode.OU_NOT_AVAILABLE.name()); }
		 */

		List<BillerDetail> billers = billerDao.fetch(billerView.getBlrId());

		BillerDetail ret = null;

		if (billers != null) {
			for (BillerDetail b : billers) {
				if (!b.getEntityStatus().equals(EntityStatus.DRAFT.name())
						&& !b.getEntityStatus().equals(EntityStatus.DELETED.name())
						&& !b.getEntityStatus().equals(EntityStatus.REJECTED.name())) {
					throw ValidationException.getInstance(ValidationErrorReason.INVALID_STATE);
				}

				if (b.getEntityStatus().equals(EntityStatus.DRAFT.name())
						|| b.getEntityStatus().equals(EntityStatus.REJECTED.name())) {
					if (ret != null) {
						throw ValidationException.getInstance(ValidationErrorReason.INVALID_STATE);
					}
					ret = b;
				}
			}
		}

		return ret;
	}

	@Override
	public BillerView submit(BillerView blrview, boolean isParent) throws ValidationException, IOException {
		String userId = CommonUtils.getLoggedInUser();
		// ActionHistory actionHistory = new ActionHistory();
		BillerDetail blr = validateRetrieveForSubmit(blrview, isParent);

		boolean isExisting = true;
		EntityStatus prevEntityStatus = null;
		if (blr == null) {
			isExisting = false;
		} else {
			prevEntityStatus = EntityStatus.valueOf(blr.getEntityStatus());
		}

		blr = mapTo(blr, blrview);

		if (!isExisting) {
			blr.setCrtnTs(CommonUtils.currentTimestamp());
			blr.setCrtnUserId(userId);
		} else {
			blr.setUpdtTs(CommonUtils.currentTimestamp());
			blr.setUpdtUserId(userId);
		}

		/*
		 * blr.setActivatedTs(CommonUtils.currentTimestamp());
		 * blr.setActivatedUserId(userId);
		 */

		blr.setEntityStatus(EntityStatus.PENDING_ACTIVATION.name());

		if (blrview.getFileType() != null) {
			BillFileConfig billFileConfig = mapToBillFileConfig(blrview);
			billFileConfigDao.createOrUpdate(billFileConfig);
		}

		if (blrview.getBillMappingConfigList() != null) {
			for (BillMappingConfigView bmc : blrview.getBillMappingConfigList()) {
				BillMappingConfig billMappingConfig = mapToBillMappingConfig(bmc);
				billMappingConfig.setBlrId(blrview.getBlrId());
				billMappingConfigDao.createOrUpdate(billMappingConfig);
			}
		}

		return mapFrom(billerDao.createOrUpdate(blr));
		/*
		 * actionHistory.setActionId(UUID.randomUUID().getLeastSignificantBits()
		 * ) ; actionHistory.setActionType(CommonConstants.ACTION_SUBMIT);
		 * actionHistory.setEntityType(CommonConstants.ENTITY_BILLER);
		 * actionHistory.setEntityId(blr.getBillerId());
		 * actionHistory.setLastUpdtBy(userId);
		 * actionHistory.setLastUpdtTs(DBUtil.currentTimestamp());
		 * actionHistory.setComments(blrview.getUserComments());
		 * 
		 * GatewayCommonUtils.getActionHistoryService().insert(actionHistory);
		 */
	}

	@Override
	public byte[] exportReportToExcel(String loggedInUsersOuId) {

		List<BillerDetail> exportList = billerDao.exportReport(loggedInUsersOuId);
		List<BillerView> exportListView = null;

		byte[] hwb = null;

		if (exportList != null && exportList.size() > 0) {
			try {
				exportListView = new ArrayList<BillerView>(exportList.size());
				for (BillerDetail od : exportList) {
					BillerView bv = new BillerView();
					bv = mapFrom(od);
					if (od.getIsParentBlr()) {
						bv.setIsParent("Yes");
					} else {
						bv.setIsParent("No");
					}
					exportListView.add(bv);
				}

				hwb = ExcelUtil.writeReportToExcelAnnotation(exportListView, "Biller");

			} catch (Exception e) {
				e.printStackTrace();
			}
			return hwb;
		}

		return null;

	}

	@Override
	public List<BillerView> getSubBillersByParentBiller(String parentBillerId) throws IOException {
		List<BillerDetail> billerList = billerDao.getSubBillersByParentId(parentBillerId);

		List<BillerView> subBillers = new ArrayList<>();
		for (BillerDetail subBiller : billerList) {
			subBillers.add(mapFrom(subBiller));
		}
		return subBillers;

	}

	@Override
	public List<BillerView> getActiveBillers() throws IOException {
		List<BillerDetail> activeBillers = billerDao.getActiveBillers();
		List<BillerView> billers = new ArrayList<>();
		for (BillerDetail activeBiller : activeBillers) {
			billers.add(mapFrom(activeBiller));
		}
		return billers;
	}

	@Override
	public BillFetchResponse fetchBill(BillFetchRequest billFetchRequest, BillerView biller)
			throws ValidationException, IOException {

		String refId = billFetchRequest.getHead().getRefId();
		BillFetchResponse response = null;
		if (biller.getBlrMode().equals(BillerMode.ONLINE.name())) {// For Online
																	// Biller.
			String endPointUrl = biller.getBlrEndpointURL() + CommonConstants.BILLER_BILL_FETCH_REQUEST_URl
					+ TransactionContext.getTenantId();
			OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
			BillFetchRequest billerBillFetchRequest = new BillFetchRequest();
			billerBillFetchRequest.setHead(billFetchRequest.getHead());
			billerBillFetchRequest.setTxn(billFetchRequest.getTxn());
			billerBillFetchRequest.setBillDetails(billFetchRequest.getBillDetails());
			// forwarding the request to Biller
			logger.info("forwarding the request to Biller--------");
			LogUtils.logReqRespMessage(billerBillFetchRequest, billerBillFetchRequest.getHead().getRefId(),
					Action.FETCH_REQUEST);

			// Adding basic authentication support.
			HttpEntity httpEntity = new HttpEntity(billerBillFetchRequest, getHttpHeaders());
			ResponseEntity<Ack> responseEntity = restTemplate.postForEntity(endPointUrl, httpEntity, Ack.class);
			Ack ack = responseEntity.getBody();

			// Ack ack = restTemplate.postForObject(endPointUrl,
			// billerBillFetchRequest, Ack.class);
			if (ack != null) {
				logger.info("Received ACK from Biller------");
				LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
				response = asyncProcessor.processBillerBillFetchRequest(billerBillFetchRequest, ack);
				if (response != null) {
					billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH,
							RequestStatus.RESPONSE_RECIEVED);
				} else {
					billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH,
							RequestStatus.RESPONSE_FAILURE);
				}
			} else {
				billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH,
						RequestStatus.BILLER_NOT_AVAILABLE);
			}
			return response;
		} else if (biller.getBlrMode().equals(BillerMode.OFFLINEA.name())) {// For
																			// OfflineA
																			// Biller.
			BillFetchResponse billFetchResponse = null;
			String custParamvalue1 = null;
			String custParamvalue2 = null;
			String custParamvalue3 = null;
			String custParamvalue4 = null;
			String custParamvalue5 = null;
			List<Tag> custParam = billFetchRequest.getBillDetails().getCustomerParams().getTags();
			List<String> params = new ArrayList<String>();
			for (Tag tag : custParam) {
				params.add(tag.getValue());
			}
			BillDetails billDetails = billerDao.getBillDetails(biller.getBlrId(), params);
			/*
			 * if(custParam!=null){ if(custParam.get(0)!=null)
			 * custParamvalue1=custParam.get(0).getValue();
			 * if(custParam.get(1)!=null)
			 * custParamvalue2=custParam.get(1).getValue();
			 * if(custParam.get(2)!=null)
			 * custParamvalue3=custParam.get(2).getValue();
			 * if(custParam.get(3)!=null)
			 * custParamvalue4=custParam.get(3).getValue();
			 * if(custParam.get(4)!=null)
			 * custParamvalue5=custParam.get(4).getValue(); } BillDetails
			 * billDetails = billerDao.getBillDetails(biller.getBlrId(),
			 * custParamvalue1,custParamvalue2,custParamvalue3,custParamvalue4,
			 * custParamvalue5);
			 */

			if (billDetails != null) {
				billFetchResponse = cerateBillFetchResponse(billFetchRequest, billDetails);
			} else {
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILL_DETAILS);
			}
			if (billFetchResponse != null) {
				billerOUtransactionDataService.update(refId, billFetchResponse);
			}
			return billFetchResponse;
		} else {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILLER_MODE);
		}
	}

	private BillFetchResponse cerateBillFetchResponse(BillFetchRequest billFetchRequest, BillDetails billDetails)
			throws ValidationException {

		BillFetchResponse billFetchResponse = new BillFetchResponse();

		BillDetailsType billDetailsType = billFetchRequest.getBillDetails();
		billDetailsType.setBiller(billFetchRequest.getBillDetails().getBiller());
		billFetchResponse.setBillDetails(billDetailsType);

		// Assigning head tag.
		// billFetchResponse.setHead(billFetchRequest.getHead());
		String ouId = TransactionContext.getTenantId();
		TenantDetail td = tenantDetailService.fetchByTenantId(ouId);
		String ouName = td.getOuName();
		billFetchResponse.setHead(RequestResponseGenerator.getHead(ouName, billFetchRequest.getHead().getRefId()));

		// Creating reason tag.
		ReasonType reasonType = new ReasonType();
		reasonType.setResponseCode(ResponseCode.Successful.name());
		billFetchResponse.setReason(reasonType);

		// Creating transaction tag.
		TxnType txnType = billFetchRequest.getTxn();
		txnType.setRiskScores(null);
		billFetchResponse.setTxn(txnType);
		billFetchResponse.setBillerResponse(null);

		// Creating BillerResponseType object to assigning it to
		// BillFetchResponse object.
		BillerResponseType billerResponse = new BillerResponseType();
		billerResponse.setAmount(billDetails.getActualAmount().toPlainString());
		billerResponse.setBillDate(billDetails.getBillDate());
		billerResponse.setBillNumber(billDetails.getBillNumber());
		billerResponse.setBillPeriod(billDetails.getBillPeriod());
		// billerResponse.setCustConvDesc("Customer Service Fee");
		// billerResponse.setCustConvFee("600");
		billerResponse.setCustomerName(billDetails.getCustomerName());
		billerResponse.setDueDate(billDetails.getDueDate());

		// Assigning additional amounts in BillerResponseType object.
		String additionalAmountJSON = billDetails.getAdditionalAmounts();
		if (additionalAmountJSON != null) {
			try {
				JSONArray jsonarray = new JSONArray(additionalAmountJSON);
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobject = jsonarray.getJSONObject(i);
					Iterator it = jsonobject.keys();
					while (it.hasNext()) {
						String key = it.next().toString();
						BillerResponseType.Tag tag = new BillerResponseType.Tag();
						tag.setName(key);
						tag.setValue(jsonobject.getString(key));
						billerResponse.getTags().add(tag);
					}
				}
			} catch (JSONException e) {
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_JSON);
			}
		}
		billFetchResponse.setBillerResponse(billerResponse);

		// Assigning additional info in BillerResponseType object.
		AdditionalInfoType additionalInfo = new AdditionalInfoType();
		String additionalInfoJSON = billDetails.getAdditionalInfo();
		if (additionalInfoJSON != null) {
			try {
				JSONArray jsonarray = new JSONArray(additionalInfoJSON);
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobject = jsonarray.getJSONObject(i);
					Iterator it = jsonobject.keys();
					while (it.hasNext()) {
						String key = it.next().toString();
						AdditionalInfoType.Tag tag = new AdditionalInfoType.Tag();
						tag.setName(key);
						tag.setValue(jsonobject.getString(key));
						additionalInfo.getTags().add(tag);
					}
				}
			} catch (JSONException e) {
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_JSON);
			}
			billFetchResponse.setAdditionalInfo(additionalInfo);
		}
		return billFetchResponse;
	}

	@Override
	public BillPaymentResponse payBill(BillPaymentRequest billPaymentRequest, BillerView biller)
			throws ValidationException, IOException {
		BillPaymentResponse billPaymentResponse = null;
		String refId = billPaymentRequest.getHead().getRefId();

		if (biller.getBlrMode().equals(BillerMode.ONLINE.name())) {
			String endPointUrl = biller.getBlrEndpointURL() + CommonConstants.BILLER_BILL_PAY_REQUEST_URl
					+ TransactionContext.getTenantId();
			OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
			BillPaymentRequest billerBillPaymentRequest = new BillPaymentRequest();
			billerBillPaymentRequest.setHead(billPaymentRequest.getHead());
			billerBillPaymentRequest.setTxn(billPaymentRequest.getTxn());
			billerBillPaymentRequest.setBillDetails(billPaymentRequest.getBillDetails());

			// Adding basic authentication support.
			HttpEntity httpEntity = new HttpEntity(billerBillPaymentRequest, getHttpHeaders());
			ResponseEntity<Ack> responseEntity = restTemplate.postForEntity(endPointUrl, httpEntity, Ack.class);
			Ack ack = responseEntity.getBody();

			// Ack ack = restTemplate.postForObject(endPointUrl,
			// billerBillPaymentRequest, Ack.class);
			if (ack == null) {
				// Biller not found.
				billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.PAYMENT,
						RequestStatus.SEND_FAILED);
				billPaymentResponse = new BillPaymentResponse();
				billPaymentResponse.setHead(billPaymentRequest.getHead());
				billPaymentResponse.getReason().setResponseCode("001");
				billPaymentResponse.getReason().setResponseReason("Biller not available.");
			} else {
				billPaymentResponse = asyncProcessor.processBillerBillPaymentRequest(billerBillPaymentRequest, ack);
			}
		} else if (biller.getBlrMode().equals(BillerMode.OFFLINEA.name())) {
			String blrId = billPaymentRequest.getBillDetails().getBiller().getId();
			String billNo = billPaymentRequest.getBillerResponse().getBillNumber();
			BillDetails billDetails = billDetailsDao.get(billNo);
			if (billDetails != null) {
				billPaymentResponse = cerateBillPaymentResponse(billPaymentRequest, billDetails);
			} else {
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILL_DETAILS);
			}
			if (billPaymentResponse != null) {
				billerOUtransactionDataService.update(refId, billPaymentResponse);
			}
		} else if (biller.getBlrMode().equals(BillerMode.OFFLINEB.name())) {
			billPaymentResponse = cerateBillPaymentResponse(billPaymentRequest, null);
			billerOUtransactionDataService.update(refId, billPaymentResponse);
		} else {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILLER_MODE);
		}
		return billPaymentResponse;

	}

	private BillPaymentResponse cerateBillPaymentResponse(BillPaymentRequest billPaymentRequest,
			BillDetails billDetails) throws ValidationException {

		BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
		BillDetailsType billDetailsType = billPaymentRequest.getBillDetails();// billPaymentResponse.getBillDetails();
		// billDetailsType.setBiller(billDetailsType.getBiller());
		billPaymentResponse.setBillDetails(billDetailsType);
		billPaymentResponse.setHead(billPaymentResponse.getHead());
		ReasonType reasonType = new ReasonType();
		reasonType.setResponseCode("000");
		reasonType.setResponseReason("Successful");
		reasonType.setValue(null);
		billPaymentResponse.setReason(reasonType);

		// Creating transaction tag.
		TxnType txnType = new TxnType();// billPaymentResponse.getTxn();
		txnType.setType(TransactionType.FORWARD_TYPE_RESPONSE.name());
		txnType.setRiskScores(null);
		billPaymentResponse.setTxn(txnType);
		billPaymentResponse.setBillerResponse(null);
		if (billDetails != null) {
			// Creating BillerResponseType object to assigning it to
			// BillFetchResponse object.
			BillerResponseType billerResponse = new BillerResponseType();
			billerResponse.setAmount(billDetails.getActualAmount().toPlainString());
			billerResponse.setBillDate(billDetails.getBillDate());
			billerResponse.setBillNumber(billDetails.getBillNumber());
			billerResponse.setBillPeriod(billDetails.getBillPeriod());
			billerResponse.setCustomerName(billDetails.getCustomerName());
			billerResponse.setDueDate(billDetails.getDueDate());

			// Assigning additional amounts in BillerResponseType object.
			// String additionalAmountJSON = billDetails.getAdditionalAmounts();
			/*
			 * try { JSONArray jsonarray = new JSONArray(additionalAmountJSON);
			 * for (int i = 0; i < jsonarray.length(); i++) { JSONObject
			 * jsonobject = jsonarray.getJSONObject(i); Iterator it =
			 * jsonobject.keys(); while(it.hasNext()) { String key =
			 * it.next().toString(); BillerResponseType.Tag tag = new
			 * BillerResponseType.Tag(); tag.setName(key);
			 * tag.setValue(jsonobject.getString(key));
			 * billerResponse.getTags().add(tag); } } } catch (JSONException e)
			 * { throw ValidationException.getInstance(ValidationException.
			 * ValidationErrorReason.INVALID_JSON); }
			 */
			billPaymentResponse.setBillerResponse(billerResponse);

			// Assigning additional info in BillerResponseType object.
			// AdditionalInfoType additionalInfo = new AdditionalInfoType();
			// String additionalInfoJSON = billDetails.getAdditionalInfo();
			/*
			 * try { JSONArray jsonarray = new JSONArray(additionalInfoJSON);
			 * for (int i = 0; i < jsonarray.length(); i++) { JSONObject
			 * jsonobject = jsonarray.getJSONObject(i); Iterator it =
			 * jsonobject.keys(); while(it.hasNext()) { String key =
			 * it.next().toString(); AdditionalInfoType.Tag tag = new
			 * AdditionalInfoType.Tag(); tag.setName(key);
			 * tag.setValue(jsonobject.getString(key));
			 * additionalInfo.getTags().add(tag); } } } catch (JSONException e)
			 * { throw ValidationException.getInstance(ValidationException.
			 * ValidationErrorReason.INVALID_JSON); }
			 */
		}
		return billPaymentResponse;
	}

	@Override
	public void updateBillDetails(String billNo, BillStatus status) {
		BillDetails bill = billDetailsDao.get(billNo);
		bill.setStatus(status.name());
		bill.setUpdtTs(new Timestamp(System.currentTimeMillis()));
		try {
			billDetailsDao.createOrUpdate(bill);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public BillPaymentResponse reversPayment(BillPaymentRequest billPaymentRequest, BillerView biller)
			throws ValidationException, IOException {
		BillPaymentResponse billPaymentResponse = null;
		String refId = billPaymentRequest.getHead().getRefId();
		if (biller.getBlrMode().equals(BillerMode.ONLINE.name())) {
			billPaymentResponse = sendReversalToBiller(billPaymentRequest, biller);
			billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_REV_STATUS, RequestType.PAYMENT,
					RequestStatus.RESPONSE_RECIEVED);
		} else if (biller.getBlrMode().equals(BillerMode.OFFLINEA.name())) {
			String blrId = billPaymentRequest.getBillDetails().getBiller().getId();
			String billNo = billPaymentRequest.getBillerResponse().getBillNumber();
			BillDetails billDetails = billDetailsDao.get(billNo);
			if (billDetails != null) {
				billPaymentResponse = cerateReversalResponse(billPaymentRequest, billDetails);
			} else {
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILL_DETAILS);
			}
			if (billPaymentResponse != null) {
				billerOUtransactionDataService.update(refId, billPaymentResponse);
				updateBillDetails(billNo, BillStatus.DUE);
			}
		} else if (biller.getBlrMode().equals(BillerMode.OFFLINEB.name())) {
			billPaymentResponse = cerateReversalResponse(billPaymentRequest, null);
			billerOUtransactionDataService.update(refId, billPaymentResponse);
		} else {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILLER_MODE);
		}
		return billPaymentResponse;

	}

	private BillPaymentResponse cerateReversalResponse(BillPaymentRequest billPaymentRequest, BillDetails billDetails)
			throws ValidationException {
		BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
		BillDetailsType billDetailsType = billPaymentRequest.getBillDetails(); // billPaymentResponse.getBillDetails();
		billDetailsType.setBiller(billPaymentRequest.getBillDetails().getBiller());
		billPaymentResponse.setBillDetails(billDetailsType);
		billPaymentResponse.setHead(billPaymentResponse.getHead());
		ReasonType reasonType = new ReasonType();
		reasonType.setResponseCode("000");
		reasonType.setResponseReason("Successful");
		reasonType.setValue(null);
		billPaymentResponse.setReason(reasonType);
		// Creating transaction tag.
		TxnType txnType = new TxnType(); // billPaymentResponse.getTxn();
		txnType.setType(TransactionType.REVERSAL_TYPE_RESPONSE.name());
		txnType.setRiskScores(null);
		billPaymentResponse.setTxn(txnType);
		if (billDetails != null) {
			// Creating BillerResponseType object to assigning it to
			// BillFetchResponse object.
			BillerResponseType billerResponse = new BillerResponseType();
			billerResponse.setAmount(billDetails.getActualAmount().toPlainString());
			billerResponse.setBillDate(billDetails.getBillDate());
			billerResponse.setBillNumber(billDetails.getBillNumber());
			billerResponse.setBillPeriod(billDetails.getBillPeriod());
			billerResponse.setCustomerName(billDetails.getCustomerName());
			billerResponse.setDueDate(billDetails.getDueDate());
			// Assigning additional amounts in BillerResponseType object.
			String additionalAmountJSON = billDetails.getAdditionalAmounts();
			if (additionalAmountJSON != null) {
				try {
					JSONArray jsonarray = new JSONArray(additionalAmountJSON);
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject jsonobject = jsonarray.getJSONObject(i);
						Iterator it = jsonobject.keys();
						while (it.hasNext()) {
							String key = it.next().toString();
							BillerResponseType.Tag tag = new BillerResponseType.Tag();
							tag.setName(key);
							tag.setValue(jsonobject.getString(key));
							billerResponse.getTags().add(tag);
						}
					}
				} catch (JSONException e) {
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_JSON);
				}
			}
			billPaymentResponse.setBillerResponse(billerResponse);
			// Assigning additional info in BillerResponseType object.
			AdditionalInfoType additionalInfo = new AdditionalInfoType();
			String additionalInfoJSON = billDetails.getAdditionalInfo();
			if (additionalInfoJSON != null) {
				try {
					JSONArray jsonarray = new JSONArray(additionalInfoJSON);
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject jsonobject = jsonarray.getJSONObject(i);
						Iterator it = jsonobject.keys();
						while (it.hasNext()) {
							String key = it.next().toString();
							AdditionalInfoType.Tag tag = new AdditionalInfoType.Tag();
							tag.setName(key);
							tag.setValue(jsonobject.getString(key));
							additionalInfo.getTags().add(tag);
						}
					}
				} catch (JSONException e) {
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_JSON);
				}
			}
		}
		return billPaymentResponse;
	}

	@Override
	public BillPaymentResponse sendReversalToBiller(BillPaymentRequest billPaymentRequest, BillerView biller)
			throws ValidationException, IOException {
		BillPaymentResponse billPaymentResponse = null;
		String refId = billPaymentRequest.getHead().getRefId();
		String endPointUrl = biller.getBlrEndpointURL() + CommonConstants.BILLER_REVERSAL_REQUEST_URl
				+ TransactionContext.getTenantId();
		OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
		BillPaymentRequest reversalPaymentRequest = populatePaymentReversalRequest(billPaymentRequest);

		// Adding basic authentication support.
		HttpEntity httpEntity = new HttpEntity(reversalPaymentRequest, getHttpHeaders());
		ResponseEntity<Ack> responseEntity = restTemplate.postForEntity(endPointUrl, httpEntity, Ack.class);
		Ack ack = responseEntity.getBody();

		// Ack ack = restTemplate.postForObject(endPointUrl,
		// reversalPaymentRequest, Ack.class);
		billPaymentResponse = asyncProcessor.processBillerBillPaymentRequest(reversalPaymentRequest, ack);
		return billPaymentResponse;
	}

	private BillPaymentRequest populatePaymentReversalRequest(BillPaymentRequest paymentRequest) {
		BillPaymentRequest bpReq = new BillPaymentRequest();
		if (paymentRequest != null) {
			HeadType head = paymentRequest.getHead();
			head.setTs(CommonUtils.getFormattedCurrentTimestamp());
			head.setOrigInst(paymentRequest.getHead().getOrigInst());
			bpReq.setHead(head);

			TxnType txn = paymentRequest.getTxn();
			txn.setType(TransactionType.REVERSAL_TYPE_REQUEST.value());
			txn.setRiskScores(null);
			bpReq.setTxn(txn);

			return bpReq;
		} else {
			return null;
		}
	}

	@Override
	public List<String> getAllCurrentBillerIds() {
		return billerDao.getAllCurrentBillerIds();
	}

	@Override
	public List<BillerView> getAllBillers() throws IOException {
		List<BillerDetail> blrList = billerDao.getAll();
		List<BillerView> blrViewList = new ArrayList<BillerView>();
		for (BillerDetail blr : blrList) {
			blrViewList.add(mapFrom(blr));
		}
		return blrViewList;
	}

	public static BillerList getBillerJaxb(List<BillerView> billerViews) {
		BillerList billerList = null;
		if (billerViews != null) {
			billerList = new BillerList();

			for (BillerView aid : billerViews) {
				billerList.getBillers().add(mapToJaxb(aid));
			}

		}

		return billerList;
	}

	public static in.co.rssoftware.bbps.schema.BillerDetail mapToJaxb(BillerView billerView) {

		in.co.rssoftware.bbps.schema.BillerDetail billerDetail = null;
		if (billerView != null) {
			billerDetail = new in.co.rssoftware.bbps.schema.BillerDetail();
			billerDetail.setId(billerView.getBlrId());
			billerDetail.setName(billerView.getBlrName());
			billerDetail.setParentBillerId(billerView.getParentBlrId());
			billerDetail.setCategoryName(billerView.getBlrCategoryName());
			billerDetail.setIsParent(billerView.isParentBlr());
			billerDetail.setMode(billerView.getBlrMode()==null?"":billerView.getBlrMode().name());
		}
		return billerDetail;
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		// String plainCreds = getPropretyFiles("BASIC_AUTH_CREDENTIAL");
		String plainCreds = BASIC_AUTH_CREDENTIAL;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		headers.add("Authorization", "Basic " + base64Creds);

		return headers;
	}

	public static String getPropretyFiles(String key) {
		String value = null;

		try {
			Properties prop = new Properties();
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("BOU_Process.properties");
			prop.load(is);
			value = prop.getProperty(key);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return value;
	}

}
