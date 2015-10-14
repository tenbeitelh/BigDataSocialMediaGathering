package de.hs.osnabrueck.htenbeitel.facebook;

import de.hs.osnabrueck.htenbeitel.facebook.model.FacebookPost;


public interface StatusListener {
	public void onPost(FacebookPost post);
	public void onException(Exception exception);
}
