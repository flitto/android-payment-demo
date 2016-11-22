package com.flitto.android.payment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.flitto.android.payment.model.UnifiedOrder;
import com.flitto.android.payment.payment.Payment;
import com.flitto.android.payment.payment.WechatPayment;
import com.flitto.android.payment.presenter.InternalPaymentPresenter;
import com.flitto.android.payment.presenter.InternalPaymentable;
import com.flitto.android.payment.view.LoadingView;

import wxapi.WXPayEntryActivity;

public class MainActivity extends AppCompatActivity implements LoadingView, Payment.Listener, InternalPaymentable.Listener {
  private static final String TAG = MainActivity.class.getSimpleName();

  private BroadcastReceiver receiver;

  private InternalPaymentable internalPaymentPresenter;
  private Payment wechatPayment;

  private long productId = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (receiver == null) {
      receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          if (intent.getAction().equals(WXPayEntryActivity.TAG)) {
            wechatPayment.onReceivedFromPayEntryActivity(intent);
          }
        }
      };
    }

    registerReceiver(receiver, new IntentFilter(WXPayEntryActivity.TAG));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    unregisterReceiver(receiver);
  }

  @Override
  public void showLoading() {
    Toast.makeText(this, "Show Loading...", Toast.LENGTH_LONG).show();
  }

  @Override
  public void hideLoading() {
    Toast.makeText(this, "Hide Loading...", Toast.LENGTH_LONG).show();
  }

  public void onStartPurchase(View v) {
    if (internalPaymentPresenter == null) {
      internalPaymentPresenter = new InternalPaymentPresenter(this);
    }

    internalPaymentPresenter.unifiedOrder(productId);
  }

  @Override
  public void onOrdered(UnifiedOrder unifiedOrder) {
    if (wechatPayment == null) {
      wechatPayment = new WechatPayment(this, this);
    }

    wechatPayment.purchaseOrder(unifiedOrder);
  }

  @Override
  public void onResultPurchase(UnifiedOrder unifiedOrder) {
    internalPaymentPresenter.verifyPurchase(unifiedOrder);
  }

  @Override
  public void onVerifiedPurchase() {
    Toast.makeText(this, "Succesful Payment!", Toast.LENGTH_LONG).show();
  }
}
