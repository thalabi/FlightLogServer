package com.kerneldc.flightlogserver;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FlightLogServerApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public static void main(String[] args) {
		SpringApplication.run(FlightLogServerApplication.class, args);
	}

    @Bean
    ApplicationRunner initCars() {
/*    	if (colorRepository.findAll().isEmpty()) {
	    	Stream.of("Red", "Green", "Blue").forEach(name -> {
	    		Color color = new Color();
	                color.setName(name);
	                colorRepository.save(color);
	    	});
    	}
        List<Color> colorList = colorRepository.findAll();
        colorList.forEach(color -> LOGGER.debug("{}",color));
*/        
    	// If repository is empty, create cars
//    	if (carSepository.findAll().isEmpty()) {
//    		AtomicInteger i = new AtomicInteger();
//	    	Stream.of("Ferrari", "Jaguar", "Porsche", "Lamborghini", "Bugatti",
//	                "AMC Gremlin", "Triumph Stag", "Ford Pinto", "Yugo GV").forEach(name -> {
//	                Car car = new Car();
//	                car.setName(name);
//	                car.setDateCreated(new Date());
//	                car.setColor(colorList.get(i.getAndIncrement()%3));
//	                carSepository.save(car);
//	    	});
//    	}
//        carSepository.findAll().forEach(car -> {
//        	LOGGER.debug("{}",car);
//        	});
        return null;
    }

////    @Bean
////    ApplicationRunner initColors(ColorRepository colorRepository) {
////    	// If repository is empty, create colors
////    	if (colorRepository.findAll().isEmpty()) {
////	    	Stream.of("Red", "Green", "Blue").forEach(name -> {
////	    		Color color = new Color();
////	                color.setName(name);
////	                colorRepository.save(color);
////	    	});
////    	}
////        colorRepository.findAll().forEach(color -> LOGGER.debug("{}",color));
////        return null;
////    }

}
