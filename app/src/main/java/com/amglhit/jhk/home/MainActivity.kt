package com.amglhit.jhk.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.amglhit.jhk.R
import com.amglhit.jhk.base.BasePermissionActivity
import com.amglhit.jhk.base.locationGotWithPermissionCheck
import com.amglhit.jhk.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BasePermissionActivity() {

  private lateinit var viewModel: HomeViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme_Light)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    text_hello.setOnClickListener {
      startActivity(SplashActivity.newIntent(this))
    }

    viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

    viewModel.locationLiveData.observe(this, Observer {
      Timber.d("\n current location (lat:${it?.latitude} - lng:${it?.longitude});\n city: (${it?.cityCode} - ${it?.city}); \n adCode:${it?.adCode}; \n type:${it?.locationType}")
    })

    btn_open.setOnClickListener {
      locationGotWithPermissionCheck()
      //      locationPermissionGotWithPermissionCheck()
//      val intent = Intent(Intent.ACTION_VIEW)
//      intent.data = Uri.parse("mobike://launch?data=1")
//      startActivity(intent)
    }
  }

  override fun locationGot() {
    viewModel.locationLiveData.requestLocation()
  }
}
