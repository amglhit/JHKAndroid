package com.amglhit.jhk.base

import android.Manifest
import com.amglhit.msuite.toast
import permissions.dispatcher.*

@RuntimePermissions
abstract class BasePermissionActivity : BaseActivity() {

  @NeedsPermission(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  )
  open fun locationGot() {
    toast("permission location got")
  }

  @OnShowRationale(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  )
  fun locationRationale(request: PermissionRequest) {
    toast("permission location rationale")
  }

  @OnPermissionDenied(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  )
  fun locationDenied() {
    toast("permission location denied")
  }

  @OnNeverAskAgain(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  )
  fun locationNeverAsk() {
    toast("permission location never ask again")
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    this@BasePermissionActivity.onRequestPermissionsResult(requestCode, grantResults)
  }
}