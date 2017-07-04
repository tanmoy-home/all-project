package com.rssoftware.ou.batch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import com.rssoftware.ou.batch.common.FileType;

public class ReaderWriterDecider implements JobExecutionDecider {
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		FileType fileType = (FileType)jobExecution.getExecutionContext().get("filetype");
		FlowExecutionStatus flowStatus = null;
		if (FileType.XML.equals(fileType)) {
			
			flowStatus = new FlowExecutionStatus("XML");
			System.out.println("FileType = XML");
		} else {
			flowStatus = new FlowExecutionStatus("FlatFile");
			System.out.println("FileType = FlatFile");
		}
		
		return flowStatus;
	}

}
