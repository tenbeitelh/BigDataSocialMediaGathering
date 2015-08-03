package de.hs.osnabrueck.htenbeitel.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.Configuration;

public class FacebookObserver {
	Facebook facebook;
	StatusListener listener;
	
	public FacebookObserver(Configuration conf){
		this.facebook = new FacebookFactory(conf).getInstance();
	}
	
	public void addListener(StatusListener listener){
		this.listener = listener;
	}
}
