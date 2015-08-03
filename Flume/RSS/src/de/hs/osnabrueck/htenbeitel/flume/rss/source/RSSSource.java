package de.hs.osnabrueck.htenbeitel.flume.rss.source;

import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
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
		
		for(int i = 0; i<urls.length; i++){
			urls[i] = urls[i].trim();
		}
		
		reader = new RSSFeedReader(urls);
		
		
	}

	@Override
	public synchronized void start() {
		super.start();
		LOG.info("Starting flume process");
		
		RSSFeedListener listener = new RSSFeedListener() {
			
			@Override
			public void onFeedUpdate(FeedEntry entry) {
				// TODO Auto-generated method stub
				
			}

			
		};
		
		reader.addListener(listener);
		
	}

	@Override
	public synchronized void stop() {
		super.stop();
	}
	
	
}
