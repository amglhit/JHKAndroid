package com.amglhit.msuite.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlin.properties.Delegates

/**
 * 可以监听变化的数据,
 * 通过observe方法注册onChange监听
 * value值改变时回调onChange
 */
class ObservableData<T>(default: T) {
  private val subject: BehaviorSubject<Pair<T, T>> =
    BehaviorSubject.createDefault(Pair(default, default))

  var value by Delegates.observable(default) { _, oldValue, newValue ->
    subject.onNext(Pair(oldValue, newValue))
  }

  fun observe(onChange: (old: T, new: T) -> Unit): Disposable {
    return subject.observeOn(AndroidSchedulers.mainThread()).subscribe(
      { onChange(it.first, it.second) },
      { it.printStackTrace() }
    )
  }
}
//
//class OPC<T>(private val subject: BehaviorSubject<Pair<T, T>>, initial: T) :
//  ObservableProperty<T>(initial) {
//  override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
//    super.afterChange(property, oldValue, newValue)
//    subject.onNext(Pair(oldValue, newValue))
//  }
//}

