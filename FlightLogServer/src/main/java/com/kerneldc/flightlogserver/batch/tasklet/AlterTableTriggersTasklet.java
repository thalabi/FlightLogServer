package com.kerneldc.flightlogserver.batch.tasklet;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.kerneldc.flightlogserver.batch.util.ReplicationUtil;

public class AlterTableTriggersTasklet implements Tasklet {

	private DataSource dataSource;

	public AlterTableTriggersTasklet(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Map<String, Object> jobParametersMap = chunkContext.getStepContext().getJobParameters();
		String tableName = (String)jobParametersMap.get("tableName");
		boolean triggerStatus = Boolean.parseBoolean((String)jobParametersMap.get("triggerStatus"));
		if (triggerStatus) {
			ReplicationUtil.enableTriggers(dataSource, tableName);
		} else {
			ReplicationUtil.disableTriggers(dataSource, tableName);
		}
		
		
		ReplicationUtil.tableReplicationStatus(dataSource, "flightlog", "flightlogv3", "flight_log");
		
		return RepeatStatus.FINISHED;
	}

}
