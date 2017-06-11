package org.annotation.trans.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

	public static void main(String[] args) {

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContext.class);
    
    HelloWorld obj = (HelloWorld) context.getBean("helloBean");
    obj.printHelloWorld("Spring3 Java Config");
    
    //Declarative transaction management
    CustomerManager customerManager = (CustomerManager) context.getBean("customerManager");
    Customer cust = createDummyCustomer();
    customerManager.createCustomer(cust);
    
    
    context.close();
}
	
	private static Customer createDummyCustomer() {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("Pankaj");
		Address address = new Address();
		address.setId(1);
		address.setCountry("India");
		// setting value more than 20 chars, so that SQLException occurs
		//address.setAddress("Albany Dr, San Jose, CA 95129");
		address.setAddress("San Jose, CA 95129");
		customer.setAddress(address);
		return customer;
	}
}
