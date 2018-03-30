package com.kerneldc.flightlogserver.batch;

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

import com.kerneldc.flightlogserver.domain.FlightLog;

@Configuration
@EnableBatchProcessing
public class CopyFlightLogTableJob {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
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
            .sql("insert into flight_log (id, version, co_pilot, created, day_dual, day_solo, flight_date, instrument_flight_sim, instrument_imc, instrument_no_ifr_appr, instrument_simulated, make_model, modified, night_dual, night_solo, pic, registration, remarks, route_from, route_to, tos_ldgs_day, tos_ldgs_night, x_country_day, x_country_night) values (:id, :version, :coPilot, :created, :dayDual, :daySolo, :flightDate, :instrumentFlightSim, :instrumentImc, :instrumentNoIfrAppr, :instrumentSimulated, :makeModel, :modified, :nightDual, :nightSolo, :pic, :registration, :remarks, :routeFrom, :routeTo, :tosLdgsDay, :tosLdgsNight, :xcountryDay, :xcountryNight)")
            .dataSource(outputDataSource)
            .build();
    }
// tag::jobstep[]
    @Bean
    public Job copyFlightLogTable(Step flightLogTableStep1, Step flightLogTableStep2) {
        return jobBuilderFactory.get("copyFlightLogTable")
            .incrementer(new RunIdIncrementer())
            .flow(flightLogTableStep1)
            .next(flightLogTableStep2)
            .end()
            .build();
    }

    @Bean
    public Step flightLogTableStep1() {
        return stepBuilderFactory.get("flightLogTableStep1")
        	.tasklet(new DeleteTable(outputDataSource, "flight_log"))
            .build();
    }

    @Bean
    public Step flightLogTableStep2(JdbcCursorItemReader<FlightLog> flightLogReader, JdbcBatchItemWriter<FlightLog> flightLogWriter) {
        return stepBuilderFactory.get("flightLogTableStep2")
            .<FlightLog, FlightLog> chunk(500)
            .reader(flightLogReader)
            .processor(new FlightLogItemProcessor())
            .writer(flightLogWriter)
            .build();
    }
    // end::jobstep[]
}
