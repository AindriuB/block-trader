package com.blocktrader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blocktrader.adapter.MarketDataAdapter;
import com.blocktrader.domain.Market;
import com.blocktrader.service.MarketPollingService;
import com.blocktrader.service.MarketWatchingService;

@Service
public class MarketPollingServiceImpl implements MarketPollingService {

	private static Logger LOGGER = LoggerFactory.getLogger(MarketPollingServiceImpl.class);

	@Autowired
	private MarketWatchingService strategyService;

	@Autowired
	private MarketAdapterServiceLocatorImpl platformDataServiceLocator;

	@Scheduled(fixedDelay = 30000)
	@Override
	public void pollMarkets() {
		for (MarketDataAdapter service : platformDataServiceLocator.getMarketDataAdapters()) {
			LOGGER.debug("Polling {} - {}", service.getClass().getSimpleName(), service.getMarket().getMarketDisplayName());
			Market marketData = service.fetchLatestMarketData();
			if (marketData != null) {
				strategyService.process(marketData);
			}
		}
	}

}
