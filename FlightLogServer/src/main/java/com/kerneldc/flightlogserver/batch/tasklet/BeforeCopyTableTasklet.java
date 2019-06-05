package com.kerneldc.flightlogserver.batch.tasklet;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import com.kerneldc.flightlogserver.batch.util.ReplicationUtil;

/**
 * Task that performs the following on a table:
 * 1. truncates table
 * 2. resets sequence
 * 3. disable table triggers
 *
 */
public class BeforeCopyTableTasklet implements Tasklet {

	private DataSource outputDataSource;
	private String tableName;

	public BeforeCopyTableTasklet(DataSource outputDataSource, String tableName) {
		this.outputDataSource = outputDataSource;
		this.tableName = tableName;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String sql;
//		if (ReplicationUtil.isOracleDatabase(outputDataSource)) {
//			sql = String.format("truncate table %s reuse storage", tableName);
//		} else {
			sql = String.format("delete from %s", tableName);
//		}
		new JdbcTemplate(outputDataSource).execute(sql);
		ReplicationUtil.resetSequence(outputDataSource, tableName);
		ReplicationUtil.disableTriggers(outputDataSource, tableName);
		return RepeatStatus.FINISHED;
	}

}
