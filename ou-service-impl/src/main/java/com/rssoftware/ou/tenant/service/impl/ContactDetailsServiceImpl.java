package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.ContactDetails;
import com.rssoftware.ou.model.tenant.ContactDetailsView;
import com.rssoftware.ou.model.tenant.ContactDetailsView.ContactType;
import com.rssoftware.ou.model.tenant.ContactDetailsView.LinkedEntityType;
import com.rssoftware.ou.tenant.dao.ContactDetailsDao;
import com.rssoftware.ou.tenant.service.ContactDetailsService;

import in.co.rssoftware.bbps.schema.EntityStatus;

@Service
public class ContactDetailsServiceImpl implements ContactDetailsService {
	
	@Autowired
	private ContactDetailsDao contactDetailsDao;

	@Override
	public void submit(ContactDetailsView cdv) throws ValidationException {
		 String userId = CommonUtils.getLoggedInUser();
		 
		 if(validateRetrieve(cdv)){
			ContactDetails contactDetail = mapTo(cdv);
			contactDetail.setCrtnUserId(userId);
			contactDetail.setCrtnTs(CommonUtils.currentTimestamp());
			
			contactDetailsDao.createOrUpdate(contactDetail);
		 }
	}
	
	@Override
	public void update(ContactDetailsView cdv) throws ValidationException {
		String userId = CommonUtils.getLoggedInUser();

		if(validateRetrieve(cdv)){
			ContactDetails contactDetail = mapTo(cdv);
			contactDetail.setUpdtUserId(userId);
			contactDetail.setUpdtTs(CommonUtils.currentTimestamp());
			
			contactDetailsDao.update(contactDetail);
		}
	}
		
	/*@Override
	public List<ContactDetailsView> fetchFunctionallyActiveList(int pageNo, int pageSize) throws ValidationException {
		List<ContactDetails> contacts = contactDetailsDao.getAll(pageNo * pageSize, pageSize, EntityStatus.ACTIVE,EntityStatus.PENDING_DELETE, EntityStatus.PENDING_DEACTIVATION,null);

        if (contacts != null) {
            List<ContactDetailsView> cdv = new ArrayList<ContactDetailsView>(contacts.size());

            for (ContactDetails contact : contacts) {
                cdv.add(mapFrom(contact));
            }

            return cdv;
        }

        return null;
	}*/

	/*@Override
	public List<ContactDetailsView> fetchDeactivatedList(int pageNo,
			int pageSize) throws ValidationException {
		List<ContactDetails> contacts = contactDetailsDao.getAll(pageNo * pageSize, pageSize, EntityStatus.ACTIVE,EntityStatus.PENDING_DELETE, EntityStatus.PENDING_DEACTIVATION, null);

        if (contacts != null) {
            List<ContactDetailsView> cdv = new ArrayList<ContactDetailsView>(contacts.size());

            for (ContactDetails contact : contacts) {
                cdv.add(mapFrom(contact));
            }

            return cdv;
        }

        return null;
	}*/

	@Override
	public ContactDetailsView fetch(String linkedEntityID,
			String linkedEntityType, String contactType,
			boolean fromPendingApproval, String entityStatus)
			throws ValidationException {
		 List<ContactDetails> contactList=null;
	       
	        if (entityStatus != null && !entityStatus.isEmpty() && !entityStatus.equals("")) {
	           
	            contactList = contactDetailsDao.fetchAll(linkedEntityID, linkedEntityType, contactType, entityStatus);
	        }

	        if (contactList != null && !contactList.isEmpty()) {
	            if (contactList.size() == 1) {
	                return mapFrom(contactList.get(0));
	            }

	        }
		return null;
	}

