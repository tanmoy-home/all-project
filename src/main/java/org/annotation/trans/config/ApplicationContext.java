package org.annotation.trans.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement(proxyTargetClass=true)
public class ApplicationContext  implements TransactionManagementConfigurer{

	private static final String PROPERTY_NAME_DATABASE_DRIVER ="com.mysql.jdbc.Driver";
	private static final String PROPERTY_NAME_DATABASE_URL ="jdbc:mysql://localhost:3306/test";
	private static final String PROPERTY_NAME_DATABASE_USERNAME ="root";
	private static final String PROPERTY_NAME_DATABASE_PASSWORD ="root";
	
	@Bean(name="customerDAO")
	public CustomerDAO customerDAO(){
		CustomerDAOImpl customerDAOImpl = new CustomerDAOImpl();
		//customerDAOImpl.setDataSource(dataSource());
		return customerDAOImpl;
	}
	
	@Bean(name="customerManager")
	public CustomerManager customerManager(){
		CustomerManagerImpl customerManagerImpl = new CustomerManagerImpl();
		//customerManagerImpl.setCustomerDAO(customerDAO());
		return customerManagerImpl;
	}
	
	@Bean(name="dataSource")
	public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		dataSource.setDriverClassName(PROPERTY_NAME_DATABASE_DRIVER);
        dataSource.setUrl(PROPERTY_NAME_DATABASE_URL);
        dataSource.setUsername(PROPERTY_NAME_DATABASE_USERNAME);
        dataSource.setPassword(PROPERTY_NAME_DATABASE_PASSWORD);
         
        return dataSource;
	}
	
	@Bean(name="transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
	
	@Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }
	
}
