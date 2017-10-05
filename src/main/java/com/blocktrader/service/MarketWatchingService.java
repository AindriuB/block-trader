package com.blocktrader.service;

import com.blocktrader.domain.Market;

import eu.verdelhan.ta4j.TimeSeries;

public interface MarketWatchingService {

	void process(Market market);

}
