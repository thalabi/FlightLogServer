package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

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

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.batch.tasklet.BeforeCopyTableTasklet;

@Configuration
@EnableBatchProcessing
public class CopyAircraftMaintenanceTablesJob {

	@Autowired
	@Qualifier("inputDataSourceH2")
	public DataSource inputDataSourceH2;
	
	@Autowired
	@Qualifier("outputDataSource")
	public DataSource outputDataSource;

	@Autowired
	private PartCachingService partCachingService;
	
    @Autowired
    @Lazy
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Lazy
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public JdbcCursorItemReader<OldPart> oldPartReader() {
    	return new JdbcCursorItemReaderBuilder<OldPart>()
                .dataSource(inputDataSourceH2)
                .name("oldPartReader")
                .sql("select component_id, name from aircraft_maintenance.component") // component is the old name of part table
                .rowMapper(new OldPartRowMapper())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Part> partWriter() {
        return new JdbcBatchItemWriterBuilder<Part>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Part>())
            .sql("insert into part (id, name, created, modified, version) values (part_seq.nextval, :name, sysdate, sysdate, 0)")
            .dataSource(outputDataSource)
            .build();
    }

    @Bean
    public JdbcCursorItemReader<OldComponent> oldComponentReader() {
    	return new JdbcCursorItemReaderBuilder<OldComponent>()
                .dataSource(inputDataSourceH2)
                .name("oldComponentReader")
                .sql("select c.name part_name, ma.activity_performed, ma.date_performed, ma.hours_performed, ma.date_due, ma.hours_due\r\n" + 
                		"  from aircraft_maintenance.maintenance_activity ma\r\n" + 
                		"  join aircraft_maintenance.component c on ma.component_id = c.component_id") // maintenance_activity is the old name of component table
                .rowMapper(new OldComponentRowMapper())
                .build();
    }
    
    @Bean
    public JdbcBatchItemWriter<Component> componentWriter() {
        return new JdbcBatchItemWriterBuilder<Component>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Component>())
            .sql("insert into component (id, name, part_id, work_performed, date_performed, hours_performed, date_due, hours_due, deleted, created, modified, version) "
            		+ "values (component_seq.nextval, :part.name, :part.id, :workPerformed, :datePerformed, :hoursPerformed, :dateDue, :hoursDue, 'n', sysdate, sysdate, 0)")
            .dataSource(outputDataSource)
            .build();
    }

    @Bean
	public Job copyAircraftMaintenanceTables(Step purgeTablesStep1, Step purgeTablesStep2, Step purgeTablesStep3,
			Step purgeTablesStep4, Step copyPartTableStep5, Step copyComponentTableStep6) {
        return jobBuilderFactory.get("copyAircraftMaintenanceTables")
            .incrementer(new RunIdIncrementer())
            .start(purgeTablesStep1)
            .next(purgeTablesStep2)
            .next(purgeTablesStep3)
            .next(purgeTablesStep4)
            .next(copyPartTableStep5)
            .next(copyComponentTableStep6)
            .build();
    }

    @Bean
    public Step purgeTablesStep1() {
        return stepBuilderFactory.get("purgeTablesStep1")
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "component_component_history"))
            .build();
    }

    @Bean
    public Step purgeTablesStep2() {
        return stepBuilderFactory.get("purgeTablesStep2")
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "component_history"))
            .build();
    }

    @Bean
    public Step purgeTablesStep3() {
        return stepBuilderFactory.get("purgeTablesStep3")
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "component"))
            .build();
    }

    @Bean
    public Step purgeTablesStep4() {
        return stepBuilderFactory.get("purgeTablesStep4")
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "part"))
            .build();
    }

    @Bean
    public Step copyPartTableStep5(JdbcCursorItemReader<OldPart> oldPartReader, JdbcBatchItemWriter<Part> partWriter) {
        return stepBuilderFactory.get("copyPartTableStep5")
            .<OldPart, Part> chunk(1000)
            .reader(oldPartReader)
            .processor(new PartItemProcessor())
            .writer(partWriter)
            .build();
    }

    @Bean
    public Step copyComponentTableStep6(JdbcCursorItemReader<OldComponent> oldComponentReader, JdbcBatchItemWriter<Component> componentWriter) {
        return stepBuilderFactory.get("copyComponentTableStep6")
            .<OldComponent, Component> chunk(1000)
            .reader(oldComponentReader)
            .processor(new ComponentItemProcessor(partCachingService))
            .writer(componentWriter)
            .listener(new ComponentItemListener(partCachingService))
            .build();
    }
}
