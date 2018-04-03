package com.kerneldc.flightlogserver.batch;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

public class SaveWriteCountStepExecutionListener implements StepExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.debug("stepExecution.getReadCount(): {}, stepExecution.getReadSkipCount(): {}, stepExecution.getWriteCount(): {}, stepExecution.getWriteSkipCount(): {}",
		stepExecution.getReadCount(), stepExecution.getReadSkipCount(), stepExecution.getWriteCount(), stepExecution.getWriteSkipCount());
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		executionContext.putInt("writeCount", stepExecution.getWriteCount());
		return ExitStatus.COMPLETED;
	}

}
