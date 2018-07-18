package com.amglhit.jhk.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.amglhit.jhk.R
import com.amglhit.jhk.base.BaseMapActivity
import com.amglhit.jhk.base.BasePermissionActivity
import com.amglhit.jhk.base.locationGotWithPermissionCheck
import com.amglhit.jhk.splash.SplashActivity
import com.crashlytics.android.Crashlytics
//import com.google.firebase.perf.metrics.AddTrace
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BasePermissionActivity() {
  private lateinit var viewModel: HomeViewModel
  //  @AddTrace(name = "onCreateMain")
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
      val intent = BaseMapActivity.newIntent(this)
      startActivity(intent)
    }

    btn_read.setOnClickListener {
      readSP()
    }
    btn_write.setOnClickListener {
      writeSP()
    }

    btn_start.setOnClickListener {
      locationGotWithPermissionCheck()
    }
    btn_stop.setOnClickListener {
      viewModel.locationLiveData.stopLocationUpdate()
    }

    btn_crash.setOnClickListener {
      Crashlytics.getInstance().crash()
    }
  }

  override fun locationGot() {
    viewModel.locationLiveData.requestLocationUpdate()
  }

  private var index = 1

  private fun writeSP() {
    index++
    viewModel.homeSP.homeName = "test-name $index"
    viewModel.homeSP.user = HomeUser("test_user  $index", 1, city = UserCity("BeiJing"))
    Thread {
      Timber.d("change state at |${Thread.currentThread()}")
      viewModel.userState.value = "state - $index"
    }.start()
  }

  private fun readSP() {
    Timber.d("read from home sp ${viewModel.homeSP.homeName}")
    Timber.d("read from home sp ${viewModel.homeSP.user}")
  }
}
