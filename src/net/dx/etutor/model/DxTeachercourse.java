package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author app
 * 教师一对一model
 */
public class DxTeachercourse extends DxLectureArea implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -100906380827176679L;
	private Integer id;
	private Integer teacherId;
	private String title;
	private String subjectItemId;
	private String subjectItemId1;
	private String subjectItemId2;
	private String subjectItemId3;
	private String introduce;
	private Integer price;
	private Integer subtime;
	private String createTime;
	private String publishTime;
	private String listenTest;
	private String listenInfo;

	private String coachTime;
	private String lectureType;
	private DxUsers dxUsers;
	private DxTeacherinfo teacherInfo;
	private String token;
	private String shield;

	private DxLectureArea area = new DxLectureArea();

	// json获取转换
	public void initWithAttributes(JSONObject json) {

		area.initWithAttributes(json);
		try {
			shield = json.getString("shield");
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
			lectureType = json.getString("lectureType");
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
			listenTest = json.getString("listenTest");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			listenInfo = json.getString("listenInfo");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			publishTime = json.getString("publishTime");
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
			subtime = json.getInt("subtime");
			if (subtime == null) {
				subtime = 0;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			price = json.getInt("price");
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

		try {
			subjectItemId3 = json.getString("subjectItemId3");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			subjectItemId2 = json.getString("subjectItemId2");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			subjectItemId1 = json.getString("subjectItemId1");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			subjectItemId = json.getString("subjectItemId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			title = json.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			teacherId = json.getInt("teacherId");
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
					.getJSONObject("teacherInfo");
			DxTeacherinfo teacherInfo = new DxTeacherinfo();
			teacherInfo.initWithAttributes(jsonObject);
			this.teacherInfo = teacherInfo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Constructors

	/** default constructor */
	public DxTeachercourse() {
	}

	/** full constructor */
	public DxTeachercourse(Integer teacherId, String title,
			String subjectItemId1, String subjectItemId2,
			String subjectItemId3, String introduce, Integer price,
			Integer subtime, String createTime, String publishTime,
			String listenTest) {
		this.teacherId = teacherId;
		this.title = title;
		this.subjectItemId1 = subjectItemId1;
		this.subjectItemId2 = subjectItemId2;
		this.subjectItemId3 = subjectItemId3;
		this.introduce = introduce;
		this.price = price;
		this.subtime = subtime;
		this.createTime = createTime;
		this.publishTime = publishTime;
		this.listenTest = listenTest;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubjectItemId() {
		return this.subjectItemId;
	}

	public void setSubjectItemId(String subjectItemId) {
		this.subjectItemId = subjectItemId;
	}

	public String getSubjectItemId1() {
		return this.subjectItemId1;
	}

	public void setSubjectItemId1(String subjectItemId1) {
		this.subjectItemId1 = subjectItemId1;
	}

	public String getSubjectItemId2() {
		return this.subjectItemId2;
	}

	public void setSubjectItemId2(String subjectItemId2) {
		this.subjectItemId2 = subjectItemId2;
	}

	public String getSubjectItemId3() {
		return this.subjectItemId3;
	}

	public void setSubjectItemId3(String subjectItemId3) {
		this.subjectItemId3 = subjectItemId3;
	}

	public String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getSubtime() {
		return subtime;
	}

	public void setSubtime(Integer subtime) {
		this.subtime = subtime;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getListenTest() {
		return this.listenTest;
	}

	public void setListenTest(String listenTest) {
		this.listenTest = listenTest;
	}

	public String getListenInfo() {
		return listenInfo;
	}

	public void setListenInfo(String listenInfo) {
		this.listenInfo = listenInfo;
	}

	public DxUsers getDxUsers() {
		return dxUsers;
	}

	public void setDxUsers(DxUsers dxUsers) {
		this.dxUsers = dxUsers;
	}

	public String getCoachTime() {
		return coachTime;
	}

	public void setCoachTime(String coachTime) {
		this.coachTime = coachTime;
	}

	public String getLectureType() {
		return lectureType;
	}

	public void setLectureType(String lectureType) {
		this.lectureType = lectureType;
	}

	public DxTeacherinfo getTeacherInfo() {
		return teacherInfo;
	}

	public void setTeacherInfo(DxTeacherinfo teacherInfo) {
		this.teacherInfo = teacherInfo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public DxLectureArea getArea() {
		return area;
	}

	public void setArea(DxLectureArea area) {
		this.area = area;
	}

	public String getShield() {
		return shield;
	}

	public void setShield(String shield) {
		this.shield = shield;
	}
	

}