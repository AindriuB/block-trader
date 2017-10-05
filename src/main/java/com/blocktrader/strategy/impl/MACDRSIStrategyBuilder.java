/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.blocktrader.strategy.impl;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;

/**
 * Strategies which compares current price to global extrema over a week.
 */
public class MACDRSIStrategyBuilder extends AbstractStrategyBuilder {

	// We assume that there were at least one trade every 5 minutes during the whole week
	private static final int NB_TICKS_PER_WEEK = 12 * 24 * 7;

	public MACDRSIStrategyBuilder() {
		super("MACD-RSI Strategy");
	}

	public MACDRSIStrategyBuilder(String name) {
		super(name);
	}

	/**
	 * @param series  a time series
	 * @return a global extrema strategy
	 */
	public Strategy build(TimeSeries series) {
		if (series == null) {
			throw new IllegalArgumentException("Series cannot be null");
		}

		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        // We use a 2-period RSI indicator to identify buying
        // or selling opportunities within the bigger trend.
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);
		
		MACDIndicator macd = new MACDIndicator(closePrice, 10, 26);
		EMAIndicator emaMacd = new EMAIndicator(macd, 9);
        // or selling opportunities within the bigger trend.

		
		// Entry rule
		Rule entryRule = new OverIndicatorRule(macd, emaMacd)//MACD
					.and(new CrossedUpIndicatorRule(rsi,Decimal.valueOf(40))); 
		// Exit rule
		Rule exitRule = new UnderIndicatorRule(macd, emaMacd) //MACD
					.and(new CrossedDownIndicatorRule(rsi,Decimal.valueOf(60)));

		return new Strategy(entryRule, exitRule);
	}

}
