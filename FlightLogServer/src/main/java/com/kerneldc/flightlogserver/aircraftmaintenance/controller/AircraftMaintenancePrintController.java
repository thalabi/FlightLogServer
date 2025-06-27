package com.kerneldc.flightlogserver.aircraftmaintenance.controller;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//import jakarta.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentAndHistoryVS;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentNameVo;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componentandhistoryview.ComponentAndHistoryV;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentAndHistoryVRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.PdfComponentReportService;
import com.kerneldc.flightlogserver.exception.ApplicationException;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/protected/aircraftMaintenancePrintController")
//@ExposesResourceFor(Component.class) // needed for unit test to create entity links
public class AircraftMaintenancePrintController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String LOG_BEGIN = "Begin ...";
	private static final String LOG_END = "End ...";
	private static final String SORTED_BY_DATE_PERFORMED = "datePerformed";
	private static final String SORTED_BY_HOURS_PERFORMED = "hoursPerformed";
	private static final String SORTED_BY_DATE_DUE = "dateDue";
	private static final String SORTED_BY_HOURS_DUE = "hoursDue";
	private static final String SORTED_BY_COMPONENT_NAME = "componentName";
	private ComponentAndHistoryVRepository componentAndHistoryVRepository;
	private PdfComponentReportService pdfComponentReportService;

	public AircraftMaintenancePrintController(ComponentAndHistoryVRepository componentAndHistoryVRepository, PdfComponentReportService pdfComponentReportService) {
		this.componentAndHistoryVRepository = componentAndHistoryVRepository;
		this.pdfComponentReportService = pdfComponentReportService;
	}
	
    @GetMapping("/verify")
	public ResponseEntity<String> verify() {

		LOGGER.debug(LOG_BEGIN);
		LOGGER.debug(LOG_END);
		return ResponseEntity.ok("AircraftMaintenancePrintController is up and running.");
	}
    
	@GetMapping(value = "/printComponentHistoryByDatePerformedDesc", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryByDatePerformedDesc() throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findAllByOrderByDatePerformedDescComponentName();
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS,
				"C-GQGD, PA-28-181, Maintenance History by Date Performed", SORTED_BY_DATE_PERFORMED);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/printComponentHistoryBetweenDatesPerformedByDatePerformedDesc", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryBetweenDatesPerformedByDatePerformedDesc(
			@RequestParam(value = "fromDatePerformed") @DateTimeFormat(iso = ISO.DATE_TIME) @Valid LocalDate fromDatePerformed,
			@RequestParam(value = "toDatePerformed") @DateTimeFormat(iso = ISO.DATE_TIME) @Valid LocalDate toDatePerformed) throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		LOGGER.debug("fromDatePerformed: {}, toDatePerformed: {}", fromDatePerformed, toDatePerformed);
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findByDatePerformedBetweenOrderByDatePerformedDescComponentName(toDate(fromDatePerformed),
						toDate(toDatePerformed));
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		if (componentAndHistoryVList.isEmpty()) {
			return ResponseEntity.ok(null);
		}
		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History between "+fromDatePerformed+" & "+toDatePerformed, SORTED_BY_DATE_PERFORMED);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/printComponentHistoryByComponentName", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryByComponentName() throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findAllByOrderByComponentNameAscDatePerformedDesc();
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		clearComponentAndPartIfDuplicate(componentAndHistoryVList);

		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History by Component", SORTED_BY_COMPONENT_NAME);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/printComponentHistoryByDateDueDesc", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryByDateDueDesc() throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findAllByDateDueNotNullOrderByDateDueDescComponentName();
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History by Date Due", SORTED_BY_DATE_DUE);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/printComponentHistoryByUpcomingDateDue", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryByUpcomingDateDue() throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		
		Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		LOGGER.info("today: {}", today);
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findByDateDueGreaterThanEqualOrderByDateDueAscComponentName(today);
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History by Upcoming Date Due", SORTED_BY_DATE_DUE);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/printComponentHistoryByHoursDueDesc", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryByHoursDueDesc() throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findAllByHoursDueNotNullOrderByHoursDueDescComponentName();
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History by Hours Due", SORTED_BY_HOURS_DUE);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/printComponentHistoryAfterLatestHoursPerformedByHoursDueDesc", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryAfterLatestHoursPerformedByHoursDueDesc() throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findByHoursDueGreaterThanLastHoursPerformedOrderByHoursDueAsc();
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History by After Latest Hrs Performed", SORTED_BY_HOURS_DUE);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/printComponentHistoryAfterHoursByHoursDueDesc", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryAfterHoursByHoursDueDesc(
			@RequestParam(value = "hoursDue") @Valid Float hoursDue) throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findByHoursDueGreaterThanEqualOrderByHoursDueAscComponentName(hoursDue);
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History by Hours Due", SORTED_BY_HOURS_DUE);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	@GetMapping(value = "/getComponentNames")
	public ResponseEntity<List<ComponentNameVo>> getComponentNames() {
		LOGGER.debug(LOG_BEGIN);
		List<String> componentNameList = componentAndHistoryVRepository.findAllDistinctComponentName();
		List<ComponentNameVo> componentNameVoList = componentNameList.stream()
				.map(name -> ComponentNameVo.builder().name(name).build()).collect(Collectors.toList());
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(componentNameVoList);
	}

	@GetMapping(value = "/printComponentHistoryByComponentNameInList", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> printComponentHistoryByComponentNameInList(
			@RequestParam(value = "componentNameList") List<String> componentNameList) throws ApplicationException {

		LOGGER.debug(LOG_BEGIN);
		List<ComponentAndHistoryV> componentAndHistoryVList = componentAndHistoryVRepository
				.findByComponentNameInOrderByComponentNameAscDatePerformedDesc(componentNameList);
		LOGGER.debug("componentAndHistoryVList: {}", componentAndHistoryVList);

		clearPartNameAndPartDescription(componentAndHistoryVList);
		
		ComponentAndHistoryVS componentAndHistoryVS = new ComponentAndHistoryVS();
		componentAndHistoryVS.setComponentAndHistoryVList(componentAndHistoryVList);
		byte[] pdfByteArray = pdfComponentReportService.generateReport(componentAndHistoryVS, "C-GQGD, PA-28-181, Maintenance History by Component", SORTED_BY_COMPONENT_NAME);
		LOGGER.debug(LOG_END);
		
		return ResponseEntity.ok(pdfByteArray);
	}

	/**
	 * clear partName & partDescription for partName = "No Parts Used"
	 */
	private void clearPartNameAndPartDescription(List<ComponentAndHistoryV> componentAndHistoryVList) {
		componentAndHistoryVList.stream()
		.filter(componentAndHistoryV -> StringUtils.equals(componentAndHistoryV.getPartName(), "No Parts Used"))
		.forEach(componentAndHistoryV -> {
			componentAndHistoryV.setPartName(null);
			componentAndHistoryV.setPartDescription(null);
		});
	}
	private void clearComponentAndPartIfDuplicate(List<ComponentAndHistoryV> componentAndHistoryVList) {
		int componentAndHistoryVListSize = CollectionUtils.size(componentAndHistoryVList); 
		if (componentAndHistoryVListSize > 1) {
			for (int i=CollectionUtils.size(componentAndHistoryVList)-1; i>0; i--) {
				ComponentAndHistoryV previousComponentAndHistoryV = componentAndHistoryVList.get(i-1);
				ComponentAndHistoryV componentAndHistoryV = componentAndHistoryVList.get(i);
				if (StringUtils.equals(componentAndHistoryV.getComponentName(), previousComponentAndHistoryV.getComponentName())) {
					componentAndHistoryV.setComponentName(null);
				}
				if (StringUtils.equals(componentAndHistoryV.getComponentDescription(), previousComponentAndHistoryV.getComponentDescription())) {
					componentAndHistoryV.setComponentDescription(null);
				}
				if (StringUtils.equals(componentAndHistoryV.getPartName(), previousComponentAndHistoryV.getPartName())) {
					componentAndHistoryV.setPartName(null);
				}
				if (StringUtils.equals(componentAndHistoryV.getPartDescription(), previousComponentAndHistoryV.getPartDescription())) {
					componentAndHistoryV.setPartDescription(null);
				}
			}
		}
	}
	private Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
