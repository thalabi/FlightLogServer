package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentAndHistoryVS;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componentandhistoryview.ComponentAndHistoryV;
import com.kerneldc.flightlogserver.exception.ApplicationException;

public class PdfComponentReportServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private static PdfComponentReportService fixture = new PdfComponentReportService();
	private static ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
	static {
		ComponentAndHistoryV componentAndHistoryV1 = ComponentAndHistoryV.builder()
				.componentName("component name 1")
				.componentDescription("component desc 1")
				.partName("part name 1")
				//.partDescription("part desc 1")
				.workPerformed("work performed 1")
				.datePerformed(new Date())
				.hoursPerformed(100f)
				.dateDue(new Date())
				.hoursDue(600f)
				.build();
		ComponentAndHistoryV componentAndHistoryV2 = ComponentAndHistoryV.builder()
				.componentName("component name 2")
				//.componentDescription("component desc 2")
				.partName("part name 2")
				.partDescription("part desc 2")
				.workPerformed("work performed 2")
				.datePerformed(new Date())
				.hoursPerformed(200f)
				.dateDue(new Date())
				.hoursDue(1600f)
				.build();
		ComponentAndHistoryV componentAndHistoryV3 = ComponentAndHistoryV.builder()
				.componentName("component 3 3 3 3 3 3 3 3")
				.componentDescription("component desc 3 3 3 3 3 3 3 3")
				.partName("part name 3 3 3 3 3 3 3 3")
				.partDescription("part desc 3 3 3 3 3 3 3 3")
				.workPerformed("work prfrmd 3 3 3 3 3 3 3 3")
				.datePerformed(new Date())
				.hoursPerformed(200f)
				.dateDue(new Date())
				.hoursDue(1600f)
				.build();
		
		componentAndHistoryVS.getComponentAndHistoryVList().addAll(Arrays.asList(componentAndHistoryV1, componentAndHistoryV2, componentAndHistoryV3));
	}
	
	@Test
    public void testBeanToXml() throws JAXBException {
		byte[] xmlBytes = fixture.beanToXml(componentAndHistoryVS, StringUtils.EMPTY);
		LOGGER.info("xmlBytes: {}", new String(xmlBytes));
	}

	@Test
    public void testGenerateReport() throws JAXBException, ApplicationException, IOException {
		byte[] pdfBytes = fixture.generateReport(componentAndHistoryVS, StringUtils.EMPTY, StringUtils.EMPTY);
		LOGGER.info("pdfBytes: {}", new String(pdfBytes));
		File tempFile = File.createTempFile("test", ".pdf");
		FileOutputStream outputStream = new FileOutputStream(tempFile);
		outputStream.write(pdfBytes);
		outputStream.close();
	}
}
