package com.amglhit.msuite.sp

import com.google.gson.Gson
import java.lang.Exception
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class PreferenceDelegate<T>(
  private val key: String = ""
) : ReadWriteProperty<PreferenceData, T> {
  protected fun getPrefKey(property: KProperty<*>): String {
    return if (key.isEmpty()) property.name else key
  }

  abstract override fun getValue(thisRef: PreferenceData, property: KProperty<*>): T
  abstract override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: T)
}

class PrefJson<T>(key: String = "", private val clazz: Class<T>, private val default: T) :
  PreferenceDelegate<T>(key) {
  override fun getValue(thisRef: PreferenceData, property: KProperty<*>): T {
    try {
      val json = thisRef.getString(getPrefKey(property), "")
      if (json.isNotEmpty())
        return Gson().fromJson(json, clazz)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return default
  }

  override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: T) {
    if (value == null)
      return
    try {
      val json = Gson().toJson(value, clazz)
      thisRef.putString(getPrefKey(property), json)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}

class PrefString(
  key: String = "",
  private val default: String = ""
) : PreferenceDelegate<String>(key) {
  override fun getValue(thisRef: PreferenceData, property: KProperty<*>): String {
    return thisRef.getString(getPrefKey(property), default)
  }

  override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: String) {
    thisRef.putString(getPrefKey(property), value)
  }
}

class PrefInt(key: String, private val default: Int = -1) : PreferenceDelegate<Int>(key) {
  override fun getValue(thisRef: PreferenceData, property: KProperty<*>): Int {
    return thisRef.getInt(getPrefKey(property), default)
  }

  override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: Int) {
    thisRef.putInt(getPrefKey(property), value)
  }
}

class PrefFloat(key: String, private val default: Float = -1F) :
  PreferenceDelegate<Float>(key) {
  override fun getValue(thisRef: PreferenceData, property: KProperty<*>): Float {
    return thisRef.getFloat(getPrefKey(property), default)
  }

  override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: Float) {
    thisRef.putFloat(getPrefKey(property), value)
  }
}

class PrefLong(key: String, private val default: Long = -1L) :
  PreferenceDelegate<Long>(key) {
  override fun getValue(thisRef: PreferenceData, property: KProperty<*>): Long {
    return thisRef.getLong(getPrefKey(property), default)
  }

  override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: Long) {
    thisRef.putLong(getPrefKey(property), value)
  }
}

class PrefBoolean(key: String, private val default: Boolean = false) :
  PreferenceDelegate<Boolean>(key) {
  override fun getValue(thisRef: PreferenceData, property: KProperty<*>): Boolean {
    return thisRef.getBoolean(getPrefKey(property), default)
  }

  override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: Boolean) {
    thisRef.putBoolean(getPrefKey(property), value)
  }
}

class JsonResource<T>(
  private val key: String = "",
  private val clazz: Class<T>,
  private val default: T
) {
  operator fun provideDelegate(
    thisRef: PreferenceData,
    prop: KProperty<*>
  ): ReadWriteProperty<PreferenceData, T> {
    return PrefJson(key, clazz, default)
  }
}

///**
// * 代码少但难看的方式
// */
//private class PrefData<T>(key: String, private val default: T) :
//  PreferenceDelegate<T>(key) {
//  override fun getValue(thisRef: PreferenceData, property: KProperty<*>): T {
//    return thisRef.getValue(getPrefKey(property), default)
//  }
//
//  override fun setValue(thisRef: PreferenceData, property: KProperty<*>, value: T) {
//    thisRef.putValue(getPrefKey(property), default)
//  }
//}

//class PrefResource<T>(
//  private val key: String = "",
//  private val default: T
//) {
//  operator fun provideDelegate(
//    thisRef: PreferenceData,
//    prop: KProperty<*>
//  ): ReadWriteProperty<PreferenceData, T> {
//    return PrefData(key, default)
//  }
//}