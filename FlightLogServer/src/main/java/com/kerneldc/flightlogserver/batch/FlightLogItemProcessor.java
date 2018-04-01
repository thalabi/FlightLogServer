package com.kerneldc.flightlogserver.batch;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.FlightLog;

public class FlightLogItemProcessor implements ItemProcessor<FlightLog, FlightLog> {

	
	private static final List<String> EXCLUDED_PROPERTY_NAMES = Arrays.asList("id", "version");
	
	private PropertyDescriptor[] pds;
	
	public FlightLogItemProcessor() {
		pds =  PropertyUtils.getPropertyDescriptors(FlightLog.class);
	}

	@Override
	public FlightLog process(FlightLog item) throws Exception {
		FlightLog result = new FlightLog();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		setZeroPropertiesToNull(result);
		return result;
	}

	private void setZeroPropertiesToNull (FlightLog flightLog) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (PropertyDescriptor pd: pds) {
			if (! /* not */ EXCLUDED_PROPERTY_NAMES.contains(pd.getName())) {
				String propertyName = pd.getName();
				Object propertyValue = PropertyUtils.getSimpleProperty(flightLog, propertyName);
				if (pd.getPropertyType().equals(Integer.class) && propertyValue.equals(Integer.valueOf(0)) || 
					pd.getPropertyType().equals(Float.class) && propertyValue.equals(Float.valueOf(0))) {
					PropertyUtils.setSimpleProperty(flightLog, propertyName, null);
				}
			}
		}
	}
}

