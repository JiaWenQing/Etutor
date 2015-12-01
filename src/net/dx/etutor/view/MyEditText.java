package net.dx.etutor.view;

import java.util.ArrayList;

import net.dx.etutor.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MyEditText extends LinearLayout {
	private ArrayList<EditText> array = new ArrayList<EditText>();
	private EditText mEdt_parking_one;
	private EditText mEdt_parking_two;
	private EditText mEdt_parking_three;
	private EditText mEdt_parking_four;
	private TextWatcher tw_pwd;
	private String inputnumber = "";  
	private onKeyListeners onkeylistener; 
	private int count = 0;

	private OnEditTextListener onEditTextListener;


	public OnEditTextListener getOnEditTextListener() {  
		return onEditTextListener;  
	}  

	public void setOnEditTextListener(OnEditTextListener onEditTextListener) {  
		this.onEditTextListener = onEditTextListener;  
	}  

	public interface OnEditTextListener{  
		public void  inputComplete(int state,String password);  
	}  

	public MyEditText(Context context) {    
		this(context, null);    
	}    

	public MyEditText(Context context, AttributeSet attrs) {    
		super(context, attrs);    
		View v = LayoutInflater.from(context).inflate(R.layout.cus_edit, this, true);

		setView(v);
		setListener(context);
	}

	private void setView(View v) {
		mEdt_parking_one = (EditText)v.findViewById(R.id.mEdt_parking_one);
		mEdt_parking_two = (EditText)v.findViewById(R.id.mEdt_parking_two);
		mEdt_parking_three = (EditText)v.findViewById(R.id.mEdt_parking_three);
		mEdt_parking_four = (EditText)v.findViewById(R.id.mEdt_parking_four);
		mEdt_parking_two.setFocusable(false);
		mEdt_parking_four.setFocusable(false);
		mEdt_parking_three.setFocusable(false);
		array.add(mEdt_parking_one);
		array.add(mEdt_parking_two);
		array.add(mEdt_parking_three);
		array.add(mEdt_parking_four);
	}
	
	private void setFocusToView(int  target){
		for(int i = 0;i < array.size() ;i++){
			array.get(i).setFocusable(true);
			array.get(i).setFocusableInTouchMode(true);
			if(i != target){
				array.get(i).setFocusable(false);
			}
		}
	}

	private void setListener(Context context) {
		onkeylistener  =  new onKeyListeners();  
		editPwdWatcher(context);
		//设置字符改变监听  
		mEdt_parking_one.addTextChangedListener(tw_pwd);  
		mEdt_parking_two.addTextChangedListener(tw_pwd);  
		mEdt_parking_three.addTextChangedListener(tw_pwd);  
		mEdt_parking_four.addTextChangedListener(tw_pwd);  
		//删除按钮监听  
		mEdt_parking_one.setOnKeyListener(onkeylistener);  
		mEdt_parking_two.setOnKeyListener(onkeylistener);  
		mEdt_parking_three.setOnKeyListener(onkeylistener);  
		mEdt_parking_four.setOnKeyListener(onkeylistener);  
	}

	/**
	 * 字符改变监听
	 * @param context
	 */
	private void editPwdWatcher(final Context context) {
		tw_pwd = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()==1){
					if(mEdt_parking_one.isFocusable()){
						mEdt_parking_two.setFocusable(true);
						mEdt_parking_two.setFocusableInTouchMode(true);
					}else if(mEdt_parking_two.isFocusable()){
						mEdt_parking_three.setFocusable(true);
						mEdt_parking_three.setFocusableInTouchMode(true);
					}else if(mEdt_parking_three.isFocusable()){
						mEdt_parking_four.setFocusable(true);
						mEdt_parking_four.setFocusableInTouchMode(true);
					}else {
						b=true;
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() == 1) {
					if (mEdt_parking_one.isFocused()) {  
						mEdt_parking_one.setFocusable(false);
						mEdt_parking_two.requestFocus();
					}else if (mEdt_parking_two.isFocused()) { 
						mEdt_parking_two.setFocusable(false);
						mEdt_parking_three.requestFocus(); 
					}else if(mEdt_parking_three.isFocused()){
						mEdt_parking_three.setFocusable(false);
						mEdt_parking_four.requestFocus();
					}else if(mEdt_parking_four.isFocused()){
						InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);  
						imm.hideSoftInputFromWindow(mEdt_parking_four.getWindowToken(), 0);  
						inputnumber = getEditNumber();
						Log.i("MainActivity", inputnumber.length()+"");
						if (onEditTextListener != null) {
							onEditTextListener.inputComplete(1, inputnumber);
						}
					}
				}
			}
		};

	}

	boolean b=false;
	/**
	 * 删除按钮监听
	 */
	class onKeyListeners implements OnKeyListener{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_DEL) {
				count++;
				if (count < 2) {
					return false;
				}
				count=0;
				inputnumber = "";
				if(mEdt_parking_four.isFocused()){
					if (!b) {
						mEdt_parking_four.clearFocus();
						mEdt_parking_four.setFocusable(false);
						mEdt_parking_three.setFocusableInTouchMode(true);
						mEdt_parking_three.getText().clear();
						mEdt_parking_three.requestFocus();
						b=true;
					}else {
						b=false;
					}
				}else if(mEdt_parking_three.isFocused()){
					mEdt_parking_three.clearFocus();
					mEdt_parking_three.setFocusable(false);
					mEdt_parking_two.setFocusableInTouchMode(true);
					mEdt_parking_two.getText().clear();
					mEdt_parking_two.requestFocus();
				}else if(mEdt_parking_two.isFocused()){
					mEdt_parking_two.clearFocus();
					mEdt_parking_two.setFocusable(false);
					mEdt_parking_one.setFocusableInTouchMode(true);
					mEdt_parking_one.getText().clear();
					mEdt_parking_one.requestFocus();
				}
			}
			return false;
		}

	}

	public String getEditNumber(){  
		String number = mEdt_parking_one.getText().toString();  
		number+=mEdt_parking_two.getText().toString();  
		number+=mEdt_parking_three.getText().toString();  
		number+=mEdt_parking_four.getText().toString();  
		return number;  
	}  
}