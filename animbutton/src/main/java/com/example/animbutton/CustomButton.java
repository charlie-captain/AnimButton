package com.example.animbutton;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by ThatNight on 2018.1.6.
 */

public class CustomButton extends android.support.v7.widget.AppCompatButton {

    protected OnAnimClickListener mOnAnimClickListener;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OnAnimClickListener getOnAnimClickListener() {
        return mOnAnimClickListener;
    }

    public void setOnAnimClickListener(OnAnimClickListener onAnimClickListener) {
        mOnAnimClickListener = onAnimClickListener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }


    /**
     * start the animation auto
     * @return
     */
    @Override
    public boolean performClick() {
        if (mOnAnimClickListener != null) {
            mOnAnimClickListener.animStart();
        }
        return super.performClick();
    }
}
