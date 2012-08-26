package com.nm.chrome.ext.app.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Represents a Thumbnail
 * @author Nir Moav
 *
 */
public class Thumbnail extends Composite {

	private FlowPanel thumbContainer ;
	private String photoUrl;
	
	/**
	 * C'tor
	 * @param photoURL 
	 */
	public Thumbnail(Image img, String photoURL) {
		this.photoUrl 	= photoURL ;
		thumbContainer 	= new FlowPanel() ;
		thumbContainer.add(img) ;
		initWidget(thumbContainer) ;
		addStyleName(CSS.THUMB) ;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((photoUrl == null) ? 0 : photoUrl.hashCode());
		result = prime * result
				+ ((thumbContainer == null) ? 0 : thumbContainer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Thumbnail other = (Thumbnail) obj;
		if (photoUrl == null) {
			if (other.photoUrl != null)
				return false;
		} else if (!photoUrl.equals(other.photoUrl))
			return false;
		if (thumbContainer == null) {
			if (other.thumbContainer != null)
				return false;
		} else if (!thumbContainer.equals(other.thumbContainer))
			return false;
		return true;
	}
}
