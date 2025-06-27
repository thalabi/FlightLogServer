package com.kerneldc.flightlogserver.batch.alterTableTriggers;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.kerneldc.flightlogserver.batch.tasklet.AlterTableTriggersTasklet;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class AlterTableTriggersJob {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManage;


    @Bean
    public Step alterTableTriggersStep(DataSource dataSource) {
        return new StepBuilder("alterTableTriggersStep", jobRepository)
        	.tasklet(new AlterTableTriggersTasklet(dataSource), transactionManage)
            .build();
    }

    @Bean
    public Job alterTableTriggers(Step alterTableTriggersStep) {
    	return new JobBuilder("alterTableTriggers", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(alterTableTriggersStep)
            .build();
    }

}
