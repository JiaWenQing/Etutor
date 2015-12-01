package net.dx.etutor.model;

import java.util.List;

import net.dx.etutor.util.PictureUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DxStudentNeed implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7305948620419117706L;

	private String publishTime;
	private List<DxTarget> targets;

	public void initWithAttributes(JSONObject json) {

		try {
			publishTime = json.getString("publishTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			targets.clear();
			JSONArray jsonArray = json.getJSONArray("target");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				DxTarget t = new DxTarget();
				t.initWithAttributes(jsonObject);
				// String imageUrl=t.getAvatarUrl();
				targets.add(t);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public List<DxTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<DxTarget> targets) {
		this.targets = targets;
	}

}
