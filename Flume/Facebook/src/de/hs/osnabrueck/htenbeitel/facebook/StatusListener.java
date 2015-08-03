package de.hs.osnabrueck.htenbeitel.facebook;

import facebook4j.Post;

public interface StatusListener {
	public void onPost(Post post);
	public void onException(Exception exception);
}
