package com.nm.chrome.ext.app.client.view;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nm.chrome.ext.app.client.bl.SearchBL;
import com.nm.chrome.ext.app.client.model.FilterLevel;
import com.nm.chrome.ext.app.client.model.IMainPresenter;
import com.nm.chrome.ext.app.client.model.Photo;
import com.nm.chrome.ext.app.client.model.PhotosSearchResult;
import com.nm.chrome.ext.app.client.model.bl.ISearchBL;

/**
 * Entry point class QuickPik
 */
public class QuickPik implements IMainPresenter, EntryPoint {

	// Labels & Constants
	private final static String SEARCH_FOR			= "Searching for: ";
	private final static String RESULTS_FOR 		= "Results for: ";
	private final static String NUMBER_OF_RESULTS	= "Number of items: " ;

	private final static String SEARCH_HTML_EL		= "search" ;
	private final static String RESULTS_HTML_EL		= "r" ;
	
	// BL
	private ISearchBL searchBL ;
	
	// View components.
	private SearchBar searchBar ;
	private ResultsPane resultsPane ;
	
	private ArrayList<Thumbnail> thumbnails	= new ArrayList<Thumbnail>() ;
	private int numberOfResults 			= 0;
	private Widget lastSelectedThumbnail	= null ;
	private int currentThumbIndex 			= 0 ;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		this.searchBL = new SearchBL(this) ;
		// adding content
		addContent() ;
		// add binding to the actio
		bind() ;
		// init the state of the app
		init() ;
	}

	/**
	 * Bind the actions.
	 */
	private void bind() {
		this.searchBar.getSearchButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				newSearch();
			}
			
		}) ;
		this.searchBar.getSearchTextBox().addKeyPressHandler(new KeyPressHandler(){
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getUnicodeCharCode() == KeyCodes.KEY_ENTER) {
					newSearch();
				}
			}

		}) ;
		this.searchBar.getMoreResults().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchBar.setSearchingImageVisible(true) ;
				searchBL.searchMore() ;
			}
		}) ;
		this.resultsPane.addKeyUpHandler(new KeyUpHandler(){

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event == null) return ;
				if(event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
					clearSelection(currentThumbIndex) ;
					currentThumbIndex++ ;
					currentThumbIndex = Math.min(currentThumbIndex, thumbnails.size()-1) ;
					selectThumb(currentThumbIndex);
				} else if(event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
					clearSelection(currentThumbIndex) ;
					currentThumbIndex--;
					currentThumbIndex = Math.max(currentThumbIndex, 0) ;
					selectThumb(currentThumbIndex);
				}
			}
		});
	}
	
	private void selectThumb(int index) {
		if(index < 0 || index >= thumbnails.size()) return ;
		Element thumbElement = getThumbnail(index);
		if(thumbElement == null) return ;
		thumbElement.addClassName(CSS.SELECTED_PHOTO) ;
		Thumbnail thumbnail = thumbnails.get(index);
		String photoUrl 	= thumbnail.getPhotoUrl();
		resultsPane.getPictureHook().setVisible(true) ;
		resultsPane.getFullImage().setUrl(photoUrl) ;
		resultsPane.focus() ;
	}

	private void clearSelection(int index) {
		if(index < 0 || index >= thumbnails.size()) return ;
		Element thumbElement = getThumbnail(index);
		if(thumbElement != null)
			thumbElement.removeClassName(CSS.SELECTED_PHOTO) ;
	}

	private Element getThumbnail(int index) {
		Element thumbnailsPanelElement = resultsPane.getThumbnailsPanel().getElement();
		if(index < 0 || index >= thumbnailsPanelElement.getChildCount()) return null;
		Node thumb 				= thumbnailsPanelElement.getChild(index) ;
		Element thumbElement	= Element.as(thumb) ;
		return thumbElement;
	}
	
	/**
	 * Execute a new search!
	 */
	private void newSearch() {
		// clear previous results and state.
		reset() ;
		String phrase 		= searchBar.getSearchTextBox().getText() ;
		FilterLevel level 	= FilterLevel.valueOf(searchBar.getFilterValue().toUpperCase()) ;
		updateViewForNewSearch(phrase);
		searchBL.search(phrase, level) ;
	}

	/**
	 * Updates the view for a new search.
	 * @param phrase 
	 */
	private void updateViewForNewSearch(String phrase) {
		this.clearPanels() ;
		this.searchBar.getSearchExpression().setText(SEARCH_FOR + phrase) ;
		this.searchBar.setSearchingImageVisible(true) ;
		this.resultsPane.getThumbnailsPanel().setVisible(true) ;
	}

	/**
	 * Clears the results panels.
	 */
	private void clearPanels() {
		this.resultsPane.getThumbnailsPanel().clear() ;
		this.resultsPane.getFullImage().setUrl("") ;
		this.numberOfResults = 0 ;
		updateNumberOfItems();
	}

	private void updateNumberOfItems() {
		this.searchBar.getNumberOfResultsMessage().setText(NUMBER_OF_RESULTS + this.numberOfResults);
	}
	
	/**
	 * @see IMainPresenter#addPhotos(PhotosSearchResult)
	 */
	@Override
	public void addPhotos(PhotosSearchResult result) {
		addPhotosToPanel(result.getPhotos(), result.getPage()==1) ;
		searchBar.getSearchExpression().setText(RESULTS_FOR + result.getSearch()) ;
	}

	/**
	 * Adds given photos to the panel.
	 * @param photos
	 */
	private void addPhotosToPanel(final LinkedList<Photo> photos, final boolean isFirstPage) {
		Scheduler.get().scheduleIncremental(new RepeatingCommand() {

			@Override
			public boolean execute() {
				if(photos.isEmpty()) {
					searchBar.setSearchingImageVisible(false) ;
					searchBar.getMoreResults().setVisible(true) ;
					// select the first thumb by default
					if(isFirstPage) {
						selectThumb(0) ;
					}
					resultsPane.focus() ;
					return false ;
				} else {
					Photo photo 				= photos.remove() ;
					final String photoURL 		= photo.getFullURL() ;
					final String thumbURL 		= photo.getThumbnail() ;
					final Image thumbnailImg 	= new Image(thumbURL) ;
					thumbnailImg.addStyleName(CSS.THUMB_IMG) ;
					final Thumbnail thumbnail	= new Thumbnail(thumbnailImg, photoURL) ;
					
					thumbnailImg.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							clearSelection(currentThumbIndex) ;
							currentThumbIndex = thumbnails.indexOf(thumbnail);
							decorateSelectThumbnail(thumbnail) ;
							resultsPane.getPictureHook().setVisible(true) ;
							resultsPane.getFullImage().setUrl(photoURL) ;
						}
					}) ;
					
					thumbnails.add(thumbnail) ;
					resultsPane.getThumbnailsPanel().add(thumbnail) ;
					numberOfResults++ ;
					// update count
					updateNumberOfItems() ;
					return true ;
				}
			}
		}) ;
	}

	/**
	 * Decorates the selected thumbnail.
	 * @param thumb
	 */
	private void decorateSelectThumbnail(Widget thumb) {
		if(thumb == null) return ;
		if(lastSelectedThumbnail != null)
			this.lastSelectedThumbnail.removeStyleName(CSS.SELECTED_PHOTO) ;
		this.lastSelectedThumbnail = thumb ;
		this.lastSelectedThumbnail.addStyleName(CSS.SELECTED_PHOTO) ;
	}

	/**
	 * Adds the contents of the panel.
	 */
	private void addContent() {
		this.searchBar 			= new SearchBar() ;
		this.resultsPane		= new ResultsPane() ;
		// Use RootPanel.get() to get the entire body element
		RootPanel.get(SEARCH_HTML_EL).add(this.searchBar);
		// Add the nameField and sendButton to the RootPanel
		RootPanel.get(RESULTS_HTML_EL).add(resultsPane);
	}

	private void init() {
		resultsPane.getPictureHook().setVisible(false) ;
		resultsPane.getThumbnailsPanel().setVisible(false) ;
		searchBar.getMoreResults().setVisible(false) ;
		updateNumberOfItems();
		int height = Window.getClientHeight() - 200;
		resultsPane.getThumbnailsPanel().getElement().getStyle().setProperty("height", height, Unit.PX) ;
		this.searchBar.setFocus() ;
	}
	
	/**
	 * Reset the entire status of the app view.
	 */
	private void reset() {
		this.currentThumbIndex = 0 ;
		this.lastSelectedThumbnail = null ;
		this.thumbnails.clear() ;
		this.resultsPane.getPictureHook().setVisible(false) ;
	}
}
