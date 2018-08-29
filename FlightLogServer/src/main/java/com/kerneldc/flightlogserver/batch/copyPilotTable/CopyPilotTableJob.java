package com.kerneldc.flightlogserver.batch.copyPilotTable;

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
import com.kerneldc.flightlogserver.domain.pilot.Pilot;

@Configuration
@EnableBatchProcessing
public class CopyPilotTableJob {
	
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
    // pilot
    //
    @Bean
    public JdbcCursorItemReader<Pilot> pilotReader() {
    	return new JdbcCursorItemReaderBuilder<Pilot>()
                .dataSource(inputDataSource)
                .name("pilotReader")
                .sql("select id, pilot, created, modified, version from pilot")
                .rowMapper(new PilotRowMapper())
                .build();
    }
       
    @Bean
    public JdbcBatchItemWriter<Pilot> pilotWriter() {
        return new JdbcBatchItemWriterBuilder<Pilot>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Pilot>())
            .sql("insert into pilot (id, pilot, created, modified, version) values (pilot_seq.nextval, :pilot, :created, :modified, :version)")
            .dataSource(outputDataSource)
            .build();
    }
       	
// tag::jobstep[]
    @Bean
    public Job copyPilotTable(Step pilotTableStep1, Step pilotTableStep2) {
        return jobBuilderFactory.get("copyPilotTable")
            .incrementer(new RunIdIncrementer())
            .flow(pilotTableStep1)
            .next(pilotTableStep2)
            .end()
            .build();
    }
    
    @Bean
    public Step pilotTableStep1() {
        return stepBuilderFactory.get("pilotTableStep1")
        	.tasklet(new InitCopyTasklet(outputDataSource, "pilot"))
            .build();
    }

    @Bean
    public Step pilotTableStep2(JdbcCursorItemReader<Pilot> pilotReader, JdbcBatchItemWriter<Pilot> pilotWriter) {
        return stepBuilderFactory.get("pilotTableStep2")
            .<Pilot, Pilot> chunk(10)
            .reader(pilotReader)
            .processor(new PilotItemProcessor())
            .writer(pilotWriter)
            .build();
    }
// end::jobstep[]
}
