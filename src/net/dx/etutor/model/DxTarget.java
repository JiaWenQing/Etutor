package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DxTarget implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2155188561773841193L;
	private String avatarUrl;
	private String subjectItemName;
	private String needTitle;
	private String teachTimes;
	private String price;
	private String name;
	private String publishTime;
	private String tradeNumber;

	/* private byte[] avatarData; */

	public void initWithAttributes(JSONObject json) {

		try {
			tradeNumber = json.getString("tradeNumber");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			publishTime = json.getString("publishTime");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			avatarUrl = json.getString("avatarUrl");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			subjectItemName = json.getString("subjectItemName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			needTitle = json.getString("needTitle");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			teachTimes = json.getString("teachTimes");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			price = json.getString("price");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getSubjectItemName() {
		return subjectItemName;
	}

	public void setSubjectItemName(String subjectItemName) {
		this.subjectItemName = subjectItemName;
	}

	public String getNeedTitle() {
		return needTitle;
	}

	public void setNeedTitle(String needTitle) {
		this.needTitle = needTitle;
	}

	public String getTeachTimes() {
		return teachTimes;
	}

	public void setTeachTimes(String teachTimes) {
		this.teachTimes = teachTimes;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(String tradeNumber) {
		this.tradeNumber = tradeNumber;
	}

	/*
	 * public byte[] getAvatarData() { return avatarData; }
	 * 
	 * public void setAvatarData(byte[] avatarData) { this.avatarData =
	 * avatarData; }
	 */

}
