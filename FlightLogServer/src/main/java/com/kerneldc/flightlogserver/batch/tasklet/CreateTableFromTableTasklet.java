package com.kerneldc.flightlogserver.batch.tasklet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.kerneldc.flightlogserver.batch.util.ReplicationUtil;
import com.kerneldc.flightlogserver.domain.EntityEnum;

public class CreateTableFromTableTasklet implements Tasklet {

	private final SimpleDateFormat dateFormatYYMMDD = new SimpleDateFormat("yyMMdd");
	private final SimpleDateFormat timeFormatHHMMSS = new SimpleDateFormat("HHmmss");
	private DataSource outputDataSource;
	private EntityEnum entityEnum;

	public CreateTableFromTableTasklet(DataSource outputDataSource, EntityEnum entityEnum) {
		this.outputDataSource = outputDataSource;
		this.entityEnum = entityEnum;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Map<String, Object> jobParametersMap = chunkContext.getStepContext().getJobParameters();
		Date backupDateTime = new Date((long)jobParametersMap.get("sysTime"));
		String backupTableName = String.format(entityEnum.getBackupTableNameTemplate(),
				dateFormatYYMMDD.format(backupDateTime), timeFormatHHMMSS.format(backupDateTime));
		String sql = String.format("create table %s as select * from %s", backupTableName, entityEnum.getTableName());
		ReplicationUtil.executeSqlStatement(outputDataSource, sql);
		return RepeatStatus.FINISHED;
	}

}
