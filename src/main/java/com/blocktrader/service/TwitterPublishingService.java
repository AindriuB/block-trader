package com.blocktrader.service;

import com.blocktrader.event.Event;

public interface TwitterPublishingService {
	
	void publishEvent(Event event);

}
