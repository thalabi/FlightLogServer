package com.kerneldc.flightlogserver.batch.tasklet;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.kerneldc.flightlogserver.batch.util.ReplicationUtil;

public class AfterCopyTableTasklet implements Tasklet {

	private DataSource outputDataSource;
	private String tableName;

	public AfterCopyTableTasklet(DataSource outputDataSource, String tableName) {
		this.outputDataSource = outputDataSource;
		this.tableName = tableName;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		ReplicationUtil.enableTriggers(outputDataSource, tableName);
		return RepeatStatus.FINISHED;
	}

}
