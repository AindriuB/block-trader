package com.blocktrader.adapter.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Matches;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.blocktrader.api.kraken.KrakenResponse;
import com.blocktrader.domain.Market;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;


@RunWith(MockitoJUnitRunner.class)
public class KrakenAdapterTest {

	@Mock
	private RestTemplate restTemplate;


	@Test
	public void testKrakenAdapter() {
		DateTime time = new DateTime();
		List<List<Object>> currencyList = new ArrayList<List<Object>>();
		List<Object> candleTick = new ArrayList<Object>();
		candleTick.add(time.getMillisOfSecond());
		candleTick.add(3);
		candleTick.add(4);
		candleTick.add(1);
		candleTick.add(2);
		candleTick.add(1);
		candleTick.add(100);
		candleTick.add(1);
		currencyList.add(candleTick);
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("last", time.getMillisOfSecond());
		objectMap.put("EOSEUR", currencyList);
		KrakenResponse krakenResponse = new KrakenResponse();
		krakenResponse.setResult(objectMap);
		
		Mockito.when(restTemplate.postForEntity(Matchers.matches("https://api.kraken.com/0/public/OHLC"), Matchers.<LinkedMultiValueMap<?,?>> any(), Matchers.<Class<KrakenResponse>> any())).thenReturn(ResponseEntity.ok(krakenResponse));
		Market market = new Market();
		market.setMarketKey("EOSEUR");
		market.setMarketDisplayName("EOS-EUR");
		KrakenAdapter adapter = new KrakenAdapter(restTemplate, market, KrakenAdapter.PERIOD_1_MINS);
		TimeSeries series = adapter.fetchLatestMarketData().getTimeSeries();
		
		assertNotNull(series);
		
		assertEquals(1, series.getTickCount());
		assertEquals("Kraken-EOSEUR-1", series.getName());
		Tick lastTick = series.getLastTick();

		assertEquals(Decimal.valueOf(3),lastTick.getOpenPrice());
		assertEquals(Decimal.valueOf(4), lastTick.getMaxPrice());
		assertEquals(Decimal.valueOf(2),lastTick.getClosePrice());
		assertEquals(Decimal.valueOf(1),lastTick.getMinPrice());
		
	}
	
	@Test
	public void testKrakenAdapterDouble() {
		int time = (int) (System.currentTimeMillis() /1000);

		//Request 1 and 2
		KrakenResponse krakenResponse = new KrakenResponse();
		List<List<Object>> currencyList = new ArrayList<List<Object>>();
		List<Object> candleTick = new ArrayList<Object>();
		candleTick.add(time);
		candleTick.add(1);
		candleTick.add(1);
		candleTick.add(1);
		candleTick.add(1);
		candleTick.add(1);
		candleTick.add(100);
		candleTick.add(1);
		currencyList.add(candleTick);
		
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("last", time);
		objectMap.put("EOSEUR", currencyList);
		krakenResponse.setResult(objectMap);
		
		
		//Request 3

		KrakenResponse krakenResponse2 = new KrakenResponse();
		List<List<Object>> currencyList2 = new ArrayList<List<Object>>();
		List<Object> candleTick2 = new ArrayList<Object>();
		candleTick2.add(time + 1000);
		candleTick2.add(2);
		candleTick2.add(2);
		candleTick2.add(2);
		candleTick2.add(2);
		candleTick2.add(2);
		candleTick2.add(200);
		candleTick2.add(2);
		currencyList2.add(candleTick2);
		
		
		Map<String, Object> objectMap2 = new HashMap<String, Object>();
		objectMap2.put("last", (time  + 1000));
		objectMap2.put("EOSEUR", currencyList2);
		krakenResponse2.setResult(objectMap2);
		
		// Set up mock of rest template to return first response twice and the second response last
		Mockito.when(restTemplate.postForEntity(Matchers.matches("https://api.kraken.com/0/public/OHLC"), Matchers.<LinkedMultiValueMap<?,?>> any(), Matchers.<Class<KrakenResponse>> any())).thenReturn(ResponseEntity.ok(krakenResponse), ResponseEntity.ok(krakenResponse),ResponseEntity.ok(krakenResponse2));
		
		//Set up the market adapter
		Market market = new Market();
		market.setMarketKey("EOSEUR");
		market.setMarketDisplayName("EOS-EUR");
		KrakenAdapter adapter = new KrakenAdapter(restTemplate, market, KrakenAdapter.PERIOD_1_MINS);
		//First result should return the time series 
		TimeSeries series = adapter.fetchLatestMarketData().getTimeSeries();
		assertNotNull(series);
		assertEquals(1, series.getTickCount());
		market = adapter.fetchLatestMarketData();
		//The last will remain the same so this should be null i.e. no new data
		assertNull(market);
		
		// Then get updated list
		market = adapter.fetchLatestMarketData();
		assertNotNull(market);
		series = market.getTimeSeries();
		assertNotNull(series);
		assertEquals(2, series.getTickCount());
		
		Tick lastTick = series.getLastTick();

		assertEquals(Decimal.valueOf(2),lastTick.getOpenPrice());
		assertEquals(Decimal.valueOf(2), lastTick.getMaxPrice());
		assertEquals(Decimal.valueOf(2),lastTick.getClosePrice());
		assertEquals(Decimal.valueOf(2),lastTick.getMinPrice());
	}

	@Test
	public void testKrakenAdapterMarket() {
		Market market = new Market();
		market.setMarketKey("EOSEUR");
		market.setMarketDisplayName("EOS-EUR");
		KrakenAdapter adapter = new KrakenAdapter(restTemplate, market, KrakenAdapter.PERIOD_1_MINS);
		assertEquals("EOSEUR", adapter.getMarket().getMarketKey());
		assertEquals("EOS-EUR", adapter.getMarket().getMarketDisplayName());
	}

	@Test
	public void testKrakenAdapterMarkets() {
		Market market = new Market();
		market.setMarketKey("EOSEUR");
		market.setMarketDisplayName("EOS-EUR");
		
		KrakenResponse krakenResponse = new KrakenResponse();
		Map<String, Object> mapResults = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test","test");
		mapResults.put("test",map);
		krakenResponse.setResult(mapResults);
		Mockito.when(restTemplate.getForEntity(Matchers.matches("https://api.kraken.com/0/public/AssetPairs"),Matchers.<Class<KrakenResponse>> any())).thenReturn(ResponseEntity.ok(krakenResponse));
		KrakenAdapter adapter = new KrakenAdapter(restTemplate, market, KrakenAdapter.PERIOD_1_MINS);
		assertNotNull(adapter.getMarkets());

	}

}
