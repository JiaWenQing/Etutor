package net.dx.etutor.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxTeacherinfo entity. @author MyEclipse Persistence Tools
 */
public class DxTeacherinfo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -9150346563871100843L;
	private Integer id;
	private String school;
	private String education;
	private String major;
	private String status;
	private String teacherBirthday;
	private String graduateTime;
	private Integer level;
	private Integer coachTime;
	private Integer rank;
	private String sex;
	private String createTime;
	private String userId;

	private DxUsers dxUsers = new DxUsers();
	private DxTeachercourse dxTeachercourse = new DxTeachercourse();
	private DxTeacherclass dxTeacherclass = new DxTeacherclass();
	private List<DxCertificate> dxCertificates = new ArrayList<DxCertificate>();
	private Integer commentCount;

	private String province;
	private String city;
	private String region;

	// json获取转换
	public void initWithAttributes(JSONObject json) {
		try {
			commentCount = json.getInt("commentCount");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			userId = json.getString("userId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			createTime = json.getString("createTime");
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
			coachTime = json.getInt("coachTime");
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
			level = json.getInt("level");
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
			teacherBirthday = json.getString("teacherBirthday");
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
			graduateTime = json.getString("graduateTime");
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
			id = json.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject jsonObject = (JSONObject) json
					.getJSONObject("teacherCourse");
			DxTeachercourse dxTeachercourse = new DxTeachercourse();
			dxTeachercourse.initWithAttributes(jsonObject);
			this.dxTeachercourse = dxTeachercourse;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject jsonObject = (JSONObject) json.getJSONObject("user");
			DxUsers user = new DxUsers();
			user.initWithAttributes(jsonObject);
			this.dxUsers = user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONObject jsonObject = (JSONObject) json
					.getJSONObject("teacherClass");
			DxTeacherclass dxTeacherclass = new DxTeacherclass();
			dxTeacherclass.initWithAttributes(jsonObject);
			this.dxTeacherclass = dxTeacherclass;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONArray jsonArray = (JSONArray) json.getJSONArray("certificate");
			for (int i = 0; i <= jsonArray.length(); i++) {
				DxCertificate dxCertificate = new DxCertificate();
				dxCertificate.initWithAttributes((JSONObject) jsonArray
						.getJSONObject(i));
				dxCertificates.add(dxCertificate);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Constructors

	/** default constructor */
	public DxTeacherinfo() {
	}

	/** full constructor */
	public DxTeacherinfo(String school, String education, String major,
			String status, Integer level, Integer coachTime, Integer rank,
			String graduateTime, String sex, String createTime, String userId,
			String teacherBirthday) {
		this.school = school;
		this.education = education;
		this.major = major;
		this.status = status;
		this.level = level;
		this.coachTime = coachTime;
		this.rank = rank;
		this.sex = sex;
		this.createTime = createTime;
		this.userId = userId;
		this.teacherBirthday = teacherBirthday;
		this.graduateTime = graduateTime;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getLevel() {
		return this.level;
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

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTeacherBirthday() {
		return teacherBirthday;
	}

	public void setTeacherBirthday(String teacherBirthday) {
		this.teacherBirthday = teacherBirthday;
	}

	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime;
	}

	public DxUsers getDxUsers() {
		return dxUsers;
	}

	public void setDxUsers(DxUsers dxUsers) {
		this.dxUsers = dxUsers;
	}

	public DxTeachercourse getDxTeachercourse() {
		return dxTeachercourse;
	}

	public void setDxTeachercourse(DxTeachercourse dxTeachercourse) {
		this.dxTeachercourse = dxTeachercourse;
	}

	public DxTeacherclass getDxTeacherclass() {
		return dxTeacherclass;
	}

	public void setDxTeacherclass(DxTeacherclass dxTeacherclass) {
		this.dxTeacherclass = dxTeacherclass;
	}

	public List<DxCertificate> getDxCertificates() {
		return dxCertificates;
	}

	public void setDxCertificates(List<DxCertificate> dxCertificates) {
		this.dxCertificates = dxCertificates;
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

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

}
