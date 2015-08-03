package de.hs.osnabrueck.htenbeitel.flume.rss.parser;

import de.hs.osnabrueck.htenbeitel.flume.rss.parser.model.FeedEntry;

public interface RSSFeedListener {
	public void onFeedUpdate(FeedEntry entry);
}
