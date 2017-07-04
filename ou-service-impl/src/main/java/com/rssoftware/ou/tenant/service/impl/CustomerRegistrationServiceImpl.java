package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.CustomerRegistration;
import com.rssoftware.ou.model.tenant.CustomerRegistrationView;
import com.rssoftware.ou.tenant.dao.CustomerRegistrationDao;
import com.rssoftware.ou.tenant.service.CustomerRegistrationService;

@Service
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService{
	
	@Autowired
	private CustomerRegistrationDao customerRegistrationDao;

	@Override
	public void save(CustomerRegistrationView customerRegistrationView) {
		customerRegistrationDao.createOrUpdate(mapTo(customerRegistrationView));
	}

	@Override
	public void delete(CustomerRegistrationView customerRegistrationView) {
		customerRegistrationDao.delete(mapTo(customerRegistrationView));
		
	}

	@Override
	public List<CustomerRegistrationView> fetchAll() {
		List<CustomerRegistration> customerList =  customerRegistrationDao.getAll();
		List<CustomerRegistrationView> customerViewList = new ArrayList<>();
		customerList.forEach( customer-> {
			customerViewList.add(mapFrom(customer));
		});
		return customerViewList;
	}

	@Override
	public List<CustomerRegistrationView> fetchAllActiveList() {
		List<CustomerRegistrationView> customerViewList = new ArrayList<>();
		for(CustomerRegistration customer : customerRegistrationDao.fetchAllActiveCustomerList()) {
			customerViewList.add(mapFrom(customer));
		}
		return customerViewList;
	}

	@Override
	public CustomerRegistrationView fetchByCustomerId(String custId) {
		return mapFrom(customerRegistrationDao.get(custId));
	}
	
	
	
	private CustomerRegistration mapTo(CustomerRegistrationView crv) {
		CustomerRegistration custReg = new CustomerRegistration();
		
		custReg.setCustID(crv.getCustID());
		custReg.setBbpouID(crv.getBbpouID());
		custReg.setCustName(crv.getCustName());
		custReg.setCustGender(crv.getCustGender());
		custReg.setCustDob(crv.getCustDob());
		custReg.setCustMobile(crv.getCustMobile());
		custReg.setCustEmail(crv.getCustEmail());
		custReg.setCustAddrLine1(crv.getCustAddrLine1());
		custReg.setCustAddrLine2(crv.getCustAddrLine2());
		custReg.setCustAddrLine3(crv.getCustAddrLine3());
		custReg.setCustCity(crv.getCustCity());
		custReg.setCustState(crv.getCustState());
		custReg.setCustPin(crv.getCustPin());
		custReg.setCustAadhaar(crv.getCustAadhaar());
		custReg.setCustPan(crv.getCustPan());
		custReg.setCustPassport(crv.getCustPassport());
		custReg.setEffectiveFrom(crv.getEffectiveFrom());
		custReg.setEffectiveTo(crv.getEffectiveTo());
		
		return custReg;
	}
	private CustomerRegistrationView mapFrom(CustomerRegistration customer) {
		if (null == customer) {
			return null;
		}
		CustomerRegistrationView crv = new CustomerRegistrationView();
		
		crv.setCustID(customer.getCustID());
		crv.setBbpouID(customer.getBbpouID());
		crv.setCustName(customer.getCustName());
		crv.setCustGender(customer.getCustGender());
		crv.setCustDob(customer.getCustDob());
		crv.setCustMobile(customer.getCustMobile());
		crv.setCustEmail(customer.getCustEmail());
		crv.setCustAddrLine1(customer.getCustAddrLine1());
		crv.setCustAddrLine2(customer.getCustAddrLine2());
		crv.setCustAddrLine3(customer.getCustAddrLine3());
		crv.setCustCity(customer.getCustCity());
		crv.setCustState(customer.getCustState());
		crv.setCustPin(customer.getCustPin());
		crv.setCustAadhaar(customer.getCustAadhaar());
		crv.setCustPan(customer.getCustPan());
		crv.setCustPassport(customer.getCustPassport());
		crv.setEffectiveFrom(customer.getEffectiveFrom());
		crv.setEffectiveTo(customer.getEffectiveTo());
		
		return crv;
	}

}
