package com.flitto.android.payment.presenter;


import com.flitto.android.payment.model.UnifiedOrder;
import com.google.gson.Gson;

public class InternalPaymentPresenter implements InternalPaymentable {

  private Listener listener;

  public InternalPaymentPresenter(Listener listener) {
    this.listener = listener;
  }

  @Override
  public void unifiedOrder(long productId) {
    // TODO: Request unified order to internal server with product indentifier

    String jsonResult = "";

    Gson gson = new Gson();
    UnifiedOrder unifiedOrder = gson.fromJson(jsonResult, UnifiedOrder.class);

    listener.onOrdered(unifiedOrder);
  }

  @Override
  public void verifyPurchase(UnifiedOrder unifiedOrder) {
    // TODO: Verify purchase to internal server

    listener.onVerifiedPurchase();
  }
}
