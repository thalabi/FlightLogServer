package com.kerneldc.flightlogserver.batch.copyRegistrationTable;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.kerneldc.flightlogserver.batch.tasklet.InitCopyTasklet;
import com.kerneldc.flightlogserver.domain.registration.Registration;

@Configuration
@EnableBatchProcessing
public class CopyRegistrationTableJob {
	
	@Autowired
	@Qualifier("inputDataSource")
	public DataSource inputDataSource;
	
	@Autowired
	@Qualifier("outputDataSource")
	public DataSource outputDataSource;
	
    @Autowired
    @Lazy
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Lazy
    public StepBuilderFactory stepBuilderFactory;
    
    //
    // registration
    //
    @Bean
    public JdbcCursorItemReader<Registration> registrationReader() {
    	return new JdbcCursorItemReaderBuilder<Registration>()
                .dataSource(inputDataSource)
                .name("registrationReader")
                .sql("select id, registration, created, modified, version from registration")
                .rowMapper(new RegistrationRowMapper())
                .build();
    }
       
    @Bean
    public JdbcBatchItemWriter<Registration> registrationWriter() {
        return new JdbcBatchItemWriterBuilder<Registration>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Registration>())
            .sql("insert into registration (id, registration, created, modified, version) values (registration_seq.nextval, :registration, :created, :modified, :version)")
            .dataSource(outputDataSource)
            .build();
    }
       	
// tag::jobstep[]
    @Bean
    public Job copyRegistrationTable(Step registrationTableStep1, Step registrationTableStep2) {
        return jobBuilderFactory.get("copyRegistrationTable")
            .incrementer(new RunIdIncrementer())
            .flow(registrationTableStep1)
            .next(registrationTableStep2)
            .end()
            .build();
    }
    
    @Bean
    public Step registrationTableStep1() {
        return stepBuilderFactory.get("registrationTableStep1")
        	.tasklet(new InitCopyTasklet(outputDataSource, "registration"))
            .build();
    }

    @Bean
    public Step registrationTableStep2(JdbcCursorItemReader<Registration> registrationReader, JdbcBatchItemWriter<Registration> registrationWriter) {
        return stepBuilderFactory.get("registrationTableStep2")
            .<Registration, Registration> chunk(10)
            .reader(registrationReader)
            .processor(new RegistrationItemProcessor())
            .writer(registrationWriter)
            .build();
    }
// end::jobstep[]
}
