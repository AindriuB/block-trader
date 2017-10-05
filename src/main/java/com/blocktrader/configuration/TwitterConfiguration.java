package com.blocktrader.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import com.blocktrader.event.impl.TwitterPublishingEventHandler;

@Profile(value = "TwitterBot")
@Configuration
public class TwitterConfiguration {

	@Value("${twitter.consumer.key}")
	String consumerKey;// The application's consumer key

	@Value("${twitter.consumer.secret}")
	String consumerSecret; // The application's consumer secret

	@Value("${twitter.oauth.token}")
	String accessToken; // The access token granted after OAuth authorization

	@Value("${twitter.oauth.secret}")
	String accessTokenSecret; // The access token secret granted after OAuth
								// authorization

	@Bean
	public TwitterTemplate twitterTemplate() {
		TwitterTemplate twitterTemplate = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		return twitterTemplate;
	}

	@Bean
	public TwitterPublishingEventHandler twitterEventHandler() {
		return new TwitterPublishingEventHandler(twitterTemplate());
	}
}
