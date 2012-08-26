package com.nm.chrome.ext.app.client.view;

import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

/**
 * The results pane UI component - where thumbnails & full image go.
 * @author nir
 */
public class ResultsPane extends Composite implements HasAllKeyHandlers {

	private Panel thumbnailsPanel ;
	private Panel pictureHook ;
	private Image fullImage	;
	
	private FocusPanel focusPanel;
	
	/**
	 * C'tor
	 */
	public ResultsPane() {
		Panel resultsPanel = createImagesPanel();
		initWidget(resultsPanel) ;
	}

	/**
	 * @return a panel for holding thumbnails & full picture.
	 */
	private Panel createImagesPanel() {
		FlowPanel resultsPanel	= new FlowPanel() ;
		createThumbsPanel();
		// Create a panel for showing full picture.
		createFullPicturePanel();

		resultsPanel.add(thumbnailsPanel) ;
		resultsPanel.add(pictureHook) ;
		
		focusPanel = new FocusPanel();
		focusPanel.setWidget(resultsPanel) ;
		return focusPanel;
	}

	/**
	 * Creates a panel for holding a full sized picture.
	 */
	private void createFullPicturePanel() {
		this.fullImage			= new Image() ;
		this.pictureHook 		= new FlowPanel() ;
		this.pictureHook.add(fullImage) ;
		this.pictureHook.addStyleName(CSS.FULL_PHOTO) ;
	}

	/**
	 * creates a panel for holding thumbnails.
	 */
	private void createThumbsPanel() {
		this.thumbnailsPanel		= new FlowPanel() ;
		this.thumbnailsPanel.addStyleName(CSS.THUMBNAILS) ;
	}

	/**
	 * @return the full picture's hook.
	 */
	Panel getPictureHook() {
		return this.pictureHook ;
	}

	/**
	 * @return the panel holding the thumbnails. 
	 */
	Panel getThumbnailsPanel() {
		return this.thumbnailsPanel ;
	}

	/**
	 * @return the full image.
	 */
	Image getFullImage() {
		return this.fullImage ;
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return focusPanel.addKeyUpHandler(handler) ;
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return focusPanel.addKeyDownHandler(handler) ;
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return focusPanel.addKeyPressHandler(handler) ;
	}

	public void focus() {
		this.focusPanel.setFocus(true) ;
	}
}
