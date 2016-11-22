package com.flitto.android.payment.model;


import com.google.gson.annotations.SerializedName;

public class UnifiedOrder {
  @SerializedName("order_id")
  private String orderId;

  @SerializedName("app_id")
  private String appId;

  @SerializedName("mch_id")
  private String partnerId;

  @SerializedName("prepay_id")
  private String prepayId;

  @SerializedName("nonce_str")
  private String nonceStr;

  @SerializedName("timestamp")
  private long timeStamp;

  @SerializedName("app_sign")
  private String sign;

  public String getOrderId() {
    return orderId;
  }

  public String getAppId() {
    return appId;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public String getPrepayId() {
    return prepayId;
  }

  public String getNonceStr() {
    return nonceStr;
  }

  public String getTimeStamp() {
    return String.valueOf(timeStamp);
  }

  public String getSign() {
    return sign;
  }
}
