package com.nm.chrome.ext.app.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

/**
 * The results pane UI component - where thumbnails & full image go.
 * @author nir
 */
public class ResultsPane extends Composite {

	private Panel thumbnailsPanel ;
	private Panel pictureHook ;
	private Image fullImage	;
	
	/**
	 * C'tor
	 */
	public ResultsPane() {
		FlowPanel resultsPanel = createImagesPanel();
		initWidget(resultsPanel) ;
	}

	/**
	 * @return a panel for holding thumbnails & full picture.
	 */
	private FlowPanel createImagesPanel() {
		FlowPanel resultsPanel	= new FlowPanel() ;
		createThumbsPanel();
		// Create a panel for showing full picture.
		createFullPicturePanel();

		resultsPanel.add(thumbnailsPanel) ;
		resultsPanel.add(pictureHook) ;
		return resultsPanel;
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
		thumbnailsPanel.addStyleName(CSS.THUMBNAILS) ;
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
}
