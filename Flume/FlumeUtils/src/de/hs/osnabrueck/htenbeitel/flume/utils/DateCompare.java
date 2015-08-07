package de.hs.osnabrueck.htenbeitel.flume.utils;

import java.util.Date;

public class DateCompare {
	public static Date getMaxDate(Date maxPublishedDateOfFeed,
			Date publishedDate) {

		Date max = null;
		if (maxPublishedDateOfFeed == null) {
			max = publishedDate;
		} else if (maxPublishedDateOfFeed.before(publishedDate)) {
			max = publishedDate;
		} else {
			max = maxPublishedDateOfFeed;
		}
		System.out.println(max.toString());
		return max;
	}

	public static boolean dateIsAfterMaxDate(Date maxDate, Date pubDate) {
		return maxDate.before(pubDate);
	}
}
