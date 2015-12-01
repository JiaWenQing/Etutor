package net.dx.etutor.util;

import net.dx.etutor.data.DataParam;

public class Constants {
	public static final String NOTICE_FLAG_ACTION = "notice_flag_action";
	public static final int STAT_200 = 200;
	public static final int STAT_401 = 401;
	public static final int STAT_403 = 403;
	public static final int STAT_404 = 404;

	/**
	 * 此账号已注册
	 */
	public static final int STAT_0 = 0;// 此账号已注册
	/**
	 * 用户名或密码不正确
	 */
	public static final int STAT_ERROR = -1;
	/**
	 * 注册成功
	 */
	public static final int STAT_1 = 1;// 注册成功
	public static final int STAT_2 = 2;// 用户名或密码不能为空
	/**
	 * 验证码不正确
	 */
	public static final int STAT_3 = 3;// 验证码不正确
	/**
	 * 短信已发送,请注意查收
	 */
	public static final int STAT_4 = 4;// 短信已发送,请注意查收
	/**
	 * 新用户注册
	 */
	public static final int STAT_5 = 5;
	/**
	 * 此手机号已绑定过了
	 */
	public static final int STAT_6 = 6;

	public static final String SHARE_USERID = "1";
	public static final String WIDCH = "widch";
	public static final String HEIGHT = "Height";
	public static final String COPY_USERID = "copy_userid";
	public static final String TOKEN_ILLEGAL = "{\"error\":\"token非法\"}";
	public static final String TOKEN_TIMEOUT = "{\"error\":\"token过期\"}";
	public static final String LOGIN_ERROR = "{\"statusCode\":\"0\"}";
	public static final String BINDING_ERROR = "{\"statusCode\":\"0\"}";
	public static final String LOGIN_ERRORS = "{\"statusCode\":\"-1\"}";
	public static final String CODE_ERROR = "{\"status\":\"3\"}";
	public static final String NEW_USER = "{\"status\":\"1\"}";
	public static final String SEND_SMS = "{\"status\":\"4\"}";
	public static final String BINDING_FAIL = "{\"status\":\"0\"}";
	public static final String BINDING_SUCCESS = "{\"status\":\"2\"}";
	public static final String KEY_UID = "uid";
	public static final String KEY_OPENID = "opedId";
	public static final String KEY_ACCESS_TOKEN = "access_token";
	public static final String KEY_EXPIRES_IN = "expires_in";
	public static final String PREFERENCES_NAME = "com_weibo_sdk_android";
	public static final String PREF_USERAVATAR = "pref_useravatar";
	public static final String PREF_USERTYPE = "pref_usertype";
	public static final String PREF_PICURI = "pref_picuri";
	public static final String PREF_AVATARURL = "pref_avatarUrl";
	public static final String PREF_COPYAVATAR = "pref_copyAvatar";
	public static final String PREF_LOGINSTATU = "pref_loginstatu";
	public static final String PREF_USERNAME = "pref_username";
	public static final String PREF_ISFIRSTUSE = "pref_isFirstUse";
	public static final String PREF_RECEIVERID = "pref_receiverid";
	public static final String PREF_DIALOGUEID = "pref_dialogueid";
	public static final String PREF_COURSETABLE = "pref_coursetable";
	public static final String PREF_SUBJECT = "pref_subject";
	public static final String RREF_DEVICEID = "rref_deviceid";
	public static final String PREF_PATH = "pref_path";
	public static final String PREF_PUSH = "pref_push";
	public static final String PREF_PUSH_FLAG = "pref_push_flag";
	public static final String PREF_LEVEL = "pref_level";
	public static final String PREF_UUID = "pref_uuid";
	public static final String PREF_TOKEN = "pref_token";
	public static final String PREF_LOCATIONCITY = "pref_locationCity";
	public static final String PREF_PROVINCE = "pref_province";
	public static final String PREF_CITY = "pref_city";
	public static final String PREF_REGION = "pref_region";
	public static final String RREF_CATEGORY = "rref_category";
	public static final String RREF_CLASSITY = "rref_classity";
	public static final String RREF_SUBJECT = "rref_subject";
	public static final String VOICE_PATH = "/sdcard/etutor/voice/";
	public static final String PICTURE_PATH = "/sdcard/etutor/picture/";
	public static final String MyAvatarDir = "/sdcard/etutor/avatar/";
	public static final String MyCredentialsDir = "/sdcard/etutor/credentials/";
	public static final String MyIdCardDir = "/sdcard/etutor/idcard/";

	public static final String MyTopicDir = "/sdcard/etutor/topic/";

	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;// 拍照修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;// 本地相册修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;// 系统裁剪头像
	public static final int REQUESTCODE_UPLOADAVATAR_PREVIEW = 4;// 系统裁剪证书
	public static final int REQUESTCODE_PICTURE_PRIVATEMESSAGE = 5;// 私信图片
	public static final String ORDER_SEND = "send";
	public static final String ORDER_RECEIVE = "receive";

