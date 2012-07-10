package com.nm.chrome.ext.app.client.datasource;

import java.util.LinkedList;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.nm.chrome.ext.app.client.datasource.model.MidResult;
import com.nm.chrome.ext.app.client.model.FilterLevel;
import com.nm.chrome.ext.app.client.model.Photo;
import com.nm.chrome.ext.app.client.model.PhotosSearchResult;

/**
 * Define FlickR as image data source.
 * @author nir
 */
public class FlickrDS implements IImagesDataSource {

	private final String FLICKR_REST_API	= "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=90485e931f687a9b9c2a66bf58a3861a&text=" ;
	private final String IS_SAFE_SEARCH		= "safe_search=" ; // 0=off, 1 = safe
	private final String CONTENT_TYPE		= "content_type=" + 1 ; // 1 = photos only
	private final String SORT				= "sort=relevance" ;
	private final String NUMBER_OF_RESULTS 	= "per_page=" + 20 ;
	private final String PAGE 				= "page=" ;
	private final String PARAM_SEP			= "&" ;
	
	/**
	 * @see IImagesDataSource#getImages(String, FilterLevel, Callback)
	 */
	@Override
	public void getImages(final String searchExpression, final FilterLevel filter, final Callback<PhotosSearchResult, Void> callback) {
		MidResult midRes = new MidResult(callback) ;
		getImages(searchExpression, filter, midRes) ;
	}
	
	/**
	 * @see IImagesDataSource#getImages(String, FilterLevel, Callback, int)
	 */
	@Override
	public void getImages(String searchExpression, FilterLevel filter, Callback<PhotosSearchResult, Void> callback, int fromPage) {
		MidResult midRes = new MidResult(callback) ;
		midRes.setPage(fromPage) ;
		getImages(searchExpression, filter, midRes) ;
	}

	/**
	 * The actual search!
	 * @param searchExpression
	 * @param filter
	 * @param midRes
	 */
	private void getImages(final String searchExpression, final FilterLevel filter, final MidResult midRes) {
		String url = constructAPIURL(searchExpression, filter, midRes.getPage());
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request,Response response) {
					LinkedList<Photo> photos = null ;
					if (200 == response.getStatusCode()) {
						String responseXMLText		= response.getText() ;
						photos	= getPhotos(responseXMLText) ;
					}
					midRes.getCallback().onSuccess(new PhotosSearchResult(searchExpression, filter, photos, 0, false)) ;
				}
				@Override
				public void onError(Request request, Throwable exception) {
					// TODO - Give the user an appropriate message something bad happened.
					System.out.println(exception) ;
				}
			}) ;
		} catch (RequestException e) {
			// TODO - Give the user an appropriate message something bad happened.
			System.out.println(e) ;
		}
	}

	private String constructAPIURL(final String searchExpression, FilterLevel filter, int pageNumber) {
		StringBuilder sb = new StringBuilder() ;
		sb.
			append(FLICKR_REST_API).append(searchExpression).append(PARAM_SEP).
			append(IS_SAFE_SEARCH).append(filter.getLevel()).append(PARAM_SEP).
			append(CONTENT_TYPE).append(PARAM_SEP).
			append(SORT).append(PARAM_SEP).
			append(NUMBER_OF_RESULTS).append(PARAM_SEP).
			append(PAGE).append(pageNumber) ;
		return sb.toString() ;
	}

	private LinkedList<Photo> getPhotos(String responseXMLText) {
		Document dom = XMLParser.parse(responseXMLText);
		NodeList elements = dom.getElementsByTagName("photo") ;
		LinkedList<Photo> photos = new LinkedList<Photo>() ;
		for(int i=0; i < elements.getLength(); i++){
			Node item = elements.item(i) ;
			Photo id = createPhoto(item);
			photos.add(id) ;
		}

		return photos ;
	}

	private Photo createPhoto(Node item) {
		// Get the JSON nodes.
		Node photoId = item.getAttributes().getNamedItem("id") ;
		Node farmId = item.getAttributes().getNamedItem("farm") ;
		Node server = item.getAttributes().getNamedItem("server") ;
		Node secret = item.getAttributes().getNamedItem("secret") ;
		// get the values.
		String id = photoId.getNodeValue();
		String farm = farmId.getNodeValue();
		String serverVal = server.getNodeValue();
		String secretVal = secret.getNodeValue();
		// construct the URLs
		String photoBase = "http://farm" + farm + ".static.flickr.com/" + serverVal + "/" + id +"_" + secretVal ;
		String thumbURL = photoBase + "_s.jpg" ;
		String photoURL = photoBase + ".jpg" ;
		return new Photo(id, thumbURL, photoURL) ;
	}
	
}
