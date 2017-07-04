package com.rssoftware.ou.cbsiso.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.npci.iso.imps.ISOMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.cbsiso.service.ISOMessageService;
import com.rssoftware.ou.cbsiso.util.ISOServiceMap;
import com.rssoftware.ou.iso8583.util.impl.BadValueException;
import com.rssoftware.ou.iso8583.util.impl.IsoGenericMapperImpl;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.schema.cbs.CBSISOConstants;

@Service
public class ISOMessageServiceImpl implements ISOMessageService{
	
	private final static Logger logger = LoggerFactory.getLogger(ISOMessageServiceImpl.class);
	
	@Autowired
	ISOServiceMap isoServiceMap;
	
	public static ISOMessageService getInstance(){
		return new ISOMessageServiceImpl();
	}
	
	Object obj = null;
	
	public byte[] packISOMsg(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException,BadValueException
	{
		obj = getISOModel();
		Map tmpMap = mapData;
		byte[] pack = new byte[0];
		
		if(obj instanceof IsoGenericMapperImpl)
		{
			try{
				pack = ((IsoGenericMapperImpl) obj).packInByte(mti, mapData);
			}
			catch(IsoMsgException e){throw new IsoMsgException("IsoMsgException");}
			catch(BadValueException e){throw new BadValueException("BadValueException");}
			catch(Exception e){
				logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
	        }
			
		}
		else
		{
			try{
				pack = ((ISOMessage) obj).createRequestMessage(mti, tmpMap);	
			}
			catch(IOException e){
				try {
				throw new IOException("IOException",e);
			    } catch (IOException e1) {
				// TODO Auto-generated catch block
			    	logger.error( e1.getMessage(), e);
			        logger.info("In Excp : " + e1.getMessage());
			      }
			}
			catch(Exception e){
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
			
		}
		
		return pack;
	} 
	
	@SuppressWarnings("unchecked")
	public Map<Integer, ? extends Object> unpackISOMsg(byte[] array) throws IsoMsgException 
	{
		obj = getISOModel();
		Map<Integer, Object> resultMap = null;
		
		if(obj instanceof IsoGenericMapperImpl)
		{
			try{
				resultMap = new HashMap<Integer, Object>();
				resultMap = (Map<Integer, Object>) ((IsoGenericMapperImpl) obj).unpackInByte(array);
				return resultMap;
			}
			catch(IsoMsgException e){throw new IsoMsgException("IsoMsgException");}
			catch(Exception e){
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
	        }
		}
		else
		{
			try{
				resultMap = new HashMap<Integer, Object>();
				resultMap = ((ISOMessage) obj).getResponseMessage(array);
				return resultMap; 
			}			
			catch(IOException e){throw new IsoMsgException("IOException");}
			catch(Exception e){
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
		}
		return resultMap;
	}
	
	
	private Object getISOModel()
	{
		if(isoServiceMap!=null)
		{
			if(isoServiceMap.getIsoEngine().equals(CBSISOConstants.isoDocEngine)){
				return new IsoGenericMapperImpl();
			}
			else
			{
				return new ISOMessage();
			}
		}
		else
		{
			return new IsoGenericMapperImpl();
		}
	}

}
