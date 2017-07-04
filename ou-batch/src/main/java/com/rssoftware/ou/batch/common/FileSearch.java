package com.rssoftware.ou.batch.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class FileSearch {

	private static Log logger = LogFactory.getLog(FileSearch.class);
	private Map<String,String> billerDetails = new HashMap<String,String>();  
	
	private String fileNameToSearch;
	private List<String> result = new ArrayList<String>();

	  public String getFileNameToSearch() {
		return fileNameToSearch;
	  }

	  public void setFileNameToSearch(String fileNameToSearch) {
		this.fileNameToSearch = fileNameToSearch;
	  }

	  public List<String> getResult() {
		return result;
	  }
	  
	  public Map<String,String> billerSearch(String parentDirectory,String indicator) {
			FileSearch fileSearch = new FileSearch();
			fileSearch.searchDirectory(new File(parentDirectory), indicator);

			int count = fileSearch.getResult().size();
			if(count ==0){
				logger.info("\nNo result found!");
			}else{
			    //System.out.println("\nFound " + count + " result!\n");
			    for (String matched : fileSearch.getResult()){
			    	String orginalFileName = matched.replace("ZERO_BYTE_FILE.", "");
			    	File orginalFile = new File(orginalFileName);
			    	if(orginalFile.exists())
			    	{
			    		String billerName = orginalFile.getParent().substring(orginalFile.getParent().lastIndexOf("\\")+1,orginalFile.getParent().length());
			    		billerDetails.put(orginalFileName,billerName);
			    	}
			    }
			}
			return billerDetails;
			
			
		  }
	  
	  public void searchDirectory(File directory, String fileNameToSearch) {

			setFileNameToSearch(fileNameToSearch);

			if (directory.isDirectory()) {
			    search(directory);
			} else {
			    //System.out.println(directory.getAbsoluteFile() + " is not a directory!");
			}

		  }
	  
	  private void search(File file) {

			if (file.isDirectory()) {
			  System.out.println("Searching directory ... " + file.getAbsoluteFile());

		            //do you have permission to read this directory?
			    if (file.canRead()) {
				for (File temp : file.listFiles()) {
				    if (temp.isDirectory()) {
					search(temp);
				    } else {
					/*if (getFileNameToSearch().equals(temp.getName().toLowerCase())) {
					    result.add(temp.getAbsoluteFile().toString());
				    }*/
				    if ( temp.getName().contains(getFileNameToSearch())) {
					    result.add(temp.getAbsoluteFile().toString());
					}	

				}
			    }

			 } else {
				 logger.info(file.getAbsoluteFile() + "Permission Denied");
			 }
		      }

		  }
}
