package com.kerneldc.flightlogserver.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class DeleteTable implements Tasklet {

	private DataSource outputDataSource;
	private String tableName;

	public DeleteTable(DataSource outputDataSource, String tableName) {
		this.outputDataSource = outputDataSource;
		this.tableName = tableName;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		new JdbcTemplate(outputDataSource).execute("truncate table " + tableName);
		return RepeatStatus.FINISHED;
	}

}
