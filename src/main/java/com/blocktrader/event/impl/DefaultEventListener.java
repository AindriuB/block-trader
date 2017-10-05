package com.blocktrader.event.impl;

import java.util.ArrayList;
import java.util.List;

import com.blocktrader.event.Event;
import com.blocktrader.event.EventHandler;
import com.blocktrader.event.EventListener;

public class DefaultEventListener implements EventListener {

	private List<EventHandler> eventHandlers;

	public DefaultEventListener() {
		eventHandlers = new ArrayList<EventHandler>();

	}

	@Override
	public void addHandler(EventHandler e) {
		eventHandlers.add(e);

	}

	@Override
	public void registerEvent(Event e) {
		for (EventHandler event : eventHandlers) {
			event.handleEvent(e);
		}
	}

}
