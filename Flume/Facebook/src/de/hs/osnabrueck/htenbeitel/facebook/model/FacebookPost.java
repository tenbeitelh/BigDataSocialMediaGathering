package de.hs.osnabrueck.htenbeitel.facebook.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import facebook4j.Comment;
import facebook4j.PagableList;
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
	private Integer likes;
	private List<FacebookComment> comments = new ArrayList<FacebookComment>();

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

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public List<String> getMessageTags() {
		return messageTags;
	}

	public void setMessageTags(List<String> messageTags) {
		this.messageTags = messageTags;
	}

	public void addMesageTag(String tag) {
		this.messageTags.add(tag);
	}

	public List<FacebookComment> getComments() {
		return comments;
	}

	public void setComments(List<FacebookComment> comments) {
		this.comments = comments;
	}

	public void addComment(FacebookComment comment) {
		this.comments.add(comment);
	}

	public static FacebookPost createFacebookPostObject(String pageId, Post post) {
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

			List<Tag> tags = post.getMessageTags();
			if (tags != null) {
				for (Tag tag : tags) {
					fPost.addMesageTag(tag.getName());
				}
			}

			fPost.setLikes(post.getLikes().size());

			PagableList<Comment> comments = post.getComments();
			if (comments != null) {
				for (Comment comment : comments) {
					FacebookComment tmpComment = new FacebookComment();
					tmpComment.setId(comment.getId());
					tmpComment.setFrom(comment.getFrom().getName());
					tmpComment.setCreatedTime(comment.getCreatedTime());
					tmpComment.setMessage(comment.getMessage());
					tmpComment.setLikeCount(comment.getLikeCount());
					fPost.addComment(tmpComment);
				}
			}

		}
		return fPost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((fromCategory == null) ? 0 : fromCategory.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((likes == null) ? 0 : likes.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((messageTags == null) ? 0 : messageTags.hashCode());
		result = prime * result
				+ ((sourcePage == null) ? 0 : sourcePage.hashCode());
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
		FacebookPost other = (FacebookPost) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (createdTime == null) {
			if (other.createdTime != null)
				return false;
		} else if (!createdTime.equals(other.createdTime))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fromCategory == null) {
			if (other.fromCategory != null)
				return false;
		} else if (!fromCategory.equals(other.fromCategory))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (likes == null) {
			if (other.likes != null)
				return false;
		} else if (!likes.equals(other.likes))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (messageTags == null) {
			if (other.messageTags != null)
				return false;
		} else if (!messageTags.equals(other.messageTags))
			return false;
		if (sourcePage == null) {
			if (other.sourcePage != null)
				return false;
		} else if (!sourcePage.equals(other.sourcePage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FacebookPost [sourcePage=" + sourcePage + ", id=" + id
				+ ", fromCategory=" + fromCategory + ", createdTime="
				+ createdTime + ", description=" + description + ", message="
				+ message + ", messageTags=" + messageTags + ", likes=" + likes
				+ ", comments=" + comments + "]";
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

}
