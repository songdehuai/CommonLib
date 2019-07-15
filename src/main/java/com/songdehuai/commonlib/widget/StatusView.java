package com.songdehuai.commonlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.songdehuai.commonlib.R;
import com.songdehuai.commonlib.utils.DisplayUtil;


/**
 * 倾斜切换控件
 */
public class StatusView extends LinearLayout {

    //中间线条画笔
    private Paint paint;
    //左边画笔
    private Paint paintLeft;
    //右边画笔
    private Paint paintRight;
    //tab个数
    private int textCount = 2;
    //角度
    private int mSlashAngle = 8;
    //是否从左倾斜
    private boolean isLeft = true;
    //中间线起点
    private int startX, startY, stopX, stopY;

    private int selectedColor = Color.WHITE;

    private int normalColor = Color.WHITE;

    private int selectedTextColor = Color.RED;

    private int textColor = Color.GRAY;

    private TextView textViewLeft;

    private TextView textViewRight;

    private String temp = "\\|";

    private String[] texts = new String[]{};

    public StatusView(Context context) {
        this(context, null);
        setWillNotDraw(false);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setWillNotDraw(false);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        int isLeftValues = ta.getInt(R.styleable.StatusView_StatusView_retinue, 0);
        isLeft = (1 == isLeftValues);
        selectedColor = ta.getColor(R.styleable.StatusView_StatusView_selectedColor, Color.WHITE);
        normalColor = ta.getColor(R.styleable.StatusView_StatusView_normalColor, Color.WHITE);
        float slashAngle = ta.getInteger(R.styleable.StatusView_StatusView_slashAngle, 8);
        mSlashAngle = DisplayUtil.dip2px(getContext(), slashAngle);
        textColor = ta.getColor(R.styleable.StatusView_StatusView_textColor, Color.GRAY);
        selectedTextColor = ta.getColor(R.styleable.StatusView_StatusView_selectedTextColor, Color.GREEN);
        temp = ta.getString(R.styleable.StatusView_StatusView_texts);
        texts = temp.split("\\|");
        ta.recycle();
        initPaint();
        initText();
    }


    private void initText() {

        setHorizontalGravity(HORIZONTAL);

        textViewLeft = new TextView(getContext());
        textViewRight = new TextView(getContext());

        textViewLeft.setText(texts[0]);
        textViewLeft.setTextColor(selectedTextColor);

        textViewRight.setText(texts[1]);
        textViewRight.setTextColor(textColor);

        textViewLeft.setGravity(Gravity.CENTER);
        textViewRight.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        textViewLeft.setLayoutParams(lp);
        textViewRight.setLayoutParams(lp);

        addView(textViewLeft);
        addView(textViewRight);

    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth((float) 5.0);

        paintLeft = new Paint();
        paintLeft.setAntiAlias(true);
        paintLeft.setColor(selectedColor);

        paintRight = new Paint();
        paintRight.setColor(normalColor);
        paintRight.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        if (isLeft) {
            startX = (getWidth() / textCount) + (mSlashAngle / 2);
            stopX = (getWidth() / textCount) - (mSlashAngle / 2);
        } else {
            startX = (getWidth() / textCount) - (mSlashAngle / 2);
            stopX = (getWidth() / textCount) + (mSlashAngle / 2);
        }
        startY = 0;
        stopY = getHeight();

        //绘制左边
        Path pathLeft = new Path();
        pathLeft.moveTo(0, 0);
        if (isLeft) {
            pathLeft.lineTo(stopX + (mSlashAngle), 0);
        } else {
            pathLeft.lineTo(stopX - (mSlashAngle), 0);
        }

        pathLeft.lineTo(stopX, getHeight());
        pathLeft.lineTo(0, getHeight());
        pathLeft.close();
        canvas.drawPath(pathLeft, paintLeft);

        //绘制右边
        Path pathRight = new Path();
        if (isLeft) {
            pathRight.moveTo(stopX + (mSlashAngle), 0);
        } else {
            pathRight.moveTo(stopX - (mSlashAngle), 0);
        }

        pathRight.lineTo(getWidth(), 0);
        pathRight.lineTo(getWidth(), getHeight());
        pathRight.lineTo(stopX, getHeight());
        pathRight.close();
        canvas.drawPath(pathRight, paintRight);

        // 绘制辅助线
        //   drawCircleHelp(canvas);

    }

    private boolean inTapRegion;
    private float mCurrentX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                inTapRegion = true;
                break;

            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if (inTapRegion) {
                    mCurrentX = event.getX();
                    if (mCurrentX < (getWidth() / 2)) {
                        if (onSelectedListener != null) {
                            onSelectedListener.onSelected(0);
                        }
                        isLeft = false;
                        textViewLeft.setTextColor(selectedTextColor);
                        textViewRight.setTextColor(textColor);
                        paintLeft.setColor(selectedColor);
                        paintRight.setColor(normalColor);
                    } else {
                        if (onSelectedListener != null) {
                            onSelectedListener.onSelected(1);
                        }
                        isLeft = true;
                        textViewLeft.setTextColor(textColor);
                        textViewRight.setTextColor(selectedTextColor);
                        paintLeft.setColor(normalColor);
                        paintRight.setColor(selectedColor);
                    }
                    invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean status = true;

    /**
     * 设置状态
     *
     * @param isStatus
     */
    public void setStatus(boolean isStatus) {
        status = isStatus;
        if (isStatus) {
            isLeft = false;
            textViewLeft.setTextColor(selectedTextColor);
            textViewRight.setTextColor(textColor);
            paintLeft.setColor(selectedColor);
            paintRight.setColor(normalColor);
        } else {
            isLeft = true;
            textViewLeft.setTextColor(textColor);
            textViewRight.setTextColor(selectedTextColor);
            paintLeft.setColor(normalColor);
            paintRight.setColor(selectedColor);
        }
        invalidate();
    }

    public boolean isStatus() {
        return status;
    }

    private void drawCircleHelp(Canvas canvas) {
        //辅助线
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        Paint paintOval;
        paintOval = new Paint();
        paintOval.setColor(selectedColor);
        int verticalCenter = getHeight() / 2;
        int horizontalCenter = getWidth() / 2;
        int circleRadius = 10;
        paintOval.setAntiAlias(true);
        canvas.drawCircle(horizontalCenter, verticalCenter, circleRadius, paintOval);
    }

    private OnSelectedListener onSelectedListener;

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }


    public OnSelectedListener getOnSelectedListener() {
        return onSelectedListener;
    }


    public interface OnSelectedListener {
        void onSelected(int index);
    }

    public void setLeftText(String text) {
        texts[0] = text;
        if (textViewLeft != null) {
            textViewLeft.setText(text);
        }
    }

    public void setRightText(String text) {
        texts[1] = text;
        if (textViewRight != null) {
            textViewRight.setText(text);
        }
    }
}
