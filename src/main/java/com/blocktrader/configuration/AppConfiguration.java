package com.blocktrader.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.blocktrader.event.EventListener;
import com.blocktrader.event.impl.DefaultEventListener;
import com.blocktrader.event.impl.LoggingEventHandler;
import com.blocktrader.event.impl.TwitterPublishingEventHandler;

@EnableAsync
@EnableScheduling
@Configuration
public class AppConfiguration {

	@Autowired(required = false)
	private TwitterPublishingEventHandler twitterEventHandler;

	@Bean
	public EventListener eventListener() {
		DefaultEventListener listener = new DefaultEventListener();
		if (twitterEventHandler != null) {
			listener.addHandler(twitterEventHandler);
		}
		listener.addHandler(new LoggingEventHandler());
		return listener;
	}

}
