package wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.flitto.android.payment.global.WechatPayConstants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
  public static final String TAG = "WXPayEntryActivity";

  public static final int PAYMENT_SUCCESS = 0;
  public static final int PAYMENT_ERROR = -1;
  public static final int PAYMENT_USER_CANCELED = -2;

  private IWXAPI api;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(new View(this));

    api = WXAPIFactory.createWXAPI(this, WechatPayConstants.APP_ID);
    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override
  public void onReq(BaseReq baseReq) {
  }

  @Override
  public void onResp(BaseResp baseResp) {
    if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
      Log.d(TAG, " onPayFinish, errCode = " + baseResp.errCode
          + " , errStr = " + baseResp.errStr
          + " , args = " + baseResp.checkArgs()
          + " , openId = " + baseResp.openId
          + " , transction = " + baseResp.transaction);

      Intent intent = new Intent(TAG);
      intent.putExtra("errCode", baseResp.errCode);
      sendBroadcast(intent);
      finish();
    }
  }
}
