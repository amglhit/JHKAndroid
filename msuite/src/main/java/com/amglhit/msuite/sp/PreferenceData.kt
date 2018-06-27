package com.amglhit.msuite.sp

import android.content.Context
import kotlin.properties.ReadWriteProperty

abstract class PreferenceData(
  private val context: Context,
  private val fileName: String
) {

  private val pref by lazy {
    context.getSharedPreferences(
      fileName,
      Context.MODE_PRIVATE
    )
  }

  public fun clear() {
    return pref.edit().clear().apply()
  }

  public fun remove(key: String) {
    if (key.isEmpty())
      return
    pref.edit().remove(key).apply()
  }

  public fun getString(key: String, default: String = ""): String {
    return pref.getString(key, default)
  }

  public fun getInt(key: String, default: Int = -1): Int {
    return pref.getInt(key, default)
  }

  public fun getFloat(key: String, default: Float = -1F): Float {
    return pref.getFloat(key, default)
  }

  public fun getLong(key: String, default: Long = -1L): Long {
    return pref.getLong(key, default)
  }

  public fun getBoolean(key: String, default: Boolean = false): Boolean {
    return pref.getBoolean(key, default)
  }

  public fun putString(key: String, value: String) {
    if (key.isEmpty())
      return
    pref.edit().putString(key, value).apply()
  }

  public fun putInt(key: String, value: Int) {
    if (key.isEmpty())
      return
    pref.edit().putInt(key, value).apply()
  }

  public fun putLong(key: String, value: Long) {
    if (key.isEmpty())
      return
    with(pref.edit()) {
      putLong(key, value).apply()
    }
  }

  public fun putFloat(key: String, value: Float) {
    if (key.isEmpty())
      return
    pref.edit().putFloat(key, value).apply()
  }

  public fun putBoolean(key: String, value: Boolean) {
    if (key.isEmpty())
      return
    pref.edit().putBoolean(key, value).apply()
  }

  /**
   * 代码少但难看的方式
   */
  protected inline fun <reified T : Any> bindPreference(
    key: String = "",
    default: T
  ): ReadWriteProperty<PreferenceData, T> {
    return when (T::class.java) {
      String::class.java -> {
        PrefString(key, default as String) as ReadWriteProperty<PreferenceData, T>
      }
      Int::class.java -> {
        PrefInt(key, default as Int) as ReadWriteProperty<PreferenceData, T>
      }
      Long::class.java -> {
        PrefLong(key, default as Long) as ReadWriteProperty<PreferenceData, T>
      }
      Float::class.java -> {
        PrefFloat(key, default as Float) as ReadWriteProperty<PreferenceData, T>
      }
      Boolean::class.java -> {
        PrefBoolean(key, default as Boolean) as ReadWriteProperty<PreferenceData, T>
      }
      else -> {
        throw UnsupportedOperationException("Unsupported class ${T::class.java}")
      }
    }
  }

  public fun <T> putValue(key: String, value: T) {
    if (key.isEmpty())
      return
    pref.edit().apply {
      when (value) {
        is String ->
          putString(key, value as String)
        is Int ->
          putInt(key, value as Int)
        is Long ->
          putLong(key, value as Long)
        is Float ->
          putFloat(key, value as Float)
        is Boolean ->
          putBoolean(key, value as Boolean)
      }
    }.apply()
  }

  public fun <T> getValue(key: String, default: T): T {
    if (key.isEmpty())
      return default
    return when (default) {
      is String -> pref.getString(key, default as String) as T
      is Int -> pref.getInt(key, default as Int) as T
      is Float -> pref.getFloat(key, default as Float) as T
      is Long -> pref.getLong(key, default as Long) as T
      is Boolean -> pref.getBoolean(key, default as Boolean) as T
      else -> default
    }
  }
}