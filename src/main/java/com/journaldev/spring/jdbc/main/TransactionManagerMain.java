package com.journaldev.spring.jdbc.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.journaldev.spring.jdbc.model.Address;
import com.journaldev.spring.jdbc.model.Customer;
import com.journaldev.spring.jdbc.service.ServiceTwo;
import com.journaldev.spring.jdbc.service.ServiceTwoImpl;

public class TransactionManagerMain {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"spring.xml");

		/*CustomerManager customerManager = ctx.getBean("customerManager",
				CustomerManagerImpl.class);*/

		Customer cust = createDummyCustomer();
		//customerManager.createCustomer(cust);
		
		
		//Programmatic transaction management using TransactionTemplate
		//Service service = ctx.getBean("service", ServiceImpl.class);
		//service.someServiceMethodWithoutResult(cust);

		//Programmatic transaction management using PlatformTransactionManager
		ServiceTwo serviceTwo = ctx.getBean("serviceTwo", ServiceTwoImpl.class);
		System.out.println("create customer is "+serviceTwo.someServiceMethod(cust));
		
		ctx.close();
	}

	private static Customer createDummyCustomer() {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("Pankaj");
		Address address = new Address();
		address.setId(1);
		address.setCountry("India");
		// setting value more than 20 chars, so that SQLException occurs
		address.setAddress("Albany Dr, San Jose, CA 95129");
		//address.setAddress("San Jose, CA 95129");
		customer.setAddress(address);
		return customer;
	}

}
