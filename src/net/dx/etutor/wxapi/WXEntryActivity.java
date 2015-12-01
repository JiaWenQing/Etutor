package net.dx.etutor.wxapi;

import net.dx.etutor.R;
import net.dx.etutor.util.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WEIXIN, false);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq arg0) {
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// 分享成功
			showShortToast(R.string.text_share_ok);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// 分享取消
			showShortToast(R.string.text_share_cancel);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// 分享拒绝
			showShortToast(R.string.text_share_fail);
			break;
		}
		this.finish();
	}

	private void showShortToast(int id) {
		// TODO Auto-generated method stub
		Toast.makeText(this, id, 0).show();
		
	}
}