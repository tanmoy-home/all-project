package com.rssoftware.ou.batch.tasklet;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rssoftware.ou.batch.common.Dictionary;

public class DictionaryInitializerTasklet implements Tasklet {

	private DataSource dataSource;
	private String sql;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		JdbcTemplate template = new JdbcTemplate(getDataSource());
		Dictionary dictionary = template.query(sql, new DictionaryResultSetExtractor());
		chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("dictionary",
				dictionary);
		if (dictionary != null) {
			chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("filetype", dictionary.getFileType());
		}
		return RepeatStatus.FINISHED;
	}
}
