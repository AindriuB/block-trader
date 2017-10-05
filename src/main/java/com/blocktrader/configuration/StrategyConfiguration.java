package com.blocktrader.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.blocktrader.adapter.MarketDataAdapter;
import com.blocktrader.adapter.impl.KrakenAdapter;
import com.blocktrader.domain.Market;
import com.blocktrader.strategy.StrategyBuilder;
import com.blocktrader.strategy.impl.GlobalExtremaStrategyBuilder;
import com.blocktrader.strategy.impl.MACDRSIStrategyBuilder;
import com.blocktrader.strategy.impl.RSI2StrategyBuilder;


@Configuration
public class StrategyConfiguration {

	@Bean
	public MarketDataAdapter krakenDataListener5MinsEUR() {
		Market market = new Market();
		market.setMarketKey("XXBTZEUR");
		market.setMarketDisplayName("XBT-EUR");
		List<StrategyBuilder> strategyList = new ArrayList<StrategyBuilder>();
		strategyList.add(new GlobalExtremaStrategyBuilder());
		strategyList.add(new RSI2StrategyBuilder());
		strategyList.add(new MACDRSIStrategyBuilder());
		strategyList.add(new RSI2StrategyBuilder());
		market.setStrategyBuilderList(strategyList);
		return new KrakenAdapter(new RestTemplate(), market, KrakenAdapter.PERIOD_5_MINS);	}

	// @Bean
	public MarketDataAdapter krakenDataListener5MinsUSD() {
		Market market = new Market();
		market.setMarketKey("XXBTZUSD");
		market.setMarketDisplayName("XBT-USD");
		return new KrakenAdapter(new RestTemplate(), market, KrakenAdapter.PERIOD_5_MINS);
	}

	// @Bean
	public MarketDataAdapter krakenDataListener5MinuteXRPEUR() {
		Market market = new Market();
		market.setMarketKey("XXRPZEUR");
		market.setMarketDisplayName("XRP-EUR");
		return new KrakenAdapter(new RestTemplate(), market, KrakenAdapter.PERIOD_4_HOURS);
	}

}
