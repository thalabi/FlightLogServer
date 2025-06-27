package com.kerneldc.flightlogserver.batch.copyRegistrationTable;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.kerneldc.flightlogserver.batch.tasklet.AfterCopyTableTasklet;
import com.kerneldc.flightlogserver.batch.tasklet.BeforeCopyTableTasklet;
import com.kerneldc.flightlogserver.domain.registration.Registration;

//@Configuration
//@EnableBatchProcessing
public class CopyRegistrationTableJob {
	
	@Autowired
	@Qualifier("inputDataSource")
	public DataSource inputDataSource;
	
	@Autowired
	@Qualifier("outputDataSource")
	public DataSource outputDataSource;
	
	@Autowired
	private JobRepository jobRepository;
//    @Autowired
//    @Lazy
//    public JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    @Lazy
//    public StepBuilderFactory stepBuilderFactory;
    
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
    public Job copyRegistrationTable(Step registrationTableStep1, Step registrationTableStep2, Step registrationTableStep3) {
        return new JobBuilder("copyRegistrationTable", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(registrationTableStep1)
            .next(registrationTableStep2)
            .next(registrationTableStep3)
            .build();
    }
    
    @Bean
    public Step registrationTableStep1() {
        return new StepBuilder("registrationTableStep1", jobRepository)
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "registration"))
            .build();
    }

    @Bean
    public Step registrationTableStep2(JdbcCursorItemReader<Registration> registrationReader, JdbcBatchItemWriter<Registration> registrationWriter) {
        return new StepBuilder("registrationTableStep2", jobRepository)
            .<Registration, Registration> chunk(10)
            .reader(registrationReader)
            .processor(new RegistrationItemProcessor())
            .writer(registrationWriter)
            .build();
    }

    @Bean
    public Step registrationTableStep3() {
        return new StepBuilder("registrationTableStep3", jobRepository)
        	.tasklet(new AfterCopyTableTasklet(outputDataSource, "registration"))
            .build();
    }
// end::jobstep[]
}
