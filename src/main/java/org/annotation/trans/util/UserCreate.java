package org.annotation.trans.util;

import org.annotation.trans.model.Address;
import org.annotation.trans.model.Customer;

public class UserCreate {
	public static Customer createDummyCustomer() {
		Customer customer = new Customer();
		customer.setId(3);
		customer.setName("Pankaj");
		Address address = new Address();
		address.setId(3);
		address.setCountry("India");
		// setting value more than 20 chars, so that SQLException occurs
		address.setAddress("Albany Dr, San Jose, CA 95129");
		//address.setAddress("San Jose, CA 95129");
		customer.setAddress(address);
		return customer;
	}
}
