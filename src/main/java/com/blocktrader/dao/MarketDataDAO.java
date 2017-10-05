package com.blocktrader.dao;

import eu.verdelhan.ta4j.TimeSeries;

public interface MarketDataDAO {
	
	void addTimeSeries(TimeSeries series);

	TimeSeries getTimeSeries(String name);
}
