package com.rssoftware.ou.tenant.service.impl;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.rssoftware.ou.database.entity.tenant.ChannelParameters;
import com.rssoftware.ou.model.tenant.ChannelParametersView;
import com.rssoftware.ou.tenant.dao.ChannelParametersDao;
import com.rssoftware.ou.tenant.service.ChannelParametersService;


@Service
public class ChannelParametersServiceImpl implements ChannelParametersService {

	@Autowired
	 ChannelParametersDao channelParametersDao ;
	@Override
	public ChannelParametersView getChannelParametersById(String refId) {
		ChannelParameters channelParameters = channelParametersDao.get(refId);
		return mapFrom(channelParameters);
		
	}

	@Override
	public void save(ChannelParametersView channelParametersView) {
		ChannelParameters channelParameters = mapTo(channelParametersView);
		channelParametersDao.createOrUpdate(channelParameters);
		
		
		
	}

	private ChannelParameters mapTo(ChannelParametersView channelParametersView) {
		ChannelParameters channelParameters = channelParametersDao.get(channelParametersView.getRefId());
		if (null == channelParameters) {
			channelParameters = new ChannelParameters();
			
		}
		channelParameters.setAccountNo(channelParametersView.getAccountNo());
		channelParameters.setApp(channelParametersView.getApp());
		channelParameters.setAuthcode(channelParametersView.getAuthcode());
		channelParameters.setBiller_Payment_channel(channelParametersView.getBiller_Payment_channel());
		channelParameters.setBiller_Payment_Mode(channelParametersView.getBiller_Payment_Mode());
		channelParameters.setCardNum(channelParametersView.getCardNum());
		channelParameters.setGeoCode(channelParametersView.getGeoCode());
		channelParameters.setIfsc(channelParametersView.getIfsc());
		channelParameters.setIp(channelParametersView.getIp());
		channelParameters.setMac(channelParametersView.getMac());
		channelParameters.setMmid(channelParametersView.getMmid());
		channelParameters.setMobile(channelParametersView.getMobile());
		channelParameters.setModileNo(channelParametersView.getModileNo());
		channelParameters.setOs(channelParametersView.getOs());
		channelParameters.setPostal_code(channelParametersView.getPostal_code());
		channelParameters.setRefId(channelParametersView.getRefId());
		channelParameters.setRemarks(channelParametersView.getRemarks());
		channelParameters.setTerminal_id(channelParametersView.getTerminal_id());
		channelParameters.setVpa(channelParametersView.getVpa());
		channelParameters.setWalletName(channelParametersView.getWalletName());
		
		return channelParameters;
	}
	
