package com.blocktrader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocktrader.domain.Market;
import com.blocktrader.event.Event;
import com.blocktrader.event.EventListener;
import com.blocktrader.event.EventPublisher;
import com.blocktrader.service.MarketWatchingService;
import com.blocktrader.strategy.impl.MACDRSIStrategyBuilder;

import eu.verdelhan.ta4j.AnalysisCriterion;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TradingRecord;
import eu.verdelhan.ta4j.analysis.CashFlow;
import eu.verdelhan.ta4j.analysis.criteria.AverageProfitableTradesCriterion;
import eu.verdelhan.ta4j.analysis.criteria.NumberOfTicksCriterion;
import eu.verdelhan.ta4j.analysis.criteria.RewardRiskRatioCriterion;
import eu.verdelhan.ta4j.analysis.criteria.TotalProfitCriterion;
import eu.verdelhan.ta4j.analysis.criteria.VersusBuyAndHoldCriterion;

@Service
public class MarketWatchingServiceImpl implements MarketWatchingService, EventPublisher {

	@Autowired
	private EventListener eventListner;

	private static final Logger LOGGER = LoggerFactory.getLogger(MarketWatchingServiceImpl.class);

	@Override
	public void process(Market market) {
		
		TimeSeries series =  market.getTimeSeries();

		LOGGER.debug("Processing market data ~ number of ticks {}", series.getTickCount());
		Strategy strat = market.getMostProfitableStrategy(series);

		// Default Strategy
		if (strat == null) {
			LOGGER.info("No strategy available for market {}", market.getMarketDisplayName());
			return;
		}

		// Attempt to detect changes in the market based on our indicators and
		// rules
		if (strat.shouldEnter(series.getEnd()) && !strat.shouldEnter(series.getEnd() - 1)) {
			LOGGER.info("Series {} - New Uptrend detected", market.getMarketDisplayName());
			publishEvent(new Event("Buy Event",
					"Uptrend detected for " + market.getMarketDisplayName() + " Buy: " + series.getLastTick().getClosePrice().toDouble()));
		} else if (strat.shouldEnter(series.getEnd()) && strat.shouldEnter(series.getEnd() - 1)) {
			LOGGER.info("Series {} - Uptrend continuing", market.getMarketDisplayName());
		} else if (strat.shouldExit(series.getEnd()) && !strat.shouldExit(series.getEnd() - 1)) {
			LOGGER.info("Series {} - New Downtrend detected", market.getMarketDisplayName());
		} else if (!strat.shouldEnter(series.getEnd()) && !strat.shouldEnter(series.getEnd() - 1)) {
			LOGGER.info("Series {} - Downtrend continuing", market.getMarketDisplayName());
			publishEvent(new Event("EventContinuing", "Downtrend Continuing"));

		}

	}

	@Override
	public void publishEvent(Event e) {
		eventListner.registerEvent(e);
	}

}
