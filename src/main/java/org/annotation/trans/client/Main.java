package org.annotation.trans.client;

import org.annotation.trans.config.ApplicationContext;
import org.annotation.trans.model.Customer;
import org.annotation.trans.service.CustomerManager;
import org.annotation.trans.util.UserCreate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

	public static void main(String[] args) {

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContext.class);
    
    //Declarative transaction management
    CustomerManager customerManager = (CustomerManager) context.getBean("customerManager");
    Customer cust = UserCreate.createDummyCustomer();
    customerManager.createCustomer(cust);
    
    
    context.close();
}
	

}
