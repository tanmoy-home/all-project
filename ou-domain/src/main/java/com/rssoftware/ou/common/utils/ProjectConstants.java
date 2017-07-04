package com.rssoftware.ou.common.utils;

/**
 *  To add new constants, simply DECLARE the constants with the value in <br>
 *  double quotes, ex: new line can be declared as NEWLINE("\n")
 */
public enum ProjectConstants {
	
	CLOSED_STR("CLOSED"),
	COMMA(","),
	USER_NAME("userName"),
	EXCEPTION_LISTENER_REFUSED_CONN("Listener refused the connection"),
	EMPTY_STRING(""),
	SIMPLE_BRACS_OPEN("("),
	SIMPLE_BRACS_ClOSE(")"),
	NO_OF_ROWS_PER_PAGE(8),
	BLANK_STRING(" "),
	HIPHAN("-"),
	APPROVED("Approved"),
	PENDING("Pending"),
	REJECTED("Rejected"),
	NEW("New"),
	ONE("1"),
	ONE_INT(1),
	TWO("2"),
	TWO_INT(2),
	THREE("3"),
	THREE_INT(3),
	ZERO(0),
	ZEROSTR("0"),
	FIVE_INT(5),
	ACTIVE("1"),
	CLOSED("2"),
	INACTIVE("3"),

	SELECT_STRING("--Please Select--"),
	SELECT_STRINGVAL("--Please Select--"),
	ACCOUNT_STATUS_ACTIVE(1),
	ACCOUNT_STATUS_INACTIVE(2),
	ACCOUNT_STATUS_BLOCKED(3),
	ACCOUNT_STATUS_CLOSED(4),
	DATABASE_EXCEPTION("There is some database problem, please contact administrator"),
	STATUS_COLUMN("A"),
	ERROR_MESSAGE("errorMessage"),
	ENGLISH("english"),
	LANG("lang"),
	
	CLOSED_STATUS_NAME("Closed"),
	SINGLE_QUOTE("'"),
	ADMIN_DATA_SERVICE("adminDataService"),
	STATUS_INFO("statusInfo"),
	COMMTYPE_ERROR("commtypeErr"),
	FROM_DATE_ERROR("fromDateErr"),
	TO_DATE_ERROR("toDateErr"),
	DATE_PATTERN1("yyyy-MM-dd"),
	DATE_PATTERN2("dd/MM/yyyy"),
	DATE_PATTERN3("ddMMyyyy"),
	DATE_PATTERN4("dd-MM-yyyy hh:mm:ss"),
	
	LOG_USER_DETAIL_FILE_PATH("LOG_USER_FILE_PATH"),
	
	ACTIVEVAL("A"),
	INACTIVEVAL("I"),
	
	RESULTBEAN_STATUS_FAILURE("failure"),
	RESULTBEAN_STATUS_SUCCESS("success"),
	SAVE_MESSAGE_SUCCESS("Saved Successfully"),
	SAVE_MESSAGE_FAILURE("Data is not saved"),
	DUPLICATE_MESSAGE_SUCCESS("Duplicate Record Exits"),
	UPDATE_MESSAGE_SUCCESS("Data updated Successfully"),
		
