package com.songdehuai.commonlib.widget;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.View;

/**
 * 描述：自定义ViewPager,解决布局嵌套，实现自动计算高度
 *
 * @author songdehuai
 * {@link AdaptViewPager}
 * @date 2018/8/27
 */
public class AdaptViewPager extends ViewPager {

    public AdaptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChildView();
    }

    public AdaptViewPager(Context context) {
        super(context);
        initChildView();
    }

    private void initChildView() {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // find the current child view
        View view = getChildAt(getCurrentItem());
        if (view != null) {
            // measure the current child view with the specified measure spec
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
    }


    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        setOffscreenPageLimit(adapter.getCount());
    }
}