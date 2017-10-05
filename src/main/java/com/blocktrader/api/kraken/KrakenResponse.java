package com.blocktrader.api.kraken;

import java.util.Map;

public class KrakenResponse {

	private String[] error;
	
	private Map<String, Object> result;

	public String[] getError() {
		return error;
	}

	public void setError(String[] error) {
		this.error = error;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	

}
