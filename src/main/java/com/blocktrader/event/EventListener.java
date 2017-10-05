package com.blocktrader.event;

public interface EventListener {

	void addHandler(EventHandler e);

	void registerEvent(Event e);

}
