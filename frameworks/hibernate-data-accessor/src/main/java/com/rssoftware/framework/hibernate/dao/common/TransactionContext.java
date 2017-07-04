package com.rssoftware.framework.hibernate.dao.common;

import java.util.HashMap;

public class TransactionContext {
	private static final String KEY_TENANT_ID = "key_tenant_id";
	
	// bean names
	public static final String BEAN_DATA_SOURCE_PREFIX = "ds_";
	public static final String BEAN_DATA_SOURCE_TRANSACTION_MANAGER_PREFIX = "txnMgrDS_";
	public static final String BEAN_JDBC_TEMPLATE_PREFIX = "jdbcTemplate_";
	public static final String BEAN_NAMED_JDBC_TEMPLATE_PREFIX = "jdbcNTemplate_";
	public static final String BEAN_HIBERNATE_SESSION_FACTORY_PREFIX = "hibSessFact_";
	public static final String BEAN_HIBERNATE_TRANSACTION_MANAGER_PREFIX = "txnMgrHib_";
	
	public static final String BEAN_TENANT_ENTITY_BASE_PACKAGE = "com.rssoftware.ou.database.entity.tenant";
	public static final String BEAN_HIBERNATE_DIALECT_POSTGRES = "org.hibernate.dialect.PostgreSQL9Dialect";
	public static final String BEAN_HIBERNATE_DIALECT_ORACLE = "org.hibernate.dialect.Oracle10gDialect";
	public static final String BEAN_HIBERNATE_SHOW_SQL = "true";

	private TransactionContext() {}
	
	private static ThreadLocal<HashMap<String, String>> threadLocalContext = new ThreadLocal<HashMap<String, String>>() {
	    @Override
	    protected HashMap<String, String> initialValue() {
	        return new HashMap<>();
	    }
	};
	
	public static void putVariable(String name, String value){
		threadLocalContext.get().put(name, value);
	}

	public static String getVariable(String name){
		return threadLocalContext.get().get(name);
	}

	public static String removeVariable(String name){
		return threadLocalContext.get().remove(name);
	}

	public static void clearVariables(){
		threadLocalContext.remove();
	}

	public static void putTenantId(String value){
		putVariable(KEY_TENANT_ID, value);
	}

	public static String getTenantId(){
		return getVariable(KEY_TENANT_ID);
	}
	
	
}
