package com.nm.chrome.ext.app.client.datasource;

import java.util.LinkedList;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nm.chrome.ext.app.client.datasource.model.MidResult;
import com.nm.chrome.ext.app.client.model.FilterLevel;
import com.nm.chrome.ext.app.client.model.Photo;
import com.nm.chrome.ext.app.client.model.PhotosSearchResult;

/**
 * A Google-Images Data source.
 * @author nir
 */
public class GoogleImagesDS implements IImagesDataSource {

	private final String GOOGLE_IMAGES_JSON_SEARCH_URL	= "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" ;
	private final String SAFETY_PARAM					= "safe=" ;
	private final int	 MAX_IMAGES_PER_PAGE			= 8 ;
	private final String NUMBER_OF_IMAGES_PARAM			= "rsz=" + MAX_IMAGES_PER_PAGE ;
	private final String PAGE_NUMBER_PARAM				= "start=" ;
	private final String PARAM_SEP						= "&" ;

	// The minimal number of images to try and load per search ("request").
	private static final int MIN_NUMBER_OF_IMAGES		= 20 ;

	@Override
	public void getImages(String searchExpression, final FilterLevel filter, final Callback<PhotosSearchResult, Void> callback) {
		MidResult res = new MidResult(callback) ;
		getImages(searchExpression, filter, res, true) ;
	}

	@Override
	public void getImages(String searchExpression, final FilterLevel filter, Callback<PhotosSearchResult, Void> callback, int fromPage) {
		MidResult res = new MidResult(callback) ;
		// Set the page number !
		res.setPage(fromPage) ;
		getImages(searchExpression, filter, res, true) ;
	}

	/**
	 * Gets the images using Google API.
	 * @param searchExpression what to search for.
	 * @param filter the filtering level of the search. 
	 * @param midRes a mid result of the search.
	 * @param shouldContinue indicates whether to continue the search (to reach the minimal amount of images to return).
	 */
	private void getImages(final String searchExpression, final FilterLevel filter, final MidResult midRes, boolean shouldContinue) {
		if(midRes.getPhotos().size() < MIN_NUMBER_OF_IMAGES && shouldContinue) {
			String searchURL = constructURL(searchExpression, filter, midRes.getPage()) ;

			JsonpRequestBuilder jsonpBuilder = new JsonpRequestBuilder() ;
			AsyncCallback<JavaScriptObject> jsonCallback = new AsyncCallback<JavaScriptObject>() {

				@Override
				public void onFailure(Throwable caught) {
					PhotosSearchResult photoSearchResult = new PhotosSearchResult(searchExpression, filter, new LinkedList<Photo>(midRes.getPhotos()), midRes.getPage(), false);
					midRes.getCallback().onSuccess(photoSearchResult) ;
				}

				@Override
				public void onSuccess(JavaScriptObject result) {
					try{
						JSONObject jsonResult	= new JSONObject(result) ;
						JSONValue data			= jsonResult.get("responseData") ;
						JSONValue dataObject 	= JSONParser.parseLenient(data.toString()) ;
						JSONValue resultsJsonValue = ((JSONObject)dataObject.isObject()).get("results");
						JSONArray dataArray 	= (JSONArray) resultsJsonValue ;
						for(int i=0; i < dataArray.size(); i++) {
							JSONValue ithJsonValue = dataArray.get(i);
							JSONObject imageJSONObj = (JSONObject)ithJsonValue ;
							JSONString thumb = (JSONString) imageJSONObj.get("tbUrl") ;
							String thumbnailURL = thumb.stringValue() ;
							JSONString image = (JSONString) imageJSONObj.get("url") ;
							String imageURL = image.stringValue() ;
							midRes.getPhotos().add(new Photo("", thumbnailURL, imageURL)) ;
						}
						midRes.setPage(midRes.getPage() + 1) ;
						getImages(searchExpression, filter, midRes, dataArray.size() > 0) ;
					}catch(Exception e) {
						midRes.setPage(midRes.getPage() + 1) ;
						getImages(searchExpression, filter, midRes, false) ;
					}
				}
			} ;
			jsonpBuilder.requestObject(URL.encode(searchURL), jsonCallback) ;
		} else {
			PhotosSearchResult photoSearchResult = new PhotosSearchResult(searchExpression, filter, new LinkedList<Photo>(midRes.getPhotos()), midRes.getPage(), shouldContinue);
			midRes.getCallback().onSuccess(photoSearchResult) ;
		}
	}

	/**
	 * @param searchExpression what to search for.
	 * @param filter a filter level to set (safe/moderate/off).
	 * @param page the page number of the search.
	 * @return an API URL to use in order to get images.
	 */
	private String constructURL(String searchExpression, FilterLevel filter, int page) {
		return GOOGLE_IMAGES_JSON_SEARCH_URL + searchExpression + PARAM_SEP + 
				SAFETY_PARAM + filter.getTitle().toLowerCase() + PARAM_SEP + 
				NUMBER_OF_IMAGES_PARAM + PARAM_SEP +
				PAGE_NUMBER_PARAM + page ;
	}
}
