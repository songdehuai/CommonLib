package com.songdehuai.commonlib.base

import android.app.Activity
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.songdehuai.commonlib.R
import com.songdehuai.commonlib.utils.DialogUtils
import com.songdehuai.commonlib.utils.ultimatebar.ultimateBarBuilder
import com.songdehuai.commonlib.widget.title.TitleCallBack
import com.songdehuai.commonlib.widget.title.TitleType
import com.songdehuai.commonlib.widget.title.TitleView


/**
 * MVPBaseActivity
 * @author songdehuai
 */
open class BaseActivity : AppCompatActivity(), TitleCallBack {

    private lateinit var mContentView: View
    open var titleView: TitleView? = null
    private lateinit var titleDrawable: Drawable
    private lateinit var dialogUtils: DialogUtils
    open lateinit var thisActivity: Activity

    private val rootView by lazy { initRootView() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)
        init()
    }

    private fun init() {
        thisActivity = this
        dialogUtils = DialogUtils(this)
    }

    private fun initRootView(): LinearLayout {
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        return linearLayout
    }

    /**
     * 右边按钮事件
     */
    override fun onPublish() {

    }

    /**
     * 返回按钮事件
     */
    override fun onBack() {
        finish()
    }

    /**
     * 初始化Title,默认取Drawable里bg_title
     */
    private fun initTitle() {
        titleView = TitleView(this)
        this.titleDrawable = ContextCompat.getDrawable(this, R.drawable.bg_title)!!
        ultimateBarBuilder()
            .statusDark(false)
            .statusDrawable(titleDrawable)
            .statusDrawable2(titleDrawable)
            .applyNavigation(false)
            .navigationDark(false)
            .navigationDrawable(titleDrawable)
            .navigationDrawable2(titleDrawable)
            .create()
            .drawableBar()
    }

    /**
     * 同时设置状态栏和标题栏Drawable
     */
    open fun setTitleBack(drawable: Drawable) {
        this.titleDrawable = drawable
        ultimateBarBuilder()
            .statusDark(false)
            .statusDrawable(titleDrawable)
            .statusDrawable2(titleDrawable)
            .applyNavigation(false)
            .navigationDark(false)
            .navigationDrawable(titleDrawable)
            .navigationDrawable2(titleDrawable)
            .create()
            .drawableBar()
    }

    /**
     * 同时设置状态栏和标题栏Drawable
     */
    open fun setTitleBack(resId: Int) {
        setTitleBack(ContextCompat.getDrawable(this, resId)!!)
    }

    /**
     * 设置暗色状态栏
     */
    open fun setStatusDark(boolean: Boolean) {
        ultimateBarBuilder()
            .statusDark(boolean)
            .create()
            .drawableBar()
    }

    /**
     * 设置ContentView
     * @param layoutResID 布局id
     */
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mContentView = View.inflate(this, layoutResID, null)
        rootView.removeAllViews()
        rootView.addView(mContentView)
    }

    /**
     * 设置ContentView，带标题和返回按钮
     * @param layoutId 布局id
     * @param title 标题
     */
    open fun setContentView(layoutId: Int, title: CharSequence) {
        initTitle()
        titleView?.setTitleText(title)
        titleView?.setTitleCallBack(TitleType.DETAIL, this)
        mContentView = View.inflate(this, layoutId, null)
        rootView.removeAllViews()
        rootView.addView(titleView)
        rootView.addView(mContentView)
    }

    /**
     * 设置ContentView，仅带标题
     * @param layoutId 布局id
     * @param title 标题
     */
    open fun setContentViewNone(layoutId: Int, title: CharSequence) {
        initTitle()
        titleView?.setTitleText(title)
        titleView?.setTitleCallBack(TitleType.NONE, this)
        mContentView = View.inflate(this, layoutId, null)
        rootView.removeAllViews()
        rootView.addView(titleView)
        rootView.addView(mContentView)
    }

    /**
     * 设置ContentView，仅带右边文字
     * @param layoutId 布局id
     * @param title 标题
     */
    open fun setContentViewPublish(layoutId: Int, title: CharSequence, publishStr: CharSequence) {
        initTitle()
        titleView?.setTitleText(title)
        titleView?.setTitleCallBack(TitleType.PUBLISH_ONE, this)
        titleView?.setPublishText(publishStr)
        mContentView = View.inflate(this, layoutId, null)
        rootView.removeAllViews()
        rootView.addView(titleView)
        rootView.addView(mContentView)
    }

    /**
     * 设置ContentView 带标题返回按钮和右边按钮
     * @param layoutId 布局id
     * @param title 标题
     * @param publishStr 右边文字
     */
    open fun setContentView(layoutId: Int, title: CharSequence, publishStr: CharSequence) {
        initTitle()
        titleView?.setTitleCallBack(TitleType.PUBLISH, this)
        titleView?.setTitleText(title)
        titleView?.setPublishText(publishStr)
        mContentView = View.inflate(this, layoutId, null)
        rootView.removeAllViews()
        rootView.addView(titleView)
        rootView.addView(mContentView)
    }


    /**
     * 显示Toast
     *
     * @param toast Toast内容
     */
    open fun showToast(toast: String) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示LoadDialog
     *
     * @param text 内容
     */
    open fun showLoadDialog(text: String) {
        dialogUtils.showLaodDialog(text)
    }

    /**
     * 关闭 LoadDialog
     */
    open fun dismissLoadDialog() {
        dialogUtils.dismissLoadDialog()
    }

    /**
     * 显示一个Dialog
     *
     * @param text 内容
     */
    open fun showDialog(text: String) {
        dialogUtils.showDialog(text)
    }

    /**
     * 关闭 Dialog
     */
    open fun dismissDialog() {
        dialogUtils.dismissDialog()
    }

    /**
     * 显示dialog
     */
    open fun showDialog(title: String, listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(thisActivity)
            .setMessage(title)
            .setPositiveButton("确定", listener).setNegativeButton(
                "取消", { dialog, which -> dialog.dismiss() })
            .show()
    }

}