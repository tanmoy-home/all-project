package com.journaldev.spring.jdbc.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.journaldev.spring.jdbc.dao.CustomerDAO;
import com.journaldev.spring.jdbc.model.Customer;

/*
 * Programmatic transaction management using the PlatformTransactionManager implementation directly
 */
public class ServiceTwoImpl implements ServiceTwo {

	
	private PlatformTransactionManager transactionManager;

	@Override
	public void setTransactionManager( PlatformTransactionManager transactionManager)
	 {    
	   this.transactionManager = transactionManager;  
	 }
	 
	 //private DefaultTransactionDefinition def;
	 //private TransactionStatus status;
	 private TransactionStatus defineTransactionStatus(){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// explicitly setting the transaction name is something that can only be done programmatically
		def.setName("SomeTxName");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
		TransactionStatus status = transactionManager.getTransaction(def);
		return status;
	 }
	 
		private CustomerDAO customerDAO;

		public void setCustomerDAO(CustomerDAO customerDAO) {
			this.customerDAO = customerDAO;
		}

	 
	@Override
	public boolean someServiceMethod(Customer cust){
		 TransactionStatus status = defineTransactionStatus();
		 try{
			 customerDAO.create(cust);
		 }catch(Exception e){
			 transactionManager.rollback(status);   
			 System.out.println(e.getMessage());
			 return false; 
		 }
		 
		 transactionManager.commit(status);
		 return true;
	 }

}
