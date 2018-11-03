package com.kerneldc.flightlogserver.batch.copySignificantEventTable;

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

import com.kerneldc.flightlogserver.batch.tasklet.AfterCopyTableTasklet;
import com.kerneldc.flightlogserver.batch.tasklet.BeforeCopyTableTasklet;
import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEvent;

@Configuration
@EnableBatchProcessing
public class CopySignificantEventTableJob {
	
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
    // significantEvent
    //
    @Bean
    public JdbcCursorItemReader<SignificantEvent> significantEventReader() {
    	return new JdbcCursorItemReaderBuilder<SignificantEvent>()
                .dataSource(inputDataSource)
                .name("significantEventReader")
                .sql("select id, event_date, event_description, created, modified, version from significant_event")
                .rowMapper(new SignificantEventRowMapper())
                .build();
    }
       
    @Bean
    public JdbcBatchItemWriter<SignificantEvent> significantEventWriter() {
        return new JdbcBatchItemWriterBuilder<SignificantEvent>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<SignificantEvent>())
            .sql("insert into significant_event (id, event_date, event_description, created, modified, version) values (significant_event_seq.nextval, :eventDate, :eventDescription, :created, :modified, :version)")
            .dataSource(outputDataSource)
            .build();
    }
       	
// tag::jobstep[]
    @Bean
    public Job copySignificantEventTable(Step significantEventTableStep1, Step significantEventTableStep2, Step significantEventTableStep3) {
        return jobBuilderFactory.get("copySignificantEventTable")
            .incrementer(new RunIdIncrementer())
            .start(significantEventTableStep1)
            .next(significantEventTableStep2)
            .next(significantEventTableStep3)
            .build();
    }
    
    @Bean
    public Step significantEventTableStep1() {
        return stepBuilderFactory.get("significantEventTableStep1")
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "significant_event"))
            .build();
    }

    @Bean
    public Step significantEventTableStep2(JdbcCursorItemReader<SignificantEvent> significantEventReader, JdbcBatchItemWriter<SignificantEvent> significantEventWriter) {
        return stepBuilderFactory.get("significantEventTableStep2")
            .<SignificantEvent, SignificantEvent> chunk(10)
            .reader(significantEventReader)
            .processor(new SignificantEventItemProcessor())
            .writer(significantEventWriter)
            .build();
    }

    @Bean
    public Step significantEventTableStep3() {
        return stepBuilderFactory.get("significantEventTableStep3")
        	.tasklet(new AfterCopyTableTasklet(outputDataSource, "significant_event"))
            .build();
    }

// end::jobstep[]
}
