package com.songdehuai.commonlib.widget.myrefreshlayout;

import android.view.View;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.IBottomView
 * @date 2017/8/14 11:14
 */
public interface IBottomView {
    View getView();

    /**
     * 上拉准备加载更多的动作
     *
     * @param fraction        上拉高度与Bottom总高度之比
     * @param maxBottomHeight 底部部可拉伸最大高度
     * @param bottomHeight    底部高度
     */
    void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight);

    void startAnim(float maxBottomHeight, float bottomHeight);

    /**
     * 上拉释放过程
     */
    void onPullReleasing(float fraction, float maxBottomHeight, float bottomHeight);

    void onFinish();

    void reset();
}
