package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.batch.tasklet.BeforeCopyTableTasklet;
import com.kerneldc.flightlogserver.batch.tasklet.CreateTableFromTableTasklet;
import com.kerneldc.flightlogserver.domain.EntityEnum;

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
                .sql("select component_id, name from public.component") // component is the old name of part table
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
                .sql("select c.name part_name, mr.work_performed, mr.date_performed, mr.hours_performed, mr.date_due, mr.hours_due\r\n" + 
                		"  from public.maint_rec mr\r\n" + 
                		"  join public.component c on mr.component_id = c.component_id") // maintenance_activity is the old name of component table
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

    /**
     * Job that copies Part and Component tables from old H2 database
     * @param parallelBackupFlow Is a parallel flow to make a backup of the part, component, component_history {@literal &} component_component_history tables
     * @param purgeTablesStep1 Step to purge component_component_history table
     * @param purgeTablesStep2 Step to purge component_history table
     * @param purgeTablesStep3 Step to purge component table
     * @param purgeTablesStep4 Step to purge part table
     * @param copyPartTableStep5 Copies part table
     * @param copyComponentTableStep6 Copied component table
     * @return
     */
    @Bean
	public Job copyAircraftMaintenanceTables(Flow parallelBackupFlow, Step purgeTablesStep1,
			Step purgeTablesStep2, Step purgeTablesStep3, Step purgeTablesStep4, Step copyPartTableStep5,
			Step copyComponentTableStep6) {
        return jobBuilderFactory.get("copyAircraftMaintenanceTables")
            .incrementer(new RunIdIncrementer())
            .start(parallelBackupFlow)
            .next(purgeTablesStep1)
            .next(purgeTablesStep2)
            .next(purgeTablesStep3)
            .next(purgeTablesStep4)
            .next(copyPartTableStep5)
            .next(copyComponentTableStep6)
            .build() // build parallelBackupFlow
            .build(); // build job
    }

    @Bean
    public Step backupPartTableStep1() {
        return stepBuilderFactory.get("backupPartTableStep1")
        	.tasklet(new CreateTableFromTableTasklet(outputDataSource, EntityEnum.PART))
            .build();
    }

    @Bean
    public Step backupComponentComponentHistoryTableStep2() {
        return stepBuilderFactory.get("backupComponentComponentHistoryTableStep2")
        	.tasklet(new CreateTableFromTableTasklet(outputDataSource, EntityEnum.COMPONENT_COMPONENT_HISTORY))
            .build();
    }

    @Bean
    public Step backupComponentHistoryTableStep3() {
        return stepBuilderFactory.get("backupComponentHistoryTableStep3")
        	.tasklet(new CreateTableFromTableTasklet(outputDataSource, EntityEnum.COMPONENT_HISTORY))
            .build();
    }

    @Bean
    public Step backupComponentTableStep4() {
        return stepBuilderFactory.get("backupComponentTableStep4")
        	.tasklet(new CreateTableFromTableTasklet(outputDataSource, EntityEnum.COMPONENT))
            .build();
    }

	@Bean
	public Flow backupFlow1(Step backupPartTableStep1) {
		return new FlowBuilder<SimpleFlow>("backupFlow1").start(backupPartTableStep1).build();
	}
	@Bean
	public Flow backupFlow2(Step backupComponentComponentHistoryTableStep2) {
		return new FlowBuilder<SimpleFlow>("backupFlow2").start(backupComponentComponentHistoryTableStep2).build();
	}
	@Bean
	public Flow backupFlow3(Step backupComponentHistoryTableStep3) {
		return new FlowBuilder<SimpleFlow>("backupFlow3").start(backupComponentHistoryTableStep3).build();
	}
	@Bean
	public Flow backupFlow4(Step backupComponentTableStep4) {
		return new FlowBuilder<SimpleFlow>("backupFlow4").start(backupComponentTableStep4).build();
	}
	@Bean
	public Flow parallelBackupFlow(Flow backupFlow1, Flow backupFlow2, Flow backupFlow3, Flow backupFlow4) {
	    return new FlowBuilder<SimpleFlow>("parallelBackupFlow")
	        .split(new SimpleAsyncTaskExecutor())
	        .add(backupFlow1, backupFlow2, backupFlow3, backupFlow4)
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
