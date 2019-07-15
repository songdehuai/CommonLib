package com.songdehuai.commonlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 描述：计算高度ViewPager
 *
 * @author songdehuai
 * @ClassName: com.jiubaisoft.nw.widget.MeasureViewpager
 * @date 2017/6/30 10:37
 */

public class MeasureViewpager extends ViewPager {

    boolean tag = true;

    private int height = 0;

    public MeasureViewpager(Context context) {
        super(context);
    }

    public MeasureViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
