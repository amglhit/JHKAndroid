package com.amglhit.jhk

//import com.facebook.soloader.SoLoader
//import com.facebook.sonar.android.utils.SonarUtils
//import com.facebook.sonar.android.AndroidSonarClient
//import com.facebook.sonar.core.SonarClient
import com.amglhit.jhk.app.JHKApplication
import com.facebook.stetho.Stetho

class DebugApplication : JHKApplication() {
  //  private lateinit var sonarClient: SonarClient
  override fun onCreate() {
    super.onCreate()
    Stetho.initializeWithDefaults(this)
//
//    SoLoader.init(this, 0)
//    if (SonarUtils.shouldEnableSonar(this)) {
//      sonarClient = AndroidSonarClient.getInstance(this)
//      sonarClient.start()
//      Timber.d("start sonar")
//    }
  }
}