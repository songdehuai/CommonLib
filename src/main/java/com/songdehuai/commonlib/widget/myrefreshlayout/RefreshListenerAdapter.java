package com.songdehuai.commonlib.widget.myrefreshlayout;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.RefreshListenerAdapter
 * @date 2017/8/14 11:14
 */
public abstract class RefreshListenerAdapter implements PullListener {
    @Override
    public void onPullingDown(MyRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onPullingUp(MyRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onPullDownReleasing(MyRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onPullUpReleasing(MyRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onRefresh(MyRefreshLayout refreshLayout) {
    }

    @Override
    public void onLoadMore(MyRefreshLayout refreshLayout) {
    }

    @Override
    public void onFinishRefresh() {

    }

    @Override
    public void onFinishLoadMore() {

    }

    @Override
    public void onRefreshCanceled() {

    }

    @Override
    public void onLoadmoreCanceled() {

    }
}