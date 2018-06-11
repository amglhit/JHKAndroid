package com.amglhit.msuite.statusbar

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import com.amglhit.msuite.px2DP
import com.amglhit.msuite.utils.statusBarHeight

object StatusBarKitkat {
  private const val TAG_FAKE_STATUS_BAR_VIEW = "statusBarView"
  private const val TAG_MARGIN_ADDED = "marginAdded"

  @TargetApi(Build.VERSION_CODES.KITKAT)
  fun setStatusBarColor(activity: Activity, color: Int) {
    val window = activity.window
    //设置Window为全透明
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

    //获取父布局
    val childView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).getChildAt(0)

    //获取状态栏高度
    val statusBarHeight = activity.statusBarHeight()

    //如果已经存在假状态栏则移除，防止重复添加
    removeFakeStatusBarViewIfExist(activity)
    //添加一个View来作为状态栏的填充
    addFakeStatusBarView(activity, color, statusBarHeight)
    //设置子控件到状态栏的间距
    childView?.let {
      addMarginTopToContentChild(it, statusBarHeight)
      //不预留系统栏位置
      it.fitsSystemWindows = false
    }

    //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
    val actionBar = actionBarView(activity)
    if (actionBar != null) {
      val typedValue = TypedValue()
      if (activity.theme.resolveAttribute(
          android.support.design.R.attr.actionBarSize,
          typedValue,
          true
        )
      ) {
        val actionBarHeight = TypedValue.complexToDimensionPixelSize(
          typedValue.data,
          activity.resources.displayMetrics
        )
        setContentTopPadding(activity, actionBarHeight)
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  fun translucentStatusBar(activity: Activity) {
    val window = activity.window
    //设置Window为透明
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

    val childView = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).getChildAt(0)
    //移除已经存在假状态栏则,并且取消它的Margin间距
    removeFakeStatusBarViewIfExist(activity)
    childView?.let {
      removeMarginTopOfContentChild(it, activity.statusBarHeight())
      //fitsSystemWindow 为 false, 不预留系统栏位置.
      it.fitsSystemWindows = false
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  fun setStatusBarColorForCollapsingToolbar(
    activity: Activity,
    appBarLayout: AppBarLayout, collapsingToolbarLayout: CollapsingToolbarLayout,
    toolbar: Toolbar, statusColor: Int
  ) {
    val window = activity.window
    //设置Window为全透明
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    //AppBarLayout,CollapsingToolbarLayout,ToolBar,ImageView的fitsSystemWindow统一改为false, 不预留系统栏位置.
    val mContentChild = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).getChildAt(0)
    mContentChild.fitsSystemWindows = false
    (appBarLayout.parent as View).fitsSystemWindows = false
    appBarLayout.fitsSystemWindows = false
    collapsingToolbarLayout.fitsSystemWindows = false
    collapsingToolbarLayout.getChildAt(0).fitsSystemWindows = false

    toolbar.fitsSystemWindows = false
    //为Toolbar添加一个状态栏的高度, 同时为Toolbar添加paddingTop,使Toolbar覆盖状态栏，ToolBar的title可以正常显示.
    val statusBarHeight = activity.statusBarHeight()

    if (toolbar.tag == null) {
      val lp = toolbar
        .layoutParams as CollapsingToolbarLayout.LayoutParams

      lp.height += statusBarHeight
      toolbar.layoutParams = lp
      toolbar.setPadding(
        toolbar.paddingLeft, toolbar.paddingTop + statusBarHeight,
        toolbar.paddingRight, toolbar.paddingBottom
      )
      toolbar.tag = true
    }

    //移除已经存在假状态栏则,并且取消它的Margin间距
    removeFakeStatusBarViewIfExist(activity)
    removeMarginTopOfContentChild(mContentChild, statusBarHeight)
    //添加一个View来作为状态栏的填充
    val statusView = addFakeStatusBarView(activity, statusColor, statusBarHeight)

    val behavior = (appBarLayout
      .layoutParams as CoordinatorLayout.LayoutParams).behavior
    if (behavior != null && behavior is AppBarLayout.Behavior) {
      val verticalOffset = behavior.topAndBottomOffset
      if (Math.abs(verticalOffset) > appBarLayout.height - collapsingToolbarLayout
          .scrimVisibleHeightTrigger
      ) {
        statusView.alpha = 1f
      } else {
        statusView.alpha = 0f
      }
    } else {
      statusView.alpha = 0f
    }
    appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
      if (Math.abs(verticalOffset) > appBarLayout.height - collapsingToolbarLayout
          .scrimVisibleHeightTrigger
      ) {
        //toolbar被折叠时显示状态栏
        if (statusView.alpha == 0f) {
          statusView.animate().cancel()
          statusView.animate().alpha(1f)
            .setDuration(collapsingToolbarLayout.scrimAnimationDuration).start()
        }
      } else {
        //toolbar展开时显示状态栏
        if (statusView.alpha == 1f) {
          statusView.animate().cancel()
          statusView.animate().alpha(0f)
            .setDuration(collapsingToolbarLayout.scrimAnimationDuration).start()
        }
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  fun setStatusBarWhiteForCollapsingToolbar(
    activity: Activity,
    appBarLayout: AppBarLayout, collapsingToolbarLayout: CollapsingToolbarLayout,
    toolbar: Toolbar, statusBarColor: Int
  ) {
    val window = activity.window
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

    val mContentChild = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).getChildAt(0)
    mContentChild.fitsSystemWindows = false
    (appBarLayout.parent as View).fitsSystemWindows = false
    appBarLayout.fitsSystemWindows = false
    collapsingToolbarLayout.fitsSystemWindows = false
    collapsingToolbarLayout.getChildAt(0).fitsSystemWindows = false
    toolbar.fitsSystemWindows = false

    val statusBarHeight = activity.statusBarHeight()

    if (toolbar.tag == null) {
      val lp = toolbar
        .layoutParams as CollapsingToolbarLayout.LayoutParams
      lp.height += statusBarHeight
      toolbar.layoutParams = lp
      toolbar.setPadding(
        toolbar.paddingLeft, toolbar.paddingTop + statusBarHeight,
        toolbar.paddingRight, toolbar.paddingBottom
      )
      toolbar.tag = true
    }

    var color = Color.BLACK
    try {
      val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
      color = statusBarColor
    } catch (e: ClassNotFoundException) {
      e.printStackTrace()
    }

    try {
      val darkFlag = WindowManager.LayoutParams::class.java
        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
      color = statusBarColor
    } catch (e: NoSuchFieldException) {
      e.printStackTrace()
    }

    val statusView = addFakeStatusBarView(activity, color, statusBarHeight)
    val behavior = (appBarLayout
      .layoutParams as CoordinatorLayout.LayoutParams).behavior
    if (behavior != null && behavior is AppBarLayout.Behavior) {
      val verticalOffset = behavior.topAndBottomOffset
      if (Math.abs(verticalOffset) > appBarLayout.height - collapsingToolbarLayout
          .scrimVisibleHeightTrigger
      ) {
        statusView.alpha = 1f
      } else {
        statusView.alpha = 0f
      }
    } else {
      statusView.alpha = 0f
    }

    appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
      private val EXPANDED = 0
      private val COLLAPSED = 1
      private var appBarLayoutState: Int = 0

      override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange - 56.px2DP(activity)
        ) {
          if (appBarLayoutState != COLLAPSED) {
            appBarLayoutState = COLLAPSED

            if (LightStatusBar.miuiSetStatusBarLightMode(activity, true) || LightStatusBar
                .flymeSetStatusBarLightMode(activity, true)
            ) {
            }
            if (statusView.alpha == 0f) {
              statusView.animate().cancel()
              statusView.animate().alpha(1f)
                .setDuration(collapsingToolbarLayout.scrimAnimationDuration).start()
            }
          }
        } else {
          if (appBarLayoutState != EXPANDED) {
            appBarLayoutState = EXPANDED

            if (LightStatusBar.miuiSetStatusBarLightMode(activity, false) || LightStatusBar
                .flymeSetStatusBarLightMode(activity, false)
            ) {
            }
            if (statusView.alpha == 1f) {
              statusView.animate().cancel()
              statusView.animate().alpha(0f)
                .setDuration(collapsingToolbarLayout.scrimAnimationDuration).start()
            }
            translucentStatusBar(activity)
          }
        }
      }
    })
  }

  private fun removeFakeStatusBarViewIfExist(activity: Activity) {
    val decorView = activity.window.decorView as ViewGroup
    val fakeView = decorView.findViewWithTag<View>(TAG_FAKE_STATUS_BAR_VIEW)
    if (fakeView != null) {
      decorView.removeView(fakeView)
    }
  }

  private fun addFakeStatusBarView(
    activity: Activity,
    statusBarColor: Int,
    statusBarHeight: Int
  ): View {
    val statusBarView = View(activity).apply {
      val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight
      )
      params.gravity = Gravity.TOP
      layoutParams = params
      setBackgroundColor(statusBarColor)
      tag = TAG_FAKE_STATUS_BAR_VIEW
    }

    val decorView = activity.window.decorView as ViewGroup
    decorView.addView(statusBarView)
    return statusBarView
  }

  private fun actionBarView(activity: Activity): View? {
    val id = activity.resources.getIdentifier("action_bar", "id", activity.packageName)
    return activity.findViewById(id)
  }

  private fun addMarginTopToContentChild(targetView: View, statusBarHeight: Int) {
    if (TAG_MARGIN_ADDED != targetView.tag) {
      val lp = targetView.layoutParams as FrameLayout.LayoutParams
      lp.topMargin += statusBarHeight
      targetView.layoutParams = lp
      targetView.tag = TAG_MARGIN_ADDED
    }
  }

  private fun removeMarginTopOfContentChild(targetView: View, statusBarHeight: Int) {
    if (TAG_MARGIN_ADDED == targetView.tag) {
      val lp = targetView.layoutParams as FrameLayout.LayoutParams
      lp.topMargin -= statusBarHeight
      targetView.layoutParams = lp
      targetView.tag = null
    }
  }

  private fun setContentTopPadding(activity: Activity, padding: Int) {
    activity.window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
      .setPadding(0, padding, 0, 0)
  }
}

