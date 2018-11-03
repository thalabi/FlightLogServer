package com.kerneldc.flightlogserver.batch.copyMakeModelTable;

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
import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;

@Configuration
@EnableBatchProcessing
public class CopyMakeModelTableJob {
	
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
    // make & model
    //
    @Bean
    public JdbcCursorItemReader<MakeModel> makeModelReader() {
    	return new JdbcCursorItemReaderBuilder<MakeModel>()
                .dataSource(inputDataSource)
                .name("makeModelReader")
                .sql("select id, make_model, created, modified, version from make_model")
                .rowMapper(new MakeModelRowMapper())
                .build();
    }
       
    @Bean
    public JdbcBatchItemWriter<MakeModel> makeModelWriter() {
        return new JdbcBatchItemWriterBuilder<MakeModel>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<MakeModel>())
            .sql("insert into make_model (id, make_model, created, modified, version) values (make_model_seq.nextval, :makeModel, :created, :modified, :version)")
            .dataSource(outputDataSource)
            .build();
    }
       	
// tag::jobstep[]
    @Bean
    public Job copyMakeModelTable(Step makeModelTableStep1, Step makeModelTableStep2, Step makeModelTableStep3) {
        return jobBuilderFactory.get("copyMakeModelTable")
            .incrementer(new RunIdIncrementer())
            .start(makeModelTableStep1)
            .next(makeModelTableStep2)
            .next(makeModelTableStep3)
            .build();
    }
    
    @Bean
    public Step makeModelTableStep1() {
        return stepBuilderFactory.get("makeModelTableStep1")
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "make_model"))
            .build();
    }

    @Bean
    public Step makeModelTableStep2(JdbcCursorItemReader<MakeModel> makeModelReader, JdbcBatchItemWriter<MakeModel> makeModelWriter) {
        return stepBuilderFactory.get("makeModelTableStep2")
            .<MakeModel, MakeModel> chunk(10)
            .reader(makeModelReader)
            .processor(new MakeModelItemProcessor())
            .writer(makeModelWriter)
            .build();
    }

    @Bean
    public Step makeModelTableStep3() {
        return stepBuilderFactory.get("makeModelTableStep3")
        	.tasklet(new AfterCopyTableTasklet(outputDataSource, "make_model"))
            .build();
    }
// end::jobstep[]
}
