# android-payment-demo

Android demo application built with [WechaPay](https://pay.weixin.qq.com/index.php/core/home/login?return_url=%2F)

Android development doc : [APP端开发步骤说明](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5) translate to [Korean](https://github.com/flitto/android-payment-demo/blob/master/ANDROID_DEV_DOC_KR.md)

Unified order API doc : [统一下单](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1)

## Usage

Wechatpay API doc : [调起支付接口](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2)

**Open Wechat Payment Popup**
```java
public void purchaseOrder(UnifiedOrder unifiedOrder) {
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
  }
}
```

