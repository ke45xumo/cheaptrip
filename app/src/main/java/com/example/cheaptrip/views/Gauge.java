package com.example.cheaptrip.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import androidx.core.content.ContextCompat;

import com.example.cheaptrip.R;

public class Gauge extends View {

    public static final int MAX = 100;
    public static final int MIN = 0;

    /**
     * Offset = -90 indicates that the progress starts from 12 o'clock.
     */
    private static final int ANGLE_OFFSET = -180;

    /**
     * The current points value.
     */
    private int mPoints = MIN;

    /**
     * The min value of progress value.
     */
    private int mMin = MIN;

    /**
     * The Maximum value that this SeekArc can be set to
     */
    private int mMax = MAX;

    /**
     * The increment/decrement value for each movement of progress.
     */
    private int mStep = 10;


    private int mProgressWidth = 12;
    private int mArcWidth = 12;

    //
    // internal variables
    //
    private float mCurrentProgress = 0;

    private RectF mArcRect = new RectF();
    private Paint mArcPaint;

    private float mProgressSweep = 0;
    private Paint mProgressPaint;

    private float mTextSize = 72;
    private Paint mTextPaint;
    private Rect mTextRect = new Rect();

    private boolean bIsinitiliazied = false;

    private boolean mGlow    = true;                // glow effect


    /**
     * The current touch angle of arc.
     */
    private double mTouchAngle;
    private RectF mScaleRect = new RectF();
    private Paint mScalePaint;
    private Paint mInnerScalePaint;

    public Gauge(Context context) {
        super(context);
        init(context, null);
    }

    public Gauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        float density = getResources().getDisplayMetrics().density;

        // Defaults, may need to link this into theme settings
        int arcColor = ContextCompat.getColor(context, R.color.color_arc);
        int progressColor = ContextCompat.getColor(context, R.color.color_progress);
        int textColor = ContextCompat.getColor(context, R.color.color_text);

        mProgressWidth = (int) (mProgressWidth * density);
        mArcWidth = (int) (mArcWidth * density);
        mTextSize = (int) (mTextSize * density);
        /*=======================================================================
         * Get Attributes from XML - layout file
         *=======================================================================*/
        if (attrs != null) {
            // Attribute initialization
            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.Gauge, 0, 0);

            mPoints = a.getInteger(R.styleable.Gauge_points, mPoints);
            mMin = a.getInteger(R.styleable.Gauge_min, mMin);
            mMax = a.getInteger(R.styleable.Gauge_max, mMax);
            mStep = a.getInteger(R.styleable.Gauge_step, mStep);

            mProgressWidth = (int) a.getDimension(R.styleable.Gauge_progressWidth, mProgressWidth);
            progressColor = a.getColor(R.styleable.Gauge_progressColor, progressColor);

            mArcWidth = (int) a.getDimension(R.styleable.Gauge_arcWidth, mArcWidth);
            arcColor = a.getColor(R.styleable.Gauge_arcColor, arcColor);

            mTextSize = (int) a.getDimension(R.styleable.Gauge_textSize, mTextSize);
            textColor = a.getColor(R.styleable.Gauge_textColor, textColor);

            mGlow = a.getBoolean(R.styleable.Gauge_glow, mGlow);

