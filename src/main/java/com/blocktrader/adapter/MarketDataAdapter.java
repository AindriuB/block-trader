package com.blocktrader.adapter;

import com.blocktrader.domain.Market;

import eu.verdelhan.ta4j.TimeSeries;

public interface MarketDataAdapter {

	Market getMarket();

	Market fetchLatestMarketData();

}
