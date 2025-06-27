package com.kerneldc.flightlogserver.batch.copyFlightLogTable;

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

import com.kerneldc.flightlogserver.batch.tasklet.BeforeCopyTableTasklet;
import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;

//@Configuration
//@EnableBatchProcessing
public class CopyFlightLogTableJob {
	
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
    // flightlog
    //
    @Bean
    public JdbcCursorItemReader<FlightLog> flightLogReader() {
    	return new JdbcCursorItemReaderBuilder<FlightLog>()
                .dataSource(inputDataSource)
                .name("flightLogReader")
                .sql("select id, version, co_pilot, created, day_dual, day_solo, flight_date, instrument_flight_sim, instrument_imc, instrument_no_ifr_appr, instrument_simulated, make_model, modified, night_dual, night_solo, pic, registration, remarks, route_from, route_to, tos_ldgs_day, tos_ldgs_night, x_country_day, x_country_night from flight_log")
                .rowMapper(new FlightLogRowMapper())
                .build();
    }
       
    @Bean
    public JdbcBatchItemWriter<FlightLog> flightLogWriter() {
        return new JdbcBatchItemWriterBuilder<FlightLog>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<FlightLog>())
            .sql("insert into flight_log (id, version, co_pilot, created, day_dual, day_solo, flight_date, instrument_flight_sim, instrument_imc, instrument_no_ifr_appr, instrument_simulated, make_model, modified, night_dual, night_solo, pic, registration, remarks, route_from, route_to, tos_ldgs_day, tos_ldgs_night, x_country_day, x_country_night) values (flight_log_seq.nextval, :version, :coPilot, :created, :dayDual, :daySolo, :flightDate, :instrumentFlightSim, :instrumentImc, :instrumentNoIfrAppr, :instrumentSimulated, :makeModel, :modified, :nightDual, :nightSolo, :pic, :registration, :remarks, :routeFrom, :routeTo, :tosLdgsDay, :tosLdgsNight, :xcountryDay, :xcountryNight)")
            .dataSource(outputDataSource)
            .build();
    }
// tag::jobstep[]
    @Bean
    public Job copyFlightLogTable(Step flightLogTableStep1, Step flightLogTableStep2) {
        return new JobBuilder("copyFlightLogTable", jobRepository)
            .incrementer(new RunIdIncrementer())
            .flow(flightLogTableStep1)
            .next(flightLogTableStep2)
            .end()
            .build();
    }

    @Bean
    public Step flightLogTableStep1() {
        return new StepBuilder("flightLogTableStep1", jobRepository)
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "flight_log"))
            .build();
    }

    @Bean
    public Step flightLogTableStep2(JdbcCursorItemReader<FlightLog> flightLogReader, JdbcBatchItemWriter<FlightLog> flightLogWriter) {
        return new StepBuilder("flightLogTableStep2", jobRepository)
            .<FlightLog, FlightLog> chunk(500)
            .reader(flightLogReader)
            .processor(new FlightLogItemProcessor())
            .writer(flightLogWriter)
            .build();
    }

//    @Bean
//    public Step significantEventTableStep3() {
//        return stepBuilderFactory.get("significantEventTableStep3")
//        	.tasklet(new AfterCopyTableTasklet(outputDataSource, "flight_log"))
//            .build();
//    }
    // end::jobstep[]
}
