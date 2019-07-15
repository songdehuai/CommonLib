package com.songdehuai.commonlib.widget.myrefreshlayout.processor;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.processor.IAnimOverScroll
 * @date 2017/8/14 11:12
 */
public interface IAnimOverScroll {
    void animOverScrollTop(float vy, int computeTimes);

    void animOverScrollBottom(float vy, int computeTimes);
}
