package de.hs.osnabrueck.htenbeitel.flume.rss.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hs.osnabrueck.htenbeitel.flume.rss.parser.RSSFeedListener;
import de.hs.osnabrueck.htenbeitel.flume.rss.parser.RSSFeedReader;
import de.hs.osnabrueck.htenbeitel.flume.rss.parser.model.FeedEntry;

public class RSSSource /*
						 * extends AbstractSource implements EventDrivenSource,
						 * Configurable
						 */{

	private static final Logger LOG = LoggerFactory.getLogger(RSSSource.class);

	private RSSFeedReader reader;

	public RSSSource(String urlString) {
		String[] urls = urlString.split(",");

		for (int i = 0; i < urls.length; i++) {
			urls[i] = urls[i].trim();
		}

		reader = new RSSFeedReader(urls);
	}

	// @Override
	// public void configure(Context context) {
	// LOG.info("Configuring Source");
	//
	// String urlString = context.getString(RSSConstants.FEED_URLS);
	// String[] urls = urlString.split(",");
	//
	// for (int i = 0; i < urls.length; i++) {
	// urls[i] = urls[i].trim();
	// }
	//
	// reader = new RSSFeedReader(urls);
	//
	// }

	// @Override
	public synchronized void start() {
		// super.start();
		LOG.info("Starting flume process");

		// final ChannelProcessor channel = getChannelProcessor();
		// final Map<String, String> headers = new HashMap<String, String>();

		RSSFeedListener listener = new RSSFeedListener() {

			@Override
			public void onFeedUpdate(FeedEntry entry) {
				// System.out.println(entry.toJson());
				// headers.put("timestamp",
				// String.valueOf(entry.getPublishedDate().getTime()));
				// Event event =
				// EventBuilder.withBody(entry.toJson().getBytes(),
				// headers);
				// channel.processEvent(event);
			}

			@Override
			public void onException(Exception ex) {
				LOG.error(ex.getMessage());
			}

		};

		reader.addListener(listener);
		reader.startProcessing();

	}

	// @Override
	// public synchronized void stop() {
	// super.stop();
	// }

	public static void main(String args[]) {
		RSSSource source = new RSSSource(
				"http://www.spdfraktion.de/presse/pressemitteilungen/feed,https://www.cdu.de/rss.xml");
		source.start();
	}
}
