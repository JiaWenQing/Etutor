package net.dx.etutor.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author app
 * 教师小班model
 */
public class DxTeacherclass implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8209920149957397300L;
	private Integer id;
	private Integer teacherId;
	private String title;
	private String subjectItemId;
	private String introduce;
	private String price;
	private String classSize;
	private String createTime;
	private String beginTime;
	private String publishTime;
	private String teachMode;
	private String listenTest;
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
			listenTest = json.getString("listenTest");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			teachMode = json.getString("teachMode");
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
			beginTime = json.getString("beginTime");
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
			classSize = json.getString("classSize");
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
			introduce = json.getString("introduce");
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

	}

	// Constructors

	/** default constructor */
	public DxTeacherclass() {
	}

	/** full constructor */
	public DxTeacherclass(Integer teacherId, String title,
			String subjectItemId, String introduce, String price,
			String classSize, String createTime, String beginTime,
			String publishTime, String teachMode, String listenTest) {
		this.teacherId = teacherId;
		this.title = title;
		this.subjectItemId = subjectItemId;
		this.introduce = introduce;
		this.price = price;
		this.classSize = classSize;
		this.createTime = createTime;
		this.beginTime = beginTime;
		this.publishTime = publishTime;
		this.teachMode = teachMode;
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

	public String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getClassSize() {
		return classSize;
	}

	public void setClassSize(String classSize) {
		this.classSize = classSize;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getTeachMode() {
		return this.teachMode;
	}

	public void setTeachMode(String teachMode) {
		this.teachMode = teachMode;
	}

	public String getListenTest() {
		return this.listenTest;
	}

	public void setListenTest(String listenTest) {
		this.listenTest = listenTest;
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