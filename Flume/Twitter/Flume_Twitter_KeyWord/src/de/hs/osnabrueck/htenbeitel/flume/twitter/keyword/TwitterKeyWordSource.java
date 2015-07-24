package de.hs.osnabrueck.htenbeitel.flume.twitter.keyword;

import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hs.osnabrueck.flume.twitter.constants.TwitterConstants;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class TwitterKeyWordSource extends AbstractSource implements
		EventDrivenSource, Configurable {

	private static final Logger LOG = LoggerFactory
			.getLogger(TwitterKeyWordSource.class);

	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;

	private String[] keywords;

	private TwitterStream tStream;

	@Override
	public void configure(Context context) {
		LOG.info("Starting configuration");
		consumerKey = context.getString(TwitterConstants.CONSUMER_KEY);
		consumerSecret = context.getString(TwitterConstants.CONSUMER_SECRET);
		accessToken = context.getString(TwitterConstants.ACCESS_TOKEN);
		accessTokenSecret = context.getString(TwitterConstants.ACCESS_SECRET);
		LOG.info("ConsumerKey: " + consumerKey);
		LOG.info("ConsumerSecre: " + consumerSecret);
		LOG.info("accessToken: " + accessToken);
		LOG.info("accessTokenSecret: " + accessTokenSecret);
		String keywordString = context
				.getString(TwitterConstants.TWITTER_KEYWORDS);
		LOG.info("Processing keywords");
		if(keywordString.length() > 0){
			this.keywords = keywordString.split(",");
			for(int i = 0; i<this.keywords.length; i++){
				this.keywords[i] = this.keywords[i].trim();
			}
		}
		LOG.info("Finished configuration");
		

		ConfigurationBuilder confBuilder = new ConfigurationBuilder();
		confBuilder.setOAuthConsumerKey(consumerKey);
		confBuilder.setOAuthConsumerSecret(consumerSecret);
		confBuilder.setOAuthAccessToken(accessToken);
		confBuilder.setOAuthAccessTokenSecret(accessTokenSecret);
		confBuilder.setJSONStoreEnabled(true);
		confBuilder.setIncludeEntitiesEnabled(true);
		Configuration conf = confBuilder.build();
		tStream = new TwitterStreamFactory(conf).getInstance();
	}

	@Override
	public synchronized void start() {
		final ChannelProcessor channel = getChannelProcessor();
		final Map<String, String> headers = new HashMap<String, String>();

		StatusListener listener = new StatusListener() {

			@Override
			public void onStatus(Status status) {
				LOG.debug(status.getUser().getScreenName() + ": "
						+ status.getText());
				headers.put("timestamp",
						String.valueOf(status.getCreatedAt().getTime()));
				Event event = EventBuilder.withBody(DataObjectFactory
						.getRawJSON(status).getBytes(), headers);
				channel.processEvent(event);
			}

			@Override
			public void onException(Exception ex) {
				LOG.error(ex.getMessage());
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
			}
		};

		tStream.addListener(listener);

		if (keywords.length == 0) {
			LOG.error("No keywords in conf file");
		} else {
			LOG.debug("Starting twitter filtering...");
			FilterQuery query = new FilterQuery().track(keywords);
			//new FilterQuery(count, follow);
			tStream.filter(query);
		}

		super.start();
	}

	@Override
	public synchronized void stop() {
		// TODO Auto-generated method stub
		LOG.debug("Stopping Twitter streaming");
		tStream.shutdown();
		super.stop();
	}
}