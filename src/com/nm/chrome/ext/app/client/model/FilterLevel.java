package com.nm.chrome.ext.app.client.model;

/**
 * Represents the filtering levels of the images content.
 * @author nir
 */
public enum FilterLevel {
	
	STRICT("Strict", 2),
	MODERATE("Moderate", 1),
	OFF("Off", 0),
	;
	
	private final String title ;
	private final int level ;
	
	/**
	 * C'tor
	 * @param title
	 * @param level
	 */
	private FilterLevel(String title, int level) {
		this.title = title;
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public int getLevel() {
		return level;
	}
}
