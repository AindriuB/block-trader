package com.blocktrader.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import com.blocktrader.dao.MarketDataDAO;

import eu.verdelhan.ta4j.TimeSeries;

@Repository
public class InMemoryMarketDataDAO implements MarketDataDAO, InitializingBean{

	Map<String, TimeSeries> inMememoryRepo;
	
	
	@Override
	public void addTimeSeries(TimeSeries series) {
		// TODO Auto-generated method stub

	}


	@Override
	public void afterPropertiesSet() throws Exception {
		inMememoryRepo = new HashMap<String, TimeSeries>();
	}


	@Override
	public TimeSeries getTimeSeries(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
