package com.blocktrader.event.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocktrader.event.Event;
import com.blocktrader.event.EventHandler;

public class LoggingEventHandler implements EventHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(LoggingEventHandler.class);

	@Override
	public void handleEvent(Event event) {
		LOGGER.info("Event: {}", event.getMessage());
	}

}
