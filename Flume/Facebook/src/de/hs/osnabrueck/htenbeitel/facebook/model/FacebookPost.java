package de.hs.osnabrueck.htenbeitel.facebook.model;

import java.util.Date;

import facebook4j.Post;

public class FacebookPost {

	private Date createdTime;

	public FacebookPost createFacebookPostObject(Post post) {
		FacebookPost fPost = new FacebookPost();
		fPost.setCreatedTime(post.getCreatedTime());
		return fPost;
	}

	private void setCreatedTime(Date createdTime) {
		// TODO Auto-generated method stub
		this.createdTime = createdTime;
	}
}
