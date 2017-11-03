package com.example.animbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
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
    private int mTextSize;
    private AnimatorSet mStartSet, mEndSet;
    private ProgressBar mProgress;

    //static state
    public static int[] mNormalState = new int[]{};
    public static int[] mPressedState = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
    public static int[] mDisableState = new int[]{-android.R.attr.state_enabled};
    public static int[] mSelectedState = new int[]{android.R.attr.state_selected, android.R.attr.state_enabled};

    //radius
    private float mRadius;

    //color
    private int mNormalColor;
    private int mPressedColor;
    private int mProgressColor;
    private int mTextColor;


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
        mNormalColor = a.getColor(R.styleable.AnimButton_color_normal, ContextCompat.getColor(context, R.color.colorPrimary));
        mPressedColor = a.getColor(R.styleable.AnimButton_color_pressed, ContextCompat.getColor(context, R.color.colorPrimaryDark));
        mProgressColor = a.getColor(R.styleable.AnimButton_color_progress, ContextCompat.getColor(context, R.color.colorAccent));
        mTextColor = a.getColor(R.styleable.AnimButton_color_text, ContextCompat.getColor(context, R.color.colorAccent));
        mRadius = a.getFloat(R.styleable.AnimButton_button_radius, 0);
//         = (int) a.getDimension(R.styleable.AnimButton_text_size, 15);
        mTextSize=a.getDimensionPixelSize(R.styleable.AnimButton_size_text,
                (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                15,
                getResources().getDisplayMetrics()));

        a.recycle();

        //inflate layout
        LayoutInflater.from(context).inflate(R.layout.fm_button_progress, this, true);
        mProgress = (ProgressBar) findViewById(R.id.pb_button);
        setProgressDrawable(context);
        mTarget = (Button) findViewById(R.id.button);
        setWrapper(mTarget);
        buildDrawableState();

        if (mStartText != null && mStartText.length() > 0) {
            mTarget.setText(mStartText);
        }
    }

    /**
     * change the button drawable
     */
    private void buildDrawableState() {
        float radius[] = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
        StateListDrawable drawable = new StateListDrawable();

        RoundRectShape rectShape = new RoundRectShape(radius, null, null);
        ShapeDrawable pressedDrawable = new ShapeDrawable(rectShape);
        pressedDrawable.getPaint().setColor(mPressedColor);
        drawable.addState(mPressedState, pressedDrawable);

        ShapeDrawable normalDrawable = new ShapeDrawable(rectShape);
        normalDrawable.getPaint().setColor(mNormalColor);
        drawable.addState(mNormalState, normalDrawable);
        mTarget.setBackground(drawable);
        mTarget.setTextColor(mTextColor);
        mTarget.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
    }

    /**
     * change the progressbar drawable     * Build.VERSION >= 21(5.0)
     *
     * @param context
     */
    private void setProgressDrawable(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RotateDrawable rotateDrawable = (RotateDrawable) ContextCompat.getDrawable(context, R.drawable.bg_progress);
            GradientDrawable gradientDrawable = (GradientDrawable) rotateDrawable.getDrawable();
            if (gradientDrawable != null) {
                gradientDrawable.setColors(new int[]{mProgressColor, Color.WHITE});
                rotateDrawable.setDrawable(gradientDrawable);
                mProgress.setIndeterminateDrawable(rotateDrawable);
            }
        }
    }

    /**
     * start Animation
     */
    public void startAnimation() {
        if (mStartSet == null) {
            initStartAnim();
            initErrorAnim();
        }
        mStartSet.start();
    }

    /**
     * end Animation
     */
    public void errorAnimation() {
        if (mStartSet.isRunning()) {
            mStartSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mEndSet.start();
                }
            });
        } else {
            mEndSet.start();
        }
    }

    /**
     * InitStartAnimation
     */
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

    /**
     * InitErrorAnimation
     */
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

    /**
     * init mButton
     *
     * @param target
     */
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

    public void setEndText(String endText) {
        mEndText = endText;
        initErrorAnim();
    }
}
