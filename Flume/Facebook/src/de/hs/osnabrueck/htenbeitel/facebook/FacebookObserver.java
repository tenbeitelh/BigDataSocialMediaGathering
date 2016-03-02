package de.hs.osnabrueck.htenbeitel.facebook;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hs.osnabrueck.htenbeitel.facebook.model.FacebookPost;
import de.hs.osnabrueck.htenbeitel.flume.utils.DateCompare;
import de.hs.osnabrueck.htenbeitel.flume.utils.StateSerDeseriliazer;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;

public class FacebookObserver {

	private static final Logger LOG = LoggerFactory
			.getLogger(FacebookObserver.class);

	private static final boolean IS_DEAMON = true;
	private static final long TIME_IN_MINUTES = 5;
	private static final String DATE_MAP = "facebook_date_map.data";

	final Timer timer = new Timer(IS_DEAMON);

	private Map<String, Date> dateMap;

	Facebook facebook;
	List<StatusListener> listeners = new ArrayList<StatusListener>();
	private String[] pages;

	public FacebookObserver(Configuration conf, String[] pages) {
		this.facebook = new FacebookFactory(conf).getInstance();
		this.pages = pages;

		this.dateMap = StateSerDeseriliazer.deserilazeDateMap(DATE_MAP);
		if (this.dateMap == null) {
			this.dateMap = new HashMap<String, Date>();
		}
	}

	public void addListener(StatusListener listener) {
		this.listeners.add(listener);
	}

	public void startProcessing() {
		LOG.info("Start processing");
		LOG.info("Initiliaze timer with a intervall of: "
				+ (TIME_IN_MINUTES * 1000 * 60) + " minutes");

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				LOG.info("Processing pages");
				processPages();
			}
			
		}, 0, (TIME_IN_MINUTES * 1000 * 60));

	}

	public void stopProcessing() {
		LOG.info("Stopping page processing");
		LOG.info("Stopping timer");
		timer.cancel();
	}

	public void processPages() {

		new Runnable() {

			@Override
			public void run() {
				for (String pageId : pages) {
					LOG.info("Processing page: " + pageId);
					processPage(pageId);
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

	public void processPage(String pageId) {
		Date maxCreatedDate = this.dateMap.get(pageId);

		if (maxCreatedDate == null) {
			LOG.info("First time to process");
			try {
				ResponseList<Post> posts = this.facebook.getFeed(pageId);
				for (Post post : posts) {

					Date createdTime = post.getCreatedTime();
					if (createdTime == null) {
						createdTime = new Date();
					}

					maxCreatedDate = DateCompare.getMaxDate(maxCreatedDate,
							createdTime);

					this.dateMap.put(pageId, maxCreatedDate);

					for (StatusListener listener : listeners) {
						listener.onPost(FacebookPost.createFacebookPostObject(
								pageId, post));
					}
				}

			} catch (FacebookException e) {
				for (StatusListener listener : listeners) {
					listener.onException(e);
				}
			}
		} else {
			try {
				ResponseList<Post> posts = this.facebook.getFeed(pageId,
						new Reading().since(maxCreatedDate));
				for (Post post : posts) {
					Date createdTime = post.getCreatedTime();
					if (createdTime == null) {
						createdTime = new Date();
					}

					maxCreatedDate = DateCompare.getMaxDate(maxCreatedDate,
							createdTime);

					for (StatusListener listener : listeners) {
						listener.onPost(FacebookPost.createFacebookPostObject(
								pageId, post));
					}

					this.dateMap.put(pageId, maxCreatedDate);
				}
			} catch (FacebookException e) {
				for (StatusListener listener : listeners) {
					listener.onException(e);
				}
			}
			if (maxCreatedDate != null) {
				this.dateMap.put(pageId, maxCreatedDate);
			} else {
				this.dateMap.put(pageId, new Date());
			}
			LOG.info("Newes post of feed " + pageId + " is from "
					+ maxCreatedDate);
			StateSerDeseriliazer.serilazeDateMap(dateMap, DATE_MAP);
		}

	}

}
