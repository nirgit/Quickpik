package com.nm.chrome.ext.app.client.bl;

import java.util.HashMap;

import com.nm.chrome.ext.app.client.datasource.DataSource;
import com.nm.chrome.ext.app.client.datasource.PhotoSearchResultCallback;
import com.nm.chrome.ext.app.client.model.FilterLevel;
import com.nm.chrome.ext.app.client.model.IMainPresenter;
import com.nm.chrome.ext.app.client.model.bl.ISearchBL;

/**
 * This class represents the Search business logic of the app.
 * @author nir
 */
public class SearchBL implements ISearchBL {

	private HashMap<DataSource, PhotoSearchResultCallback> callbacks = new HashMap<DataSource, PhotoSearchResultCallback>() ;
	
	private final IMainPresenter mainPresenter ;
	
	/**
	 * C'tor
	 * @param mainPresenter
	 */
	public SearchBL(IMainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

	/**
	 * @see ISearchBL#search(String, com.nm.chrome.ext.app.client.model.FilterLevel)
	 */
	public void search(String phrase, FilterLevel filter) {
		// remove old callbacks...
		callbacks.clear() ;
		for(DataSource ds : DataSource.values()) {
			if(ds.isEnabled()){
				PhotoSearchResultCallback callback = ds.createSearchCallback(mainPresenter) ;
				callbacks.put(ds, callback) ;
				// Go get the images!
				ds.getSource().getImages(phrase, filter, callback) ;
			}
		}
	}

	/**
	 * @see ISearchBL#searchMore(String)
	 */
	public void searchMore() {
		for(DataSource dataSource : callbacks.keySet()) {
			PhotoSearchResultCallback callback = callbacks.get(dataSource) ;
			if(callback == null) continue ;
			String phrase 		= callback.getLastResult().getSearch() ;
			FilterLevel filter 	= callback.getLastResult().getFilter() ;
			dataSource.getSource().getImages(phrase, filter, callback, callback.getLastResult().getPage()+1) ;
		}
	}
	
}
