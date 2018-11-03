package com.kerneldc.flightlogserver.batch.tasklet;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import com.kerneldc.flightlogserver.batch.util.DatabaseInfo;

public class BeforeCopyTableTasklet implements Tasklet {

	private DataSource outputDataSource;
	private String tableName;

	public BeforeCopyTableTasklet(DataSource outputDataSource, String tableName) {
		this.outputDataSource = outputDataSource;
		this.tableName = tableName;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String storageClause = DatabaseInfo.isOracleDatabase(outputDataSource) ? "reuse storage" : StringUtils.EMPTY;
		new JdbcTemplate(outputDataSource).execute(String.format("truncate table %s %s", tableName, storageClause));
		new JdbcTemplate(outputDataSource).execute(String.format("drop sequence %s_seq", tableName));
		new JdbcTemplate(outputDataSource).execute(String.format("create sequence %s_seq", tableName));
		DatabaseInfo.disableTriggers(outputDataSource, tableName);
		return RepeatStatus.FINISHED;
	}

}
