package com.blocktrader.event.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import com.blocktrader.event.Event;
import com.blocktrader.event.EventHandler;

public class TwitterPublishingEventHandler implements EventHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(TwitterPublishingEventHandler.class);

	private TwitterTemplate twitterTemplate;

	public TwitterPublishingEventHandler(TwitterTemplate twitterTemplate) {
		this.twitterTemplate = twitterTemplate;
	}

	@Override
	public void handleEvent(Event event) {
		LOGGER.info("Publishing message to twitter: {}", event.getMessage());
		twitterTemplate.timelineOperations().updateStatus(event.getMessage());
	}

}
