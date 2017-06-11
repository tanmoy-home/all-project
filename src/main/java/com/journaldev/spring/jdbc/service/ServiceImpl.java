package com.journaldev.spring.jdbc.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.journaldev.spring.jdbc.dao.CustomerDAO;
import com.journaldev.spring.jdbc.model.Customer;
/*
 * Programmatic transaction management using the TransactionTemplate (Recommended by Spring Team)
 */
public class ServiceImpl implements Sevice {
	private CustomerDAO customerDAO;

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	/*
	 * Instances of the TransactionTemplate class are thread safe, in these instances do not maintain any 
	 * conversational state.
	 * 
	 * TransactionTemplate instances do however maintain configuration state, so while a number of classes
	 * may share a single instance of a TransactionTemplate, if a class needs to use a TransactionTemplate
	 * with different settings (for example, a different isolation level), then you need to create two 
	 * distinct TransactionTemplate instances.
	 */
	private final TransactionTemplate transactionTemplate;

	// use constructor-injection to supply the PlatformTransactionManager
	public ServiceImpl(PlatformTransactionManager transactionManager)
	 {     
		this.transactionTemplate = new TransactionTemplate(transactionManager);   
		// the transaction settings can be set here explicitly if so desired hence better control
		// This can also be done in xml file
		this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
		this.transactionTemplate.setTimeout(30); // 30 seconds
	 }																																	

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object someServiceMethod(final Customer cust)
	  { 
		
		//Transaction Callback With Result
	    return transactionTemplate.execute(new TransactionCallback() 
	    {
	     // the code in this method executes in a transactional context           
	     public Object doInTransaction(TransactionStatus status)
	     { 
	    	 customerDAO.create(cust);
	    	 return null;
	     }
	   });   
	    
	}
	
	@Override
	public Object someServiceMethodWithoutResult(final Customer cust)
	  { 
		
		//If there is no return value, use the convenient TransactionCallbackWithoutResult class
	    return transactionTemplate.execute(new TransactionCallbackWithoutResult() 
	    {
	     // the code in this method executes in a transactional context           
	     protected void doInTransactionWithoutResult(TransactionStatus status)
	     { 
	    	 customerDAO.create(cust);
	     }
	   });   
	    
	}
	
}
