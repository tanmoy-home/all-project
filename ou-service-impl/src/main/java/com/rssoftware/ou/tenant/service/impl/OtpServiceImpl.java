/**
 * 
 */
package com.rssoftware.ou.tenant.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.OTPGenUtil;
import com.rssoftware.ou.common.OtpStatus;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.OtpConfig;
import com.rssoftware.ou.database.entity.tenant.OtpDetails;
import com.rssoftware.ou.tenant.dao.OtpConfigDao;
import com.rssoftware.ou.tenant.dao.OtpDetailsDao;
import com.rssoftware.ou.tenant.service.OtpService;
import com.rssoftware.ou.tenant.service.PGService;
import com.rssoftware.ou.tenant.service.TenantParamService;

@Service
public class OtpServiceImpl implements OtpService {

	private static final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);
	
	@Autowired
	private OtpDetailsDao otpDetailsDao;
	
	@Autowired
	private OtpConfigDao otpConfigDao;
	@Autowired
	private TenantParamService paramService;
	
	@Override
	public OtpDetails generateOtp(OtpDetails otpDetails) 
	{
		if (log.isDebugEnabled()) { 
			log.debug("Called generateOtp");
		};
		String otpExpireTime=paramService.retrieveStringParamByName(CommonConstants.OTP_EXPIRE_TIME_IN_SEC);
		if(otpExpireTime==null||otpExpireTime.isEmpty())
		{
			log.info("####################################### OTP_EXPIRE_TIME_IN_SEC is not present in Tenant_Param ##############################" );
			return null;
		}
		boolean otpGenerated = false;
		{
			long tm = OTPGenUtil.getOTPTime();
			String steps = "0";
			String seed64 = "";
			steps = OTPGenUtil.getSteps(tm);
			seed64 = OTPGenUtil.getRandomSeed(64);
			otpDetails.setOtpSeed(seed64);
			otpDetails.setOtpTs(steps);
			
			try {
			List<OtpDetails> obj = otpDetailsDao.fetchAllActiveRow(otpDetails.getMobileNo(), otpDetails.getOtpStatus());
			Timestamp currentTs = CommonUtils.currentTimestamp();
			
			if(obj.size() > 0){		
				
				OtpDetails otpd = obj.get(0);
				otpd.setOtpStatus(OtpStatus.INACTIVE.name());
				otpDetailsDao.update(otpd);
				System.out.println("!!!!!!!!!!!!"+otpd);
			
			}			
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(currentTs.getTime());
				cal.add(Calendar.SECOND, Integer.parseInt(otpExpireTime));
				Timestamp otpValidTs = new Timestamp(cal.getTime().getTime());
				//otpDetails.setOtpId(null);
				otpDetails.setCreationTs(currentTs);
				otpDetails.setValidUpto(otpValidTs);
				otpDetails.setCreatedBy("BATCH");
				otpDetails.setOtpStatus(OtpStatus.ACTIVE.name());
				otpDetailsDao.create(otpDetails);

			} catch (Exception e) {
				log.error("Failed to generate OTP " + e.getMessage(), e);
				throw e;
			}
			
			if (otpDetails != null) 
			{
				String otp = OTPGenUtil.generateTOTP512(seed64, steps);
				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+otp);
				if (log.isDebugEnabled()) { 
					log.debug("TestOTP : "+otp);
				};
				
				OtpConfig otpConfig = new OtpConfig();
				otpConfig.setMobileNo(otpDetails.getMobileNo());
				otpConfig.setOtpNo(otp);
				otpConfig.setCreationTs(CommonUtils.currentTimestamp());
				otpConfigDao.create(otpConfig);
				
				otpDetails.setOtpNo(otp);
				//Message sending process
				/*ComponentMessage msg=new ComponentMessage();
				msg.setUpiTransactionId(IDGenerator.generateId());
				msg.setEventType(EventType.OTP_SEND.name());
				OtpDeatils otpobj = new OtpDeatils();
				otpobj.setMobileNo(otpDtls.getMobileNo());
				otpobj.setOtpVal(otp);
				msg.setPayload(otpobj);
				eventBus.notify(ComponentNames.NOTIFICATION_MANAGER, Event.wrap(msg));
				SMSSender.sendOTPSms(otpDetails.getMobileNo(), otp);
				SMSSender.sendSMS(otpServiceDetails.getMobileNo(), otp);*/
				otpGenerated = true;
			} 
	
		} 
		return otpDetails;
	}

	@Override
	public String generatePassword()
	{
		String password = null;
		
		{
			long tm = OTPGenUtil.getOTPTime();
			String steps = "0";
			String seed64 = "";
			steps = OTPGenUtil.getSteps(tm);
			seed64 = OTPGenUtil.getRandomSeed(64);
			password = OTPGenUtil.generateTOTP512(seed64, steps, "6");
			if (log.isDebugEnabled()) { 
				log.debug("Password : "+password);
			};

		} 
		
		return password;
	}

	@Override
	public OtpStatus validateOtp(OtpDetails otpDetails, String providedOtp) {
		   boolean validOTP = false;	
			try 
			{List<OtpDetails> otpList = otpDetailsDao.fetchAllActiveRow(otpDetails.getMobileNo(),OtpStatus.ACTIVE.name());
				if(otpList.size()>0){
					OtpDetails otpObj = otpList.get(0);
					Timestamp currentTs = CommonUtils.currentTimestamp();
					if (log.isDebugEnabled()) { 
						log.debug("valid upto in otpDetailsDev " + (otpDetails !=null ? otpDetails.getValidUpto() : null));
					}
					if( otpObj.getValidUpto().getTime() < currentTs.getTime()){
						otpObj.setOtpStatus(OtpStatus.OTP_EXPIRED.name());
						otpDetailsDao.createOrUpdate(otpObj);
						return OtpStatus.OTP_EXPIRED;
					}
					String OtpGen = null;
					if (otpObj != null)
					{
						OtpGen = OTPGenUtil.generateOtp(otpObj.getOtpSeed(), otpObj.getOtpTs()).trim();
						if (OtpGen.equals(providedOtp)){
							System.out.println("Otp matched");
							validOTP = true;							
							otpObj.setOtpStatus(OtpStatus.USED.name());
							otpDetailsDao.createOrUpdate(otpObj);
						}
						else {
							System.out.println("Otp mismatched");
							validOTP = false;
						}
						
					} 
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}

			return validOTP?OtpStatus.VALID_OTP:
				OtpStatus.INVALID_OTP;
		}


	/*private static String  getEncryptPassword(String passwordIn)
	{
			
			String output = null;
			//String dataToEncrypt = token + "|" + k0String + "|" + deviceId;
			String dataToEncrypt = passwordIn   ;
			try {
				GatewayConfiguration.getInstance().getConfigParamService().refresh();
				MobileCryptoUtil crypto = new MobileCryptoUtil();
				byte[] calculatedHash = crypto.clSHA256(dataToEncrypt);
				String publicKey = GatewayConfiguration.getInstance().getPublicKey();
				
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, readPublicKeyFromString(publicKey));
				byte[] encryptedData = cipher.doFinal(calculatedHash);
	            output = Base64.encodeBase64String(encryptedData);
	            System.out.println(output);
			}
			catch(Exception e)
			{
				log.error(e.getMessage(), e);
			}	
			return output;
	   }*/
	
}
