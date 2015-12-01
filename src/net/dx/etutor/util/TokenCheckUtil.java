package net.dx.etutor.util;

import java.io.File;

import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.DataParam;
import net.dx.etutor.data.UrlEngine;
import net.dx.etutor.model.DxUsers;

import org.apache.http.Header;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TokenCheckUtil {

	private static boolean flag = false;
	private static DxUsers dxUsers = new DxUsers();
	private static boolean statu;

	/**
	 * 更新token，level，Rating
	 * 
	 * @return
	 */
	public static boolean checkToken() {
		HttpUtil.post(UrlEngine.refreshUser(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				if (statusCode == Constants.STAT_200) {
					try {
						flag = true;
						dxUsers.initWithAttributes(response);
						EtutorApplication.getInstance().getSpUtil()
								.setToken(dxUsers.getToken());
						EtutorApplication.getInstance().getSpUtil()
								.setLevel(dxUsers.getLevel());
						EtutorApplication.getInstance().getSpUtil()
								.setUserRank(dxUsers.getRank());
						EtutorApplication.getInstance().getSpUtil()
								.setUserName(dxUsers.getUserName());
						EtutorApplication.getInstance().getSpUtil()
								.setLabel(dxUsers.getIdentify());
						int count = dxUsers.getNewMsg();
						if (count == 0) {
							statu = false;
						} else {
							statu = true;
						}
						EtutorApplication.getInstance().getSpUtil()
								.setMsgStatu(statu);
						String avatarUrl = EtutorApplication.getInstance()
								.getSpUtil().getAvatarUrl();

						if (!TextUtils.isEmpty(avatarUrl)
								&& !TextUtils.isEmpty(dxUsers.getAvatarUrl())
								&& !avatarUrl.equals(dxUsers.getAvatarUrl())) {
							EtutorApplication.getInstance().getSpUtil()
									.setAvatarUrl(dxUsers.getAvatarUrl());
							String avatarName = dxUsers.getAvatarUrl()
									.substring(
											dxUsers.getAvatarUrl().lastIndexOf(
													"/") + 1);
							File file = new File(Constants.MyAvatarDir
									+ avatarName);
							if (!file.exists()) {
								getUserAvatar(dxUsers.getAvatarUrl());
							} else {
								EtutorApplication
										.getInstance()
										.getSpUtil()
										.setUserAvatar(
												"file:////sdcard/etutor/avatar/"
														+ avatarName);
							}
						}

					} catch (Exception e) {
						// TODO Auto-generated catch
						e.printStackTrace();
					}
				}
			}

		});

		return flag;

	}

	/**
	 * 异步获取头像
	 */
	public static void getUserAvatar(final String url) {
		String urlString = DataParam.REMOTE_SERVE + url;
		HttpUtil.get(urlString, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == Constants.STAT_200) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody,
							0, responseBody.length);
					String avatarName = url.substring(url.lastIndexOf("/") + 1);
					PictureUtil.saveBitmap(Constants.MyAvatarDir, avatarName,
							bitmap, true);
					EtutorApplication
							.getInstance()
							.getSpUtil()
							.setUserAvatar(
									"file:////sdcard/etutor/avatar/"
											+ avatarName);
				}
			}
		});

	}

}
