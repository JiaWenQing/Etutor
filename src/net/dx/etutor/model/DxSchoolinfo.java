package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxSchoolinfo entity. @author MyEclipse Persistence Tools
 */
public class DxSchoolinfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6574250619586422346L;
	// Fields

	private Integer id;
	private String name;
	private String address;
	// private String province;
	// private String city;
	// private String region;
	private String introduce;
	private String imageUrl;
	private String type;
	private String category;
	private String createTime;
	private String phoneNumber;
	private String webSite;
	private String characteristic;// 特色
	private String range;// 范围

	private double latitude;
	private double longitude;
	private double distance;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			range = json.getString("range");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			characteristic = json.getString("characteristic");
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
			createTime = json.getString("createTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			category = json.getString("category");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			type = json.getString("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			imageUrl = json.getString("imageUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			introduce = json.getString("introduce");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// region = json.getString("region");
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// city = json.getString("city");
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// province = json.getString("province");
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			latitude = json.getDouble("latitude");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			longitude = json.getDouble("longitude");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			distance = json.getDouble("distance");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			address = json.getString("address");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			name = json.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Constructors

	/** default constructor */
	public DxSchoolinfo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// public String getProvince() {
	// return province;
	// }
	//
	// public void setProvince(String province) {
	// this.province = province;
	// }
	//
	// public String getCity() {
	// return city;
	// }
	//
	// public void setCity(String city) {
	// this.city = city;
	// }
	//
	// public String getRegion() {
	// return region;
	// }
	//
	// public void setRegion(String region) {
	// this.region = region;
	// }

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	

}