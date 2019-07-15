package com.songdehuai.commonlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.songdehuai.commonlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 星级评价控件
 */
public class StarView extends LinearLayout {

    private int max = 5;
    private int value = 1;
    private Drawable selectedDrawable;
    private Drawable unSelectedDrawable;
    private List<ImageView> imageViewList = new ArrayList<>();
    private boolean clickEnable = false;

    public StarView(Context context) {
        this(context, null);
        setWillNotDraw(false);
    }

    public StarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setWillNotDraw(false);
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StarView);
        selectedDrawable = ta.getDrawable(R.styleable.StarView_selectedDrawable);
        unSelectedDrawable = ta.getDrawable(R.styleable.StarView_unSelectedDrawable);
        max = ta.getInt(R.styleable.StarView_max, 0);
        value = ta.getInt(R.styleable.StarView_value, 0);
        clickEnable = ta.getBoolean(R.styleable.StarView_clickEnable, false);
        ta.recycle();
        initViews();
    }

    public void setMax(int max) {
        this.max = max;
        initViews();
    }

    public void setValue(int value) {
        this.value = value;
        initViews();
    }

    public int getMax() {
        return max;
    }

    public int getValue() {
        return value;
    }


    private void initViews() {
        imageViewList.clear();
        removeAllViews();
        setWeightSum(max);
        for (int i = 0; i < max; i++) {
            addView(getStarImageView(i));
        }
    }

    private ImageView getStarImageView(int index) {
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        if (value > index) {
            if (selectedDrawable != null) {
                imageView.setImageDrawable(selectedDrawable);
            } else {
                imageView.setBackgroundColor(Color.YELLOW);
            }

        } else {
            if (unSelectedDrawable != null) {
                imageView.setImageDrawable(unSelectedDrawable);
            } else {
                imageView.setBackgroundColor(Color.GRAY);
            }
        }
        imageViewList.add(imageView);
        return imageView;
    }

    private boolean isDown = false;
    private float mCurrentX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                isDown = true;
            }
            case MotionEvent.ACTION_UP: {
                if (isDown && clickEnable) {
                    mCurrentX = event.getX();
                    value = (int) (mCurrentX / (getWidth() / max)) + 1;
                    setValue(value);
                    isDown = false;
                    if (onSeletedListener != null) {
                        onSeletedListener.onSeleted(value);
                    }
                }
            }
        }
        return true;
    }

    private OnSeletedListener onSeletedListener;

    public void setOnSeletedListener(OnSeletedListener onSeletedListener) {
        this.onSeletedListener = onSeletedListener;
    }

    public OnSeletedListener getOnSeletedListener() {
        return onSeletedListener;
    }

    public interface OnSeletedListener {
        void onSeleted(int value);
    }
}