	private ChannelParametersView  mapFrom(ChannelParameters channelParameters ) {
		if (null == channelParameters) {
			return null;
		}
		ChannelParametersView channelParametersView = new ChannelParametersView();
		channelParametersView.setRefId(channelParameters.getRefId());
		channelParametersView.setAccountNo(channelParameters.getAccountNo());
		channelParametersView.setApp(channelParameters.getApp());
		channelParametersView.setAuthcode(channelParameters.getAuthcode());
		channelParametersView.setBiller_Payment_channel(channelParameters.getBiller_Payment_channel());
		channelParametersView.setBiller_Payment_Mode(channelParameters.getBiller_Payment_Mode());
		channelParametersView.setCardNum(channelParameters.getCardNum());
		channelParametersView.setGeoCode(channelParameters.getGeoCode());
		channelParametersView.setIfsc(channelParameters.getIfsc());
		channelParametersView.setIp(channelParameters.getIp());
		channelParametersView.setMac(channelParameters.getMac());
		channelParametersView.setMmid(channelParameters.getMmid());
		channelParametersView.setMobile(channelParameters.getMobile());
		channelParametersView.setModileNo(channelParameters.getModileNo());
		channelParametersView.setOs(channelParameters.getOs());
		channelParametersView.setPostal_code(channelParameters.getPostal_code());
		channelParametersView.setRemarks(channelParameters.getRemarks());
		channelParametersView.setTerminal_id(channelParameters.getTerminal_id());
		channelParametersView.setVpa(channelParameters.getVpa());
		channelParametersView.setWalletName(channelParameters.getWalletName());
		
		return channelParametersView;
}

	
	public static ChannelParametersView formChannelParamsView(String refId, Map<String, String> map) {
		ChannelParametersView channelparaView = new ChannelParametersView();
		channelparaView.setRefId(refId);

		for (String key : map.keySet()) {
			if (key.equalsIgnoreCase("IP")) {
				channelparaView.setIp(map.get(key));
			}
			if (key.equalsIgnoreCase("MAC")) {
				channelparaView.setMac(map.get(key));
			}
			if (key.equalsIgnoreCase("OS")) {
				channelparaView.setOs(map.get(key));
			}
			if (key.equalsIgnoreCase("APP")) {
				channelparaView.setApp(map.get(key));
			}
			if (key.equalsIgnoreCase("TERMINAL_ID")) {
				channelparaView.setTerminal_id(map.get(key));
			}
			if (key.equalsIgnoreCase("MOBILE")) {
				channelparaView.setMobile(map.get(key));
			}
			if (key.equalsIgnoreCase("GEOCODE")) {
				channelparaView.setGeoCode(map.get(key));
			}
			if (key.equalsIgnoreCase("POSTAL_CODE")) {
				channelparaView.setPostal_code(map.get(key));
			}
			if (key.equalsIgnoreCase("IFSC")) {
				channelparaView.setIfsc(map.get(key));
			}
			if (key.equalsIgnoreCase("Remarks")) {
				channelparaView.setRemarks(map.get(key));
			}
			if (key.equalsIgnoreCase("AccountNo")) {
				channelparaView.setAccountNo(map.get(key));
			}
			if (key.equalsIgnoreCase("CardNum")) {
				channelparaView.setCardNum(map.get(key));
			}
			if (key.equalsIgnoreCase("AuthCode")) {
				channelparaView.setAuthcode(map.get(key));
			}
			if (key.equalsIgnoreCase("MobileNo")) {
				channelparaView.setModileNo(map.get(key));
			}
			if (key.equalsIgnoreCase("MMID")) {
				channelparaView.setMmid(map.get(key));
			}
			if (key.equalsIgnoreCase("WalletName")) {
				channelparaView.setWalletName(map.get(key));
			}
			if (key.equalsIgnoreCase("VPA")) {
				channelparaView.setVpa(map.get(key));
			}
			if (key.equalsIgnoreCase("billerPaymentChannels")) {
				channelparaView.setBiller_Payment_channel(map.get(key));
			}
			if (key.equalsIgnoreCase("billerPaymentModes")) {
				channelparaView.setBiller_Payment_Mode(map.get(key));
			}
		}
		return channelparaView;
	}
	
	@SafeVarargs
	public static Map<String, String> getChannelParams(String paymentinfo, Set<String> paymenttag, MultiValueMap<String, Object>... paramsArr) {

		String payinfo[] = paymentinfo.split("\\|");

		System.out.println(payinfo[0]);

		Map<String, String> paramMap = new HashMap<String, String>();
		if (paramsArr != null) {
			for (MultiValueMap<String, Object> params : paramsArr) {
				if (params != null) {
					for (String key : params.keySet()) {
						if (params.get(key) != null
								&& !params.get(key).isEmpty()
								&& params.get(key).get(0) != null) {
							for (String payinf : payinfo) {
								if (payinf.equalsIgnoreCase(key)) {
									paramMap.put(payinf, params.get(key).get(0)
											.toString());

								}

							}
							if (paymenttag != null) {
								for (String paymnttag : paymenttag) {
									if (paymnttag.equalsIgnoreCase(key)) {
										paramMap.put(paymnttag, params.get(key)
												.get(0).toString());

									}
								}
							}

						}
					}
				}
			}
		}
		return paramMap;
	}
}
