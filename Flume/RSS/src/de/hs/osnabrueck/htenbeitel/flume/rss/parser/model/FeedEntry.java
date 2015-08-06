package de.hs.osnabrueck.htenbeitel.flume.rss.parser.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;

public class FeedEntry {
	private String sourceFeed;
	private String feedTitle;
	private String feedLink;
	private String feedDescription;
	private String feedComments;
	private Date publishedDate;
	private List<String> feedCategories = new ArrayList<String>();
	private List<String> contents = new ArrayList<String>();

	private static Logger LOG = LoggerFactory.getLogger(FeedEntry.class);

	public String getSourceFeed() {
		return sourceFeed;
	}

	public void setSourceFeed(String sourceFeed) {
		this.sourceFeed = sourceFeed;
	}

	public String getFeedTitle() {
		return feedTitle;
	}

	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}

	public String getFeedLink() {
		return feedLink;
	}

	public void setFeedLink(String feedLink) {
		this.feedLink = feedLink;
	}

	public String getFeedDescription() {
		return feedDescription;
	}

	public void setFeedDescription(String feedDescription) {
		this.feedDescription = feedDescription;
	}

	public String getFeedComments() {
		return feedComments;
	}

	public void setFeedComments(String feedComments) {
		this.feedComments = feedComments;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public List<String> getFeedCategories() {
		return feedCategories;
	}

	public void setFeedCategories(List<String> feedCategories) {
		this.feedCategories = feedCategories;
	}

	public void addFeedCategory(String category) {
		this.feedCategories.add(category);
	}

	public List<String> getContents() {
		return contents;
	}

	public void setContents(List<String> contents) {
		this.contents = contents;
	}

	public void addContent(String content) {
		this.contents.add(content);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contents == null) ? 0 : contents.hashCode());
		result = prime * result
				+ ((feedCategories == null) ? 0 : feedCategories.hashCode());
		result = prime * result
				+ ((feedComments == null) ? 0 : feedComments.hashCode());
		result = prime * result
				+ ((feedDescription == null) ? 0 : feedDescription.hashCode());
		result = prime * result
				+ ((feedLink == null) ? 0 : feedLink.hashCode());
		result = prime * result
				+ ((feedTitle == null) ? 0 : feedTitle.hashCode());
		result = prime * result
				+ ((publishedDate == null) ? 0 : publishedDate.hashCode());
		result = prime * result
				+ ((sourceFeed == null) ? 0 : sourceFeed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedEntry other = (FeedEntry) obj;
		if (contents == null) {
			if (other.contents != null)
				return false;
		} else if (!contents.equals(other.contents))
			return false;
		if (feedCategories == null) {
			if (other.feedCategories != null)
				return false;
		} else if (!feedCategories.equals(other.feedCategories))
			return false;
		if (feedComments == null) {
			if (other.feedComments != null)
				return false;
		} else if (!feedComments.equals(other.feedComments))
			return false;
		if (feedDescription == null) {
			if (other.feedDescription != null)
				return false;
		} else if (!feedDescription.equals(other.feedDescription))
			return false;
		if (feedLink == null) {
			if (other.feedLink != null)
				return false;
		} else if (!feedLink.equals(other.feedLink))
			return false;
		if (feedTitle == null) {
			if (other.feedTitle != null)
				return false;
		} else if (!feedTitle.equals(other.feedTitle))
			return false;
		if (publishedDate == null) {
			if (other.publishedDate != null)
				return false;
		} else if (!publishedDate.equals(other.publishedDate))
			return false;
		if (sourceFeed == null) {
			if (other.sourceFeed != null)
				return false;
		} else if (!sourceFeed.equals(other.sourceFeed))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FeedEntry [sourceFeed=" + sourceFeed + ", feedTitle="
				+ feedTitle + ", feedLink=" + feedLink + ", feedDescription="
				+ feedDescription + ", feedComments=" + feedComments
				+ ", publishedDate=" + publishedDate + ", feedCategories="
				+ feedCategories + ", contents=" + contents + "]";
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public static FeedEntry buildCustomFeedEntry(String sourceFeed,
			SyndEntry entry) {
		// DateFormat readFormat = new SimpleDateFormat(
		// "EEE, d MMM yyyy HH:mm:ss Z", Locale.GERMAN);

		FeedEntry customEntry = new FeedEntry();
		customEntry.setSourceFeed(sourceFeed);
		customEntry.setFeedTitle(entry.getTitle());
		customEntry.setFeedLink(entry.getLink());
		customEntry.setFeedDescription(entry.getDescription().getValue());
		customEntry.setFeedComments(entry.getComments());

		customEntry.setPublishedDate(entry.getPublishedDate());

		for (SyndCategory cat : entry.getCategories()) {
			customEntry.addFeedCategory(cat.getName());
		}
		for (SyndContent cont : entry.getContents()) {
			customEntry.addContent(cont.getValue());
		}
		return customEntry;

	}

}
