package de.hs.osnabrueck.htenbeitel.flume.rss.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import de.hs.osnabrueck.htenbeitel.flume.rss.parser.model.FeedEntry;
import de.hs.osnabrueck.htenbeitel.flume.utils.DateCompare;
import de.hs.osnabrueck.htenbeitel.flume.utils.StateSerDeseriliazer;

public class RSSFeedReader {

	private static final Logger LOG = LoggerFactory
			.getLogger(RSSFeedReader.class);
	private static final long TIME_IN_MINUTES = 30;
	private static final String DATE_MAP = "rss_date_map.data";

	private Map<String, Date> lastParsedItemMap;
	private Map<String, URL> urlMap;

	private boolean closed = false;

	private List<RSSFeedListener> listener = new ArrayList<RSSFeedListener>();

	public RSSFeedReader(String[] urls) {
		super();
		LOG.info("Loading backed up dateMap");
		lastParsedItemMap = StateSerDeseriliazer.deserilazeDateMap(DATE_MAP);
		if (lastParsedItemMap == null) {
			lastParsedItemMap = new HashMap<String, Date>();
		}

		if (urlMap == null) {
			urlMap = new HashMap<String, URL>();
		}
		for (String url : urls) {
			if (!urlMap.containsKey(url)) {

				try {
					urlMap.put(url, new URL(url));
				} catch (MalformedURLException e) {
					LOG.error(e.getMessage());
					LOG.trace(e.getMessage(), e);
				}

			}
			if (!lastParsedItemMap.containsKey(url)) {
				lastParsedItemMap.put(url, null);
			}
		}

	}

	public void startProcessing() {
		this.closed = false;
		LOG.info("Starting reading timer with interval of: "
				+ (TIME_IN_MINUTES * 1000 * 60) + " minutes");
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (!closed) {
					LOG.info("Process feeds");
					processFeeds();

				} else {
					LOG.info("Stopping timer");
					timer.cancel();
				}

			}
		}, 0, TIME_IN_MINUTES * 1000 * 60);
	}

	public void processFeeds() {
		final SyndFeedInput input = new SyndFeedInput();
		input.setAllowDoctypes(true);
		new Runnable() {

			@Override
			public void run() {
				for (URL url : urlMap.values()) {
					LOG.info("Processing " + url.toString());
					// System.out.println("Processing " + url.toString());
					processFeed(url, input);
					LOG.info("Saving state");
					StateSerDeseriliazer.serilazeDateMap(lastParsedItemMap,
							DATE_MAP);
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e) {
						LOG.error(e.getMessage());
						LOG.trace(e.getMessage(), e);
					}
				}

			}
		}.run();

	}

	private void processFeed(URL url, SyndFeedInput input) {
		Date maxPublishedDateOfFeed = lastParsedItemMap.get(url.toString());
		try (InputStream stream = url.openConnection().getInputStream()) {
			SyndFeed feed = input.build(new InputStreamReader(stream));

			if (maxPublishedDateOfFeed == null) {
				// Process all items because the feed wasn't processed
				// before

				for (SyndEntry entry : feed.getEntries()) {

					if (entry.getPublishedDate() == null) {
						if (feed.getPublishedDate() == null) {
							entry.setPublishedDate(new Date());
						} else {
							entry.setPublishedDate(feed.getPublishedDate());
						}
					} else {
						// nothing to do

					}
					maxPublishedDateOfFeed = DateCompare.getMaxDate(
							maxPublishedDateOfFeed, entry.getPublishedDate());
					FeedEntry customEntry = FeedEntry.buildCustomFeedEntry(
							feed.getTitle(), entry);
					for (RSSFeedListener list : this.listener) {
						list.onFeedUpdate(customEntry);
					}
				}
			} else {
				// If the feed was processed before, process only new
				// Feed-Items.

				for (SyndEntry entry : feed.getEntries()) {
					// Get a published date if none avaialbe in the entry
					if (entry.getPublishedDate() == null) {
						if (feed.getPublishedDate() == null) {
							entry.setPublishedDate(new Date());
						} else {
							entry.setPublishedDate(feed.getPublishedDate());
						}
					} else {
						if (DateCompare.dateIsAfterMaxDate(
								maxPublishedDateOfFeed,
								entry.getPublishedDate())) {
							maxPublishedDateOfFeed = entry.getPublishedDate();
							FeedEntry customEntry = FeedEntry
									.buildCustomFeedEntry(feed.getTitle(),
											entry);
							for (RSSFeedListener list : this.listener) {
								list.onFeedUpdate(customEntry);
							}
						}
					}
				}
			}
			lastParsedItemMap.put(url.toString(), maxPublishedDateOfFeed);

		} catch (IOException e) {
			for (RSSFeedListener rssFeedListener : listener) {
				rssFeedListener.onException(e);
			}
		} catch (IllegalArgumentException e) {
			for (RSSFeedListener rssFeedListener : listener) {
				rssFeedListener.onException(e);
			}
		} catch (FeedException e) {
			for (RSSFeedListener rssFeedListener : listener) {
				rssFeedListener.onException(e);
			}
		}
	}

	public void shutdown() {
		LOG.info("Shutodown called");
		this.closed = true;
	}

	public Map<String, Date> getLastParsedItemMap() {
		return lastParsedItemMap;
	}

	public void setLastParsedItemMap(Map<String, Date> lastParsedItemMap) {
		this.lastParsedItemMap = lastParsedItemMap;
	}

	public Map<String, URL> getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(Map<String, URL> urlMap) {
		this.urlMap = urlMap;
	}

	public List<RSSFeedListener> getListener() {
		return listener;
	}

	public void addListener(RSSFeedListener listener) {
		this.listener.add(listener);
	}

}
