package com.flitto.android.payment.payment;


import android.content.Intent;

import com.flitto.android.payment.model.UnifiedOrder;

public interface Payment {
  void purchaseOrder(UnifiedOrder unifiedOrder);
  void onReceivedFromPayEntryActivity(Intent intent);

  interface Listener {
    void onResultPurchase(UnifiedOrder unifiedOrder);
  }
}
