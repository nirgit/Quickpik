package com.nm.chrome.ext.app.client.model;

/**
 * This class represents a photo.
 * @author nir
 */
public class Photo {

	private String id ;
	private String thumbnail;
	private String fullURL ;
	
	/**
	 * C'tor
	 * @param id the photo's Identifier.
	 * @param thumbnail the thumbnail URL.
	 * @param fullURL the full photo URL.
	 */
	public Photo(String id, String thumbnail, String fullURL) {
		this.id			= id;
		this.thumbnail	= thumbnail ;
		this.fullURL 	= fullURL ;
	}

	public String getId() {
		return id;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public String getFullURL() {
		return fullURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullURL == null) ? 0 : fullURL.hashCode());
		result = prime * result
				+ ((thumbnail == null) ? 0 : thumbnail.hashCode());
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
		Photo other = (Photo) obj;
		if (fullURL == null) {
			if (other.fullURL != null)
				return false;
		} else if (!fullURL.equals(other.fullURL))
			return false;
		if (thumbnail == null) {
			if (other.thumbnail != null)
				return false;
		} else if (!thumbnail.equals(other.thumbnail))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Photo [id=" + id + ", fullURL=" + fullURL + ", thumbnail="
				+ thumbnail + "]";
	}
}
