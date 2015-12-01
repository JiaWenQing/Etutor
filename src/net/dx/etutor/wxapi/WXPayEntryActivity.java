package net.dx.etutor.wxapi;


import net.dx.etutor.R;
import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.util.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WEIXIN);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		System.out.println(resp.errStr+" onPayFinish, errCode = " + resp.errCode);
		boolean flag=false;
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				// 成功
				showShortToast(R.string.text_deal_ok);
				flag=true;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				// 取消
				showShortToast(R.string.text_deal_cancel);
				break;
			case BaseResp.ErrCode.ERR_COMM:
				// 失败
				showShortToast(R.string.text_deal_fail);
				break;
			}
		}
		EtutorApplication.getInstance().setWeiXinPay(flag);
		finish();
	}
	private void showShortToast(int id) {
		// TODO Auto-generated method stub
		Toast.makeText(this, id, 0).show();
		
	}
}