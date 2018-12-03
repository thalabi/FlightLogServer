package com.kerneldc.flightlogserver.batch.alterTableTriggers;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kerneldc.flightlogserver.batch.tasklet.AlterTableTriggersTasklet;

@Configuration
@EnableBatchProcessing
public class AlterTableTriggersJob {

    @Bean
    public Step alterTableTriggersStep(StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        return stepBuilderFactory.get("alterTableTriggersStep")
        	.tasklet(new AlterTableTriggersTasklet(dataSource))
            .build();
    }

    @Bean
    public Job alterTableTriggers(JobBuilderFactory jobBuilderFactory, Step alterTableTriggersStep) {
    	return jobBuilderFactory.get("alterTableTriggers")
            .incrementer(new RunIdIncrementer())
            .start(alterTableTriggersStep)
            .build();
    }

}
