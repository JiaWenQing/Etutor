package net.dx.etutor.util;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

/**
 * 录音管理工具类
 * 
 * @author 贾文庆
 * 
 */
public class RecordMangerUtil {
	/** 录音后文件 */
	private File file;
	/** android媒体录音类 */
	private MediaRecorder mr;
	/** 声波振幅监听器 */
	private SoundAmplitudeListen soundAmplitudeListen;
	/** 录音存储路径 */
	private static final String PATH = Constants.VOICE_PATH;

	/** 启动计时器监听振幅波动 */
	private final Handler mHandler = new Handler();
	private Runnable mUpdateMicStatusTimer = new Runnable() {
		/**
		 * 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600
		 */
		private int BASE = 600;
		private int RATIO = 5;
		private int postDelayed = 200;

		public void run() {
			// int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;
			int ratio = mr.getMaxAmplitude() / BASE;
			int db = (int) (20 * Math.log10(Math.abs(ratio)));
			int value = db / RATIO;
			if (value < 0)
				value = 0;
			if (soundAmplitudeListen != null)
				soundAmplitudeListen.amplitude(ratio, db, value);
			mHandler.postDelayed(mUpdateMicStatusTimer, postDelayed);

		}
	};

	/**
	 * 启动录音并生成文件
	 * 
	 * @throws IOException
	 */
	public void startRecordCreateFile() throws IOException {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		file = new File(PATH + System.currentTimeMillis() + ".aac");
		mr = new MediaRecorder(); // 创建录音对象
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置MediaRecorder的音频源为麦克风
		mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		// 设置MediaRecorder录制的音频格式
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mr.setOutputFile(file.getAbsolutePath());// 设置输出文件
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();// 创建目录
		}
		// 创建文件
		file.createNewFile();
		// 准备录制
		mr.prepare();
		// 开始录制
		mr.start();
		// 启动振幅监听计时器
		mHandler.post(mUpdateMicStatusTimer);
	}

	/**
	 * 停止录音并返回录音文件
	 * 
	 */
	public File stopRecord() {

		if (mr != null) {
			mr.stop();
			mr.release();
			mr = null;
			mHandler.removeCallbacks(mUpdateMicStatusTimer);
		}
		return file;

	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public MediaRecorder getMr() {
		return mr;
	}

	public void setMr(MediaRecorder mr) {
		this.mr = mr;
	}

	public SoundAmplitudeListen getSoundAmplitudeListen() {
		return soundAmplitudeListen;
	}

	public void setSoundAmplitudeListen(
			SoundAmplitudeListen soundAmplitudeListen) {
		this.soundAmplitudeListen = soundAmplitudeListen;
	}

	public interface SoundAmplitudeListen {
		public void amplitude(int amplitude, int db, int value);
	}

}