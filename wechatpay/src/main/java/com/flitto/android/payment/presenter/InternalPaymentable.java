package com.flitto.android.payment.presenter;


import com.flitto.android.payment.model.UnifiedOrder;

public interface InternalPaymentable {
  void unifiedOrder(long productId);
  void verifyPurchase(UnifiedOrder unifiedOrder);

  interface Listener {
    void onOrdered(UnifiedOrder unifiedOrder);
    void onVerifiedPurchase();
  }
}
