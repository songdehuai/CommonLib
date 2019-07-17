package com.commonlib.widget.title

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.commonlib.R
import com.commonlib.ext.gone
import com.commonlib.ext.visible

import com.commonlib.widget.title.TitleType.NONE

/**
 * 标题view
 */
class TitleView : RelativeLayout {

    private var titleType = NONE
    private var view: View? = null
    private val finishTv by lazy { findViewById<TextView>(R.id.title_finish_tv) }
    private val finishIv by lazy { findViewById<ImageView>(R.id.title_finish_iv) }
    private val titleTv by lazy { findViewById<TextView>(R.id.base_title_tv) }
    private val finishLi by lazy { findViewById<LinearLayout>(R.id.title_finish_li) }
    private val rightTv by lazy { findViewById<TextView>(R.id.title_right_tv) }
    private val rightIv by lazy { findViewById<ImageView>(R.id.title_right_iv) }
    private val rightRl by lazy { findViewById<RelativeLayout>(R.id.title_right_rl) }
    private var titleCallBack: TitleCallBack? = null
    private val titleRootView by lazy { findViewById<RelativeLayout>(R.id.base_title_root) }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        view = LayoutInflater.from(context).inflate(R.layout.base_title, this)
        rightRl?.setOnClickListener {
            if (titleCallBack != null) {
                titleCallBack!!.onPublish()
            }
        }

        finishLi?.setOnClickListener {
            if (titleCallBack != null) {
                titleCallBack!!.onBack()
            }
        }
    }

    fun setTitleCallBack(titleType: TitleType, titleCallBack: TitleCallBack) {
        this.titleType = titleType
        this.titleCallBack = titleCallBack
        when (titleType) {
            NONE -> {
                rightRl?.gone()
                finishLi?.gone()
            }
            TitleType.DETAIL -> {
                rightRl?.gone()
                finishLi?.visible()
            }
            TitleType.PUBLISH -> {
                rightRl?.visible()
                finishLi?.visible()
            }
            TitleType.PUBLISH_ONE -> {
                rightRl?.visible()
                finishLi?.gone()
            }
        }

    }

    fun setTitleText(titleText: CharSequence) {
        titleTv?.text = titleText
    }

    /**
     * 设置TitleView右侧按钮点击文字
     */
    fun setPublishText(text: CharSequence) {
        rightTv?.text = text
        rightTv?.visible()
        rightRl?.visible()
    }

    fun showPublishText() {
        rightTv?.visible()
        rightRl?.visible()
    }

    fun hidePublishText() {
        rightTv?.gone()
    }

    fun showLeft() {
        finishLi?.visible()
        finishIv?.visible()
    }

    fun hideLeft() {
        finishIv?.gone()
    }

}