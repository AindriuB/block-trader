package com.blocktrader.domain;

public class Analysis {
	
	public Analysis(int totalTrades, double profitableTradeRatio, double riskRewardRatio, double totalProfit){
		this.totalTrades = totalTrades;
		this.profitableTradeRatio = profitableTradeRatio;
		this.totalProfit = totalProfit;
		this.riskRewardRatio = riskRewardRatio;
	}
	private int totalTrades;
	private double profitableTradeRatio;
	private double riskRewardRatio;
	private double totalProfit;
	public int getTotalTrades() {
		return totalTrades;
	}
	public void setTotalTrades(int totalTrades) {
		this.totalTrades = totalTrades;
	}
	public double getProfitableTradeRatio() {
		return profitableTradeRatio;
	}
	public void setProfitableTradeRatio(double profitableTradeRatio) {
		this.profitableTradeRatio = profitableTradeRatio;
	}
	public double getRiskRewardRatio() {
		return riskRewardRatio;
	}
	public void setRiskRewardRatio(double riskRewardRatio) {
		this.riskRewardRatio = riskRewardRatio;
	}
	public double getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(double totalProfit) {
		this.totalProfit = totalProfit;
	}

	
	
}
