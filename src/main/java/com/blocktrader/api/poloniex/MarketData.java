package com.blocktrader.api.poloniex;

public class MarketData {

	private long date;
	private double high;
	private double low;
	private double open;
	private double close;
	private double volume;
	private double quoteVolume;
	private double weightedAverage;
	
	
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getQuoteVolume() {
		return quoteVolume;
	}
	public void setQuoteVolume(double quoteVolume) {
		this.quoteVolume = quoteVolume;
	}
	public double getWeightedAverage() {
		return weightedAverage;
	}
	public void setWeightedAverage(double weightyedAverage) {
		this.weightedAverage = weightyedAverage;
	}
	
	
}
