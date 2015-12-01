package net.dx.etutor.util;

import net.dx.etutor.app.EtutorApplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {
	private SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor mEditor;

	public SharePreferenceUtil(Context context) {
		mSharedPreferences = context.getSharedPreferences("etutor",
				Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void clearSharePerference() {
		setUserAvatar(null);
		setUploadPath(null);
		setToken(null);
		setUserId(null);
		setUserIdCopy(null);
		setUserType(0);
		setUserName(null);
		setLoginStatu(0);
		setLevel(0);
		setQQOpedId(null);
		setSinaUid(null);
		setUserRank(0);
		setMsgCount(null);
		setMsgStatu(false);
		setCollectItemId(null);
		setCollectId(null);
		setNewMsgContent(null);
		setNewMsgTime(null);
		setLabel(0);
		setCardListFlag(true);
		isCommentFlag(false);
		setWidch(0);
		setHeight(0);
	}

	// 图片高度
	public void setHeight(int height) {
		mEditor.putInt(Constants.HEIGHT, height);
		mEditor.commit();
	}

	public int getHeight() {
		return mSharedPreferences.getInt(Constants.HEIGHT, 0);
	}

	// 图片宽度
	public void setWidch(int widch) {
		mEditor.putInt(Constants.WIDCH, widch);
		mEditor.commit();
	}

	public int getWidch() {
		return mSharedPreferences.getInt(Constants.WIDCH, 0);
	}

	// 用户ID
	public void setUserId(String userId) {
		mEditor.putString(Constants.SHARE_USERID, userId);
		mEditor.commit();
	}

	public String getUserId() {
		return mSharedPreferences.getString(Constants.SHARE_USERID, null);
	}

	// copy用户ID
	public void setUserIdCopy(String userId) {
		mEditor.putString(Constants.COPY_USERID, userId);
		mEditor.commit();
	}

	public String getUserIdCopy() {
		return mSharedPreferences.getString(Constants.COPY_USERID, null);
	}

	/**
	 * 保存头像的url
	 * 
	 * @param avatarUrl
	 */
	public void setAvatarUrl(String avatarUrl) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_AVATARURL, avatarUrl);
		mEditor.commit();
	}

	public String getAvatarUrl() {
		return mSharedPreferences.getString(Constants.PREF_AVATARURL, null);
	}

	// 用户头像
	public void setUserAvatar(String head) {
		mEditor.putString(Constants.PREF_USERAVATAR, head);
		mEditor.commit();
	}

	public String getUserAvatar() {
		return mSharedPreferences.getString(Constants.PREF_USERAVATAR, null);
		// file:////sdcard/etutor/avatar/140903130722
	}

	// 用户名称
	public void setUserName(String userName) {
		mEditor.putString(Constants.PREF_USERNAME, userName);
		mEditor.commit();
	}

	public String getUserName() {
		return mSharedPreferences.getString(Constants.PREF_USERNAME, "易家教");
	}

	// 用户名称
	public void setUserSchool(String school) {
		mEditor.putString(Constants.PREF_USER_SCHOOL, school);
		mEditor.commit();
	}

	public String getUserSchool() {
		return mSharedPreferences.getString(Constants.PREF_USER_SCHOOL, null);
	}

	// 等级
	public void setLevel(int level) {
		mEditor.putInt(Constants.PREF_LEVEL, level);
		mEditor.commit();
	}

	public int getLevel() {
		return mSharedPreferences.getInt(Constants.PREF_LEVEL, 0);
	}

	/**
	 * 登录用户的 标签类型
	 * 
	 * @param identify
	 */
	public void setIdentify(int identify) {
		mEditor.putInt(Constants.PREF_IDENTIFY, identify);
		mEditor.commit();
	}

	public int getIdentify() {
		return mSharedPreferences.getInt(Constants.PREF_IDENTIFY, 0);
	}

	/**
	 * @param b
	 */
	public void setLabel(int b) {
		mEditor.putInt(Constants.PREF_LABEL, b);
		mEditor.commit();
	}

	/**
	 * 标记 ：1认证、2核实、4听课
	 * 
	 * @return
	 */
	public int getLabel() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getInt(Constants.PREF_LABEL, 0);
	}

	/**
	 * 是否是第一次使用
	 * 
	 * @return
	 */
	public void setPushFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_PUSH, b);
		mEditor.commit();
	}

	public boolean getPushFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(Constants.PREF_PUSH, true);
	}

	public void setCardListFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_CARDLIST_FLAG, b);
		mEditor.commit();
	}

	public boolean getCardListFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(Constants.PREF_CARDLIST_FLAG,
				false);
	}

	public void isCommentFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_COMMENT_FLAG, b);
		mEditor.commit();
	}

	public boolean getCommentFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences
				.getBoolean(Constants.PREF_COMMENT_FLAG, false);
	}

	/**
	 * 用户类型 0学生，1老师
	 * 
	 * @param userType
	 */
	public void setUserType(int userType) {
		mEditor.putInt(Constants.PREF_USERTYPE, userType);
		mEditor.commit();
	}

	/**
	 * 用户类型 0学生，1老师
	 * 
	 * @param userType
	 */
	public int getUserType() {
		return mSharedPreferences.getInt(Constants.PREF_USERTYPE, 0);
	}

	public String getReceiverId() {
		return mSharedPreferences.getString(Constants.PREF_USERNAME, null);
	}

	public void setReceiverId(String receiverId) {
		mEditor.putString(Constants.PREF_RECEIVERID, receiverId);
		mEditor.commit();
	}

	public String getDialogueId() {
		return mSharedPreferences.getString(Constants.PREF_USERNAME, null);
	}

	public void setDialogueId(String dialogueId) {
		mEditor.putString(Constants.PREF_DIALOGUEID, dialogueId);
		mEditor.commit();
	}

	/**
	 * 登录状态 0未登录，1登录，2第三方登录，3广播登录
	 * 
	 * @param loginStatu
	 */
	public void setLoginStatu(int loginStatu) {
		mEditor.putInt(Constants.PREF_LOGINSTATU, loginStatu);
		mEditor.commit();
	}

	/**
	 * 登录状态 0未登录，1登录，2第三方登录，3广播登录
	 * 
	 * @param loginStatu
	 */
	public int getLoginStatu() {
		return mSharedPreferences.getInt(Constants.PREF_LOGINSTATU, 0);
	}

	/**
	 * 用户证件
	 * 
	 * @param path
	 */
	public void setUploadPath(String path) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_PATH, path);
		mEditor.commit();
	}

	/**
	 * 用户证件
	 * 
	 * @param path
	 */
	public String getUploadPath() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.PREF_PATH, null);
	}

	/* *//**
	 * 保存 Token 对象到 SharedPreferences。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * @param token
	 *            Token 对象
	 */
	public static void writeAccessToken(Context context, Oauth2AccessToken token) {
		if (null == context || null == token) {
			return;
		}

		SharedPreferences pref = context.getSharedPreferences(
				Constants.PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(Constants.KEY_UID, token.getUid());
		editor.putString(Constants.KEY_ACCESS_TOKEN, token.getToken());
		editor.putLong(Constants.KEY_EXPIRES_IN, token.getExpiresTime());
		editor.commit();
	}

	/* *//**
	 * 从 SharedPreferences 读取 Token 信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * 
	 * @return 返回 Token 对象
	 */
	public static Oauth2AccessToken readAccessToken(Context context) {
		if (null == context) {
			return null;
		}

		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PREFERENCES_NAME, Context.MODE_APPEND);
		token.setUid(pref.getString(Constants.KEY_UID, ""));
		token.setToken(pref.getString(Constants.KEY_ACCESS_TOKEN, ""));
		token.setExpiresTime(pref.getLong(Constants.KEY_EXPIRES_IN, 0));
		return token;
	}

	/**
	 * QQ 的 OpedId
	 * 
	 * @param OpedId
	 */
	public void setQQOpedId(String opedId) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.KEY_OPENID, opedId);
		mEditor.commit();
	}

	/**
	 * QQ 的 OpedId
	 * 
	 * @param OpedId
	 */
	public String getQQOpedId() {
		return mSharedPreferences.getString(Constants.KEY_OPENID, null);
	}

	/**
	 * sina uid
	 * 
	 * @param token
	 */
	public void setSinaUid(String uid) {
		mEditor.putString(Constants.KEY_UID, uid);
		mEditor.commit();
	}

	/**
	 * sina uid
	 * 
	 * @param token
	 */
	public String getSinaUid() {
		return mSharedPreferences.getString(Constants.KEY_UID, null);
	}

	/**
	 * 清空 SharedPreferences 中 Token信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 */
	public static void clear(Context context) {
		if (null == context) {
			return;
		}

		SharedPreferences pref = context.getSharedPreferences(
				Constants.PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 存放课表
	 * 
	 * @param courseTable
	 */
	public void setCourseTable(int courseTable) {
		// TODO Auto-generated method stub
		mEditor.putInt(Constants.PREF_COURSETABLE, courseTable);
		mEditor.commit();
	}

	public int getCourseTable() {
		return mSharedPreferences.getInt(Constants.PREF_COURSETABLE, 0);
	}

	/**
	 * 是否是第一次使用当前版本
	 * 
	 * @return
	 */
	public void setFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_ISFIRSTUSE, b);
		mEditor.commit();
	}

	/**
	 * 是否是第一次使用当前版本
	 * 
	 * @return
	 */
	public boolean getFlag() {
		// TODO Auto-generated method stub
		String versionNumber = EtutorApplication.getInstance().getSdUtil()
				.getVersionNumber();
		String NewVersion = EtutorApplication.getInstance().getVersionNumber();
		if (!versionNumber.equals(NewVersion)) {
			setFlag(true);
		}
		return mSharedPreferences.getBoolean(Constants.PREF_ISFIRSTUSE, true);
	}

	/**
	 * uuid
	 * 
	 * @param uuid
	 */
	public void setUuid(String uuid) {
		mEditor.putString(Constants.PREF_UUID, uuid);
		mEditor.commit();
	}

	public String getUuid() {
		return mSharedPreferences.getString(Constants.PREF_UUID, null);
	}

	/**
	 * token
	 * 
	 * @param token
	 */
	public void setToken(String token) {
		mEditor.putString(Constants.PREF_TOKEN, token);
		mEditor.commit();
	}

	public String getToken() {
		return mSharedPreferences.getString(Constants.PREF_TOKEN, "");
	}

	/**
	 * 保存定位选择城市
	 * 
	 * @param value
	 */
	public void setLocationCity(String location) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_LOCATIONCITY, location);
		mEditor.commit();
	}

	public String getLocationCity() {
		return mSharedPreferences.getString(Constants.PREF_LOCATIONCITY, "上海市");
	}

	/**
	 * 保存筛选省份
	 * 
	 * @param value
	 */
	public void setProvince(String province) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_PROVINCE, province);
		mEditor.commit();
	}

	public void clearArea() {
		setProvince(null);
		setCity(null);
		setRegion(null);
		setCategory(null);
		setSubject(null);
		setClassify(null);
	}

	/**
	 * 筛选省份
	 * 
	 * @return
	 */
	public String getProvince() {
		return mSharedPreferences.getString(Constants.PREF_PROVINCE, null);
	}

	/**
	 * 保存筛选城市
	 * 
	 * @param value
	 */
	public void setCity(String city) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_CITY, city);
		mEditor.commit();
	}

	/**
	 * 筛选城市
	 * 
	 * @return
	 */
	public String getCity() {
		return mSharedPreferences.getString(Constants.PREF_CITY, null);
	}

	/**
	 * 保存筛选区县
	 * 
	 * @param value
	 */
	public void setRegion(String region) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_REGION, region);
		mEditor.commit();
	}

	/**
	 * 筛选区县
	 * 
	 * @return
	 */
	public String getRegion() {
		return mSharedPreferences.getString(Constants.PREF_REGION, null);
	}

	/**
	 * 保存QQ头像的URL
	 * 
	 * @param qQImageUri
	 */
	public void setQQAvatarUrl(String qq) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_QQAVATARURL, qq);
		mEditor.commit();
	}

	/**
	 * 获取QQ头像的URL
	 * 
	 * @return
	 */
	public String getQQAvatarUrl() {
		return mSharedPreferences.getString(Constants.PREF_QQAVATARURL, null);
	}

	/**
	 * 保存Sina头像的URL
	 * 
	 * @param qQImageUri
	 */
	public void setSinaAvatarUrl(String sina) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_SINAAVATARURL, sina);
		mEditor.commit();
	}

	/**
	 * 获取Sina头像的URL
	 * 
	 * @return
	 */
	public String getSinaAvatarUrl() {
		return mSharedPreferences.getString(Constants.PREF_QQAVATARURL, null);
	}

	/**
	 * 保存QQ的Name
	 * 
	 * @param qQImageUri
	 */
	public void setQQName(String qq) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_QQNAME, qq);
		mEditor.commit();
	}

	/**
	 * 获取QQ的Name
	 * 
	 * @return
	 */
	public String getQQName() {
		return mSharedPreferences.getString(Constants.PREF_QQNAME, null);
	}

	/**
	 * 保存Sina的Name
	 * 
	 * @param qQImageUri
	 */
	public void setSinaName(String sina) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_SINANAME, sina);
		mEditor.commit();
	}

	/**
	 * 获取Sina的Name
	 * 
	 * @return
	 */
	public String getSinaName() {
		return mSharedPreferences.getString(Constants.PREF_SINANAME, null);
	}

	/**
	 * 保存用户手机号
	 * 
	 * @param telephone
	 */
	public void setUserPhone(String telephone) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_TELEPHONE, telephone);
		mEditor.commit();
	}

	/**
	 * 获取用户手机号
	 * 
	 * @param telephone
	 */
	public String getUserPhone() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.PREF_TELEPHONE, null);
	}

	/**
	 * 保存用户Rating
	 * 
	 * @param rating
	 */
	public void setUserRank(float rating) {
		// TODO Auto-generated method stub
		mEditor.putFloat(Constants.PREF_RATING, rating);
		mEditor.commit();
	}

	/**
	 * 获取用户Rating
	 * 
	 * @param rating
	 */
	public float getUserRank() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getFloat(Constants.PREF_RATING, 0);
	}

	/**
	 * 保存APP版本信息
	 * 
	 * @param version
	 */
	public void setVersionInfo(int version) {
		// TODO Auto-generated method stub
		mEditor.putInt(Constants.PREF_VSERION_IFON, version);
		mEditor.commit();
	}

	/**
	 * 获取APP版本信息
	 * 
	 * @param version
	 */
	public int getVersionInfo() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getInt(Constants.PREF_VSERION_IFON, -1);
	}

	/**
	 * 保存未阅读的消息状态
	 * 
	 * @param version
	 */
	public void setMsgStatu(Boolean statu) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_MSG_STATU, statu);
		mEditor.commit();
	}

	/**
	 * 获取未阅读的消息状态
	 * 
	 * @param count
	 */
	public boolean getMsgStatu() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(Constants.PREF_MSG_STATU, false);
	}

	/**
	 * 保存未阅读的消息条数
	 * 
	 * @param version
	 */
	public void setMsgCount(String count) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_MSG_COUNT, count);
		mEditor.commit();
	}

	/**
	 * 获取未阅读的消息条数
	 * 
	 * @param count
	 */
	public String getMsgCount() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.PREF_MSG_COUNT, "");
	}

	/**
	 * 被收藏者的userId 或 帖子replyId
	 * 
	 * @param collectItemId
	 */
	public void setCollectItemId(String collectItemId) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_COLLECT_TYPE, collectItemId);
		mEditor.commit();
	}

	/**
	 * 被收藏者的userId 或 帖子replyId
	 * 
	 */
	public String getCollectItemId() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.PREF_COLLECT_TYPE, null);
	}

	/**
	 * 收藏id
	 * 
	 * @param collectId
	 */
	public void setCollectId(String collectId) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_COLLECT_ID, collectId);
		mEditor.commit();
	}

	/**
	 * 收藏id
	 * 
	 * @param collectId
	 */
	public String getCollectId() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.PREF_COLLECT_ID, null);
	}

	public void setTopicFlag(boolean type) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_TYPE, type);
		mEditor.commit();
	}

	public boolean getTopicFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(Constants.PREF_TYPE, false);
	}

	/**
	 * 保存最新的消息内容
	 * 
	 * @param
	 */
	public void setNewMsgContent(String msg) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_NEWMSG_CONTENT, msg);
		mEditor.commit();
	}

	/**
	 * 最新的消息内容
	 * 
	 * @return
	 */
	public String getNewMsgContent() {
		// TODO Auto-generated method stub
		return mSharedPreferences
				.getString(Constants.PREF_NEWMSG_CONTENT, null);
	}

	/**
	 * 保存最新的消息时间
	 * 
	 * @param type
	 */
	public void setNewMsgTime(String time) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.PREF_NEWMSG_TIME, time);
		mEditor.commit();
	}

	/**
	 * 最新的消息时间
	 * 
	 * @return
	 */
	public String getNewMsgTime() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.PREF_NEWMSG_TIME, null);
	}

	public void setDeviceId(String deviceId) {
		mEditor.putString(Constants.RREF_DEVICEID, deviceId);
		mEditor.commit();
	}

	public String getDeviceId() {
		return mSharedPreferences.getString(Constants.RREF_DEVICEID, null);
	}

	/**
	 * 保存筛选类别
	 * 
	 * @param category
	 */
	public void setCategory(String category) {
		mEditor.putString(Constants.RREF_CATEGORY, category);
		mEditor.commit();
	}

	/**
	 * 筛选类别
	 * 
	 * @return
	 */
	public String getCategory() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.RREF_CATEGORY, null);
	}

	/**
	 * 筛选科目
	 * 
	 * @param student
	 */
	public void setSubject(String subject) {
		mEditor.putString(Constants.RREF_SUBJECT, subject);
		mEditor.commit();
	}

	/**
	 * 筛选科目
	 * 
	 * @return
	 */
	public String getSubject() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.RREF_SUBJECT, null);
	}

	/**
	 * 机构分类
	 * 
	 * @param classify
	 */
	public void setClassify(String classify) {
		// TODO Auto-generated method stub
		mEditor.putString(Constants.RREF_CLASSITY, classify);
		mEditor.commit();

	}

	/**
	 * 机构分类
	 * 
	 * @return
	 */
	public String getClassify() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.RREF_CLASSITY, "全部");
	}

	public void setCardSort(String names) {
		mEditor.putString(Constants.PREF_CARD_SORT, names);
		mEditor.commit();
	}

	public String getCardSort() {
		// TODO Auto-generated method stub
		String names = "[{\"id\":16,\"selected\":1,\"name\":\"专职教师\"},"
				+ "{\"id\":4,\"selected\":1,\"name\":\"推荐教师\"},"
				+ "{\"id\":2,\"selected\":0,\"name\":\"新入住教师\"},"
				+ "{\"id\":1,\"selected\":0,\"name\":\"我的收藏\"},"
				+ "{\"id\":8,\"selected\":0,\"name\":\"我的帖子\"}]";
		return mSharedPreferences.getString(Constants.PREF_CARD_SORT, names);
	}

	/**
	 * 1我的收藏 2新入住教师 4推荐教师
	 * 
	 * @return
	 */
	public int getCardType() {
		return mSharedPreferences.getInt(Constants.PREF_CARD_TYPE, 4);
	}

	public void setCardType(int type) {
		// TODO Auto-generated method stub
		mEditor.putInt(Constants.PREF_CARD_TYPE, type);
		mEditor.commit();
	}

	/**
	 * 是否刷新首页卡片
	 * 
	 * @param b
	 */
	public void isCardListFlushFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_CARDLIST_FLUSH_FLAG, b);
		mEditor.commit();
	}

	/**
	 * @return 是否刷新首页卡片
	 */
	public boolean getCardListFlushFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(
				Constants.PREF_CARDLIST_FLUSH_FLAG, false);
	}

	/**
	 * 刷新数据
	 * 
	 * @param b
	 */
	public void isRefreshFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_REFRESH_FLAG, b);
		mEditor.commit();
	}

	/**
	 * 刷新数据
	 * 
	 * @return
	 */
	public boolean getRefreshFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences
				.getBoolean(Constants.PREF_REFRESH_FLAG, false);
	}

	public void isNewOrderFlag(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_NEW_ORDER_FLAG, b);
		mEditor.commit();
	}

	public boolean getNewOrderFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(Constants.PREF_NEW_ORDER_FLAG,
				false);
	}

	public void isFirstEnterOrder(boolean b) {
		// TODO Auto-generated method stub
		mEditor.putBoolean(Constants.PREF_FIRST_ENTER_ORDER_FLAG, b);
		mEditor.commit();
	}

	public boolean getFirstEnterOrderFlag() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(
				Constants.PREF_FIRST_ENTER_ORDER_FLAG, true);
	}

	/**
	 * 浏览过的帖子id
	 * 
	 * @return 32,33,52
	 */
	public String getPostsIds() {
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(Constants.PREF_LOOKED_POSTS_ID,
				null);
	}

	/**
	 * 浏览过的帖子id
	 * 
	 * @param ids
	 *            32,33,52
	 */
	public void setPostsIds(String ids) {
		mEditor.putString(Constants.PREF_LOOKED_POSTS_ID, ids);
		mEditor.commit();
	}

}
