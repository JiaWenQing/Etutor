package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Version {

	private String version;
	private String url;
	private String description = "gengciin";

	public void initWithAttributes(JSONObject json) {
		try {
			url = json.getString("url");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			description = json.getString("description");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			version = json.getString("version");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
