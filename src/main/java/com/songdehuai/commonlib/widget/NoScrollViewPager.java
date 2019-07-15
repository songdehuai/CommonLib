package com.songdehuai.commonlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 描述：不可滑动的ViewpPager
 *
 * @author songdehuai
 * @date 2018/6/21 下午1:31
 */
public class NoScrollViewPager extends ViewPager {

    //是否禁止滑动
    private boolean noScroll = true;

    boolean tag = true;

    private int height = 0;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (tag) {
            for (int i = 0; i < (getChildCount() > 1 ? 1 : 0); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight();
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        changeHeight(height);
                    }
                });
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

            if (height > 0) {
                tag = false;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }


    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    /**
     * 动态改变高度
     *
     * @param
     * @return
     * @throws
     * @date 2017/6/30 10:12
     */
    public void changeHeight(int height) {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.height = height;
        setLayoutParams(params);
    }
}
