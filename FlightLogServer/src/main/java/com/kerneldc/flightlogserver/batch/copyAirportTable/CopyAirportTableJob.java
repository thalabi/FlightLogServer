package com.kerneldc.flightlogserver.batch.copyAirportTable;

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
import com.kerneldc.flightlogserver.domain.airport.Airport;

//@Configuration
//@EnableBatchProcessing
public class CopyAirportTableJob {
	
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
    // airport
    //
    @Bean
    public JdbcCursorItemReader<Airport> airportReader() {
    	return new JdbcCursorItemReaderBuilder<Airport>()
                .dataSource(inputDataSource)
                .name("airportReader")
                .sql("select id, identifier, name, province, city, country, latitude, longitude, upper_winds_station_id, created, modified, version from airport")
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
    public Job copyAirportTable(Step airportTableStep1, Step airportTableStep2, Step airportTableStep3) {
        return new JobBuilder("copyAirportTable", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(airportTableStep1)
            .next(airportTableStep2)
            .next(airportTableStep3)
            .build();
    }
    
    @Bean
    public Step airportTableStep1() {
        return new StepBuilder("airportTableStep1", jobRepository)
        	.tasklet(new BeforeCopyTableTasklet(outputDataSource, "airport"))
            .build();
    }

    @Bean
    public Step airportTableStep2(JdbcCursorItemReader<Airport> airportReader, JdbcBatchItemWriter<Airport> airportWriter) {
        return new StepBuilder("airportTableStep2", jobRepository)
            .<Airport, Airport> chunk(1000)
            .reader(airportReader)
            .processor(new AirportItemProcessor())
            .writer(airportWriter)
            .build();
    }

    @Bean
    public Step airportTableStep3() {
        return new StepBuilder("airportTableStep3", jobRepository)
        	.tasklet(new AfterCopyTableTasklet(outputDataSource, "airport"))
            .build();
    }
// end::jobstep[]
}
