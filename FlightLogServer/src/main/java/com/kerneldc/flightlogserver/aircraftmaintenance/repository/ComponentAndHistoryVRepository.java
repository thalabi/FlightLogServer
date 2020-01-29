package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componentandhistoryview.ComponentAndHistoryV;

public interface ComponentAndHistoryVRepository extends JpaRepository<ComponentAndHistoryV, Long>, JpaSpecificationExecutor<ComponentAndHistoryV> {

	List<ComponentAndHistoryV> findAllByOrderByDatePerformedDescComponentName();
	List<ComponentAndHistoryV> findByDatePerformedBetweenOrderByDatePerformedDescComponentName(Date fromDatePerformed, Date toDatePerformed);
	
	List<ComponentAndHistoryV> findAllByOrderByComponentNameAscDatePerformedDesc();
	
	List<ComponentAndHistoryV> findAllByDateDueNotNullOrderByDateDueDescComponentName();
	List<ComponentAndHistoryV> findByDateDueGreaterThanEqualOrderByDateDueAscComponentName(Date dateDue);
	
	List<ComponentAndHistoryV> findAllByHoursDueNotNullOrderByHoursDueDescComponentName();
	@Query(value = "select * from component_and_history_v where hours_due > (select max(hours_performed) from component_and_history_v) order by component_name", nativeQuery = true)
	List<ComponentAndHistoryV> findByHoursDueGreaterThanLastHoursPerformedOrderByHoursDueAsc();
	List<ComponentAndHistoryV> findByHoursDueGreaterThanEqualOrderByHoursDueAscComponentName(Float hoursDue);
	
	@Query(value = "select distinct component_name from component_and_history_v order by component_name", nativeQuery = true)
	List<String> findAllDistinctComponentName();
	
	List<ComponentAndHistoryV> findByComponentNameInOrderByComponentNameAscDatePerformedDesc(List<String> componentName);
}
