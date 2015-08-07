package de.hs.osnabrueck.htenbeitel.facebook;

import java.util.ArrayList;
import java.util.List;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.Configuration;

public class FacebookObserver {
	Facebook facebook;
	List<StatusListener> listener = new ArrayList<StatusListener>();
	private String[] pages;
	
	public FacebookObserver(Configuration conf, String[] pages){
		this.facebook = new FacebookFactory(conf).getInstance();
		this.pages = pages;
	}
	
	
	
	public void addListener(StatusListener listener){
		this.listener.add(listener);
	}

}
