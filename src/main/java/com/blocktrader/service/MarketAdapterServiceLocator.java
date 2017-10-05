package com.blocktrader.service;

import java.util.List;

import com.blocktrader.adapter.MarketDataAdapter;

public interface MarketAdapterServiceLocator {
	
	
	List<MarketDataAdapter> getMarketDataAdapters();

}
