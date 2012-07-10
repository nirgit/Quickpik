package com.nm.chrome.ext.app.client.datasource.model;

import java.util.HashSet;

import com.google.gwt.core.client.Callback;
import com.nm.chrome.ext.app.client.model.Photo;
import com.nm.chrome.ext.app.client.model.PhotosSearchResult;

/**
 * This class represents a Mid Result for a search. 
 * @author nir
 */
public class MidResult {
	
	private final Callback<PhotosSearchResult, Void> callback ;
	private HashSet<Photo> photos = new HashSet<Photo>() ;
	private int page = 0 ;
	
	/**
	 * C'tor
	 * @param callback
	 */
	public MidResult(Callback<PhotosSearchResult, Void> callback) {
		this.callback = callback ;
	}

	public Callback<PhotosSearchResult, Void> getCallback() {
		return callback;
	}

	public HashSet<Photo> getPhotos() {
		return photos;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}