	/**
	 * 标签个数
	 */
	public static final int LABEL_NUMBER_TEACHER = 4;
	public static final int LABEL_NUMBER_STUDENT = 3;
	public static final int TYPE_TEACHER = 1;
	public static final int TYPE_STUDENT = 0;
	public static final int NEED_SEX = 1;
	public static final int NEED_AGE = 44;
	public static final int NEED_TEACH = 2;
	public static final int NEED_LECTURE = 3;
	public static final int NEED_SUBJECT = 4;
	public static final int NEED_SUBJECT1 = 41;
	public static final int NEED_SUBJECT2 = 42;
	public static final int NEED_SUBJECT3 = 43;
	public static final int STUDENT_NEED_REQUESTCODE = 100;
	public static final int REQUESTCODE_SETTING_CLASS = 101;
	public static final int NEED_PROVINCE = 5;
	public static final int NEED_CITY = 6;
	public static final int NEED_REGION = 7;
	public static final int NEED_TEACHERCATEGORY = 8;
	public static final int AGENCY_CLASSITY = 9;
	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static final String APP_KEY = "2468819156";
	// public static final String APP_KEY = "2045436852";

	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://www.weibo.com";
	// public static final String REDIRECT_URL = "http://www.sina.com";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	public static final String PREF_SINAAVATARURL = "pref_sinaavatarurl";
	public static final String PREF_QQAVATARURL = "pref_qqavatarurl";
	public static final String PREF_SINANAME = "pref_sinaname";
	public static final String PREF_QQNAME = "pref_qqname";
	public static final String PREF_TELEPHONE = "pref_telephone";
	public static final String PREF_RATING = "pref_rating";
	public static final String PREF_VSERION_IFON = "pref_vserion_ifon";
	public static final String PREF_MSG_COUNT = "pref_msg_count";
	public static final String PREF_MSG_STATU = "pref_msg_statu";
	public static final String PREF_COLLECT_TYPE = "pref_collect_type";
	public static final String PREF_COLLECT_ID = "pref_collect_id";
	public static final String PREF_PRAISE_ID = "pref_praise_id";
	public static final String PREF_TYPE = "pref_type";
	public static final String PREF_NEWMSG_CONTENT = "pref_newmsg_content";
	public static final String PREF_NEWMSG_TIME = "pref_newmsg_time";
	public static final String PREF_CARD_STATUS = "pref_card_status";
	public static final String PREF_CARD_SORT = "pref_card_sort";

	public static final String STATISTIC_VERSION_NUMBER = "statistic_version_number";
	public static final String STATISTIC_FIRST_USETIME = "statistic_first_usetime";
	public static final String STATISTIC_RESTART_FLAG = "statistic_restart_flag";
	public static final String STATISTIC_START_TIME = "statistic_start_time";
	public static final String STATISTIC_END_TIME = "statistic_end_time";
	public static final String STATISTIC_FLAG = "statistic_flag";
	public static final String STATISTIC_ENTER_TIME = "statistic_enter_time";
	public static final String STATISTIC_ID = "statistic_id";

	/**
	 * 应用渠道 1、服务器 (MyServer)2、360(360) 3、豌豆夹(Wandoujia) 4、小米市场(Xiaomi)
	 * 5、应用宝(Yingyongbao) 6、百度(Baidu) 7、魅族(Meizu) 8、华为(Huawei)
	 */
	public static final int CHANNEL = 4;
	public static final String APP_CHANNEL = "Xiaomi";

	public static final String PREF_LABEL = "pref_label";
	public static final String PREF_CARD_TYPE = "pref_card_type";
	public static final String PREF_IDENTIFY = "pref_identify";
	public static final String PREF_CARDLIST_FLAG = "pref_cardlist_flag";
	// public static final String PREF_MSGORORDER_LOGIN_FLAG =
	// "pref_msgororder_login_flag";
	public static final String PREF_COMMENT_FLAG = "pref_comment_flag";
	public static final String PREF_CARDLIST_FLUSH_FLAG = "pref_cardlist_flush_flag";
	public static final String PREF_USER_SCHOOL = "pref_user_school";
	public static final String PREF_REFRESH_FLAG = "pref_refresh_flag";
	public static final String PREF_NEW_ORDER_FLAG = "pref_new_order_flag";
	public static final String PREF_FIRST_ENTER_ORDER_FLAG = "pref_first_enter_order_flag";
	public static final String APP_ID_WEIXIN = "wxebdfc8b0041e0347";// 测试Id
	// public static final String APP_ID_WEIXIN = "wxf3e38f7900ef8e61";// 正式id
	// 微信商户号
	public static final String MCH_ID_WEIXIN = "1252577701";
	// 微信API密钥，在商户平台设置
	public static final String API_KEY_WEIXIN = "w4sfdpdim2zhzruy2umjuquklh8malra";
	public final static String APP_ID_QQ = "1103377550";
	public static final String PREF_LOOKED_POSTS_ID = "pref_looked_posts_id";
	public final static String URL_SHARE_LOGO = DataParam.REMOTE_SERVE
			+ "share/app_logo.png";
	public final static String URL_SHARE = DataParam.REMOTE_SERVE
			+ "share/share.jsp?topicId=";
}
