package com.nm.chrome.ext.app.client.bl;

import com.nm.chrome.ext.app.client.datasource.FlickrDS;
import com.nm.chrome.ext.app.client.datasource.GoogleImagesDS;
import com.nm.chrome.ext.app.client.datasource.IImagesDataSource;
import com.nm.chrome.ext.app.client.datasource.PhotoSearchResultCallback;
import com.nm.chrome.ext.app.client.model.FilterLevel;
import com.nm.chrome.ext.app.client.model.IMainPresenter;
import com.nm.chrome.ext.app.client.model.bl.ISearchBL;

/**
 * This class represents the Search business logic of the app.
 * @author nir
 */
public class SearchBL implements ISearchBL {
	
	private IImagesDataSource googleDS					= new GoogleImagesDS() ;
	private IImagesDataSource flickrDS					= new FlickrDS() ;

	// Create callbacks to process images!
	private PhotoSearchResultCallback flickrCallback 	= null ;
	private PhotoSearchResultCallback googleCallback 	= null ;

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
		flickrCallback = new PhotoSearchResultCallback(mainPresenter) ;
		googleCallback = new PhotoSearchResultCallback(mainPresenter) ;
		// Go get the images!
		googleDS.getImages(phrase, filter, googleCallback);
		flickrDS.getImages(phrase, filter, flickrCallback) ;
	}

	/**
	 * @see ISearchBL#searchMore(String)
	 */
	public void searchMore() {
		String phrase 		= googleCallback.getLastResult().getSearch() ;
		FilterLevel filter 	= googleCallback.getLastResult().getFilter() ;
		googleDS.getImages(phrase, filter, googleCallback, googleCallback.getLastResult().getPage()) ;
	}
	
}
