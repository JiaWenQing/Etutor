package net.dx.etutor.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.dx.etutor.R;
import net.dx.etutor.activity.base.BaseActivity;
import net.dx.etutor.activity.home.MyUploadPreviewActivity;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.data.UrlEngine;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 上传头像Util
 * 
 * @author app
 * 
 */
public class UpdateHeadIconUtil {
	// 上传文件响应
	protected static final int UPLOAD_FILE_DONE = 2;
	// private static String path;
//	private static String filePath;
	/**
	 * 0、头像 1、身份证 2、证件照 3、私信,4,帖子图片
	 */
	private static int flag;
	private static String files;
	private static String style;

	/**
	 * 更新头像 refreshAvatar
	 * 
	 * @return void
	 * @throws
	 */
	public static void refreshAvatar(ImageView mUserHead) {
		String avatar = EtutorApplication.getInstance().getSpUtil()
				.getUserAvatar();
		if (!TextUtils.isEmpty(avatar)) {
			ImageLoader.getInstance().displayImage(avatar, mUserHead,
					ImageLoadOptionsUtil.getOptions());
		} else {
			mUserHead.setImageResource(R.drawable.default_avatar);
		}
	}

	/**
	 * 更新证件照 refreshIdCard
	 * 
	 * @return void
	 * @throws
	 */
	public static void refreshIdCard(ImageView mIdCard) {
		String path = EtutorApplication.getInstance().getSpUtil()
				.getUploadPath();
		if (!TextUtils.isEmpty(path)) {
			ImageLoader.getInstance().displayImage(path, mIdCard,
					ImageLoadOptionsUtil.getOptions());
		} else {
			mIdCard.setImageResource(R.drawable.upload_idcard);
		}
	}

	public static void startImageAction(Activity activity, Uri uri,
			int outputX, int outputY, int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开相机或相册上传头像
	 * 
	 * @param activity
	 * @param v
	 */
	@SuppressLint("SimpleDateFormat")
	public static void choiceOnClick(Activity activity, View v, int flags) {
		Intent intent = null;
		flag = flags;
		files = Constants.MyAvatarDir;
		style = ".png";
		switch (flag) {
		case 0:
			files = Constants.MyAvatarDir;
			style = ".png";
			break;
		case 1:
			files = Constants.MyIdCardDir;
			style = ".jpg";
			break;
		case 2:
			files = Constants.MyCredentialsDir;
			style = ".jpg";
			break;
		case 3:
			files = Constants.PICTURE_PATH;
			style = ".jpg";
			break;
		case 4:  //帖子图片
			files = Constants.MyTopicDir;
			style = ".png";
			break;
		default:
			files = Constants.MyAvatarDir;
			style = ".png";
			break;
		}
		switch (v.getId()) {
		case R.id.photo_camera:
			File dir = new File(files);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir,
					new SimpleDateFormat("yyMMddHHmmss").format(new Date())
							+ style);
			EtutorApplication.filePath = file.getAbsolutePath();// 获取相片的保存路径
			Uri imageUri = Uri.fromFile(file);

			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			activity.startActivityForResult(intent,
					Constants.REQUESTCODE_UPLOADAVATAR_CAMERA);
			break;
		case R.id.photo_gallery:
			intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			activity.startActivityForResult(intent,
					Constants.REQUESTCODE_UPLOADAVATAR_LOCATION);
			break;
		case R.id.photo_cancel:
			break;
		default:
			break;
		}
	}

