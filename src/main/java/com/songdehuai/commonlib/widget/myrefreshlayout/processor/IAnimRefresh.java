package com.songdehuai.commonlib.widget.myrefreshlayout.processor;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.processor.IAnimRefresh
 * @date 2017/8/14 11:12
 */
public interface IAnimRefresh {
    void scrollHeadByMove(float moveY);

    void scrollBottomByMove(float moveY);

    void animHeadToRefresh();

    void animHeadBack(boolean isFinishRefresh);

    void animHeadHideByVy(int vy);

    void animBottomToLoad();

    void animBottomBack(boolean isFinishRefresh);

    void animBottomHideByVy(int vy);
}
