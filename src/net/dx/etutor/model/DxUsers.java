package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxUsers entity. @author MyEclipse Persistence Tools
 */

public class DxUsers implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7515745926607494105L;
	private Integer userId;
	private String userName;
	private String password;
	private String telephone;
	private String address;
	private Double longitude;
	private Double latitude;
	private String status;
	private String sex;
	private String avatarUrl;
	private String userType; // 0学生，1老师
	private String certificatePhotoUrl;
	private String qqOpenId;
	private String sinaOpenId;
	private Integer level;
	private String token;

	private String school;
	private String graduateTime;
	private String education;
	private String major;
	private String teacherBirthday;
	private String coachTime;
	private Integer rank;
	private Integer newMsg;
	/**
	 * 标记：认证、核实、听课
	 */
	private Integer identify=0;
	private Integer age;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			identify = json.getInt("identify");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			coachTime = json.getString("coachTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			teacherBirthday = json.getString("teacherBirthday");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			graduateTime = json.getString("graduateTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			major = json.getString("major");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			education = json.getString("education");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			school = json.getString("school");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			token = json.getString("token");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			level = json.getInt("level");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rank = json.getInt("rank");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sinaOpenId = json.getString("sinaOpenId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			qqOpenId = json.getString("qqOpenId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			certificatePhotoUrl = json.getString("certificatePhotoUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			userType = json.getString("userType");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			avatarUrl = json.getString("avatarUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			status = json.getString("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sex = json.getString("sex");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			address = json.getString("address");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			telephone = json.getString("telephone");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			password = json.getString("password");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			userName = json.getString("userName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			userId = json.getInt("userId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			newMsg = json.getInt("newMsg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Constructors

	/** default constructor */
	public DxUsers() {
	}

	/** full constructor */
	public DxUsers(String userName, String password, String telephone,
			String address, Double longitude, Double latitude, String sex,
			String createTime, String status, String avatarUrl,
			String userType, String certificatePhotoUrl, String qqOpenId,
			String sinaOpenId) {
		this.userName = userName;
		this.password = password;
		this.telephone = telephone;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.status = status;
		this.sex = sex;
		this.avatarUrl = avatarUrl;
		this.userType = userType;
		this.certificatePhotoUrl = certificatePhotoUrl;
		this.qqOpenId = qqOpenId;
		this.sinaOpenId = sinaOpenId;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAvatarUrl() {
		return this.avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCertificatePhotoUrl() {
		return this.certificatePhotoUrl;
	}

	public void setCertificatePhotoUrl(String certificatePhotoUrl) {
		this.certificatePhotoUrl = certificatePhotoUrl;
	}

	public String getQqOpenId() {
		return this.qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getSinaOpenId() {
		return this.sinaOpenId;
	}

	public void setSinaOpenId(String sinaOpenId) {
		this.sinaOpenId = sinaOpenId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime;
	}

	public String getTeacherBirthday() {
		return teacherBirthday;
	}

	public void setTeacherBirthday(String teacherBirthday) {
		this.teacherBirthday = teacherBirthday;
	}

	public String getCoachTime() {
		return coachTime;
	}

	public void setCoachTime(String coachTime) {
		this.coachTime = coachTime;
	}

	public Integer getNewMsg() {
		return newMsg;
	}

	public void setNewMsg(Integer newMsg) {
		this.newMsg = newMsg;
	}

	public Integer getIdentify() {
		return identify;
	}

	public void setIdentify(Integer identify) {
		this.identify = identify;
	}

}