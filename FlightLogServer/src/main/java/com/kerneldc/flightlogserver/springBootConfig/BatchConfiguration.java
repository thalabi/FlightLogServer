package com.kerneldc.flightlogserver.springBootConfig;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.kerneldc.flightlogserver.batch.RegistrationItemProcessor;
import com.kerneldc.flightlogserver.batch.RegistrationRowMapper;
import com.kerneldc.flightlogserver.domain.Registration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	public DataSource inputDataSource;
	
	@Autowired
	public DataSource outputDataSource;
	
    @Autowired
    @Lazy
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Lazy
    public StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public JdbcCursorItemReader<Registration> reader(DataSource inputDataSource) {
    	return new JdbcCursorItemReaderBuilder<Registration>()
                .dataSource(inputDataSource)
                .name("registrationReader")
                .sql("select id, registration, created, modified, version from registration")
                .rowMapper(new RegistrationRowMapper())
                .build();
    }
    
    @Bean
    public RegistrationItemProcessor processor() {
        return new RegistrationItemProcessor();
    }
    
    @Bean
    public JdbcBatchItemWriter<Registration> writer(DataSource outputDataSource) {
        return new JdbcBatchItemWriterBuilder<Registration>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("insert into registration (id, registration, created, modified, version) values (:id, :registration, :created, :modified, :version)")
            .dataSource(outputDataSource)
            .build();
    }
    
// tag::jobstep[]
    @Bean
    public Job copyFlightLogTable(Step step1, JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("copyFlightLogTable")
            .incrementer(new RunIdIncrementer())
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Registration> writer, StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step1")
            .<Registration, Registration> chunk(10)
            .reader(reader(inputDataSource))
            .processor(processor())
            .writer(writer)
            .build();
    }
    // end::jobstep[]
}
