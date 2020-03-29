package com.kerneldc.flightlogserver.aircraftmaintenance.domain.componentandhistoryview;

import java.util.Date;
import java.util.function.Function;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="component_and_history_v")
@Immutable
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
@ToString
public class ComponentAndHistoryV {

	public static final Function<ComponentAndHistoryV, Object> idExtractor = ComponentAndHistoryV::getId;

	@Id
	private Long id;

    private String componentName;
    private String componentDescription;
    private String partName;
    private String partDescription;
    private String workPerformed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePerformed;
    private Float hoursPerformed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDue;
    private Float hoursDue;
}
