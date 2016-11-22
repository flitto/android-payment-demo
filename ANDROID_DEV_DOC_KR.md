# Android 개발 요점 설명

## 1. 백그라운드 설정

위챗 오픈플랫폼에서 개발 어플리케이션을 신청하시면 위챗 오픈플랫폼에서는 어플리케이션의 고유 식별 아이디를 생성해 드립니다. 안전한 결제를 위해 오픈플랫폼에 업체의 어플리케이션 패키지 이름과 어플리케이션 서명(signature)을 연동시키셔야 합니다. 연동 후에 정상적으로 결제가 가능합니다. 설정화면은 <오픈플랫폼>의 <관리센터/어플리케이션 수정/개발데이터 수정>메뉴에 있으며 그림 8.8의 빨간 네모로 표시되어 있는 부분입니다.

![](https://github.com/flitto/android-payment-demo/raw/master/assets/wechatpay_161021_chapter8_5_2.png)

패키지명: 어플리케이션 프로젝트의 AndroidManifest.xml에 설정한 package 이름값이다. 예를들어 DEMO에서 package값은 net.sourceforge.simcpux.
어플리케이션 서명: 프로젝트의 패키지명 및 컴파일에 사용된 keystore을 signing툴을 사용하여 생성한 32비트길이의 MD5문자열입니다. 테스트기기에 signing툴을 설치하고 실행하면 application의 서명을 생성합니다. 그림8.9와 같이 녹색문자열이 곧 어플리케이션의 서명이다. signing툴 다운로드 url https://open.weixin.qq.com/zh_CN/htmledition/res/dev/download/sdk/Gen_Signature_Android.apk
 
![](https://github.com/flitto/android-payment-demo/raw/master/assets/wechatpay_161021_chapter8_5_3.png)
 
## 2. APP ID 등록하기

WeChat jar패킷을 개발자의 앱프로젝트로 가져옵니다. 하지만,api를 사용하려면 우선 위챗으로 APPID를 등록해야 합니다. 코드는 아래와 같습니다:

```java
final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
// app id를 위챗에 등록하기
msgApi.registerApp("wxd930ea5d5a258f4f");
```
 
## 3. 결제 호출
업체서버(개발 서버)로부터 결제 청구를 생성합니다. 먼저 통합구매API를 호출해(자세한 내용은 7장 참조) 구매리스트를 생성 후, prepay_id 값을 받아서 다시 사인 후 앱에 전송하여 결제 청구를 제출한다. 아래는 웨이신 결제를 호출하는 메인 코드입니다.

```java
IWXAPI api;
PayReq request = new PayReq();
request.appId = "wxd930ea5d5a258f4f";
request.partnerId = "1900000109";
request.prepayId= "1101000000140415649af9fc314aa427",;
request.packageValue = "Sign=WXPay";
request.nonceStr = "1101000000140429eb40476f8896f4c9";
request.timeStamp = "1398746574";
request.sign = "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
api.sendReq(req);
```

주의: sign 필드명으로 생성되는 리스트는 API 발급 설정을 참고하세요.
 
## 4. 지불결과 콜백
위쳇 SDK Sample을 참조하여, net.sourceforge.simcpux.wxapi 패캣경로에서 WXPayEntryActivity클래스를 생성합니다.(패킷명 혹은 클래스명이 일치하지 않으면 콜백이 불가능합니다), WXPayEntryActivity클래스에서 onResp 함수 구현, 지불완료 후 위쳇 엡은 판매자 어플리케이션으로 돌아가면서 onResp 함수를 콜백합니다. 개발자는 해당 함수내에서 알림메세지를 받아야하며 기존으로 돌아가기 위한 에러코드를 판단해야 합니다. 지불 성공 후 백오피스로 가서 지불결과를 체크한 후, 유저의 실제 지불결과를 보여줍니다. 클라이언트의 복귀를 유저 결제결과로 적용하지 않도록 꼭 주의해야 합니다. 서버사이드에서 받은 결제 알림메세지 혹은 API복귀결과를 체크하여 기준으로 해야 합니다, 코드예시는 다음과 같습니다:

```java
@Override
public void onResp(BaseResp resp) {
  if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
    Log.d(TAG,"onPayFinish,errCode=" + resp.errCode);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.app_tip);
  }
}
```
 
콜백한 errCode 리스트：

| 명칭 | 기술 | 솔루션 |
|------|------------|---------------------------------------------------------------------------------------------------------------------------|
| 0 | 성공 | 성공화면 |
| -1 | 오류 | 발생가능한이유 : 사인(sign)오류, APPID 미등록, 프로젝트상에서 APPID설정오류, 등록된APPID와 세팅한것 맞지 않는 경우, 그 외 |
| -2 | 사용자취소 | 발생가능한이유 : 사용자취소버튼 누르고 APP으로 복귀 |
