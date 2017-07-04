package com.rssoftware.ou.tenant.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.SettlementFile;
import com.rssoftware.ou.database.entity.tenant.SettlementFilePK;
import com.rssoftware.ou.model.tenant.SettlementFileView;
import com.rssoftware.ou.tenant.dao.SettlementFileDao;
import com.rssoftware.ou.tenant.service.SettlementFileService;

@Service
public class SettlementFileServiceImpl implements SettlementFileService {

	private static Logger log = LoggerFactory.getLogger(SettlementFileServiceImpl.class);
	@Autowired
	private SettlementFileDao settlementFileDao;
	
	@Override
	public void insert(SettlementFileView sFileView) {
		String path = System.getProperty("reconFileLocation").substring(5)+"/cu";
		String fileName = sFileView.getFileName();
		byte[] fileBytes = sFileView.getFile();		
		SettlementFile sFile = new SettlementFile();
		SettlementFilePK pk = new SettlementFilePK();
		
		if(SettlementFile.FileType.CSV_REPORT == sFileView.getFileType()) {
			fileName = fileName+".csv";
			sFile.setCsvFile(fileBytes);
			sFile.setCsvFilename(sFileView.getFileName());
		}
		if(SettlementFile.FileType.PDF_REPORT == sFileView.getFileType()) {
			fileName = fileName+".pdf";
			sFile.setPdfFile(fileBytes);
			sFile.setPdfFilename(sFileView.getFileName());
		}
		if(SettlementFile.FileType.XLS_REPORT == sFileView.getFileType()) {
			fileName = fileName+".xls";
			sFile.setXlsFile(fileBytes);
			sFile.setXlsFilename(sFileView.getFileName());
		}
		if(SettlementFile.FileType.MNSB == sFileView.getFileType()) {
			fileName = fileName+".txt";
			sFile.setMnsbFile(fileBytes);
			sFile.setMnsbFilename(sFileView.getFileName());
		}
		if(SettlementFile.FileType.FINRAW == sFileView.getFileType()) {
			fileName = fileName+".txt";
			sFile.setTxtFile(fileBytes);
			sFile.setTxtFilename(sFileView.getFileName());
		}
		
		pk.setSettlementCycleId(extractSettlementCycleIdFromFileName(sFileView.getFileName()));
		pk.setFileType(sFileView.getFileType().name());
		sFile.setSettlementFilePK(pk);
		
		String outputFile = path+"/"+fileName;
		OutputStream fileOut;
		try {
			File file = new File(outputFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileOut = new FileOutputStream(file);
			fileOut.write(sFileView.getFile());
			log.debug("write done"+file.getAbsolutePath());
			fileOut.flush();
			fileOut.close();
		} 
		catch (FileNotFoundException e) {
			log.error("Error: ", e);		
		}
		catch (IOException e) {
			log.error("Error: ", e);		
		}
		settlementFileDao.create(sFile);
	}
	
	
	private String extractSettlementCycleIdFromFileName(String originalFileName) {
		String settlementCycleId = null;
		try {
			settlementCycleId =  originalFileName.substring(2, 5) +
									originalFileName.substring(9, originalFileName.length() -2);			
		} catch(StringIndexOutOfBoundsException sioue) {
			    log.error( sioue.getMessage(), sioue);
	            log.info("In Excp : " + sioue.getMessage());
		}
		return settlementCycleId;
	}

	public SettlementFileDao getSettlementFileDao() {
		return settlementFileDao;
	}

	public void setSettlementFileDao(SettlementFileDao settlementFileDao) {
		this.settlementFileDao = settlementFileDao;
	}
}
