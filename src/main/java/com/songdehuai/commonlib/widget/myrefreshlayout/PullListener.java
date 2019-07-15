package com.songdehuai.commonlib.widget.myrefreshlayout;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.PullListener
 * @date 2017/8/14 11:14
 */
public interface PullListener {
    /**
     * 下拉中
     *
     * @param refreshLayout
     * @param fraction
     */
    void onPullingDown(MyRefreshLayout refreshLayout, float fraction);

    /**
     * 上拉
     */
    void onPullingUp(MyRefreshLayout refreshLayout, float fraction);

    /**
     * 下拉松开
     *
     * @param refreshLayout
     * @param fraction
     */
    void onPullDownReleasing(MyRefreshLayout refreshLayout, float fraction);

    /**
     * 上拉松开
     */
    void onPullUpReleasing(MyRefreshLayout refreshLayout, float fraction);

    /**
     * 刷新中。。。
     */
    void onRefresh(MyRefreshLayout refreshLayout);

    /**
     * 加载更多中
     */
    void onLoadMore(MyRefreshLayout refreshLayout);

    /**
     * 手动调用finishRefresh或者finishLoadmore之后的回调
     */
    void onFinishRefresh();

    void onFinishLoadMore();

    /**
     * 正在刷新时向上滑动屏幕，刷新被取消
     */
    void onRefreshCanceled();

    /**
     * 正在加载更多时向下滑动屏幕，加载更多被取消
     */
    void onLoadmoreCanceled();
}