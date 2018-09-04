package com.amglhit.msuite.toast;

import android.os.Handler;
import android.os.Message;

class SafelyHandlerWarpper extends Handler {

  private Handler impl;

  SafelyHandlerWarpper(Handler impl) {
    this.impl = impl;
  }

  @Override
  public void dispatchMessage(Message msg) {
    try {
      super.dispatchMessage(msg);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void handleMessage(Message msg) {
    impl.handleMessage(msg);
  }
}
