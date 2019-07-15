package com.songdehuai.commonlib.widget.myrefreshlayout;

import android.view.MotionEvent;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.OnGestureListener
 * @date 2017/8/14 11:14
 */
public interface OnGestureListener {
    void onDown(MotionEvent ev);

    void onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY);

    void onUp(MotionEvent ev, boolean isFling);

    void onFling(MotionEvent downEvent, MotionEvent upEvent, float velocityX, float velocityY);
}