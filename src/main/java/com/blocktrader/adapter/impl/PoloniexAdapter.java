package com.blocktrader.adapter.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.blocktrader.adapter.MarketDataAdapter;
import com.blocktrader.api.poloniex.MarketData;
import com.blocktrader.domain.Market;

import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;

public class PoloniexAdapter implements MarketDataAdapter {

	private static Logger LOGGER = LoggerFactory.getLogger(PoloniexAdapter.class);

	private Market market;

	private int period;

	public RestTemplate poloniexClient;

	public static final int PERIOD_5_MINS = 300;
	public static final int PERIOD_15_MINS = 900;
	public static final int PERIOD_30_MINS = 1800;

	public PoloniexAdapter(Market market, int period) {
		this.market = market;
		this.period = period;
		poloniexClient = new RestTemplate();
	}

	/***
	 * Gets the latest market data from the poloniex api
	 */
	@Override
	public Market fetchLatestMarketData() {

		List<Tick> ticks = new ArrayList<Tick>();
		// Calculate time offset;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -(period / 60) * 10000);
		Date fetchFrom = cal.getTime();
		long unixTime = fetchFrom.getTime() / 1000L;

		// GET last 100 pips
		LOGGER.info("Getting data since {}", unixTime);
		ResponseEntity<List<MarketData>> marketResponse = poloniexClient.exchange(
				"https://poloniex.com/public?command=returnChartData&currencyPair={market}&start={unixTime}&end=9999999999&period={period}",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<MarketData>>() {
				}, market, String.valueOf(unixTime), period);

		// Convert to internal format
		List<MarketData> marketDataList = marketResponse.getBody();
		long highestDate = 0;
		for (MarketData md : marketDataList) {
			if (md.getDate() > highestDate) {
				highestDate = md.getDate();
			}
			ticks.add(marketSnapshotFromMarketData(md));
		}
		Date latest = new Date(highestDate * 1000);
		LOGGER.info("Most recent tick {}", latest);
		TimeSeries marketPrices = new TimeSeries(market.getMarketKey(), ticks);
		market.setTimeSeries(marketPrices);
		return market;
	}

	/***
	 * Transform from api unit to internal unit.
	 * 
	 * @param marketData
	 * @return
	 */
	private Tick marketSnapshotFromMarketData(MarketData marketData) {
		return new Tick(new DateTime(marketData.getDate()), marketData.getOpen(), marketData.getHigh(), marketData.getLow(), marketData.getClose(),
				marketData.getVolume());
	}

	@Override
	public Market getMarket() {
		return market;
	}

}
