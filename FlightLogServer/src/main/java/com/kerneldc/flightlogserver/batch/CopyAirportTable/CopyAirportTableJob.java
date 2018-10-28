package com.kerneldc.flightlogserver.batch.CopyAirportTable;

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
import com.kerneldc.flightlogserver.domain.airport.Airport;

@Configuration
@EnableBatchProcessing
public class CopyAirportTableJob {
	
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
    // airport
    //
    @Bean
    public JdbcCursorItemReader<Airport> airportReader() {
    	return new JdbcCursorItemReaderBuilder<Airport>()
                .dataSource(inputDataSource)
                .name("airportReader")
                .sql("select id, identifier, name, province, city, country, latitude, longitude, upper_winds_station_id, created, modified, version from airport")
                //.sql("select id, event_date, event_description, created, modified, version from significant_event")
                .rowMapper(new AirportRowMapper())
                .build();
    }
       
    @Bean
    public JdbcBatchItemWriter<Airport> airportWriter() {
        return new JdbcBatchItemWriterBuilder<Airport>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Airport>())
            .sql("insert into airport (id, identifier, name, province, city, country, latitude, longitude, upper_winds_station_id, created, modified, version) values (airport_seq.nextval, :identifier, :name, :province, :city, :country, :latitude, :longitude, :upperWindsStationId, :created, :modified, :version)")
            .dataSource(outputDataSource)
            .build();
    }
       	
// tag::jobstep[]
    @Bean
    public Job copyAirportTable(Step airportTableStep1, Step airportTableStep2) {
        return jobBuilderFactory.get("copyAirportTable")
            .incrementer(new RunIdIncrementer())
            .flow(airportTableStep1)
            .next(airportTableStep2)
            .end()
            .build();
    }
    
    @Bean
    public Step airportTableStep1() {
        return stepBuilderFactory.get("airportTableStep1")
        	.tasklet(new InitCopyTasklet(outputDataSource, "airport"))
            .build();
    }

    @Bean
    public Step airportTableStep2(JdbcCursorItemReader<Airport> airportReader, JdbcBatchItemWriter<Airport> airportWriter) {
        return stepBuilderFactory.get("airportTableStep2")
            .<Airport, Airport> chunk(1000)
            .reader(airportReader)
            .processor(new AirportItemProcessor())
            .writer(airportWriter)
            .build();
    }
// end::jobstep[]
}
