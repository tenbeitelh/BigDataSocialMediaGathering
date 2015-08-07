package de.hs.osnabrueck.htenbeitel.facebook;

import de.hs.osnabrueck.htenbeitel.facebook.model.FacebookPost;
import facebook4j.Post;

public interface StatusListener {
	public void onPost(FacebookPost post);
	public void onException(Exception exception);
}
