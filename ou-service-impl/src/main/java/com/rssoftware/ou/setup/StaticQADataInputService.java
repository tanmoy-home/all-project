package com.rssoftware.ou.setup;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.model.tenant.CertificateView;
import com.rssoftware.ou.model.tenant.TenantTemplateView;

public class StaticQADataInputService {
	
	private final static Logger logger = LoggerFactory.getLogger(StaticQADataInputService.class);

	private static org.joda.time.format.DateTimeFormatter formatteryyyy_MM_ddHH_mm_ss = DateTimeFormat
			.forPattern("yyyy-MM-ddHH:mm:ss");

	private static final String INSERT_CERTIFICATES_SQL = "INSERT INTO CERTIFICATES (ORG_ID,CERT_TYPE,KEY_FILE,PEM_FILE,P12_FILE,KEY_ALIAS,KEY_PWD,CSR_FILE,CSR_CHALLENGE_PWD,CRT_CER_FILE,CRT_CER_ALIAS,VALID_FROM,VALID_UPTO) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_TEMPLATE = "INSERT INTO TENANT_TEMPLATES (TEMPLATE_NAME,TEMPLATE_TYPE,CONTENT_CHAR,CONTENT_BIN) VALUES (?, ?, ?, ?)";
	private static final String DOWNLOAD_SETTLEMENT_FILE = "SELECT * FROM SETTLEMENT_FILE WHERE SETTLEMENT_CYCLE_ID = ?";

	@SuppressWarnings("resource") 
	public static void main(String... args) {
		System.out.println("***********************************************************");
		System.out.println("***********************************************************");
		System.out.println("**\tWelcome to Signer/Encryption Population Util\t***");
		System.out.println("***********************************************************");
		System.out.println("***********************************************************");

		System.out.println("\n Please Input 1 for uploading certificates, 2 for uploading Logo & Side Menu Image, 3 for downloading settlement file");
		Scanner scanner = new Scanner(System.in);
		int signOrEncrypt = 1;
		if (scanner.hasNextInt())
			signOrEncrypt = scanner.nextInt();
		scanner = new Scanner(System.in);
		System.out.println("\n Please Input BBPS HOME Folder Location");
		String bbpsHomeFolderLocation = null;
		if (scanner.hasNextLine())
			bbpsHomeFolderLocation = scanner.nextLine();
		System.out.println("\n Please Input Database URI : ");
		String databaseUrl = null;
		if (scanner.hasNextLine())
			databaseUrl = scanner.nextLine();
		System.out.println("\n Please Input Database User ID : ");
		String databaseUsername = null;
		if (scanner.hasNextLine())
			databaseUsername = scanner.nextLine();
		System.out.println("\n Please Input Database User Password : ");
		String databasePassword = null;
		if (scanner.hasNextLine())
			databasePassword = scanner.nextLine();

		if (signOrEncrypt == 1) {
			System.out.println("\n Please Input Tenant ID : ");
			if (scanner.hasNextLine()) {
				String tenantId = scanner.nextLine();
				insertCertificates(bbpsHomeFolderLocation, new String[] { databaseUrl, databaseUsername, databasePassword }, tenantId);
			}
		} else if (signOrEncrypt == 2) {
			insertLogoAndImages(bbpsHomeFolderLocation, new String[] { databaseUrl, databaseUsername, databasePassword });
		} else if (signOrEncrypt == 3) {
			downloadSettlementFile(bbpsHomeFolderLocation, new String[] { databaseUrl, databaseUsername, databasePassword });
		}
		
	}

