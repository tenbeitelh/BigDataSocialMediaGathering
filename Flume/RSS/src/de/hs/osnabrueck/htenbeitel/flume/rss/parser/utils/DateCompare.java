package de.hs.osnabrueck.htenbeitel.flume.rss.parser.utils;

import java.util.Date;

public class DateCompare {
	public static Date getMaxDate(Date maxPublishedDateOfFeed, Date publishedDate) {
		Date max = null;
		if (maxPublishedDateOfFeed == null) {
			max = publishedDate;
		} else if (maxPublishedDateOfFeed.before(publishedDate)) {
			max = publishedDate;
		}
		return max;
	}
}
