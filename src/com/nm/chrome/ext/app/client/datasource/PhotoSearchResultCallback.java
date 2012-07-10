package com.nm.chrome.ext.app.client.datasource;

import com.google.gwt.core.client.Callback;
import com.nm.chrome.ext.app.client.model.IMainPresenter;
import com.nm.chrome.ext.app.client.model.PhotosSearchResult;

public class PhotoSearchResultCallback implements Callback<PhotosSearchResult, Void> {

	private final IMainPresenter presenter ;
	private PhotosSearchResult lastResult;
	
	
	/**
	 * C'tor
	 * @param presenter
	 */
	public PhotoSearchResultCallback(IMainPresenter presenter) {
		this.presenter 	= presenter;
		this.lastResult = null ;
	}

	public PhotosSearchResult getLastResult() {
		return lastResult;
	}

	@Override
	public void onFailure(Void reason) {}

	@Override
	public void onSuccess(PhotosSearchResult result) {
		this.lastResult = result ;
		presenter.addPhotos(result) ;
	}
}
