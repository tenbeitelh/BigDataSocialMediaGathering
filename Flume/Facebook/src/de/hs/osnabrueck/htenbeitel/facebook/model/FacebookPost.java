package de.hs.osnabrueck.htenbeitel.facebook.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import facebook4j.Post;
import facebook4j.Tag;

public class FacebookPost {

	private String sourcePage;
	private String id;
	private String fromCategory;
	private Date createdTime;
	private String description;
	private String message;
	private List<String> messageTags = new ArrayList<String>();
	private int likes;

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromCategory() {
		return fromCategory;
	}

	public void setFromCategory(String from) {
		this.fromCategory = from;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public List<String> getMessageTags() {
		return messageTags;
	}

	public void setMessageTags(List<String> messageTags) {
		this.messageTags = messageTags;
	}
	
	public void addMesageTag(String tag){
		this.messageTags.add(tag);
	}

	public FacebookPost createFacebookPostObject(String pageId, Post post) {
		FacebookPost fPost = new FacebookPost();
		if (post != null) {
			fPost.setSourcePage(pageId);
			fPost.setId(post.getId());
			if (post.getFrom() != null) {
				fPost.setFromCategory(post.getFrom().getCategory());
			} else {
				fPost.setFromCategory(null);
			}
			fPost.setCreatedTime(post.getCreatedTime());
			fPost.setDescription(post.getDescription());
			fPost.setMessage(post.getMessage());
			List<Tag> tags;
			if ((tags = post.getMessageTags()) != null) {
				for (Tag tag : tags) {
					fPost.addMesageTag(tag.getName());
				}
			}

		}
		return fPost;
	}

}
