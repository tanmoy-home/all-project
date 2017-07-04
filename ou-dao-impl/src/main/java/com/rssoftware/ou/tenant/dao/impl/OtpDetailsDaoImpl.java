package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.OtpDetails;
import com.rssoftware.ou.tenant.dao.OtpDetailsDao;

@Repository
public class OtpDetailsDaoImpl extends GenericDynamicDaoImpl<OtpDetails, Long>implements OtpDetailsDao {

	public OtpDetailsDaoImpl() {
		super(OtpDetails.class);
	}

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final String SELECT_OTP_VALIDATE_SQL = "select b from OtpDetails b where b.mobileNo =:mobile_no and b.otpStatus=:otpStatus order by b.creationTs desc fetch first 1 rows only";
	
	@Override 
	  public List<OtpDetails> fetchAllActiveRow (String custMobileNo, String otp_status){
		  
		OtpDetails otpd = new OtpDetails();
		Criteria criteria=getSessionFactory().getCurrentSession().createCriteria(OtpDetails.class).addOrder(Order.desc("creationTs"));
        criteria.setMaxResults(1);
		//List<OtpDetails> result = getSessionFactory().getCurrentSession().createQuery(SELECT_OTP_VALIDATE_SQL).setString("mobile_no",custMobileNo).setString("otpStatus", otp_status).list();//.setString("mobile_no",custMobileNo).list();
        List<OtpDetails> result = (List<OtpDetails>) criteria.list();
		  if(result!=null) {
			  System.out.println("@@@@@@@@@@@@@@@@@@@@@@ "+result.size());
		  }
			  return result;	  
	}
	 
}
