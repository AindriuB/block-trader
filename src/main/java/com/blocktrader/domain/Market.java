package com.blocktrader.domain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocktrader.strategy.StrategyBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.verdelhan.ta4j.AnalysisCriterion;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TradingRecord;
import eu.verdelhan.ta4j.analysis.criteria.AverageProfitableTradesCriterion;
import eu.verdelhan.ta4j.analysis.criteria.RewardRiskRatioCriterion;
import eu.verdelhan.ta4j.analysis.criteria.TotalProfitCriterion;

public class Market {

	private static Logger LOGGER = LoggerFactory.getLogger(Market.class);

	private String marketKey;
	private String marketDisplayName;
	private List<StrategyBuilder> strategyBuilderList;

	private TimeSeries timeSeries;
	
	
	public TimeSeries getTimeSeries() {
		return timeSeries;
	}

	public void setTimeSeries(TimeSeries timeSeries) {
		this.timeSeries = timeSeries;
	}

	public String getMarketKey() {
		return marketKey;
	}

	public void setMarketKey(String marketKey) {
		this.marketKey = marketKey;
	}

	public String getMarketDisplayName() {
		return marketDisplayName;
	}

	public void setMarketDisplayName(String marketDisplayName) {
		this.marketDisplayName = marketDisplayName;
	}

	public List<StrategyBuilder> getStrategyList() {
		return strategyBuilderList;
	}

	public void setStrategyBuilderList(List<StrategyBuilder> strategyBuilderList) {
		this.strategyBuilderList = strategyBuilderList;
	}
	
	
	public List<StrategyBuilder> getStrategyBuilderList() {
		return strategyBuilderList;
	}
	
	

	public Strategy getMostProfitableStrategy(TimeSeries series) {

		String highestProfitabilityStrategyName = null;
		if (strategyBuilderList == null || strategyBuilderList.isEmpty()) {
			return null;
		} else {
			Strategy mostProfitable = null;
			double maxProfitability = 0.0d;
			for (StrategyBuilder sb : strategyBuilderList) {
				Strategy current = sb.build(series);
				// Running our juicy trading strategy...
				TradingRecord tradingRecord = series.run(current);
				int tradeCount = tradingRecord.getTradeCount();
				if (tradeCount > 0) {
					AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();
					AnalysisCriterion rewardRiskRatio = new RewardRiskRatioCriterion();
					AnalysisCriterion totalProfit = new TotalProfitCriterion();
					double profitability = profitTradesRatio.calculate(series, tradingRecord);
					double riskRewardValue = rewardRiskRatio.calculate(series, tradingRecord);
					double totalProfitValue = totalProfit.calculate(series, tradingRecord);

					Analysis analysis = new Analysis(tradeCount, profitability, riskRewardValue, totalProfitValue);
					if (LOGGER.isDebugEnabled()) {
						ObjectMapper objectMapper = new ObjectMapper();
						try {
							LOGGER.debug("Analysis - {}:{}",sb.getName(), objectMapper.writeValueAsString(analysis));
						} catch (JsonProcessingException e) {
							LOGGER.error("Error writing analysis object", e);
						}
					
					}

					if (maxProfitability < profitability) {
						mostProfitable = current;
						maxProfitability = profitability;
						highestProfitabilityStrategyName = sb.getName();
						LOGGER.info("Strategy with new highest profitaility: {}", sb.getName());
					}
				} else {
					LOGGER.debug("No trades for this strategy");
				}
			}
			
			if (highestProfitabilityStrategyName != null) {
				LOGGER.info("Strategy with new highest profitaility: {}", highestProfitabilityStrategyName);
			}

			return mostProfitable;
		}
		
	}
	
	
}
