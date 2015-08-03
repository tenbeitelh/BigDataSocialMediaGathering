package de.hs.osnabrueck.htenbeitel.flume.facebook.source;

import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hs.osnabrueck.htenbeitel.facebook.FacebookObserver;
import de.hs.osnabrueck.htenbeitel.facebook.StatusListener;
import de.hs.osnabrueck.htenbeitel.flume.facebook.utils.FacebookConstants;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookSource extends AbstractSource implements
		EventDrivenSource, Configurable {

	private static final Logger LOG = LoggerFactory
			.getLogger(FacebookSource.class);

	private String appId;
	private String appSecret;
	private String accessToken;
	private String[] pages;
	
	private FacebookObserver facebook;

	@Override
	public void configure(Context context) {
		LOG.info("Starting configuration");

		this.appId = context.getString(FacebookConstants.API_ID);
		this.appSecret = context.getString(FacebookConstants.API_SECRET);
		this.accessToken = context.getString(FacebookConstants.ACCESS_TOKEN);
		String pageString = context.getString(FacebookConstants.PAGES);
		this.pages = pageString.split(",");
		
		for (int i = 0; i < pages.length; i++) {
			this.pages[i] = this.pages[i].trim();
		}
		ConfigurationBuilder confBuilder = new ConfigurationBuilder();
		
		confBuilder.setOAuthAppId(appId);
		confBuilder.setOAuthAppSecret(appSecret);
		confBuilder.setOAuthAccessToken(accessToken);
		
		this.facebook = new FacebookObserver(confBuilder.build());
		
		StatusListener listener = new StatusListener() {
			final ChannelProcessor channel = getChannelProcessor();
			final Map<String, String> headers = new HashMap<String, String>();
			@Override
			public void onPost(Post post) {
				LOG.info("");
			}
			
			@Override
			public void onException(Exception exception) {
				LOG.error(exception.getLocalizedMessage());
			}
		};
		
		this.facebook.addListener(listener);
	}

	@Override
	public synchronized void start() {
		super.start();
	}

	@Override
	public synchronized void stop() {
		super.stop();
	}

}
