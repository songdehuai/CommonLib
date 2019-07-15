package com.songdehuai.commonlib.widget.marqueeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.songdehuai.commonlib.R;
import com.songdehuai.commonlib.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * 描述：轮播文字
 *
 * @author songdehuai
 * @ClassName: com.jiubaisoft.nw.widget.marqueeview.MarqueeView
 * @date 2017/10/12 15:02
 */
public class MarqueeView extends ViewFlipper {

    private Context mContext;
    private List<? extends CharSequence> notices;
    private boolean isSetAnimDuration = false;
    private OnItemClickListener onItemClickListener;

    private int interval = 2000;
    private int animDuration = 1000;
    private int textSize = 14;
    private int textColor = 0xFF888888;

    private boolean singleLine = false;
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        singleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyle_mvSingleLine, false);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtil.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        int gravityType = typedArray.getInt(R.styleable.MarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (gravityType) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        typedArray.recycle();

        setFlipInterval(interval);
    }

    // 根据公告字符串启动轮播
    public void startWithText(@NonNull final String notice) {
        if (TextUtils.isEmpty(notice)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    /**
     * 根据公告字符串列表启动轮播
     *
     * @param notices
     */
    public void startWithList(List<? extends CharSequence> notices) {
        setNotices(notices);
        start();
    }

    /**
     * 设置字符串列表不开始轮播
     *
     * @param notices
     */
    public void setWithList(List<? extends CharSequence> notices) {
        setNotices(notices);
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(@NonNull String notice, int width) {
        int noticeLength = notice.length();
        int dpW = DisplayUtil.px2dip(mContext, width);
        int limit = dpW / textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }
        List list = new ArrayList();
        if (noticeLength <= limit) {
            list.add(notice);
        } else {
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit);
                list.add(notice.substring(startIndex, endIndex));
            }
        }
        notices.addAll(list);
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();
        resetAnimation();

        for (int i = 0; i < notices.size(); i++) {
            final TextView textView = createTextView(notices.get(i), i);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(finalI, textView);
                    }
                }
            });
            addView(textView);
        }

        if (notices.size() > 1) {
            startFlipping();
        } else {
            stopFlipping();
        }
        return true;
    }

    private void resetAnimation() {
        clearAnimation();

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    // 创建ViewFlipper下的TextView
    @NonNull
    private TextView createTextView(CharSequence text, int position) {
        TextView tv = new TextView(mContext);
        tv.setGravity(gravity);
        tv.setText(Html.fromHtml((String) text));
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(textSize);
        tv.setSingleLine(singleLine);
        tv.setTag(position);
        return tv;
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<? extends CharSequence> getNotices() {
        return notices;
    }

    public void setNotices(List<? extends CharSequence> notices) {
        this.notices = notices;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }

}
