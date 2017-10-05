package com.blocktrader.event.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import com.blocktrader.event.Event;

@RunWith(MockitoJUnitRunner.class)
public class DefaultEventListenerTest {

	@Mock
	private TwitterTemplate  twitterTemplate;
	
	@Mock
	private TimelineOperations timelineOperations;
	
	@Mock
	private LoggingEventHandler loggingEventHandler; 
	
	@Test
	public void testPublishTwitterEvent() {
		Mockito.when(twitterTemplate.timelineOperations()).thenReturn(timelineOperations);
		DefaultEventListener eventListener = new DefaultEventListener();
		eventListener.addHandler(new TwitterPublishingEventHandler(twitterTemplate));
		eventListener.addHandler(loggingEventHandler);
		Event e = new Event("DefaultEvent", "Test");
		
		eventListener.registerEvent(e);
		
		Mockito.verify(timelineOperations, Mockito.times(1)).updateStatus(e.getMessage());
		Mockito.verify(loggingEventHandler, Mockito.times(1)).handleEvent(e);
		
	}
	
	@Test
	public void testPublishLoggingEvent() {
		DefaultEventListener eventListener = new DefaultEventListener();
		eventListener.addHandler(loggingEventHandler);
		Event e = new Event("DefaultEvent", "Test");
		
		eventListener.registerEvent(e);
		
		Mockito.verify(loggingEventHandler, Mockito.times(1)).handleEvent(e);
		
	}
	
	@Test
	public void testEvent(){
		Event e = new Event("DefaultEvent", "Test");
		assertEquals("DefaultEvent", e.getEventName());
		assertEquals("Test", e.getMessage());
		
		e.setEventName("DefaultEvent2");
		e.setMessage("Test2");
		assertEquals("DefaultEvent2", e.getEventName());
		assertEquals("Test2", e.getMessage());
	}

}
