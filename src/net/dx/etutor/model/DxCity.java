package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxCityId entity. @author MyEclipse Persistence Tools
 */
public class DxCity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 355975134900863581L;
	// Fields

	private String cityCode;
	private String cityName;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			cityCode = json.getString("cityCode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			cityName = json.getString("cityName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxCity() {
	}

	/** full constructor */
	public DxCity(String cityCode, String cityName) {
		this.cityCode = cityCode;
		this.cityName = cityName;
	}

	// Property accessors

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DxCity))
			return false;
		DxCity castOther = (DxCity) other;

		return ((this.getCityCode() == castOther.getCityCode()) || (this
				.getCityCode() != null && castOther.getCityCode() != null && this
				.getCityCode().equals(castOther.getCityCode())))
				&& ((this.getCityName() == castOther.getCityName()) || (this
						.getCityName() != null
						&& castOther.getCityName() != null && this
						.getCityName().equals(castOther.getCityName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCityCode() == null ? 0 : this.getCityCode().hashCode());
		result = 37 * result
				+ (getCityName() == null ? 0 : this.getCityName().hashCode());
		return result;
	}

}