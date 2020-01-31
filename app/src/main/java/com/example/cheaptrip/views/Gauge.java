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

    public static final int MAX = 100;      // maximum stepsize of the seekbar (100 means 100%)
    public static final int MIN = 0;        // minimum stepsize of the seekbar (0 means 0%)


    private static final int ANGLE_OFFSET = -180;   // this means it starts as a half circle

    private int mPoints = MIN;              // The value of the current progress

    private int mMin = MIN;                 //The min value of progress value.
    private int mMax = MAX;                 // The Maximum value that this SeekArc can be set to

    private int mStep = 10;                 // Steps to be taken when moving the progress

    private int mProgressWidth = 12;        // progress circle width
    private int mArcWidth = 12;             // arc width

    private float mCurrentProgress = 0;     // starting with this progress values

    private RectF mArcRect = new RectF();   // Rectangle measuring the arc
    private Paint mArcPaint;                // Paint to be used for the arc

    private float mProgressSweep = 0;       // current progress value
    private Paint mProgressPaint;           // paint of the current progress

    private float mTextSize = 72;           // Inner text size
    private Paint mTextPaint;               // Inner text paint
    private Rect mTextRect = new Rect();    // Rect measuring the inner textbox

    private boolean bIsinitiliazied = false;    // indicator to determine if it has been initiliazed
                                                // (ensuring the animation is only used once)

    private boolean mGlow    = true;            // glow effect
    private boolean mTextEnabled = false;       // Indeicator to wheter enable or disable text

    private Paint mNeedlePaint;                 // Color of the needle

    private boolean mAnimate = false;           // inidcator if it should be animated on startup

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
        int needleColor = ContextCompat.getColor(context,R.color.needle_color);

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
            mTextEnabled = a.getBoolean(R.styleable.Gauge_text_enabled, mGlow);

            needleColor = a.getColor(R.styleable.Gauge_needle_color, needleColor);

            mAnimate = a.getBoolean(R.styleable.Gauge_animate, mAnimate);

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

        if(mGlow){
            BlurMaskFilter filter = new BlurMaskFilter(mArcWidth/2, BlurMaskFilter.Blur.SOLID);
            mArcPaint.setMaskFilter(filter);
        }

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

        if(mGlow){
            BlurMaskFilter filter = new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID);
            mScalePaint.setMaskFilter(filter);
        }
        /*=======================================================================
         * Set inner Scale Paint
         *=======================================================================*/
        mInnerScalePaint = new Paint();
        mInnerScalePaint.setColor(arcColor);
        mInnerScalePaint.setAntiAlias(true);
        mInnerScalePaint.setStyle(Paint.Style.STROKE);
        mInnerScalePaint.setStrokeWidth(25);

        if(mGlow){
            BlurMaskFilter filter = new BlurMaskFilter(12, BlurMaskFilter.Blur.SOLID);
            mScalePaint.setMaskFilter(filter);
        }
        /*=======================================================================
         * Set Needle Paint
         *=======================================================================*/
        mNeedlePaint = new Paint();
        mNeedlePaint.setColor(needleColor);
        mNeedlePaint.setAntiAlias(true);
        mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedlePaint.setStrokeWidth(4);

        if(mGlow){
            BlurMaskFilter filter = new BlurMaskFilter(4, BlurMaskFilter.Blur.SOLID);
            mNeedlePaint.setMaskFilter(filter);
        }
    }

    /**
     * This will be called constantly.
     * It will be measuring and updating the current Values of the Gauge.
     * It will update the field variables, which will be used for the painting.
     *
     * @param widthMeasureSpec      width of the gauge view
     * @param heightMeasureSpec     height of the gauge view
     */
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
    /**
     * This is a callback function which gets consistently called to draw the components to
     * the screen.
     *
     * It draws
     *      * Arc of the gauge
     *      * Progress of the gauge
     *      * text of the gauge
     *      * the needle of the gauge
     *
     * @param canvas        Canvas to draw the Gauge
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the scale
        drawScale(canvas);

        // Draw the Text if( enabled)
        if (mTextEnabled){
            // draw the text
            String textPoint = String.valueOf(mPoints);
            mTextPaint.getTextBounds(textPoint, 0, textPoint.length(), mTextRect);

            // center the text
            int xPos = canvas.getWidth() / 2 - mTextRect.width() / 2;
            int yPos = (int) ((mArcRect.centerY()) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
            canvas.drawText(String.valueOf(mPoints), xPos, yPos - 50, mTextPaint);
        }

        // draw the arc and progress
        canvas.drawArc(mArcRect, -180, 180, false, mArcPaint);
        canvas.drawArc(mArcRect, ANGLE_OFFSET, mProgressSweep, false, mProgressPaint);


        // Draw the needle
        canvas.drawArc(mArcRect,mProgressSweep +180,2,true, mNeedlePaint);

        if(bIsinitiliazied == false && mAnimate){
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

    /**
     * Gets called when user touches this View.
     * It updates the current Progress Sweep based on the coordinates of the Motion event.
     *
     * @param event     Motion event that triggered the call of this function
     * @return          true, always
     */
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


    /**
     * Draws the scale of the Gauge.
     * 4 long Scale bars at the quarters of the Arc.
     * Between the long Scale Bars another 4 short scale bars.
     * In sum there are 16 scale bars to be drawn to the arc.
     *
     * @param canvas    Canvas the scale will be drawn to
     */
    void drawScale(Canvas canvas){
        drawReserve(canvas);

        int quarterStep = (180/4)-1;

        // Draw thick bars (4 times)
        for(int angle =-177 ; angle <= 10 ; angle+= quarterStep){
            canvas.drawArc(mScaleRect, angle, Math.signum(angle)*2,false,mScalePaint);

            // Draw inner thin bars (4 times)
            for(int innerAngle = angle; innerAngle < angle + quarterStep ; innerAngle += quarterStep/4){
                canvas.drawArc(mScaleRect, innerAngle, Math.signum(innerAngle) * 1, false,mInnerScalePaint);
            }
        }
    }

    /**
     * Draws the reserve area of the Gauge
     *
     * @param canvas    Canvas the Reserve area will be drawn to
     */
    void drawReserve(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);

        float tankPercentReserve = 180/10;  // Angle of the Reserve Area
        canvas.drawArc(mScaleRect, ANGLE_OFFSET , tankPercentReserve ,false,paint);
    }

    /**
     * This function will be used to convert the coordinates of the touch event
     * to an angle to draw the progress sweep.
     *
     * @param xPos  X postion of the touch event
     * @param yPos  Y position of the touch event
     *
     * @return  return the converted Angle the touch event
     */
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

    /**
     * Converts an selected Angle to the amount of porgress steps betwen the mMax and mMin in points.
     * (In this case it is in percent)
     *
     * @param angle     Angle to convert to progress steps
     * @return          progress steps, that refer to given angle
     */
    private int convertAngleToProgress(double angle) {
        return (int) Math.round(valuePerDegree() * angle);
    }

    /**
     * Gets the progress ratio to Angle.
     * @return  Angle to progress ratio of the gauge
     */
    private float valuePerDegree() {
        return (float) (mMax) / 180.0f;
    }

    /**
     * Updates the progress fields of the Gauge from given progress points.
     *
     * @param progress      progress steps
     * @param fromUser      boolean determining, if it is set from function of the user
     */
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

    /**
     * Gets the current progress steps.
     * This can be called by the user.
     *
     * @return the current progress steps
     */
    public float getProgress() {
        return mCurrentProgress;
    }

    /**
     * Sets the current progress steps.
     * This can be called by the user.
     *
     * @param progress progress set by user programmatically
     */
    public void setProgress(int progress) {
        mCurrentProgress = progress;
        updateProgress(progress,false);
    }
}