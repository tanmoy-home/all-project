package com.rssoftware.framework.hibernate.dao.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanLocator implements ApplicationContextAware{
	public enum DatabaseType {
		ORACLE, POSTGRES
	};

	public static Map<String, DatabaseType> driverDBMap = new HashMap<>();

	static {
		driverDBMap.put("oracle.jdbc.OracleDriver", DatabaseType.ORACLE);
		driverDBMap.put("org.postgresql.Driver", DatabaseType.POSTGRES);
	}

	private static ApplicationContext appContext;

	private static DatabaseType databaseType = null;

	private static Lock databaseTypeLoaderLock = new ReentrantLock();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) appContext.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		return appContext.getBean(beanName, clazz);
	}

	public static <T> T getBean(Class<T> clazz) {
		return appContext.getBean(clazz);
	}

	public static DatabaseType getDatabaseType(DataSource ds) {
		if (databaseType == null) {
			try {
				databaseTypeLoaderLock.lock();
				if (databaseType == null) {
					if (ds == null) {
						ds = getBean("ds", DataSource.class);
					}
					if (ds != null) {
						try {
							Class<? extends DataSource> clazz = ds.getClass();
							Method getDriverMethod = clazz.getMethod("getDriverClassName");

							String driverClass = (String) getDriverMethod.invoke(ds);
							databaseType = driverDBMap.get(driverClass);

							if (databaseType == null) {
								databaseType = DatabaseType.ORACLE;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} finally {
				databaseTypeLoaderLock.unlock();
			}
		}

		return databaseType;
	}


}
