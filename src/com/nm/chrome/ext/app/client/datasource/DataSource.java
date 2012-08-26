package com.nm.chrome.ext.app.client.datasource;

import com.nm.chrome.ext.app.client.model.IMainPresenter;

/**
 * DataSource enumeration of various available data sources to search.
 * @author Nir Moav
 */
public enum DataSource {

	// add here your data sources
	FLICKR(true, new FlickrDS()),
	;

	private final boolean isEnabled ;
	private final IImagesDataSource source ;
	
	/**
	 * C'tor
	 * @param source the actual source to run a search on.
	 * @param isEnabled true iff the data source is enabled. (Whether you should use it).
	 */
	private DataSource(boolean isEnabled, IImagesDataSource source) {
		this.isEnabled 	= isEnabled;
		this.source 	= source;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public IImagesDataSource getSource() {
		return source;
	}
	
	public PhotoSearchResultCallback createSearchCallback(IMainPresenter presenter) {
		return new PhotoSearchResultCallback(presenter) ;
	}
	
}