	COMMISSION_BOTH_TYPE("Both"),
	COMMISSION_NAVIGATION_STATUS("registration"),
	AMOUNT_ZERO_CHECK(0.0),
	PAYMENT_STATUS_SUCCESS("success"),
	COMM_EXCEL_FROM_DATE("FROM DATE"),
	COMM_EXCEL_TO_DATE("TO DATE"),
	COMM_EXCEL_OFFICE_CODE("Office Code"),
	COMM_EXCEL_AGENT_ARN("Agent ARN No"),
	SUCCESS_USER("5004"),
	LDAP_USER_EXIST("5005"),
	USER_DETAIL_BEAN("userDetailBean"),
	STATUS_MESSAGE("statusMessage"),
	ADMIN_DUPLICATE_ERROR("5000"),
	ADMIN_DB_DOWN_ERROR("5001"),
	ADMIN_TRANS_ERROR("5002"),
	ADMIN_DATA_ACCESS_ERROR("5003"),
	UPDATE_USER_MSG("5006"),
	NO_RECORD_USER_MSG("5007"),
	SAVE_GROUP_MSG("5008"),
	UPDATE_GROUP_MSG("5009"),
	NO_RECORD_GROUP_MSG("5010"),
	USER_ID("userId"),
	NAVIGATE_USER_LIST("listUser"),
	NAVIGATE_USER_UPDATE("userUpdate"),
	NAVIGATE_USER_CREATE("userCreate"),
	CONTEXT_SOURCE("contextSource"),
	LDAP_CN_USERS("cn=users"),
	LDAP_UID("cn"),
	GROUP_DETAIL_BEAN("groupDetailsBean"),
	GROUP_AUTH_BEAN("groupAuthBean"),
	NAVIGATE_GROUP_LIST("listGroup"),
	NAVIGATE_GROUP_CREATE("groupCreate"),
	NAVIGATE_GROUP_UPDATE("groupUpdate"),
	GROUP_ID("groupId"),
	/*STATUS_INACTIVE("I"),*/
	ROLE_URLACCESS("ROLE_URLACCESS"),
	LOGIN_MSG("5011"),
	INVALID_CREDENTIALS("5012"),
	LDAP_SERVER_DOWN("5014"),
	DATE_FORMAT("dd/MM/yyyy"),
	QUESTION_MARK ('?'),
	AMPERSANT_CHAR('&'),
	SINGLE_QOUTE("\'"),
	NULL_VALUE("null"),
	AMPERSANT("&"),
	COMMA_SEPERATOR(","),
	ZERO_STR("0"),
	LOCALE("LOCALE"),
	STATUS_COLUMN_NAME("status"),
	INACTIVE_STATUS_VALUE("3"),
	ACTION_STATUS_TRUE("true"),
	ACTION_STATUS_FALSE("false"),
	LOG4J("log4j.properties"),
	LANGUAGE_BEAN("langBean"),
	BLOCK("Block"),
	ALL_STRING("ALL"),
	COMMITTED_TYPE("committed"),
	SUSPENSE_TYPE("suspense"),
	CANNOT_OPEN_CONNECTION("Cannot open connection"),
	COMMUNICATION_EXCEPTION("CommunicationException"),
	AGENT_HEADER_SEARCH("Search"),
	AGENT_HEADER_UPDATE("Update"),
	NAVIGATE_USER_SEARCH("searchUser"),
	AGENT_RCPT_BOOK_SERIALNO("serialNo"),
	NAVIGATE_LANDING_PAGE("landingPage"),
	INVALID("Invalid"),
	COLON(":"),
	PENDING_FOR_APPR_STR("Pending for approval"),
	PLS_TRY_AFTER_SOME_TIME_STR("Please try after some time"),
	DOES_NOT_EXIST_STR("doesn't exist"),
	APP_STR("app"),
	HASH("#"),
	NAVIGATE_CLOSE_AGENT("closeAgent"),
	COLUMN_CURRENT_BALANCE("currentBalance"),
	TEMP_LOG_PATTERN("[%d{dd MMM yyyy HH:mm:ss}] %-5p %c{1}:%L - %m%n"),
	LOGOUT("logout"),
	ALPHA_NUMERIC_PATTERN("[a-zA-z0-9]*"),
	UN_TOKEN("KPPkZIc1caizNPRp95AEjQ=="),
	DR_MSG("iACERSzSXErpiBWrbCHH1QAJ/GBoOfen"),
	USERDETAIL("userDetail"),
	SEMI_COLON(";"),
	DATE_FORMAT1("dd-MM-yyyy"),
	SELECT("select"),
	
	STATUS_NO("N"),
	STATUS_YES("Y"),
	
	ALLUSERS("KEY_ALLUSERS"),
	ALLPROFILES("KEY_ALLPROFILES"),
	ALLROLES("KEY_ALLROLES"),
	ALLPRIVILEGES("KEY_ALLPRIVILEGES"),
	
	STATUS_ACTIVE_NO(1),
	STATUS_DEACTIVE_NO(2),
	STATUS_DISABLE_NO(3),
	STATUS_BLOCK_NO(4),
	