	/**
	 * 处理activity头像返回
	 * 
	 * @param activity
	 * @param imageView
	 *            头像控件
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public static void onActivityResultToHead(Activity activity,
			ImageView imageView, int requestCode, int resultCode, Intent data,
			TextView mStatus) {
		if (resultCode != Activity.RESULT_OK) {// result is not correct
			return;
		} else {
			switch (requestCode) {
			case Constants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改
				if (resultCode == Activity.RESULT_OK) {
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						Toast.makeText(activity, "SD不可用", 0).show();
						return;
					}
					File file = new File(EtutorApplication.filePath);
					Bitmap bitmap;
					if (flag == 0) {
						UpdateHeadIconUtil.startImageAction(activity,
								Uri.fromFile(file), 200, 200,
								Constants.REQUESTCODE_UPLOADAVATAR_CROP, true);
					} else {
						bitmap = PictureUtil.getimage(EtutorApplication.filePath, file.length());
						PictureUtil.saveBitmap(files, file.getName(), bitmap,
								true);
						if (flag == 1) {
							uploadAvatar(imageView, false);
							Toast.makeText(activity, "证件照上传成功！", 0).show();
							mStatus.setText("上传手持证件照(待审核)");
						} else if (flag == 2) {
							Intent intent = new Intent(activity,
									MyUploadPreviewActivity.class);
							intent.putExtra("path", EtutorApplication.filePath);
							activity.startActivityForResult(intent,
									Constants.REQUESTCODE_UPLOADAVATAR_PREVIEW);
						}
					}
				}
				break;
			case Constants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改
				Uri uri = null;
				if (data == null) {
					return;
				}
				if (resultCode == Activity.RESULT_OK) {
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						Toast.makeText(activity, "SD不可用", 0).show();
						return;
					}
					uri = data.getData();
					if (flag == 0) {
						UpdateHeadIconUtil.startImageAction(activity, uri, 200,
								200, Constants.REQUESTCODE_UPLOADAVATAR_CROP,
								true);
					} else {
						String img_path;
						if (!TextUtils.isEmpty(uri.getAuthority())) {    
					        Cursor cursor = activity.getContentResolver().query(uri,  
					                new String[] { MediaStore.Images.Media.DATA },null, null, null);    
					        if (null == cursor) { 
					        	Toast.makeText(activity, "图片没找到", 0).show();
					            return;   
					        }    
					        cursor.moveToFirst();    
					        img_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));  
					        cursor.close();
					    } else {    
					    	img_path = uri.getPath();    
					    }
						File file = new File(img_path);
						Bitmap bitmap = PictureUtil.getimage(img_path,
								file.length());
						PictureUtil.saveBitmap(files, file.getName(), bitmap,
								true);
						EtutorApplication.filePath = files + file.getName();
						if (flag == 1) {
							uploadAvatar(imageView, false);
							Toast.makeText(activity, "证件照上传成功！", 0).show();
							mStatus.setText("上传手持证件照(待审核)");
						} else if (flag == 2) {
							Intent intent = new Intent(activity,
									MyUploadPreviewActivity.class);
							intent.putExtra("path", EtutorApplication.filePath);
							activity.startActivityForResult(intent,
									Constants.REQUESTCODE_UPLOADAVATAR_PREVIEW);
						}
					}

				} else {
					Toast.makeText(activity, "照片获取失败", 0).show();
				}

				break;
			case Constants.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
				// TODO sent to crop
				if (data != null) {
					saveCropAvator(data, imageView);
					uploadAvatar(imageView, true);
					Toast.makeText(activity, "头像上传成功！", 0).show();
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 上传头像、身份证
	 * 
	 * @param mUserHead
	 * @param flag
	 */
	public static void uploadAvatar(ImageView mUserHead, boolean flag) {
		File file = new File(EtutorApplication.filePath);
		Map<String, String> param = new HashMap<>();
		String type = "png";
		String fileKey = "img";

		if (flag) {
			EtutorApplication.getInstance().getSpUtil()
					.setUserAvatar("file:///" + file.getAbsolutePath());
			refreshAvatar(mUserHead);
			param.put("avatarUrl", file.getAbsolutePath());
			type = "png";

		} else {
			EtutorApplication.getInstance().getSpUtil()
					.setUploadPath("file:///" + file.getAbsolutePath());
			refreshIdCard(mUserHead);
			param.put("certificatePhotoUrl", file.getAbsolutePath());
			type = "jpg";
		}
		param.put("type", type);
		UploadHeadUtil uploadUtil = UploadHeadUtil.getInstance();
		File fileHead = new File(file.getAbsolutePath());
		uploadUtil.uploadFile(fileHead, fileKey, UrlEngine.uploadHead(), param);
	}

	/**
	 * 保存裁剪的头像
	 * 
	 * @param data
	 */
	@SuppressLint("SimpleDateFormat")
	public static void saveCropAvator(Intent data, ImageView mUserHead) {
		Bundle extras = data.getExtras();
		String filename = null;
		String dirpath = null;
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			mUserHead.setImageBitmap(bitmap);
			filename = new SimpleDateFormat("yyMMddHHmmss").format(new Date())
					+ ".png";
			dirpath = Constants.MyAvatarDir;
			EtutorApplication.filePath = dirpath + filename;
			PictureUtil.saveBitmap(dirpath, filename, bitmap, true);
			if (bitmap != null && bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}

	public static String getFilePath() {
		// TODO Auto-generated method stub
		return EtutorApplication.filePath;
	}
}
