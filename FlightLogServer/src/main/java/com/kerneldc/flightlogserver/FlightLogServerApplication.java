package com.kerneldc.flightlogserver;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableCaching
public class FlightLogServerApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(FlightLogServerApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter()); // creates a pid file
		ApplicationContext applicationContext = springApplication.run(args);
		displayAllBeans(applicationContext);
	}

    public static void displayAllBeans(ApplicationContext applicationContext) {
        String[] beanNameList = applicationContext.getBeanDefinitionNames();
        List<String> classNameAndBeanNameList = new ArrayList<>(beanNameList.length);
        for (String beanName : beanNameList) {
        	classNameAndBeanNameList.add(applicationContext.getBean(beanName).getClass().toString()+", "+beanName);
        }
        try {
        	Path path = Paths.get(System.getProperty("java.io.tmpdir")+"/FlightLogServerApplication-beans-list.txt");
        	LOGGER.debug("Writing list of beans to: {}", path);
			Files.write(path, String.join("\n", classNameAndBeanNameList).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
