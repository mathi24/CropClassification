package com.jsn.cropclassification.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.jsn.cropclassification.R;
import com.jsn.cropclassification.utils.StepBean;

import java.util.ArrayList;
import java.util.List;


public class HorizontalStepsViewIndicator extends View
{
    //  definition default height
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

    private float mCompletedLineHeight;//     definition completed line height
    private float mCircleRadius;//  definition circle radius

    private Drawable mCompleteIcon;//   definition default completed icon
    private Drawable mAttentionIcon;//     definition default underway icon
    private Drawable mDefaultIcon;//  definition default unCompleted icon
    private float mCenterY;//    definition view centerY position
    private float mLeftY;//  definition rectangle LeftY position
    private float mRightY;//  definition rectangle RightY position

    private List<StepBean> mStepBeanList ;//    there are currently few step
    private int mStepNum = 0;
    private float mLinePadding;//  definition the spacing between the two circles

    private List<Float> mCircleCenterPointPositionList;// definition all of circles center point list
    private Paint mUnCompletedPaint;//Paint  definition mUnCompletedPaint
    private Paint mCompletedPaint;//paint      definition mCompletedPaint
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(), R.color.uncompleted_color);//  definition
    private int mCompletedLineColor = Color.WHITE;//     definition mCompletedLineColor
    private PathEffect mEffects;
    private int mComplectingPosition;//position   underway position


    private Path mPath;

    private OnDrawIndicatorListener mOnDrawListener;
    private int screenWidth;//this screen width


    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener)
    {
        mOnDrawListener = onDrawListener;
    }

    /**
     * get get circle radius
     *
     * @return
     */
    public float getCircleRadius()
    {
        return mCircleRadius;
    }


    public HorizontalStepsViewIndicator(Context context)
    {
        this(context, null);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * init
     */
    private void init()
    {
        mStepBeanList = new ArrayList<>();
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mCircleCenterPointPositionList = new ArrayList<>();

        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setStrokeWidth(2);

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //set mCompletedLineHeight
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;
        //  set mCircleRadius
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //   set mLinePadding
        mLinePadding = 1.2f * defaultStepIndicatorNum;

        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);//icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);//icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.default_icon);//icon
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = defaultStepIndicatorNum * 2;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec))
        {
            screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = defaultStepIndicatorNum;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec))
        {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        width = (int) (mStepNum * mCircleRadius * 2 - (mStepNum - 1) * mLinePadding);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        //   get view centerY，keep current stepview center vertical
        mCenterY = 0.5f * getHeight();

        mLeftY = mCenterY - (mCompletedLineHeight / 2);

        mRightY = mCenterY + mCompletedLineHeight / 2;

        mCircleCenterPointPositionList.clear();
        for(int i = 0; i < mStepNum; i++)
        {

            float paddingLeft = (screenWidth - mStepNum * mCircleRadius * 2 - (mStepNum - 1) * mLinePadding) / 2;
            //add to list
            mCircleCenterPointPositionList.add(paddingLeft + mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
        }

        /**
         * set listener
         */
        if(mOnDrawListener!=null)
        {
            mOnDrawListener.ondrawIndicator();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mOnDrawListener!=null)
        {
            mOnDrawListener.ondrawIndicator();
        }
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        //------------------------------draw line-----------------------------------------------
        for(int i = 0; i < mCircleCenterPointPositionList.size() -1; i++)
        {
            //ComplectedXPosition
            final float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            //omplectedXPosition
            final float afterComplectedXPosition = mCircleCenterPointPositionList.get(i + 1);

            if(i <= mComplectingPosition&&mStepBeanList.get(0).getState()!=StepBean.STEP_UNDO)
            {

                canvas.drawRect(preComplectedXPosition + mCircleRadius - 10, mLeftY, afterComplectedXPosition - mCircleRadius + 10, mRightY, mCompletedPaint);
            } else
            {
                mPath.moveTo(preComplectedXPosition + mCircleRadius, mCenterY);
                mPath.lineTo(afterComplectedXPosition - mCircleRadius, mCenterY);
                canvas.drawPath(mPath, mUnCompletedPaint);
            }
        }
        //------------------------------draw line-----------------------------------------------


        //----------------------------draw icon-----------------------------------------------
        for(int i = 0; i < mCircleCenterPointPositionList.size(); i++)
        {
            final float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition - mCircleRadius), (int) (mCenterY - mCircleRadius), (int) (currentComplectedXPosition + mCircleRadius), (int) (mCenterY + mCircleRadius));

            StepBean stepsBean = mStepBeanList.get(i);

            if(stepsBean.getState()==StepBean.STEP_UNDO)
            {
                mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            }else if(stepsBean.getState()==StepBean.STEP_CURRENT)
            {
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(currentComplectedXPosition, mCenterY, mCircleRadius * 1.1f, mCompletedPaint);
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            }else if(stepsBean.getState()==StepBean.STEP_COMPLETED)
            {
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            }
        }
        //----------------------------draw icon-----------------------------------------------
    }


    public List<Float> getCircleCenterPointPositionList()
    {
        return mCircleCenterPointPositionList;
    }


    public void setStepNum(List<StepBean> stepsBeanList)
    {
        this.mStepBeanList = stepsBeanList;
        mStepNum = mStepBeanList.size();

        if(mStepBeanList!=null&&mStepBeanList.size()>0)
        {
            for(int i = 0;i<mStepNum;i++)
            {
                StepBean stepsBean = mStepBeanList.get(i);
                {
                    if(stepsBean.getState()==StepBean.STEP_COMPLETED)
                    {
                        mComplectingPosition = i;
                    }
                }
            }
        }

        requestLayout();
    }


    public void setUnCompletedLineColor(int unCompletedLineColor)
    {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }


    public void setCompletedLineColor(int completedLineColor)
    {
        this.mCompletedLineColor = completedLineColor;
    }


    public void setDefaultIcon(Drawable defaultIcon)
    {
        this.mDefaultIcon = defaultIcon;
    }


    public void setCompleteIcon(Drawable completeIcon)
    {
        this.mCompleteIcon = completeIcon;
    }


    public void setAttentionIcon(Drawable attentionIcon)
    {
        this.mAttentionIcon = attentionIcon;
    }



    public interface OnDrawIndicatorListener
    {
        void ondrawIndicator();
    }
}
