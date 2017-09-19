package com.example.animbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


/**
 * Created by thatnight on 2017.9.16.
 */

public class AnimButton extends RelativeLayout {

    private int mWidth = 0;
    private int mHeight = 0;
    private int mLeft;
    private ViewWrapper mViewWrapper;
    private Button mTarget;
    private int mDuration;
    private String mStartText = "";
    private String mEndText;
    private AnimatorSet mStartSet, mEndSet;
    private ProgressBar mProgress;

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
        a.recycle();

        //inflate layout
        LayoutInflater.from(context).inflate(R.layout.fm_button_progress, this, true);
        mProgress = findViewById(R.id.pb_button);
        mTarget = findViewById(R.id.button);
        setWrapper(mTarget);

        if (mStartText != null && mStartText.length() > 0) {
            mTarget.setText(mStartText);
        }
    }

    public void startAnimation() {
        if (mStartSet == null) {
            initStartAnim();
            initErrorAnim();
        }
        mStartSet.start();
    }

    public void errorAnimation() {
        if(mStartSet.isRunning()){
            mStartSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mEndSet.start();
                }
            });
        }else{
            mEndSet.start();
        }
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
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mTarget.setText("");
                mTarget.setClickable(false);
            }
        });
        ObjectAnimator startAnim = ObjectAnimator.ofInt(mViewWrapper, "width", mWidth, mHeight);
        startAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        mStartSet.setDuration(mDuration);
        mStartSet.playTogether(progressAnim, startAnim);
    }

    private void initErrorAnim() {
        mEndSet = new AnimatorSet();
        //create reverse animation
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

        //create error animation
        ObjectAnimator errorAnim = ObjectAnimator.ofFloat(mTarget, "X", mLeft, mLeft + 10, mLeft);
        errorAnim.setRepeatCount(100);
        errorAnim.setRepeatMode(ValueAnimator.REVERSE);
        errorAnim.setDuration(10);
        errorAnim.setInterpolator(new AccelerateInterpolator());
        errorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTarget.setClickable(true);
                mTarget.setText(mStartText);
            }
        });
        mEndSet.play(progressAnim).with(endAnim).before(errorAnim);
    }

    public void setWrapper(Button target) {
        if (mTarget != null) {
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
            mWidth = mTarget.getMeasuredWidth();
            mHeight = mTarget.getMeasuredHeight();
            mLeft = mTarget.getLeft();
        }
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mTarget.setOnClickListener(l);
    }


}
