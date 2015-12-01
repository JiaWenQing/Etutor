package net.dx.etutor.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.dx.etutor.util.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DxParentsinfo entity. @author MyEclipse Persistence Tools
 */
public class DxParentsinfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -846268235181953011L;
	// Fields

	private Integer id;
	private String grade;
	private String method;
	private String sex;
	private String age;
	private String parentsBirthday;
	private String childBirthday;
	private String createTime;
	private String userId;

	// json获取转换
	public void initWithAttributes(JSONObject json) {

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
			childBirthday = json.getString("childBirthday");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			parentsBirthday = json.getString("parentsBirthday");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			method = json.getString("method");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			grade = json.getString("json");
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
	public DxParentsinfo() {
	}

	/** full constructor */
	public DxParentsinfo(String grade, String method, String parentsBirthday,
			String childBirthday, String createTime, String userId,String sex) {
		this.grade = grade;
		this.method = method;
		this.parentsBirthday = parentsBirthday;
		this.childBirthday = childBirthday;
		this.createTime = createTime;
		this.userId = userId;
		this.sex=sex;
	}

	// Property accessors
	
	public Integer getId() {
		return this.id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		age=Integer.toString(DateUtil.getOffectAge(getParentsBirthday()));
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParentsBirthday() {
		return this.parentsBirthday;
	}

	public void setParentsBirthday(String parentsBirthday) {
		this.parentsBirthday = parentsBirthday;
	}

	public String getChildBirthday() {
		return this.childBirthday;
	}

	public void setChildBirthday(String childBirthday) {
		this.childBirthday = childBirthday;
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

}