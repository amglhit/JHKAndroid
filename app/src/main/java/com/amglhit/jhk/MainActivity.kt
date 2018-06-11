package com.amglhit.jhk

import android.os.Bundle
import com.amglhit.jhk.base.BaseActivity
import com.amglhit.jhk.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme_Light)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    text_hello.setOnClickListener {
      startActivity(SplashActivity.newIntent(this))
    }
  }
}
