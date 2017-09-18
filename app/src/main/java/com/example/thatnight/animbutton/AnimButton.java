package com.example.thatnight.animbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;


/**
 * Created by thatnight on 2017.9.16.
 */

public class AnimButton extends AppCompatButton {

    private int mWidth = 0;
    private int mHeight = 0;
    private int[] mPoint = new int[4];
    private ViewWrapper mViewWrapper;
    private AnimButton mTarget;
    private ProgressBar mProgress;
    private int mDuration;
    private String mStartText = "";
    private String mEndText;
    private AnimatorSet mStartSet, mEndSet;

    public AnimButton(Context context) {
        this(context, null);
    }

    public AnimButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimButton);
        mStartText = a.getString(R.styleable.AnimButton_start_text);
        mEndText = a.getString(R.styleable.AnimButton_end_text);
        mDuration = a.getInt(R.styleable.AnimButton_duration, 300);
        if (mStartText != null && mStartText.length() > 0) {
            setText(mStartText);
        }
        a.recycle();
    }

    public void startAnimation() {
        if (mStartSet == null) {
            initStartAnim();
            initErrorAnim();
        }
        mStartSet.start();
    }

    public void errorAnimation() {
        mEndSet.start();
    }

    private void initStartAnim() {
        if (mProgress == null || mTarget == null) {
            try {
                throw new Exception("No binding a progress or target");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mStartSet = new AnimatorSet();
        ObjectAnimator progressAnim = ObjectAnimator.ofFloat(mProgress, "alpha", 1);
        progressAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTarget.setClickable(false);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mTarget.setText("");
            }
        });
        ObjectAnimator startAnim = ObjectAnimator.ofInt(mViewWrapper, "width", mWidth, mHeight);
        startAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                errorAnimation();
            }
        });
        mStartSet.setDuration(mDuration);
        mStartSet.playTogether(progressAnim, startAnim);
    }

    private void initErrorAnim() {
        mEndSet = new AnimatorSet();
        //创建复原宽度动画
        ObjectAnimator endAnim = ObjectAnimator.ofInt(mViewWrapper, "width", mWidth);
        endAnim.setDuration(mDuration);

        ObjectAnimator progressAnim = ObjectAnimator.ofFloat(mProgress, "alpha", 0);
        progressAnim.setDuration(mDuration);
        progressAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mEndText != null && mEndText.length() > 0) {
                    mTarget.setText(mEndText);
                } else {
                    mTarget.setText(mStartText);
                }
            }
        });

        //创建错误抖动动画
        ObjectAnimator errorAnim = ObjectAnimator.ofFloat(mTarget, "X", mPoint[0], mPoint[0] + 10, mPoint[0]);
        errorAnim.setRepeatCount(100);
        errorAnim.setRepeatMode(ValueAnimator.REVERSE);
        errorAnim.setDuration(10);
        errorAnim.setInterpolator(new AccelerateInterpolator());
        errorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTarget.setClickable(true);
                setText(mStartText);
            }
        });
        mEndSet.play(progressAnim).with(endAnim).before(errorAnim);
    }

    public void setmProgress(ProgressBar mProgress) {
        this.mProgress = mProgress;
    }

    public void setWrapper(AnimButton target) {
        if (mTarget == null) {
            mTarget = target;
            mViewWrapper = new ViewWrapper(target);
        }
    }

    /**
     * use a wrapper to change button width
     */
    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }

    /**
     * get button width and height
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mWidth == 0 && mHeight == 0) {
            mWidth = getWidth();
            mHeight = getHeight();
            mPoint[0] = left;
            mPoint[1] = top;
            mPoint[2] = right;
            mPoint[3] = bottom;
        }
    }

}
