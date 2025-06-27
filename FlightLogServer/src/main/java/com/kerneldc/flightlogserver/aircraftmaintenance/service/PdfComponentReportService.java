package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
//import javax.xml.transform.Result;
//import javax.xml.transform.Source;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.sax.SAXResult;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.transform.Result;
//import javax.xml.transform.Source;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.sax.SAXResult;
//import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentAndHistoryVS;
import com.kerneldc.flightlogserver.exception.ApplicationException;


@Service
public class PdfComponentReportService {

	private static final String COMPONENT_HISTORY_XSLT = "componentToFo.xsl";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

	public byte[] generateReport (ComponentAndHistoryVS componentAndHistoryVS, String reportTitle, String sortedColumn) throws ApplicationException {
		
		byte[] xmlBytes;
		try {
			xmlBytes = beanToXml(componentAndHistoryVS, reportTitle);
		} catch (JAXBException e) {
			throw new ApplicationException(e);
		} 
		return xmlToPdf(xmlBytes, COMPONENT_HISTORY_XSLT, reportTitle, sortedColumn);
	}

	protected byte[] beanToXml(ComponentAndHistoryVS componentAndHistoryVS, String reportTitle) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(componentAndHistoryVS.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();

		// output pretty printed
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		ByteArrayOutputStream xmlByteArrayOutputStream = new ByteArrayOutputStream();

		marshaller.marshal(componentAndHistoryVS, xmlByteArrayOutputStream);

		return xmlByteArrayOutputStream.toByteArray();
	}

	protected byte[] xmlToPdf(byte[] xmlByteArray, String xsltFile, String reportTitle, String sortedColumn) throws ApplicationException {

		// Configure fopFactory as desired
		final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

		// Configure foUserAgent as desired
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		try {
			// Setup output
			ByteArrayOutputStream pdfByteArrayOutputStream = new ByteArrayOutputStream();

			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfByteArrayOutputStream);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			//factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			
			Transformer transformer = factory.newTransformer(new StreamSource(Thread.currentThread()
					.getContextClassLoader().getResourceAsStream(xsltFile)));

			// Set the value of a <param> in the stylesheet
			transformer.setParameter("reportTitle", reportTitle);
			transformer.setParameter("timeGenerated", DATE_FORMAT.format(new Date()));
			transformer.setParameter("sortedColumn", sortedColumn);

			// Setup input for XSLT transformation
			Source src = new StreamSource(new ByteArrayInputStream(xmlByteArray));

			// Resulting SAX events (the generated FO) must be piped through
			// to FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
			return pdfByteArrayOutputStream.toByteArray();

		} catch (Exception e) {
			throw new ApplicationException(ExceptionUtils.getRootCauseMessage(e));
		}
	}
}