	STATUS_ACTIVE("ACTIVE"),
	STATUS_DEACTIVE("DEACTIVE"),
	STATUS_DISABLE("DISABLE"),
	STATUS_BLOCK("BLOCKED"),
	
	DEFAULT_USER("SYSTEM"),
	USER_SUPERADMIN("superadmin"),
	/*
	ACCOUNT_STATUS_STRING_ACTIVE("ACTIVE"),
	ACCOUNT_STATUS_STRING_INACTIVE("INACTIVE"),
	ACCOUNT_STATUS_STRING_DISABLE("DISABLE"),
	ACCOUNT_STATUS_STRING_BLOCK("BLOCK"),*/
	
	VALUE_EMPTY("EMPTY"),
	
	VALUE_PROFILE("PROFILE"),
	VALUE_ROLE("ROLE"),
	VALUE_PRIVILEGE("PRIV"),
	
	COMPONENT_TYPE_USER_STATUS("USER_STATUS"),
	COMPONENT_TYPE_USER("USER"),
	COMPONENT_TYPE_PROFILE("PROFILE"),
	COMPONENT_TYPE_ROLE("ROLE"),
	COMPONENT_TYPE_PRIVILEGE("PRIVILEGE"),
	
	COMPONENT_TYPE_ACCESSPOINT("ACCESSPOINT"),
	COMPONENT_TYPE_ACCESSPOINT_KEY("KEY"),
	COMPONENT_TYPE_ACCESSPOINT_SYSTEM("SYSTEM"),
	COMPONENT_TYPE_ACCESSPOINT_CERTIFICATE("CERTIFICATE"),
	COMPONENT_TYPE_ACCESSPOINT_MCC("MCC"),
	COMPONENT_TYPE_ACCESSPOINT_SCHEDULED_EOD("SCHEDULED_EOD"),
	COMPONENT_TYPE_ACCESSPOINT_SETTLEMENT("SETTLEMENT"),
	COMPONENT_TYPE_ACCESSPOINT_ERROR_CD("ERROR_CD"),
	COMPONENT_TYPE_ACCESSPOINT_VAE("VAE"),
	COMPONENT_TYPE_DEFAULT("DEFAULT"),
	
	APPROVAL_STATUS_INITIATED("INITIATED"),
	APPROVAL_STATUS_APPROVED("APPROVED"),
	APPROVAL_STATUS_REJECTED("REJECTED"),
	
	PRIVILIGE_VIEW("VIEW"),
	PRIVILIGE_MAKER("MAKER"),
	PRIVILIGE_CHECKER("CHECKER"),
	
	PRIVILIGE_SUBMIT("SUBMIT"),
	PRIVILIGE_APPROVE("APPROVE"),
	
	ACTION_INSERT("insert"),
	ACTION_UPDATE("update"),
	ACTION_DELETE("delete"),
	
	CU_SUPER_ADMIN("ROLE_PROFILE_CU_SUPER_ADMIN"),
	CU_LOCAL_ADMIN("ROLE_PROFILE_CU_LOCAL_ADMIN"),
	OU_LOCAL_ADMIN("ROLE_PROFILE_OU_LOCAL_ADMIN"),
	
	UNDERSCORE("_")
	;
	
	private String constantValue;
	private int intConstantValue;
	private double doubleConstantValue;
	private char charConstantValue;
	
	private ProjectConstants(char charConstantValue) {
		this.charConstantValue=charConstantValue;
	}
	private ProjectConstants(String constantValue) {
		this.constantValue=constantValue;
	}
	private ProjectConstants(int intConstantValue) {
		this.intConstantValue=intConstantValue;
	}
	private ProjectConstants(double doubleConstantValue)
	{
		this.doubleConstantValue=doubleConstantValue;
	}
	public String getConstantValue(){
		return constantValue;
	}
	
	public int getIntConstantValue(){
		return intConstantValue;
	}
	public double getDoubleConstantValue()
	{
		return doubleConstantValue;
	}
	public char getCharConstantValue(){
		return charConstantValue;
	}
}
