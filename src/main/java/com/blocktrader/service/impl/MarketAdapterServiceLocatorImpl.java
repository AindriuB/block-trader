package com.blocktrader.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.blocktrader.adapter.MarketDataAdapter;
import com.blocktrader.service.MarketAdapterServiceLocator;

@Service
public class MarketAdapterServiceLocatorImpl implements MarketAdapterServiceLocator, InitializingBean{
	
	private static Logger LOGGER = LoggerFactory.getLogger(MarketAdapterServiceLocatorImpl.class);
	
	private Map<String, MarketDataAdapter> platformDataServices;	
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	public List<MarketDataAdapter> getMarketDataAdapters() {
		List<MarketDataAdapter> dataServiceList = new ArrayList<MarketDataAdapter>();
		for(String serviceKey: platformDataServices.keySet()){
			dataServiceList.add(platformDataServices.get(serviceKey));
		}
		return dataServiceList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		platformDataServices = context.getBeansOfType(MarketDataAdapter.class);
		for(String key: platformDataServices.keySet()){
			LOGGER.info("Found data service: {}", key);
		}
		
	}

}
