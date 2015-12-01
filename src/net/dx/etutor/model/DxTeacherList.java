package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DxTeacherList implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6869073532309444196L;

	private String id;
	private String avatarUrl;
	private String userName="";
	private Integer level;
	private Integer coachTime;
	private String subject;
	private String price;
	private Integer rank;
	private Integer subtime;
	private String telephone;
	private String courseId;
	private String collectId;
	private String needId;
	private String listenTest;
	/**
	 * 1 专职教师 0非专职教师
	 */
	private int fullTime;
	/**
	 * 认证 核实 听
	 */
	private Integer identify = 0;
	
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
		try {
			identify = json.getInt("identify");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			collectId = json.getString("collectId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			needId = json.getString("needId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			courseId = json.getString("courseId");
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
			subtime = json.getInt("subtime");
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
			price = json.getString("price");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			subject = json.getString("subject");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			coachTime = json.getInt("coachTime");
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
			id = json.getString("id");
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
			userName = json.getString("userName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fullTime = json.getInt("fullTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getCoachTime() {
		return coachTime;
	}

	public void setCoachTime(Integer coachTime) {
		this.coachTime = coachTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

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
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getSubtime() {
		return subtime;
	}

	public void setSubtime(Integer subtime) {
		this.subtime = subtime;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Integer getIdentify() {
		return identify;
	}

	public void setIdentify(Integer identify) {
		this.identify = identify;
	}

	public String getNeedId() {
		return needId;
	}

	public void setNeedId(String needId) {
		this.needId = needId;
	}

	public String getListenTest() {
		return listenTest;
	}

	public void setListenTest(String listenTest) {
		this.listenTest = listenTest;
	}

	public int getFullTime() {
		return fullTime;
	}

	public void setFullTime(int fullTime) {
		this.fullTime = fullTime;
	}

	
}