            a.recycle();
        }

        // range check
        mPoints = (mPoints > mMax) ? mMax : mPoints;
        mPoints = (mPoints < mMin) ? mMin : mPoints;

        mProgressSweep = (float) mPoints / valuePerDegree();
        /*=======================================================================
         * Initialize Arc Paint
         *=======================================================================*/
        mArcPaint = new Paint();
        mArcPaint.setColor(arcColor);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        /*=======================================================================
         * Initialize Progress Paint
         *=======================================================================*/
        mProgressPaint = new Paint();
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressWidth);

        // Glow Effect
        if(mGlow){
            BlurMaskFilter filter = new BlurMaskFilter(mProgressWidth/2, BlurMaskFilter.Blur.SOLID);
            mProgressPaint.setMaskFilter(filter);
        }
        /*=======================================================================
         * Set Sweep Gradient
         *=======================================================================*/
        int[] colors = { Color.GREEN,Color.RED};
        float []positions = {0, 45f/360};

        Shader gradient = new SweepGradient(0,0, colors,positions);
        mProgressPaint.setShader(gradient);
        /*=======================================================================
         * Set Text
         *=======================================================================*/
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);
        /*=======================================================================
         * Set Paint of the Scale
         *=======================================================================*/
        mScalePaint = new Paint();
        mScalePaint.setColor(arcColor);
        mScalePaint.setAntiAlias(true);
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setStrokeWidth(50);
        /*=======================================================================
         * Set inner Scale Paint
         *=======================================================================*/
        mInnerScalePaint = new Paint();
        mInnerScalePaint.setColor(arcColor);
        mInnerScalePaint.setAntiAlias(true);
        mInnerScalePaint.setStyle(Paint.Style.STROKE);
        mInnerScalePaint.setStrokeWidth(25);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        float top = mProgressWidth/2 +5;
        float left = getPaddingLeft();
        float right = width -left;
        float bottom = 2* height - top;

        mArcRect.set(left, top, right, bottom);

        mScaleRect.left = left + 25;
        mScaleRect.right = right - 25;
        mScaleRect.top = top + 25;
        mScaleRect.bottom = bottom -25;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the scale
        drawScale(canvas);

        // draw the text
        String textPoint = String.valueOf(mPoints);
        mTextPaint.getTextBounds(textPoint, 0, textPoint.length(), mTextRect);

        // center the text
        int xPos = canvas.getWidth() / 2 - mTextRect.width() / 2;
        int yPos = (int) ((mArcRect.centerY()) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
        canvas.drawText(String.valueOf(mPoints), xPos  , yPos - 50, mTextPaint);

        // draw the arc and progress
        canvas.drawArc(mArcRect, -180, 180, false, mArcPaint);
        canvas.drawArc(mArcRect, ANGLE_OFFSET, mProgressSweep, false, mProgressPaint);


        // Draw the needle
       /* Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight(),canvas.getHeight()/10, paint);*/
        canvas.drawArc(mArcRect,mProgressSweep +180,2,true, mTextPaint);

        if(bIsinitiliazied == false){
            ValueAnimator animation = ValueAnimator.ofInt(0, 75);
            animation.setDuration(1000);
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int mCurrentProgress = (int) valueAnimator.getAnimatedValue();
                    setProgress(mCurrentProgress);
                    invalidate();
                }
            });

            bIsinitiliazied =true;
            animation.start();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            this.getParent().requestDisallowInterceptTouchEvent(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    updateOnTouch(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateOnTouch(event);
                    break;
                case MotionEvent.ACTION_UP:
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return true;

    }

    /**
     * Update all the UI components on touch events.
     *
     * @param event MotionEvent
     */
    private void updateOnTouch(MotionEvent event) {
        setPressed(true);
        mTouchAngle = convertTouchEventPointToAngle(event.getX(), event.getY());
        int progress = convertAngleToProgress(mTouchAngle);
        updateProgress(progress, true);
    }


    void drawScale(Canvas canvas){
        drawReserve(canvas);

        int quarterStep = (180/4)-1;

        for(int angle =-177 ; angle <= 10 ; angle+= quarterStep){
            canvas.drawArc(mScaleRect, angle, Math.signum(angle)*2,false,mScalePaint);

            for(int innerAngle = angle; innerAngle < angle + quarterStep ; innerAngle += quarterStep/4){
                canvas.drawArc(mScaleRect, innerAngle, Math.signum(innerAngle) * 1, false,mInnerScalePaint);
            }

        }

    }

    void drawReserve(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);

        float tankPercentReserve = 180/10;  // Angle of the Reserve Area
        canvas.drawArc(mScaleRect, ANGLE_OFFSET , tankPercentReserve ,false,paint);
    }


    private double convertTouchEventPointToAngle(float xPos, float yPos) {
        // transform touch coordinate into component coordinate
        float midX = mArcRect.centerX();
        float midY = mArcRect.centerY();

        float x = xPos - midX;
        float y = yPos - midY;

        double angle = Math.toDegrees(Math.atan2(y, x));
        angle += 180;

        return angle;
    }

    private int convertAngleToProgress(double angle) {
        return (int) Math.round(valuePerDegree() * angle);
    }

    private float valuePerDegree() {
        return (float) (mMax) / 180.0f;
    }

    private void updateProgress(int progress, boolean fromUser) {
        if(progress > mMax ){
            mCurrentProgress = mMax;
            return;
        }

        if(progress < mMin){
            mCurrentProgress = mMin;
        }

        mCurrentProgress = progress;
        mPoints = progress - (progress % mStep);
        mProgressSweep = (float) progress / valuePerDegree();
        invalidate();
    }

    public float getProgress() {
        return mCurrentProgress;
    }

    public void setProgress(int progress) {
        mCurrentProgress = progress;
        updateProgress(progress,false);
    }
}