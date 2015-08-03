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

import de.hs.osnabrueck.htenbeitel.flume.rss.parser.utils.DateCompare;
import de.hs.osnabrueck.htenbeitel.flume.rss.parser.utils.StateSerDeseriliazer;

public class RSSFeedReader {
	private Map<String, Date> lastParsedItemMap;
	private Map<String, URL> urlMap;

	private boolean closed = false;

	private static final Logger LOG = LoggerFactory
			.getLogger(RSSFeedReader.class);
	private static final long TIME_IN_MINUTES = 30;

	private List<RSSFeedListener> listener = new ArrayList<RSSFeedListener>();

	public RSSFeedReader(String[] urls) {
		super();
		LOG.info("Loading backed up dateMap");
		lastParsedItemMap = StateSerDeseriliazer.deserilazeDateMap();
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
				}
			}
			if (!lastParsedItemMap.containsKey(url)) {
				lastParsedItemMap.put(url, null);
			}
		}

	}

	public void startProcessing() {
		Timer timer = new Timer();
		while (!closed) {
			timer.schedule(new RSSProcessingTask(), TIME_IN_MINUTES * 60000);
		}
		timer.cancel();
	}

	public void processFeeds() {
		SyndFeedInput input = new SyndFeedInput();
		input.setAllowDoctypes(true);

		for (URL url : urlMap.values()) {
			Date maxPublishedDateOfFeed = lastParsedItemMap.get(url.toString());
			try (InputStream stream = url.openConnection().getInputStream()) {
				SyndFeed feed = input.build(new InputStreamReader(stream));
				if (maxPublishedDateOfFeed == null) {
					// Process all items because the feed wasn't processed
					// before

					for (SyndEntry entry : feed.getEntries()) {
						maxPublishedDateOfFeed = DateCompare.getMaxDate(maxPublishedDateOfFeed, entry.getPublishedDate());
						
					}
				} else {
					// If the feed was processed before, process only new
					// Feed-Items.
					for(SyndEntry entry : feed.getEntries()){
						maxPublishedDateOfFeed = DateCompare.getMaxDate(maxPublishedDateOfFeed, entry.getPublishedDate());
					}
				}
				lastParsedItemMap.put(url.toString(), maxPublishedDateOfFeed);

			} catch (IOException e) {
				LOG.error(e.getMessage());
			} catch (IllegalArgumentException e) {
				LOG.error(e.getMessage());
			} catch (FeedException e) {
				LOG.error(e.getMessage());
			}

		}
	}

	

	public void shutdown() {
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

	class RSSProcessingTask extends TimerTask {

		public void run() {
			processFeeds();
		}
	}

}
