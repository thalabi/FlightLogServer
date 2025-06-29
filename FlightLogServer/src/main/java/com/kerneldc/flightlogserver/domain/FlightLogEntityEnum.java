package com.kerneldc.flightlogserver.domain;

import java.util.Arrays;

import com.google.common.base.Enums;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.domain.airport.Airport;
import com.kerneldc.flightlogserver.domain.flightlogpending.FlightLogPending;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;
import com.kerneldc.flightlogserver.domain.pilot.Pilot;
import com.kerneldc.flightlogserver.domain.registration.Registration;
import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEvent;

public enum FlightLogEntityEnum implements IEntityEnum {
//	INSTRUMENT(Instrument.class, false, new String[] {"NAME","TICKER","EXCHANGE","CURRENCY"}),
//	INSTRUMENT_STOCK(InstrumentStock.class, false),
//	INSTRUMENT_ETF(InstrumentEtf.class, false),
//	INSTRUMENT_MUTUAL_FUND(InstrumentMutualFund.class, false),
//	INSTRUMENT_INTEREST_BEARING(InstrumentInterestBearing.class, false),
//	INSTRUMENT_BOND(InstrumentBond.class, false),
//	PORTFOLIO(Portfolio.class, false, new String[] {"INSTITUTION","ACCOUNT_NUMBER","CURRENCY","NAME"}),
//	HOLDING(Holding.class, false, new String[] {"AS_OF_DATE","TICKER","EXCHANGE","QUANTITY","INSTITUTION","ACCOUNT_NUMBER"}),
//	POSITION(Position.class, false, new String[] {"TICKER", "EXCHANGE", "ACCOUNT_NUMBER", "INSTITUTION", "PRICE_TIMESTAMP", "POSITION_TIMESTAMP", "QUANTITY"}),
//	EXCHANGE_RATE(ExchangeRate.class, false, new String[] {"AS_OF_DATE","FROM_CURRENCY","TO_CURRENCY", "RATE"}),
	FLIGHT_LOG_TOTALS_V(FlightLogTotalsV.class, true),
	FLIGHT_LOG_MONTHLY_TOTAL_V(FlightLogMonthlyTotalV.class, true),
	FLIGHT_LOG_YEARLY_TOTAL_V(FlightLogYearlyTotalV.class, true),
	FLIGHT_LOG_LAST_X_DAYS_TOTAL_V(FlightLogLastXDaysTotalV.class, true),
	AIRPORT(Airport.class, false),
	MAKE_MODEL(MakeModel.class, false),
	PILOT(Pilot.class, false),
	REGISTRATION(Registration.class, false),
	SIGNIFICANT_EVENT(SignificantEvent.class, false),
	PART(Part.class, false),
	COMPONENT(Component.class, false),
//	PRICE(Price.class, false, new String[] {"TICKER", "EXCHANGE", "PRICE", "PRICE_TIMESTAMP", "PRICE_TIMESTAMP_FROM_SOURCE"/*, "SOURCECSVLINENUMBER"*/}),
//	INSTRUMENT_BY_ACCOUNT_V(InstrumentByAccountV.class, true, new String[] {"TICKER_EXCHANGE", "INSTRUMENT_NAME", "QUANTITY", "ACCOUNT_NUMBER", "ACCOUNT_NAME"}),
//	INSTRUMENT_DUE_V(InstrumentDueV.class, true, new String[] {}),
//	FIXED_INCOME_AUDIT(FixedIncomeAudit.class, false, new String[] {}),
	FLIGHT_LOG_PENDING(FlightLogPending.class, false),
	;

	Class<? extends AbstractEntity> entity;
	boolean immutable;
	String[] writeColumnOrder;
	
	FlightLogEntityEnum(Class<? extends AbstractEntity> entity, boolean immutable) {
		this.entity = entity;
		this.immutable = immutable;
	}
	FlightLogEntityEnum(Class<? extends AbstractEntity> entity, boolean immutable, String[] writeColumnOrder) {
		this.entity = entity;
		this.immutable = immutable;
		// tag SOURCECSVLINENUMBER to the end of the writeColumnOrder
		this.writeColumnOrder = Arrays.copyOf(writeColumnOrder, writeColumnOrder.length+1);
		this.writeColumnOrder[this.writeColumnOrder.length-1] = "SOURCECSVLINENUMBER";  
	}
	
	@Override
	public Class<? extends AbstractEntity> getEntity() {
		return entity;
	}
	
	@Override
	public boolean isImmutable() {
		return immutable;
	}

	@Override
	public String[] getWriteColumnOrder() {
		return writeColumnOrder;
	}
	
	public static IEntityEnum valueIfPresent(String name) {
	    return Enums.getIfPresent(FlightLogEntityEnum.class, name).orNull();
	}

}
