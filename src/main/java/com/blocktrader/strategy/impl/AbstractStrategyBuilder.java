package com.blocktrader.strategy.impl;

import com.blocktrader.strategy.StrategyBuilder;

public abstract class AbstractStrategyBuilder implements StrategyBuilder {

	private String name;

	AbstractStrategyBuilder(String name) {
		this.name = name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {

		return name;
	}

}
