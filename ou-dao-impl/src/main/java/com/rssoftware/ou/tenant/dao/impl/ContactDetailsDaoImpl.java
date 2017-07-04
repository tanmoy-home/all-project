package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.AgentInstDetail;
import com.rssoftware.ou.database.entity.tenant.ContactDetails;
import com.rssoftware.ou.tenant.dao.ContactDetailsDao;

import in.co.rssoftware.bbps.schema.EntityStatus;

@Repository
public class ContactDetailsDaoImpl extends GenericDynamicDaoImpl<ContactDetails, String> implements ContactDetailsDao  {
	private static final String FETCH_ALL_QUERY = "select a from contact_details a where a.entityStatus =";
	private static final String FETCH_ALL_QUERY1 = " and linked_entity_id = ";
	private static final String FETCH_ALL_QUERY2 = " and linked_entity_type = ";
	private static final String FETCH_ALL_QUERY3 = " and contact_type = ";
	
	public ContactDetailsDaoImpl() {
		super(ContactDetails.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<ContactDetails> fetchAll(String linkedEntityID, String linkedEntityType, String contactType,
			String entityStatus) {
		/*Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ContactDetails.class);
		criteria.add(Restrictions.eq("linkedEntityID", linkedEntityID));
		criteria.add(Restrictions.eq("linkedEntityType", linkedEntityType));
		criteria.add(Restrictions.eq("contactType", contactType));
		criteria.add(Restrictions.eq("entityStatus", entityStatus));
		return (List<ContactDetails>) criteria.list();*/
		StringBuilder query = new StringBuilder("");
		if (entityStatus == null){
			query.append(FETCH_ALL_QUERY);
			query.append(entityStatus);
			query.append(FETCH_ALL_QUERY1);
			query.append(linkedEntityID);
			query.append(FETCH_ALL_QUERY2);
			query.append(linkedEntityType);
			query.append(FETCH_ALL_QUERY3);
			query.append(contactType);
		}
		List<ContactDetails> contactList = getSessionFactory().getCurrentSession().createQuery(query.toString()).list();
			
		
		return contactList;
	}
}
