package com.blocktrader.strategy;

import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;

public interface StrategyBuilder {

	Strategy build(TimeSeries series);

	void setName(String name);

	String getName();
}
