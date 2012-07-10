package com.nm.chrome.ext.app.client.model.bl;

import com.nm.chrome.ext.app.client.model.FilterLevel;

/**
 * An interface for a Search BL. 
 * @author nir
 */
public interface ISearchBL {

	/**
	 * Start new search images that match a given phrase.
	 * @param phrase a phrase to search images for.
	 * @param level a filter level for the search.
	 */
	public void search(String phrase, FilterLevel level) ;

	/**
	 * Continue previous search with given phrase.
	 */
	public void searchMore();
}
