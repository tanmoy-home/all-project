package com.rssoftware.ou.tenant.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.CertificatePK;
import com.rssoftware.ou.model.tenant.CertificateView;
import com.rssoftware.ou.model.tenant.CertificateView.CertType;
import com.rssoftware.ou.tenant.dao.CertificateDao;
import com.rssoftware.ou.tenant.service.CertificateService;

@Service
public class CertificateServiceImpl implements CertificateService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CertificateDao certificateDao; 
	
	private final Map<String, X509TrustManager> trustManagerMap = new ConcurrentHashMap<String, X509TrustManager>();
	
	private final Map<String, Certificate> cuSignerMap = new ConcurrentHashMap<String, Certificate>();
	
	private final Map<String,PrivateKey> ouSignerPrivateKeyMap = new ConcurrentHashMap<String, PrivateKey>();
	
	private final Map<String,PublicKey> ouSignerPublicKeyMap = new ConcurrentHashMap<String, PublicKey>();
	
	private Lock loadingLock = new ReentrantLock(); 
	
	@Override
	public void refresh() throws KeyStoreException, FileNotFoundException, CertificateException, 
	IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
		String METHOD_NAME = "refresh";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		
		try {
			String tenantId = TransactionContext.getTenantId();
			if (!CommonUtils.hasValue(tenantId)){
				throw new IllegalArgumentException("tenant ID blank");
			}

			loadingLock.lock();
			trustManagerMap.remove(tenantId);
			cuSignerMap.remove(tenantId);
			ouSignerPrivateKeyMap.remove(tenantId);
			ouSignerPublicKeyMap.remove(tenantId);
			try {
				// load keystore from specified cert store (or default)
				KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
				InputStream in = new FileInputStream(CommonUtils.getDefaultTrustStore());
				try { 
					ts.load(in, null); 
				}
				finally { 
					in.close(); 
				}

				Certificate cuSigner = null;
				PrivateKey ouSignerPrivateKey = null;
				PublicKey ouSignerPublicKey = null;
				
				List<com.rssoftware.ou.database.entity.tenant.Certificate> certs = certificateDao.getAll();
				if (certs != null){
					for (com.rssoftware.ou.database.entity.tenant.Certificate ce : certs) {
						CertificateView cv = mapFrom(ce);
						
						if (cv != null){
							Certificate c = extractCertificateFromCertificateEntry(cv);
							if (c != null){
								if (cv.getCertType() == CertType.SSL && CommonConstants.BBPS_SYSTEM_NAME.equals(cv.getOrgId())){
									try {
										ts.deleteEntry(CertType.SSL.name()+tenantId);
									} catch (Exception e) {}
									ts.setCertificateEntry(CertType.SSL.name()+tenantId, c);
								}
								else if (cv.getCertType() == CertType.SIGNER && CommonConstants.BBPS_SYSTEM_NAME.equals(cv.getOrgId())){
									cuSigner = c;
								}
								else if (cv.getCertType() == CertType.SIGNER && tenantId.equals(cv.getOrgId())){
									KeyStore keystore = KeyStore.getInstance("PKCS12");

					                /*Information for certificate to be generated */ 
					                String password = cv.getKeyPwd();
					                String alias = ce.getKeyAlias();

					    			InputStream is = new ByteArrayInputStream(ce.getP12File());
					    			InputStream caInput = new BufferedInputStream(is);
					                
					                /* getting the key*/
					                keystore.load(caInput, password.toCharArray());
					                PrivateKey key = (PrivateKey)keystore.getKey(alias, password.toCharArray());
					                
					                ouSignerPrivateKey = key;
					                ouSignerPublicKey = c.getPublicKey();
					                
					                try {
										caInput.close();
									} catch (Exception e) {}
					                try {
										is.close();
									} catch (Exception e) {}
								}
							}
						}
					}
				}
				
				if (cuSigner != null){
					cuSignerMap.put(tenantId, cuSigner);
				}
				
				if (ouSignerPrivateKey != null){
					ouSignerPrivateKeyMap.put(tenantId, ouSignerPrivateKey);	
				}

				if (ouSignerPublicKey != null){
					ouSignerPublicKeyMap.put(tenantId, ouSignerPublicKey);	
				}

				// initialize a new TMF with the ts we just loaded
				TrustManagerFactory tmf = TrustManagerFactory
						.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(ts);

				// acquire X509 trust manager from factory
				TrustManager tms[] = tmf.getTrustManagers();
				for (int i = 0; i < tms.length; i++) {
					if (tms[i] instanceof X509TrustManager) {
						trustManagerMap.put(tenantId, (X509TrustManager) tms[i]);
						return;
					}
				}

				throw new NoSuchAlgorithmException(
						"No X509TrustManager in TrustManagerFactory");
			}
			finally {
				loadingLock.unlock();
			}
		}
		catch (KeyStoreException e){
			log.error( e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			throw e;
		}
		catch (FileNotFoundException e){
			log.error( e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			throw e;
		}
		catch (CertificateException e){
			log.error( e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			throw e;
		}
		catch (IOException e){
			log.error( e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			throw e;
		}
		catch (NoSuchAlgorithmException e){
			log.error( e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			throw e;
		}
		catch (UnrecoverableKeyException e){
			log.error( e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			throw e;
		}
		
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	@Override
	public void save(CertificateView ce){
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			throw new IllegalArgumentException("tenant ID blank");
		}
		certificateDao.createOrUpdate(mapTo(ce));
	}

	@Override
	public void delete(CertificateView ce){
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			throw new IllegalArgumentException("tenant ID blank");
		}
		certificateDao.delete(mapTo(ce));
	}

	@Override
	public CertificateView[] retrieveCertificateList() {
		String tenantId = TransactionContext.getTenantId();
		if (!CommonUtils.hasValue(tenantId)){
			throw new IllegalArgumentException("tenant ID blank");
		}
		List<com.rssoftware.ou.database.entity.tenant.Certificate> certs = certificateDao.getAll();
		if (certs != null){
			return certs.toArray(new CertificateView[certs.size()]);
		}
		
		return null;
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		String METHOD_NAME = "checkServerTrusted";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			String tenantId = TransactionContext.getTenantId();
			if (!CommonUtils.hasValue(tenantId)){
				throw new IllegalArgumentException("tenant ID blank");
			}

			X509TrustManager localTrustManager = trustManagerMap.get(tenantId);
			if (localTrustManager == null){
				try {
					loadingLock.lock();
					trustManagerMap.get(tenantId).checkServerTrusted(chain, authType);
					return;
				}
				finally{
					loadingLock.unlock();
				}
			}
			localTrustManager.checkServerTrusted(chain, authType);
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	@Override
	public Certificate getCUSignerCertificate() {
		String METHOD_NAME = "getCUSignerCertificate";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			String tenantId = TransactionContext.getTenantId();
			if (!CommonUtils.hasValue(tenantId)){
				throw new IllegalArgumentException("tenant ID blank");
			}

			Certificate localSigner = cuSignerMap.get(tenantId);
			if (localSigner == null){
				try {
					loadingLock.lock();
					return cuSignerMap.get(tenantId);
				}
				finally{
					loadingLock.unlock();
				}
			}
			return localSigner;
		}
		catch (Exception e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	@Override
	public Key[] getOUSignerPrivatePublicKey() {
		String METHOD_NAME = "getOUSignerPrivatePublicKey";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			String tenantId = TransactionContext.getTenantId();
			if (!CommonUtils.hasValue(tenantId)){
				throw new IllegalArgumentException("tenant ID blank");
			}
			
			PrivateKey privateKey = ouSignerPrivateKeyMap.get(tenantId);
			PublicKey publicKey = ouSignerPublicKeyMap.get(tenantId);
			
			if (privateKey == null || publicKey == null){
				try {
					loadingLock.lock();
					Key[] keys = new Key[2];
					keys[0] = ouSignerPrivateKeyMap.get(tenantId);
					keys[1] = ouSignerPublicKeyMap.get(tenantId);
					return keys;
				}
				finally{
					loadingLock.unlock();
				}
			}
			
			Key[] keys = new Key[2];
			keys[0] = privateKey;
			keys[1] = publicKey;
			return keys;
		}
		catch (Exception e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	@Override
	public X509TrustManager getTrustManager() {
		String METHOD_NAME = "getTrustManager";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			String tenantId = TransactionContext.getTenantId();
			if (!CommonUtils.hasValue(tenantId)){
				throw new IllegalArgumentException("tenant ID blank");
			}

			X509TrustManager localTrustManager = trustManagerMap.get(tenantId);
			if (localTrustManager == null){
				try {
					loadingLock.lock();
					return trustManagerMap.get(tenantId);
				}
				finally{
					loadingLock.unlock();
				}
			}
			return localTrustManager;
		}
		catch (Exception e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	private static String convert(Date dt){
		if (dt == null){
			return null;
		}
		return CommonUtils.getFormattedDateyyyyMMdd(dt);
	}

	private static Certificate extractCertificateFromCertificateEntry(CertificateView ce) throws CertificateException{
    	if (ce != null && ce.getOrgId() != null
    			&& ce.getCrtCerFile() != null){
    		boolean isValid = false;
    		
    		if (ce.getValidUpto() == null || ce.getValidFrom() == null){
    			isValid = false;
    		}
    		else {
    			String validUpto = ce.getValidUpto();
    			String validFrom = ce.getValidFrom();
    			String today = convert(new Date());
    			if (today.compareTo(validUpto) <= 0 && today.compareTo(validFrom) >= 0){
    				isValid = true;
    			}
    		}
    		
    		if (isValid){
    			CertificateFactory cf = CertificateFactory.getInstance("X.509");
    			InputStream is = new ByteArrayInputStream(ce.getCrtCerFile());
    			InputStream caInput = new BufferedInputStream(is);
    			Certificate ca;
    			try {
    				ca = cf.generateCertificate(caInput);
    				return ca;
    			} finally {
    				try {
						caInput.close();
					} catch (IOException e) {}
    				try {
						is.close();
					} catch (IOException e) {}
    			}
    		}
    	}
    	return null;
	}

	private static CertificateView mapFrom(com.rssoftware.ou.database.entity.tenant.Certificate cert){
		if (cert == null){
			return null;
		}
		
		CertificateView cv = new CertificateView();
		cv.setOrgId(cert.getId().getOrgId());
		cv.setCertType(CertType.valueOf(cert.getId().getCertType()));
		cv.setCrtCerAlias(cert.getCrtCerAlias());
		cv.setCrtCerFile(cert.getCrtCerFile());
		cv.setCsrChallengePwd(cert.getCsrChallengePwd());
		cv.setCsrFile(cert.getCsrFile());
		cv.setKeyAlias(cert.getKeyAlias());
		cv.setKeyFile(cert.getKeyFile());
		cv.setKeyPwd(cert.getKeyPwd());
		cv.setP12File(cert.getP12File());
		cv.setPemFile(cert.getPemFile());
		cv.setValidFrom(cert.getValidFrom());
		cv.setValidUpto(cert.getValidUpto());
		
		return cv;
	}
	
	private static com.rssoftware.ou.database.entity.tenant.Certificate mapTo(CertificateView cert){
		if (cert == null){
			return null;
		}
		
		com.rssoftware.ou.database.entity.tenant.Certificate cv = new com.rssoftware.ou.database.entity.tenant.Certificate();
		CertificatePK pk = new CertificatePK();
		
		pk.setOrgId(cert.getOrgId());
		pk.setCertType(cert.getCertType().name());
		cv.setId(pk);
		
		cv.setCrtCerAlias(cert.getCrtCerAlias());
		cv.setCrtCerFile(cert.getCrtCerFile());
		cv.setCsrChallengePwd(cert.getCsrChallengePwd());
		cv.setCsrFile(cert.getCsrFile());
		cv.setKeyAlias(cert.getKeyAlias());
		cv.setKeyFile(cert.getKeyFile());
		cv.setKeyPwd(cert.getKeyPwd());
		cv.setP12File(cert.getP12File());
		cv.setPemFile(cert.getPemFile());
		cv.setValidFrom(cert.getValidFrom());
		cv.setValidUpto(cert.getValidUpto());
		
		return cv;
	}

}
