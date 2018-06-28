package com.amglhit.jhk.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.amglhit.jhk.R
import com.amglhit.jhk.base.ui.basemap.BaseMapFragment

class BaseMapActivity : BaseActivity() {
  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, BaseMapActivity::class.java)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.base_map_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.map_container, BaseMapFragment.newInstance())
        .commitNow()
    }
  }
}
