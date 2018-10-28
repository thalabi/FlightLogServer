package com.kerneldc.flightlogserver.batch.tasklet;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class InitCopyTasklet implements Tasklet {

	private DataSource outputDataSource;
	private String tableName;

	public InitCopyTasklet(DataSource outputDataSource, String tableName) {
		this.outputDataSource = outputDataSource;
		this.tableName = tableName;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		new JdbcTemplate(outputDataSource).execute("truncate table " + tableName);
		//new JdbcTemplate(outputDataSource).execute("alter sequence " + tableName + "_seq restart with 1 increment by 1");
		new JdbcTemplate(outputDataSource).execute("drop sequence " + tableName + "_seq");
		new JdbcTemplate(outputDataSource).execute("create sequence " + tableName + "_seq");
		return RepeatStatus.FINISHED;
	}

}