	/*@Override	
	public List<ContactDetailsView> fetchPendingApprovalList()
			throws ValidationException {
		List<ContactDetails> contacts = contactDetailsDao.getAll(0, 0, EntityStatus.PENDING_REACTIVATION,
                EntityStatus.PENDING_ACTIVATION, EntityStatus.PENDING_DELETE, EntityStatus.PENDING_DEACTIVATION);
        String userId = CommonUtils.getLoggedInUser();
        if (contacts != null) {
            List<ContactDetailsView> cdv = new ArrayList<ContactDetailsView>(contacts.size());

            for (ContactDetails contact : contacts) {
                cdv.add(mapFrom(contact));
            }

            return cdv;
        }
		return null;
	}*/
	
	private boolean validateRetrieve(ContactDetailsView contact) throws ValidationException {
		boolean isvalid = false;
        if (contact == null || !CommonUtils.hasValue(contact.getLinkedEntityID())) {
            throw ValidationException.getInstance(ValidationErrorReason.NULL);
        }

        List<ContactDetails> contacts = contactDetailsDao.fetchAll(contact.getLinkedEntityID(),contact.getLinkedEntityType().name(), contact.getContactType().name(), EntityStatus.CU_APPROVED.name());
        		
        ContactDetails ret = null;

        if (contacts != null && contacts.size() > 0) {
            for (ContactDetails c : contacts) {
                if (c.getEntityStatus() != EntityStatus.REJECTED.name()) {
                	isvalid = false;
                	throw ValidationException.getInstance(ValidationErrorReason.INVALID_STATE);
                }

                if (c.getEntityStatus() == EntityStatus.REJECTED.name()) {
                    if (ret != null) {
                    	isvalid = false;
                    	throw ValidationException.getInstance(ValidationErrorReason.INVALID_STATE);
                    }
                    ret = c;
                }
            }
            isvalid = true;
        }
        
        if (ret == null) {
        	isvalid = false;
        	throw ValidationException.getInstance(ValidationErrorReason.REQUEST_NOT_FOUND);
        }

        return isvalid;
    }
	
	private static ContactDetailsView mapFrom(ContactDetails contact) {
        if (contact == null) {
            return null;
        }

        ContactDetailsView cdv = new ContactDetailsView();
        cdv.setLinkedEntityID(contact.getLinkedEntityId());
        cdv.setLinkedEntityType(LinkedEntityType.valueOf(contact.getLinkedEntityType()));
        cdv.setFirstName(contact.getFirstName());
        cdv.setLastName(contact.getLastName());
        cdv.setDesignation(contact.getDesignation());
        cdv.setDepartment(contact.getDepartment());
        cdv.setPhoneNo(contact.getPhoneNo());
        cdv.setMobileNo(contact.getMobileNo());
        cdv.setEmailID(contact.getEmail());
        cdv.setContactType(ContactType.valueOf(contact.getContactType()));
        return cdv;
    }

    private static ContactDetails mapTo(ContactDetailsView cdv) {
        ContactDetails contact = new ContactDetails();
    	
        contact.setLinkedEntityId(cdv.getLinkedEntityID());
        contact.setLinkedEntityType(cdv.getLinkedEntityType().toString());
        contact.setContactType(cdv.getContactType().toString());
        contact.setFirstName(cdv.getFirstName());
        contact.setLastName(cdv.getLastName());
       
        if(cdv.getDesignation()==null || cdv.getDesignation().equals("")){
            contact.setDesignation(null);   
        }else{
            contact.setDesignation(cdv.getDesignation());
        }
       
        if(cdv.getDepartment()==null || cdv.getDepartment().equals("")){
            contact.setDepartment(null);
        }else{
            contact.setDepartment(cdv.getDepartment());
        }
        if(cdv.getPhoneNo()==null || cdv.getPhoneNo().equals("")){
           
            contact.setPhoneNo(null);
        }else{
            contact.setPhoneNo(cdv.getPhoneNo());
        }
           
        if(cdv.getMobileNo()==null || cdv.getMobileNo().equals("")){
           
            contact.setMobileNo(null);
        }else{
            contact.setMobileNo(cdv.getMobileNo());
        }

        if(cdv.getEmailID()==null || cdv.getEmailID().equals("")){
            contact.setEmail(null);
        }else{
            contact.setEmail(cdv.getEmailID());
        }
       
        return contact;
    }

	
}
