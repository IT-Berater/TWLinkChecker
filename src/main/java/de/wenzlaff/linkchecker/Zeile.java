package de.wenzlaff.linkchecker;

/**
 * Eine Zeile in der Excel Tabelle.
 * 
 * @author Thomas Wenzlaff
 *
 */
public class Zeile {

	private String id;

	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Zeile [id=" + id + ", " + url + "]";
	}
}