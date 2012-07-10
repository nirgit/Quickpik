package com.nm.chrome.ext.app.client.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.nm.chrome.ext.app.client.model.FilterLevel;

/**
 * This class represents the Search Bar component in the view. 
 * @author nir
 */
public class SearchBar extends Composite {

	private final static String APPLICATION_TITLE	= "QuickPik";
	private final static String MORE_LABEL 			= "more...";
	
	private final static String SAFE_SEARCH_LABEL 	= "Safe Search:";
	private final static String SEARCH_LABEL 		= "Search";
	
	private final Image SEARCHING_IMG 				= new Image("ajax-loader.gif") ;
	
	private TextBox		searchBox ;
	private Button 		searchButton ;
	private Label 		searchExpression;
	private Label 		moreResults;
	private Label 		numberOfResultsMessage;
	private Label	 	filterLabel ; // content filter label - strict/moderate/off
	private ListBox 	filter ; // content filter listbox (dropdown) - strict/moderate/off

	/**
	 * C'tor
	 */
	SearchBar() {
		FlowPanel panel = createSearchBarPanel();
		// initialize the widget with the search bar panel.
		initWidget(panel) ;
	}

	/**
	 * @return a panel holding the search bar.
	 */
	private FlowPanel createSearchBarPanel() {
		FlowPanel searchInputPanel = createSearchInputPane();
		// Create the search status panel...
		FlowPanel searchStats = createSearchStatusPane();
		
		// Create the search panel.
		FlowPanel panel 		= new FlowPanel() ;
		panel.add(searchInputPanel) ;
		panel.add(searchStats) ;
		return panel;
	}
	
	/**
	 * @return a list box representing the safe search options.
	 */
	private ListBox createContentFilter() {
		ListBox filter = new ListBox();
		filter.addStyleName(CSS.FLOAT) ;
		for(FilterLevel level : FilterLevel.values())
			filter.addItem(level.getTitle()) ;
		filter.setVisibleItemCount(1) ;
		return filter ;
	}

	/**
	 * @return a panel for search status.
	 */
	private FlowPanel createSearchStatusPane() {
		FlowPanel searchStats	= new FlowPanel() ;
		searchStats.addStyleName(CSS.SEARCH_STATUS_PANEL) ;
		this.searchExpression	= new Label() ;
		this.searchExpression.addStyleName(CSS.SEARCH_EXPRESSION) ;
		this.searchExpression.addStyleName(CSS.FLOAT) ;
		this.moreResults = new Label(MORE_LABEL) ;
		this.moreResults.addStyleName(CSS.FLOAT) ;
		this.moreResults.addStyleName(CSS.MORE_RESULTS) ;
		this.numberOfResultsMessage = new Label() ;
		this.numberOfResultsMessage.addStyleName(CSS.NUMBER_OF_ITEMS) ;
		searchStats.add(this.searchExpression) ;
		searchStats.add(this.SEARCHING_IMG) ;
		searchStats.add(this.moreResults) ;
		searchStats.add(this.numberOfResultsMessage) ;
		this.SEARCHING_IMG.setVisible(false) ;
		return searchStats;
	}
	
	/**
	 * @return a search controls panel. 
	 */
	private FlowPanel createSearchInputPane() {
		// Create a security handle feature - Strict/Moderate/Off
		this.filterLabel			= new Label(SAFE_SEARCH_LABEL) ;
		this.filterLabel.addStyleName(CSS.FLOAT) ;
		this.filter 				= createContentFilter() ;
		// create the search box & button.
		this.searchBox				= new TextBox();
		this.searchBox.addStyleName(CSS.SEARCH_BOX) ;
		this.searchButton			= new Button(SEARCH_LABEL) ;
		this.searchButton.addStyleName(CSS.SEARCH_BUTTON) ;
		// Create the application's title.
		Label appTitle = new Label(APPLICATION_TITLE);
		appTitle.addStyleName(CSS.APP_TITLE) ;
		// Put everything together.
		FlowPanel searchInputPanel	= new FlowPanel() ;
		searchInputPanel.add(searchBox) ;
		searchInputPanel.add(searchButton) ;
		searchInputPanel.add(filterLabel) ;
		searchInputPanel.add(filter) ;
		searchInputPanel.add(appTitle) ;
		return searchInputPanel;
	}

	/**
	 * Sets the focus on the Search text box.
	 */
	void setFocus() {
		this.searchBox.setFocus(true) ;
	}
	
	/**
	 * @return the search button.
	 */
	Button getSearchButton() {
		return this.searchButton ;
	}

	/**
	 * @return the search text box.
	 */
	TextBox getSearchTextBox() {
		return this.searchBox ;
	}

	Label getMoreResults() {
		return this.moreResults ;
	}

	Label getNumberOfResultsMessage() {
		return this.numberOfResultsMessage;
	}


	Label getSearchExpression() {
		return this.searchExpression;
	}
	
	/**
	 * Sets the search image to be visible or hidden accordingly.
	 * @param isVisible
	 */
	void setSearchingImageVisible(boolean isVisible) {
		this.SEARCHING_IMG.setVisible(isVisible) ;
	}

	public String getFilterValue() {
		return filter.getItemText(filter.getSelectedIndex());
	}
}
