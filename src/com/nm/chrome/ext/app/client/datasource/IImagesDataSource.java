package com.nm.chrome.ext.app.client.datasource;

import com.google.gwt.core.client.Callback;
import com.nm.chrome.ext.app.client.model.FilterLevel;
import com.nm.chrome.ext.app.client.model.PhotosSearchResult;

/**
 * An Images data source interface.
 * @author nir
 */
public interface IImagesDataSource {

	/**
	 * @param searchExpression
	 * @param filter
	 * @return a list of photos for a given search expression.
	 */
	void getImages(String searchExpression, FilterLevel filter, Callback<PhotosSearchResult, Void> callback) ;
	
	void getImages(String searchExpression, FilterLevel filter, Callback<PhotosSearchResult, Void> callback, int fromPage) ;
}
