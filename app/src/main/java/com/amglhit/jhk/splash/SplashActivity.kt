package com.amglhit.jhk.splash

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amglhit.jhk.R
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber

class SplashActivity : AppCompatActivity() {
  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, SplashActivity::class.java)
    }
  }

  private lateinit var viewModel: SplashViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

    bindViewModel()

    startSplash()
  }

  private fun bindViewModel() {
    viewModel.endSplashLiveData.observe(this, Observer {
      val isEnd = it?.getAndHandleIfNotHandled() ?: false
      if (isEnd) {
        Timber.d("splash end")
        endSplash()
      }
    })

    viewModel.splashTimeRemainsLiveData.observe(this, Observer {
      Timber.d("splash remains: $it")
      text_time_remains.text = it?.toString()
    })
  }

  private fun startSplash() {
    viewModel.startSplash()
  }

  private fun endSplash() {
    setResult(Activity.RESULT_OK)
    finish()
  }
}
