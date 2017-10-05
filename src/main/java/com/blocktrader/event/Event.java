package com.blocktrader.event;

public class Event {

	private String eventName;

	private String message;

	public Event(String eventName, String message) {
		this.eventName = eventName;
		this.message = message;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
