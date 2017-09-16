package com.example.thatnight.animbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


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
    private String mText = "";

    public AnimButton(Context context) {
        super(context);
    }

    public AnimButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startAnimation() {
        if (mProgress != null) {
            mProgress.setVisibility(View.VISIBLE);
        }
        ObjectAnimator startAnim = ObjectAnimator.ofInt(mViewWrapper, "width", mWidth, mHeight);
        startAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                errorAnimation();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mTarget.setClickable(false);
                mTarget.setText("");
            }
        });
        startAnim.setDuration(300).start();
    }

    public void errorAnimation() {
        if (mProgress != null) {
            mProgress.setVisibility(View.INVISIBLE);
        }
        ObjectAnimator endAnim = ObjectAnimator.ofInt(mViewWrapper, "width", mHeight, mWidth);
        endAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTarget.setText(mText);
            }
        });
        endAnim.setDuration(300).start();

        ObjectAnimator errorAnim = ObjectAnimator.ofFloat(mTarget, "X", 0, 10, 0, 0);
        errorAnim.setRepeatCount(100);
        errorAnim.setRepeatMode(ValueAnimator.REVERSE);
        errorAnim.setDuration(10);
        errorAnim.setInterpolator(new AccelerateInterpolator());
        errorAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mTarget.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mTarget.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        errorAnim.start();
//        TranslateAnimation errorAnim = new TranslateAnimation(0, 10, 0, 0);
//        errorAnim.setDuration(5);
//        errorAnim.setRepeatCount(5);
//        errorAnim.setRepeatMode(Animation.REVERSE);
//        target.startAnimation(errorAnim);
    }

    public void setmText(String mText) {
        this.mText = mText;
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("layout", "onLayout: " + left + "  " + top + "  " + right + "  " + bottom);
        if (mWidth == 0 && mHeight == 0) {
            mWidth = getWidth();
            mHeight = getHeight();
            Log.d("layout_1", "onLayout: " + mWidth + "  " + mHeight);
        }

    }

}
