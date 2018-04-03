package com.kerneldc.flightlogserver.batch.tasklet;

import java.lang.invoke.MethodHandles;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class ResetSequenceTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private DataSource outputDataSource;
	private String sequenceName;
	
	public ResetSequenceTasklet(DataSource outputDataSource, String tableName) {
		this.outputDataSource = outputDataSource;
		this.sequenceName = tableName;
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		Integer writeCount = (Integer)chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("writeCount");
		LOGGER.debug("sequenceName: {}, writeCount: {}", sequenceName, writeCount);
		new JdbcTemplate(outputDataSource).execute("alter sequence " + sequenceName + " restart with " + writeCount);
		return RepeatStatus.FINISHED;
	}

}
