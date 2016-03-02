package de.hs.osnabrueck.htenbeitel.flume.rss.source;

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

import de.hs.osnabrueck.htenbeitel.flume.rss.parser.RSSFeedListener;
import de.hs.osnabrueck.htenbeitel.flume.rss.parser.RSSFeedReader;
import de.hs.osnabrueck.htenbeitel.flume.rss.parser.model.FeedEntry;

public class RSSSource extends AbstractSource implements EventDrivenSource,
		Configurable {

	private static final Logger LOG = LoggerFactory.getLogger(RSSSource.class);

	private RSSFeedReader reader;

	@Override
	public void configure(Context context) {
		LOG.info("Configuring Source");

		String urlString = context.getString(RSSConstants.FEED_URLS);
		String[] urls = urlString.split(",");

		for (int i = 0; i < urls.length; i++) {
			urls[i] = urls[i].trim();
		}
		LOG.info("Initilize Reader");
		reader = new RSSFeedReader(urls);

	}

	@Override
	public synchronized void start() {
		super.start();
		LOG.info("Starting flume process");

		final ChannelProcessor channel = getChannelProcessor();
		final Map<String, String> headers = new HashMap<String, String>();
		LOG.info("Registering listener");
		RSSFeedListener listener = new RSSFeedListener() {

			@Override
			public void onFeedUpdate(FeedEntry entry) {
				headers.put("timestamp",
						String.valueOf(entry.getPublishedDate().getTime()));
				headers.put("source_feed", entry.getSourceFeed());
				Event event = EventBuilder.withBody(entry.toJson().getBytes(),
						headers);
				channel.processEvent(event);
			}

			@Override
			public void onException(Exception ex) {
				LOG.error(ex.getMessage());
			}

		};

		reader.addListener(listener);
		LOG.info("Starting Reader process");
		reader.startProcessing();

	}

	@Override
	public synchronized void stop() {
		reader.shutdown();
		super.stop();
	}

}
