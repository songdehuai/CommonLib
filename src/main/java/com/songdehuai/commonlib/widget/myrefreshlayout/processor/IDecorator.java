package com.songdehuai.commonlib.widget.myrefreshlayout.processor;

import android.view.MotionEvent;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.processor.IDecorator
 * @date 2017/8/14 11:13
 */

public interface IDecorator {
    boolean dispatchTouchEvent(MotionEvent ev);

    boolean interceptTouchEvent(MotionEvent ev);

    boolean dealTouchEvent(MotionEvent ev);

    void onFingerDown(MotionEvent ev);

    void onFingerUp(MotionEvent ev, boolean isFling);

    void onFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY, float velocityX, float velocityY);

    void onFingerFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
}