	public static void insertCertificates(String certificateRootPath, String[] DB_DETAILS, String tenantId) {
		Connection conn = null;
		int returnRowCount = 0;
		try {
			conn = DriverManager.getConnection(DB_DETAILS[0], DB_DETAILS[1], DB_DETAILS[2]);

			PreparedStatement pstmt = conn.prepareStatement(INSERT_CERTIFICATES_SQL);
			returnRowCount = 0;
			for (CertificateView ce : getCertificates(certificateRootPath, tenantId)) {
				pstmt.setString(1, ce.getOrgId());
				pstmt.setString(2, ce.getCertType().name());
				if (null != ce.getKeyFile()) {
					pstmt.setBinaryStream(3, new ByteArrayInputStream(ce.getKeyFile()), ce.getKeyFile().length);
				} else {
					pstmt.setBinaryStream(3, null);
				}
				if (null != ce.getPemFile()) {
					pstmt.setBinaryStream(4, new ByteArrayInputStream(ce.getPemFile()), ce.getPemFile().length);
				} else {
					pstmt.setBinaryStream(4, null);
				}
				if (null != ce.getP12File()) {
					pstmt.setBinaryStream(5, new ByteArrayInputStream(ce.getP12File()), ce.getP12File().length);
				} else {
					pstmt.setBinaryStream(5, null);
				}
				pstmt.setString(6, ce.getKeyAlias());
				pstmt.setString(7, ce.getKeyPwd());
				if (null != ce.getCsrFile()) {
					pstmt.setBinaryStream(8, new ByteArrayInputStream(ce.getCsrFile()), ce.getCsrFile().length);
				} else {
					pstmt.setBinaryStream(8, null);
				}
				pstmt.setString(9, ce.getCsrChallengePwd());
				if (null != ce.getCrtCerFile()) {
					pstmt.setBinaryStream(10, new ByteArrayInputStream(ce.getCrtCerFile()), ce.getCrtCerFile().length);
				} else {
					pstmt.setBinaryStream(10, null);
				}
				pstmt.setString(11, ce.getCrtCerAlias());
				pstmt.setString(12, ce.getValidFrom());
				pstmt.setString(13, ce.getValidUpto());

				returnRowCount += pstmt.executeUpdate();
			}
			System.out.println("Total Number of Inserted Certificate Details " + returnRowCount);
		} catch (SQLException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error( e.getMessage(), e);
			        logger.info("In Excp : " + e.getMessage());
				}
			}
		}
	}

	private static List<CertificateView> getCertificates(String certificateRootPath, String tenantId) {
		List<CertificateView> ces = new ArrayList<CertificateView>();
		CertificateView ce = new CertificateView();
		ce.setOrgId(CommonConstants.BBPS_SYSTEM_NAME);
		ce.setCertType(CertificateView.CertType.SIGNER);
		ce.setCrtCerFile(convert(certificateRootPath + "/config/certificate/CU/signer.crt"));
		ce.setCrtCerAlias("1");
		populateFromAndToDates(ce);
		ces.add(ce);

		ce = new CertificateView();
		ce.setOrgId(CommonConstants.BBPS_SYSTEM_NAME);
		ce.setCertType(CertificateView.CertType.SSL);
		ce.setCrtCerFile(convert(certificateRootPath + "/config/certificate/CU/haproxy.crt"));
		ce.setCrtCerAlias("1");
		populateFromAndToDates(ce);
		ces.add(ce);

		ce = new CertificateView();
		ce.setOrgId(tenantId);
		ce.setCertType(CertificateView.CertType.SIGNER);
		ce.setKeyFile(convert(certificateRootPath + "/config/certificate/OU/ousigner.key"));
		ce.setP12File(convert(certificateRootPath + "/config/certificate/OU/ousigner.p12"));
		ce.setKeyAlias("1");
		ce.setKeyPwd("npciupi");
		ce.setCsrFile(convert(certificateRootPath + "/config/certificate/OU/ousigner.csr"));
		ce.setCrtCerFile(convert(certificateRootPath + "/config/certificate/OU/ousigner.crt"));
		ce.setCrtCerAlias("1");
		populateFromAndToDates(ce);
		ces.add(ce);
		return ces;
	}

	private static void insertLogoAndImages(String bbpsHomeFolderLocation, String[] DB_DETAILS) {
		Connection conn = null;
		int returnRowCount = 0;
		try {
			conn = DriverManager.getConnection(DB_DETAILS[0], DB_DETAILS[1], DB_DETAILS[2]);
			PreparedStatement pstmt = conn.prepareStatement(INSERT_TEMPLATE);
			returnRowCount = 0;
			for (TenantTemplateView tv : getTemplates(bbpsHomeFolderLocation)) {
				pstmt.setString(1, tv.getTemplateName());
				pstmt.setString(2, tv.getTemplateType());
				if("STRING".equalsIgnoreCase(tv.getTemplateType())) {
					pstmt.setCharacterStream(3 , new StringReader(tv.getContentChar()));
					pstmt.setBinaryStream(4, null);
				} else if("BYTE_ARRAY".equalsIgnoreCase(tv.getTemplateType())) {
					pstmt.setCharacterStream(3, null);
					pstmt.setBinaryStream(4, new ByteArrayInputStream(tv.getContentBin()), tv.getContentBin().length);
				}

				returnRowCount += pstmt.executeUpdate();
			}
			System.out.println("Total Number of Templates Inserted " + returnRowCount);
		} catch (SQLException | IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error( e.getMessage(), e);
			        logger.info("In Excp : " + e.getMessage());
				}
			}
		}
	}
	
	private static void downloadSettlementFile(String bbpsHomeFolderLocation, String[] DB_DETAILS) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_DETAILS[0], DB_DETAILS[1], DB_DETAILS[2]);
			PreparedStatement pstmt = conn.prepareStatement(DOWNLOAD_SETTLEMENT_FILE);
			Scanner scanner = new Scanner(System.in);
			System.out.println("\n Please Input SettlementCycleID : ");
			String settlementCycleId = "1";
			if (scanner.hasNextLine())
				settlementCycleId = scanner.nextLine();
			scanner.close();
			pstmt.setString(1, settlementCycleId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("pdf_file_name") !=null) {
					System.out.println("Saving File with Name "+rs.getString("pdf_file_name"));
					Files.write(Paths.get(URI.create(bbpsHomeFolderLocation)+"/"+rs.getString("pdf_file_name")+".pdf"), rs.getBytes("pdf_file"), StandardOpenOption.CREATE);
				} else if(rs.getString("csv_file_name") !=null) {
					System.out.println("Saving File with Name "+rs.getString("csv_file_name"));
					Files.write(Paths.get(URI.create(bbpsHomeFolderLocation)+"/"+rs.getString("csv_file_name")+".csv"), rs.getBytes("csv_file"), StandardOpenOption.CREATE);
				} else if(rs.getString("xls_file_name") !=null) {
					System.out.println("Saving File with Name "+rs.getString("xls_file_name"));
					Files.write(Paths.get(URI.create(bbpsHomeFolderLocation)+"/"+rs.getString("xls_file_name")+".xls"), rs.getBytes("xls_file"), StandardOpenOption.CREATE);
				} else if(rs.getString("txt_file_name") !=null) {
					System.out.println("Saving File with Name "+rs.getString("txt_file_name"));
					Files.write(Paths.get(URI.create(bbpsHomeFolderLocation)+"/"+rs.getString("txt_file_name")+".xml"), rs.getBytes("txt_file"), StandardOpenOption.CREATE);
				}				
			}
		} catch (SQLException | IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error( e.getMessage(), e);
			        logger.info("In Excp : " + e.getMessage());
				}
			}
		}
	}

	private static List<TenantTemplateView> getTemplates(String bbpsHomeFolderLocation) throws IOException {
		List<TenantTemplateView> tvs = new ArrayList<TenantTemplateView>();

		TenantTemplateView tv = new TenantTemplateView();
		tv.setTemplateName("BBPOULOGO");
		tv.setTemplateType("BYTE_ARRAY");
		tv.setContentBin(convert(bbpsHomeFolderLocation + "/Misc/Files/BBPOULOGO"));
		tvs.add(tv);

		tv = new TenantTemplateView();
		tv.setTemplateName("sideMenuSwitchImage");
		tv.setTemplateType("BYTE_ARRAY");
		tv.setContentBin(convert(bbpsHomeFolderLocation + "/Misc/Files/sideMenuSwitchImage"));
		tvs.add(tv);

		tv = new TenantTemplateView();
		tv.setTemplateName("tenantBillPayHTML");
		tv.setTemplateType("STRING");
		tv.setContentChar(
				new String(Files.readAllBytes(Paths.get(bbpsHomeFolderLocation + "/Misc/Files/tenantBillPayHTML"))));
		tvs.add(tv);
		
		tv = new TenantTemplateView();
		tv.setTemplateName("tenantHeader");
		tv.setTemplateType("STRING");
		tv.setContentChar(
				new String(Files.readAllBytes(Paths.get(bbpsHomeFolderLocation + "/Misc/Files/tenantHeader")))); 
		tvs.add(tv);
		
		tv = new TenantTemplateView();
		tv.setTemplateName("BBPOUCSS");
		tv.setTemplateType("STRING");
		tv.setContentChar(
				new String(Files.readAllBytes(Paths.get(bbpsHomeFolderLocation + "/Misc/Files/BBPOUCSS")))); 
		tvs.add(tv);
		
		return tvs;
	}

	private static byte[] convert(String file) {
		try {
			byte[] data = Files.readAllBytes((new File(file)).toPath());
			return data;
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
		}
		return null;
	}

	private static String getFormattedDateyyyy_MM_ddHH_mm_ss(Date dt) {
		return formatteryyyy_MM_ddHH_mm_ss.print(new DateTime(dt));
	}

	public static String toStringForHash(Object obj) {
		if (obj == null) {
			return "";
		}
		String ret = null;
		if (obj instanceof Date) {
			ret = getFormattedDateyyyy_MM_ddHH_mm_ss((Date) obj);
		} else if (obj instanceof String) {
			ret = ((String) obj).trim();
		} else if (obj instanceof byte[]) {
			ret = (new String((byte[]) obj)).trim();
		} else {
			ret = obj.toString();
		}
		return ret;
	}

	private static void populateFromAndToDates(CertificateView cv) {
		if (cv != null) {
			try {
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				InputStream is = new ByteArrayInputStream(cv.getCrtCerFile());
				InputStream caInput = new BufferedInputStream(is);
				Certificate ca;
				try {
					ca = cf.generateCertificate(caInput);

					if (ca instanceof X509Certificate) {
						X509Certificate xca = (X509Certificate) ca;
						cv.setValidFrom(CommonUtils.getFormattedDateyyyyMMdd(xca.getNotBefore()));
						cv.setValidUpto(CommonUtils.getFormattedDateyyyyMMdd(xca.getNotAfter()));
					}
				} finally {
					try {
						caInput.close();
					} catch (IOException e) {
					}
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			} catch (CertificateException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
		}
	}
}