package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DxAgencyinfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2607844260724078483L;
	private Integer id;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private double distance;
//	private String province;
//	private String city;
//	private String region;
	private String introduce;
	private String imageUrl;
	private String createTime;
	private String webSite;
	private String phoneNumber;
	private String feature;
	private String types;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			types = json.getString("type");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			feature = json.getString("feature");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			webSite = json.getString("webSite");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			phoneNumber = json.getString("phoneNumber");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			id = json.getInt("id");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			name = json.getString("name");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			address = json.getString("address");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			latitude = json.getDouble("latitude");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			longitude = json.getDouble("longitude");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			distance = json.getDouble("distance");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
//		try {
//			province = json.getString("province");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		try {
//			city = json.getString("city");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//		try {
//			region = json.getString("region");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		try {
			introduce = json.getString("introduce");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			imageUrl = json.getString("imageUrl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			createTime = json.getString("createTime");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public DxAgencyinfo() {
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLocation(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	
//	public String getProvince() {
//		return this.province;
//	}
//
//	public void setProvince(String province) {
//		this.province = province;
//	}
//
//	public String getCity() {
//		return this.city;
//	}
//
//	public void setCity(String city) {
//		this.city = city;
//	}
//
//	public String getRegion() {
//		return this.region;
//	}
//
//	public void setRegion(String region) {
//		this.region = region;
//	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	
}