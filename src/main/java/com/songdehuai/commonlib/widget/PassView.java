package com.songdehuai.commonlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;


import com.songdehuai.commonlib.R;

import androidx.annotation.Nullable;

/**
 * 密码输入眼睛
 */
@SuppressLint("AppCompatCustomView")
public class PassView extends ImageView {

    private boolean isShow = false;
    private EditText editText;
    private Drawable showRes;
    private Drawable hideRes;

    public PassView(Context context) {
        super(context, null);
        setWillNotDraw(false);
    }

    public PassView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        setWillNotDraw(false);
    }

    public PassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PassView);
        showRes = ta.getDrawable(R.styleable.PassView_showRes);
        hideRes = ta.getDrawable(R.styleable.PassView_hideRes);
        ta.recycle();
        initViews();
    }

    private void initViews() {
        setImageDrawable(hideRes);
        setOnClickListener(v -> {
            if (isShow) {
                setImageDrawable(hideRes);
                isShow = false;
                if (editText != null) {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            } else {
                setImageDrawable(showRes);
                isShow = true;
                if (editText != null) {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

}
