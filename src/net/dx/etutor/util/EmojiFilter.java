package net.dx.etutor.util;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 检测是否有emoji字符
 * 
 * @author 贾文庆
 * 
 */
public class EmojiFilter {

	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		boolean flag = false;
		if (StringUtils.isBlank(source)) {
			flag = false;
		} else {
			int len = source.length();

			for (int i = 0; i < len; i++) {
				char codePoint = source.charAt(i);
				if (isEmojiCharacter(codePoint)) {
					// do nothing，判断到了这里表明，确认有表情字符
					flag = true;
				}
			}
		}

		return flag;
	}

	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {

		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		} else {
			// 到这里铁定包含
			StringBuilder buf = null;

			int len = source.length();

			for (int i = 0; i < len; i++) {
				char codePoint = source.charAt(i);

				if (isEmojiCharacter(codePoint)) {
					if (buf == null) {
						buf = new StringBuilder(source.length());
					}

					buf.append(codePoint);
				} else {
				}
			}

			if (buf == null) {
				return source;// 如果没有找到 emoji表情，则返回源字符串
			} else {
				if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
					buf = null;
					return source;
				} else {
					return buf.toString();
				}
			}
		}

	}

	// 输入表情前的光标位置
	static int cursorPos;
	// 输入表情前EditText中的文本
	static String tmp;
	// 是否重置了EditText的内容
	static boolean resetText;

	/**
	 * edittext 检查emoji
	 * 
	 * @param activity
	 * @param editText
	 * @param textView
	 *            显示剩余字数
	 * @param total
	 *            最大长度
	 */
	public static void checkEmoji(final Activity activity,
			final EditText editText, final TextView textView, final int total) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (!resetText) {
					String input = s.toString() + " ";
					String str = EmojiFilter.filterEmoji(input);
					if (input.length() > str.length()) {
						resetText = true;
						// 是表情符号就将文本还原为输入表情符号之前的内容
						editText.setText(str);
						editText.invalidate();
						Toast.makeText(activity, "不支持表情输入", 0).show();
					} else {
						resetText = false;
					}
				} else {
					resetText = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (null != textView) {
					int count = total - s.length();
					textView.setText(count + "");
				}
			}
		});
	}
}
