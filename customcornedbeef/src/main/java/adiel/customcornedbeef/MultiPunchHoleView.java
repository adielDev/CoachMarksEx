package adiel.customcornedbeef;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * The helper view for the punch hole and listeners.
 */
public class MultiPunchHoleView extends LinearLayout {

    // Helpers to punch a hole
    private final Paint mPaint;

    private int[] mCircleCenterX;
    private int[] mCircleCenterY ;
    private float [] mCircleRadius;
    private Rect[] mRect ; // Contains target view's rect

    private OnClickListener mPunchHoleClickListener;
    private OnClickListener mGlobalClickListener;

    public MultiPunchHoleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }
    public void init(int numOfViews){
        mCircleCenterX = new int[numOfViews];
        mCircleCenterY=new int[numOfViews];
        mCircleRadius = new float[numOfViews];
        mRect = new Rect[numOfViews];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Punch a hole to target (x, y) position with given radius.
        for (int i = 0; i <mCircleRadius.length ; i++) {
            canvas.drawCircle(mCircleCenterX[i], mCircleCenterY[i], mCircleRadius[i], mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i<mCircleRadius.length  ; i++) {
                    if (mRect[i].contains((int) event.getX(), (int) event.getY())) {
                        if (mPunchHoleClickListener != null) {
                            mPunchHoleClickListener.onClick(this);
                            return true;
                        }
                    } else {
                        if (mGlobalClickListener != null) {
                            mGlobalClickListener.onClick(this);
                            return true;
                        }
                    }
                }

                return false;
            default:
                return false;
        }
    }


    /**
     * Set the punch hole's coordinates and radius
     *
     * @param centerX circle's x coordinate
     * @param centerY circle's y coordinate
     * @param radius circle's radius
     * @return true if value is changed
     */
    public boolean setCircle(int centerX, int centerY, float radius,int index) {
        boolean changed = false;
            changed |= setCircleCenterX(centerX,index);
            changed |= setCircleCenterY(centerY,index);
            changed |= setCircleRadius(radius,index);

            if (!changed) {
                return false;
            }

            mRect[index] = new Rect(
                    centerX - (int) radius, centerY- (int) radius,
                    centerX + (int) radius, centerY + (int) radius);

        postInvalidate();

        return true;
    }

    /**
     * Set the punch hole's x coordinate.
     *
     * This needs to be public to do the horizontal translation animation.
     *
     * @param centerX circle's x coordinate
     * @return true if value is changed
     */
    public boolean setCircleCenterX(int centerX,int index) {
        if (this.mCircleCenterX [index]!= centerX) {
            this.mCircleCenterX[index] = centerX;
            postInvalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the punch hole's y coordinate
     *
     * @param centerY circle's y coordinate
     * @return true if value is changed
     */
    private boolean setCircleCenterY(int centerY,int index) {
        if (this.mCircleCenterY[index] != centerY) {
            this.mCircleCenterY[index] = centerY;
            postInvalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the punch hole's radius
     *
     * @param radius circle's radius
     * @return true if value is changed
     */
    private boolean setCircleRadius(float radius ,int index) {
        if (this.mCircleRadius[index] != radius) {
            this.mCircleRadius[index] = radius;
            postInvalidate();
            return true;
        } else {
            return false;
        }
    }


    public void setOnTargetClickListener(OnClickListener listener) {
        this.mPunchHoleClickListener = listener;
    }

    public void setOnGlobalClickListener(OnClickListener listener) {
        this.mGlobalClickListener = listener;
    }
}
