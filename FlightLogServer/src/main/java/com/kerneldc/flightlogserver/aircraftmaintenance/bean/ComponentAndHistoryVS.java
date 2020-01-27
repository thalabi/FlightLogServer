package com.kerneldc.flightlogserver.aircraftmaintenance.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componentandhistoryview.ComponentAndHistoryV;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class ComponentAndHistoryVS {

	@XmlElement(name="componentAndHistoryV")
	private List<ComponentAndHistoryV> componentAndHistoryVList = new ArrayList<>();

	// utility
	public void setComponentAndHistoryVList(List<ComponentAndHistoryV> componentAndHistoryVList) {
		this.componentAndHistoryVList = new ArrayList<>(componentAndHistoryVList);
	}

}
