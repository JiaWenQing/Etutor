package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxSubject entity. @author MyEclipse Persistence Tools
 */
public class DxLectureArea implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7886667596634105830L;
	// Fields
	private String province;
	private String city;
	private String region;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			province = json.getString("province");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			city = json.getString("city");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			region = json.getString("region");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxLectureArea() {
	}

	// Property accessors
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}