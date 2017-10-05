package com.blocktrader.adapter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.blocktrader.adapter.MarketDataAdapter;
import com.blocktrader.api.kraken.KrakenResponse;
import com.blocktrader.domain.Market;

import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;

public class KrakenAdapter implements MarketDataAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(KrakenAdapter.class);
	
	private static final String KRAKEN_OLHC_URL = "https://api.kraken.com/0/public/OHLC";

	private static final String KRAKEN_ASSET_PAIRS_URL ="https://api.kraken.com/0/public/AssetPairs";
	
	
	public static final int PERIOD_1_MINS = 1;
	public static final int PERIOD_5_MINS = PERIOD_1_MINS * 5;
	public static final int PERIOD_15_MINS = PERIOD_1_MINS * 15;
	public static final int PERIOD_30_MINS = PERIOD_1_MINS * 30;
	public static final int PERIOD_1_HOUR = PERIOD_1_MINS * 60;
	public static final int PERIOD_4_HOURS = PERIOD_1_HOUR * 4;
	public static final int PERIOD_1_DAY = PERIOD_1_HOUR * 24;

	private TimeSeries timeSeries;

	private long lastPoll;

	private Market market;

	private int period;

	private RestTemplate krakenClient;

	public KrakenAdapter(RestTemplate krakenClient, Market market, int period) {
		this.lastPoll = 0;
		this.market = market;
		this.period = period;
		//TODO: Need to add in http configuration in here. Kraken api frequently times out. 
		this.krakenClient = krakenClient;
	}

	@Override
	public Market fetchLatestMarketData() {

		List<Tick> series = new ArrayList<Tick>();
		
		//Construct our form post
		LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add("pair", market.getMarketKey());
		formData.add("interval", String.valueOf(period));
		
		//If its the first request, get default history. Otherwise we just poll for data since the last request.
		if (lastPoll > 0) {
			LOGGER.debug("Getting data since {}",  new Date((long)lastPoll * 1000));
			formData.add("since", String.valueOf(lastPoll));
		}
		ResponseEntity<KrakenResponse> marketResponse = krakenClient.postForEntity(KRAKEN_OLHC_URL, formData,
				KrakenResponse.class);
		KrakenResponse response = marketResponse.getBody();
		Map<String, Object> result = response.getResult();
		
		//Process our response.
		int last = (int) result.get("last");

		//Unfortunately because Kraken use an array of mixed types there's no better way  of unmarshalling the response in to a POJO
		List<List<Object>> list = (List<List<Object>>) result.get(market.getMarketKey().toUpperCase());

		LOGGER.debug("Number of Results: {} - Last {}", list.size(), new Date((long)last * 1000));
		if (last > lastPoll) {
			lastPoll = last;
		} else if (last == lastPoll) {
			LOGGER.info("No new data for {}", market.getMarketKey());
			return null;
		}

		for (List<Object> arrayList : list) {
			Integer timestamp = (Integer) arrayList.get(0);
			Object open = arrayList.get(1);
			Object high = arrayList.get(2);
			Object low = arrayList.get(3);
			Object close = arrayList.get(4);
			// Volume Weighted Average Market Price
			Object vwamp = arrayList.get(5);
			Object volume = arrayList.get(6);
			Object count = arrayList.get(7);
			if (timestamp != null)
				series.add(new Tick(new DateTime((long) timestamp.intValue()), String.valueOf(open), String.valueOf(high), String.valueOf(low),
						String.valueOf(close), String.valueOf(volume)));
		}
		
		//Add the latest data to our time series
		if (timeSeries == null) {
			timeSeries = new TimeSeries("Kraken-" + market.getMarketKey()+ "-"+ period, series);
		} else if (timeSeries != null && series.size() > 0) {
			for (Tick tick : series) {
				if (!timeSeries.getLastTick().getEndTime().equals(tick.getEndTime())) {
					timeSeries.addTick(tick);
				}
			}
		}
		
		LOGGER.info("Market: {}  - Price: {}", market.getMarketDisplayName(), timeSeries.getLastTick().getClosePrice());
		market.setTimeSeries(timeSeries);
		return market;
	}

	@Override
	public Market getMarket() {
		return market;
	}

	
	public List<String> getMarkets(){
		List<String> marketList;
		Map<String, Object> result = krakenClient.getForEntity(KRAKEN_ASSET_PAIRS_URL, KrakenResponse.class).getBody().getResult();
		marketList = new ArrayList<String>(result.keySet());
		for(String m: marketList){
			Map<String,Object> map = (Map<String,Object>) result.get(m);
			LOGGER.debug("Market: {} - Key: {}", map.get("altname"), m);
			
		}
		return marketList;
	}
}
