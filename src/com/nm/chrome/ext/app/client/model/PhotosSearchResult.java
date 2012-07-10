package com.nm.chrome.ext.app.client.model;

import java.util.LinkedList;

/**
 * A result of photos from a search.
 * @author nir
 */
public class PhotosSearchResult {

	private final String search ;
	private final FilterLevel filter ;
	private final LinkedList<Photo> photos ;
	private final int page ;
	private final boolean isMore ;

	/**
	 * C'tor
	 * @param search the search expression.
	 * @param filter a filter level of the search
	 * @param photos the list of photos matching the search.
	 * @param page indicates the page number of the result. -1 for undefined.
	 * @param indicates if there is more to ask.
	 */
	public PhotosSearchResult(String search, FilterLevel filter, LinkedList<Photo> photos, int page, boolean isMore) {
		this.search = search;
		this.filter = filter;
		this.photos = photos;
		this.page	= page;
		this.isMore = isMore;
	}

	public String getSearch() {
		return search;
	}

	public LinkedList<Photo> getPhotos() {
		return photos;
	}

	public int getPage() {
		return page;
	}
	
	public boolean isMore() {
		return isMore;
	}
	
	public FilterLevel getFilter() {
		return filter;
	}

	@Override
	public String toString() {
		return "PhotosSearchResult [search=" + search + ", filter=" + filter
				+ ", photos=" + photos + ", page=" + page + ", isMore="
				+ isMore + "]";
	}
}
