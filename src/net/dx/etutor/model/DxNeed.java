package net.dx.etutor.model;

import net.dx.etutor.view.SlideView;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * DxNeed entity. @author MyEclipse Persistence Tools
 */
public class DxNeed implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4115186790624077087L;
	private String needId;
	private String needTitle;
	private String subjectItemId;
	private String collectId;
	private String introduce;
	private double price;
	private String subtime;
	private String status;
	private String createTime;
	private String teacherSex;
	private String userId;
	private int tradeNumber;
	private String publishTime;
	private String teachMode;
	private String lectureMode;
	private DxUsers dxUser;

	public SlideView slideView;

	private String token;
	
	private String commentCount;

	// json获取转换
	public void initWithAttributes(JSONObject json) {

		try {
			commentCount = json.getString("commentCount");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			collectId = json.getString("collectId");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			teachMode = json.getString("teachMode");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			lectureMode = json.getString("lectureMode");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			publishTime = json.getString("publishTime");

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			tradeNumber = json.getInt("tradeNumber");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			userId = json.getString("userId");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			teacherSex = json.getString("teacherSex");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			createTime = json.getString("createTime");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			status = json.getString("status");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			subtime = json.getString("subtime");
			if (TextUtils.isEmpty(subtime)) {
				subtime="0";
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			price = json.getInt("price");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			introduce = json.getString("introduce");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			subjectItemId = json.getString("subjectItemId");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			needTitle = json.getString("needTitle");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			needId = json.getString("needId");
			if (TextUtils.isEmpty(needId)) {
				needId="-1";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject jsonObject = (JSONObject) json.getJSONObject("user");
			DxUsers user = new DxUsers();
			user.initWithAttributes(jsonObject);
			this.dxUser = user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Constructors

	/** default constructor */
	public DxNeed() {
	}

	/** full constructor */
	public DxNeed(String needTitle, String subjectItemId, String introduce,
			Integer price, String subtime, String status, String createTime,
			String teacherSex, String userId, Integer tradeNumber,String publishTime, String teachMode) {
		this.needTitle = needTitle;
		this.subjectItemId = subjectItemId;
		this.introduce = introduce;
		this.price = price;
		this.subtime = subtime;
		this.status = status;
		this.createTime = createTime;
		this.teacherSex = teacherSex;
		this.userId = userId;
		this.tradeNumber = tradeNumber;
		this.publishTime = publishTime;
		this.teachMode = teachMode;
	}

	// Property accessors
	public String getNeedId() {
		if (TextUtils.isEmpty(needId)) {
			needId="-1";
		}
		return this.needId;
	}

	public void setNeedId(String needId) {
		this.needId = needId;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getNeedTitle() {
		return this.needTitle;
	}

	public void setNeedTitle(String needTitle) {
		this.needTitle = needTitle;
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

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSubtime() {
		return this.subtime;
	}

	public void setSubtime(String subtime) {
		this.subtime = subtime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTeacherSex() {
		return this.teacherSex;
	}

	public void setTeacherSex(String teacherSex) {
		this.teacherSex = teacherSex;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getTradeNumber() {
		return this.tradeNumber;
	}

	public void setTradeNumber(Integer tradeNumber) {
		this.tradeNumber = tradeNumber;
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

	public String getLectureMode() {
		return lectureMode;
	}

	public void setLectureMode(String lectureMode) {
		this.lectureMode = lectureMode;
	}


	public DxUsers getDxUser() {
		return dxUser;
	}

	public void setDxUser(DxUsers dxUser) {
		this.dxUser = dxUser;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	

}