package com.flitto.android.payment.payment;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.flitto.android.payment.global.WechatPayConstants;
import com.flitto.android.payment.model.UnifiedOrder;
import com.flitto.android.payment.view.LoadingView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import wxapi.WXPayEntryActivity;

public class WechatPayment implements Payment {
  private static final String TAG = WechatPayment.class.getSimpleName();

  private LoadingView view;
  private Listener listener;

  private UnifiedOrder unifiedOrder;

  public WechatPayment(LoadingView view, Listener listener) {
    this.view = view;
    this.listener = listener;
  }

  @Override
  public void purchaseOrder(UnifiedOrder unifiedOrder) {
    this.unifiedOrder = unifiedOrder;

    IWXAPI api = WXAPIFactory.createWXAPI((Context) view, WechatPayConstants.APP_ID);
    api.registerApp(WechatPayConstants.APP_ID);

    if (api.isWXAppInstalled()) {
      PayReq request = new PayReq();
      request.appId = unifiedOrder.getAppId();
      request.nonceStr = unifiedOrder.getNonceStr();
      request.packageValue = "Sign=WXPay";
      request.partnerId = unifiedOrder.getPartnerId();
      request.prepayId = unifiedOrder.getPrepayId();
      request.timeStamp = String.valueOf(unifiedOrder.getTimeStamp());
      request.sign = unifiedOrder.getSign();
      api.sendReq(request);
    } else {
      Toast.makeText((Context) view, "WeChat is not installed!", Toast.LENGTH_LONG).show();
      view.hideLoading();
    }
  }

  @Override
  public void onReceivedFromPayEntryActivity(Intent intent) {
    if (intent.getExtras().getInt("errCode") == WXPayEntryActivity.PAYMENT_SUCCESS) {
      listener.onResultPurchase(unifiedOrder);
    } else if (intent.getExtras().getInt("errCode") == WXPayEntryActivity.PAYMENT_USER_CANCELED) {
      Toast.makeText((Context) view, "User Canceled!", Toast.LENGTH_LONG).show();
      view.hideLoading();
    } else {
      Toast.makeText((Context) view, "Payment Error!", Toast.LENGTH_LONG).show();
      view.hideLoading();
    }
  }
}
