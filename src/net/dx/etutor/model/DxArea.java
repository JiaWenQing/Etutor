package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxAreaId entity. @author MyEclipse Persistence Tools
 */
public class DxArea implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5751077357362400775L;
	// Fields

	private String areaCode;
	private String cityCode;
	private String areaName;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			areaCode = json.getString("areaCode");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			cityCode = json.getString("cityCode");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			areaName = json.getString("areaName");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxArea() {
	}

	/** full constructor */
	public DxArea(String areaCode, String cityCode, String areaName) {
		this.areaCode = areaCode;
		this.cityCode = cityCode;
		this.areaName = areaName;
	}

	// Property accessors

	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}