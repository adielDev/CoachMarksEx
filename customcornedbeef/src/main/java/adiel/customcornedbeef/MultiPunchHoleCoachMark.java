package adiel.customcornedbeef;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * The coach mark for a "punch hole" to present a transparent circle onto the given view.
 */
public class MultiPunchHoleCoachMark extends InternallyAnchoredCoachMark implements View.OnClickListener {

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.nextBtn) {
            if(currentViewKey>=mTargetView.length-1){
                Toast.makeText(mContext, "currentViewKey>=viewsMap.size()", Toast.LENGTH_SHORT).show();
            }else {
                currentViewKey++;
                changeMultiTarget(mTargetView[currentViewKey]);
            }
        }else if(i==R.id.prevBtn){
            if(currentViewKey<0){
                Toast.makeText(mContext, "currentViewKey<0", Toast.LENGTH_SHORT).show();
            }else {
                currentViewKey--;
                changeMultiTarget(mTargetView[currentViewKey]);
            }

        }
        if(currentViewKey==mTargetView.length-1){
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        }
        if(currentViewKey<=0){
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(false);
        }
        if(currentViewKey>0 && currentViewKey<mTargetView.length-1){
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }
        if(i==R.id.dismissBtn){
            dismiss();
        }


    }

    @IntDef({POSITION_CONTENT_AUTOMATICALLY, POSITION_CONTENT_ABOVE, POSITION_CONTENT_BELOW,POSITION_CONTENT_FIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PunchMarkContentPosition {}

    /**
     * Decide whether to place the content above or below the punch hole at runtime depending on
     * which side has the most space available
     */
    public static final int POSITION_CONTENT_AUTOMATICALLY = 0;
    /**
     * Position the coach mark content above the punch hole
     */
    public static final int POSITION_CONTENT_ABOVE = 1;
    /**
     * Position the coach mark content below the punch hole
     */
    public static final int POSITION_CONTENT_BELOW = 2;
    public static final int POSITION_CONTENT_FIX = 3;

    private final float mGap;
    private final long mHorizontalTranslationDuration;
    private final int mContentPosition;
    private  View [][] mTargetView ;
    private final int[] mTargetViewLoc = new int[2];
    private final int[] mAnchorViewLoc = new int[2];
    private float [][]mRelCircleRadius ;

    private MultiPunchHoleView mPunchHoleView;
    private View mPunchHoleContent;
    private Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private AnimatorSet mHorizontalAnimators;
    Button nextBtn;
    Button prevBtn;
    Button dismissBtn;
    int currentViewKey=0;

    protected MultiPunchHoleCoachMark(PunchHoleCoachMarkBuilder builder) {
        super(builder);

        mGap = mContext.getResources().getDimension(R.dimen.punchhole_coach_mark_gap);

        mTargetView = builder.targetView;
        mRelCircleRadius = new float[mTargetView.length][];
        for (int i = 0; i <mTargetView.length ; i++) {
            mRelCircleRadius[i]=new float[mTargetView[i].length];
        }

        mPunchHoleView.setOnTargetClickListener(builder.targetClickListener);
        mPunchHoleView.setOnGlobalClickListener(builder.globalClickListener);
        mPunchHoleView.setBackgroundColor(builder.overlayColor);

        mPunchHoleContent.setLayoutParams(
                new LinearLayout.LayoutParams(builder.contentWidth, builder.contentHeight));

        mHorizontalTranslationDuration = builder.horizontalAnimationDuration;

        mContentPosition = builder.contentPositioning;
    }

    @Override
    protected View createContentView(View content) {
//        final MultiPunchHoleView view = (MultiPunchHoleView) LayoutInflater.from(mContext)
//                .inflate(R.layout.multi_punchhole_coach_mark, null);

        MultiPunchHoleView view = new MultiPunchHoleView(mContext);

        view.addView(content);

        mPunchHoleView = view;
        mPunchHoleContent = content;

        prevBtn = (Button) mPunchHoleContent.findViewById(R.id.prevBtn);
        prevBtn.setOnClickListener(this);
        if(currentViewKey==0){
            prevBtn.setEnabled(false);
        }
        nextBtn= (Button) mPunchHoleContent.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        mPunchHoleContent.findViewById(R.id.dismissBtn).setOnClickListener(this);


        return view;
    }

    @Override
    protected PopupWindow createNewPopupWindow(View contentView) {
        PopupWindow popup = new PopupWindow(contentView, WRAP_CONTENT, WRAP_CONTENT);
        popup.setTouchable(true);
        return popup;
    }

    @Override
    protected CoachMarkDimens<Integer> getPopupDimens(CoachMarkDimens<Integer> anchorDimens) {
        return anchorDimens;
    }

    @Override
    protected void updateView(CoachMarkDimens<Integer> popupDimens,  CoachMarkDimens<Integer> anchorDimens) {
        mPopup.update(popupDimens.x, popupDimens.y, popupDimens.width, popupDimens.height);
        mPunchHoleView.init(mTargetView[currentViewKey].length);
        for (int i = 0; i <mTargetView[currentViewKey].length; i++) {
            mTargetView[currentViewKey][i].getLocationOnScreen(mTargetViewLoc);
            mAnchor.getLocationOnScreen(mAnchorViewLoc);
            mRelCircleRadius[currentViewKey][i] = (mTargetView[currentViewKey][i].getHeight() + mGap) / 2;

            // If the coachmark has an horizontal translation animation, draw the
            // circle on the start of the target view (it will move to the end).
            // However, if the width of the target view is smaller than the diameter
            // of the punch hole, just center the circle (no point in animating).
            final int startOffsetX = hasHorizontalTranslation(i)
                    ?  isRtlConfig()
                    ? mTargetViewLoc[0] + mTargetView[currentViewKey][i].getWidth() - (int) mRelCircleRadius[currentViewKey][i]
                    : mTargetViewLoc[0] + (int) mRelCircleRadius[currentViewKey][i]
                    : (mTargetView[currentViewKey][i].getWidth() / 2);

            final int relCircleX = mTargetViewLoc[0] - mAnchorViewLoc[0] + startOffsetX;
            final int relCircleY = mTargetViewLoc[1] - mAnchorViewLoc[1] + (mTargetView[currentViewKey][i].getHeight() / 2);

            if (!mPunchHoleView.setCircle(relCircleX, relCircleY, mRelCircleRadius[currentViewKey][i],i)) {
                return;
            }

            if (hasHorizontalTranslation(i)) {
                animateHorizontalTranslation(i);
            }

            // Calculating the proper padding of layout
            int positioning = mContentPosition;

            if (mContentPosition == POSITION_CONTENT_AUTOMATICALLY) {
                positioning = relCircleY < (mAnchor.getHeight() / 2) ?
                        POSITION_CONTENT_BELOW : POSITION_CONTENT_ABOVE;
            }

            int upperPadding = 0;
            int lowerPadding = 0;

            if (positioning == POSITION_CONTENT_BELOW) {
                // Circle in upper side
                upperPadding = (int) (relCircleY + mRelCircleRadius[currentViewKey][i]);
            } else {
                // Circle in lower side
                lowerPadding = mAnchor.getHeight() - (int) (relCircleY - mRelCircleRadius[currentViewKey][i]);
            }
            if (mContentPosition == POSITION_CONTENT_FIX) {
                upperPadding=0;
                lowerPadding=0;
            }


            int horizontalPadding = (int) mContext.getResources().getDimension(R.dimen.punchhole_coach_mark_horizontal_padding);
            int verticalPadding = (int) mContext.getResources().getDimension(R.dimen.punchhole_coach_mark_vertical_padding);
            mPunchHoleView.setPadding(
                    horizontalPadding, verticalPadding + upperPadding,
                    horizontalPadding, verticalPadding + lowerPadding);
        }

    }

    @Override
    void updateViewWithNewTarget(View newTarget, CoachMarkDimens<Integer> popupDimens, CoachMarkDimens<Integer> anchorDimens) {

    }

    @Override
    void updateViewWithNewMultiTarget(View[] newTargets, CoachMarkDimens<Integer> popupDimens, CoachMarkDimens<Integer> anchorDimens) {
        mTargetView[currentViewKey] = newTargets;
        updateView(popupDimens,anchorDimens);
    }

    /**
     * Move the punch hole from start to end of the target view and back from
     * end to start, unless the width of the target view is smaller than the
     * diameter of the punch hole in which case the circle will be centered and
     * the animation is pointless.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void animateHorizontalTranslation(int index) {
        if (hasHorizontalTranslation(index) && mHorizontalAnimators == null) {
            for (int i = 0; i < mTargetView.length; i++) {
                final int leftMostPosition = mTargetViewLoc[0] + (int) mRelCircleRadius[currentViewKey][i];
                final int rightMostPosition = mTargetViewLoc[0] + mTargetView[currentViewKey][i].getWidth() - (int) mRelCircleRadius[currentViewKey][i];

                final int startX = isRtlConfig() ? rightMostPosition : leftMostPosition;
                final int endX = isRtlConfig() ? leftMostPosition : rightMostPosition;

                final ValueAnimator[] horizontalAnimations = new ValueAnimator[]{
                        ObjectAnimator.ofInt(mPunchHoleView, "circleCenterX", startX, endX),
                        ObjectAnimator.ofInt(mPunchHoleView, "circleCenterX", endX, startX)
                };

                mHorizontalAnimators = new AnimatorSet();
                mHorizontalAnimators.playSequentially(horizontalAnimations);
                // Set both durations to half the overall animation length (both animations together
                // will then sum to the duration)
                mHorizontalAnimators.setDuration(mHorizontalTranslationDuration / 2);
                mHorizontalAnimators.setInterpolator(INTERPOLATOR);
                mHorizontalAnimators.start();
            }

        }
    }

    /**
     * Check if the punch hole should have a horizontal animation. Checks:
     *  - the width of the target view is bigger than the diameter of the circle
     *      (otherwise there's no space to perform the animation).
     *  - the duration is greater than 0
     *
     * @return  whether to display the animation
     */
    private boolean hasHorizontalTranslation(int index) {
        return mHorizontalTranslationDuration > 0 && mTargetView[currentViewKey][index].getWidth() > 2 * mRelCircleRadius[currentViewKey][index];
    }

    /**
     * For APIs above JELLY_BEAN_MR1, we can check if the locale of a device is
     * LTR or RTL. Below that API, we assume it's LTR.
     * We need this to show the horizontal translation from start to end
     * (from left to right in LTR and from right to left in LTR), if possible.
     * @return  whether the device has a RTL locale
     */
    private boolean isRtlConfig() {
        final Configuration config = mAnchor.getResources().getConfiguration();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static class PunchHoleCoachMarkBuilder extends InternallyAnchoredCoachMarkBuilder {

        protected View[][]targetView;
        protected int overlayColor = 0xBF000000;

        protected View.OnClickListener targetClickListener;
        protected View.OnClickListener globalClickListener;

        protected long horizontalAnimationDuration;
        //private int contentPositioning = POSITION_CONTENT_AUTOMATICALLY;
        private int contentPositioning = POSITION_CONTENT_FIX;
        /**
         * Default layout parameters for {@link LinearLayout}, from which {@link PunchHoleView}
         * inherits
         *
         * @see LinearLayout#generateDefaultLayoutParams()
         */
        private int contentWidth = MATCH_PARENT;
        //private int contentHeight = WRAP_CONTENT;
        private int contentHeight = MATCH_PARENT;

        public PunchHoleCoachMarkBuilder(Context context, View anchor, String message) {
            super(context, anchor, message);
        }

        public PunchHoleCoachMarkBuilder(Context context, View anchor, View content) {
            super(context, anchor, content);
        }

        public PunchHoleCoachMarkBuilder(Context context, View anchor, @LayoutRes int contentResId) {
            super(context, anchor, contentResId);
        }

        /**
         * Set a target view where the "punch hole" will display.
         *
         * @param view
         */
        public PunchHoleCoachMarkBuilder setTargetView(View[][] view) {
            this.targetView = view;
            return this;
        }

        /**
         * Set a listener to be called when the target view is clicked.
         *
         * @param listener
         */
        public PunchHoleCoachMarkBuilder setOnTargetClickListener(View.OnClickListener listener) {
            this.targetClickListener = listener;
            return this;
        }

        /**
         * Set a listener to be called when the coach mark is clicked.
         *
         * @param listener
         */
        public PunchHoleCoachMarkBuilder setOnGlobalClickListener(View.OnClickListener listener) {
            this.globalClickListener = listener;
            return this;
        }

        /**
         * Set the color of this coach mark.
         *
         * @param overlayColor the color to set
         */
        public PunchHoleCoachMarkBuilder setOverlayColor(int overlayColor) {
            this.overlayColor = overlayColor;
            return this;
        }

        /**
         * Set the layout information for the content inside the coach mark, allowing explicit
         * placement and sizing of the content with respect to the punch hole
         *
         * @param contentWidth the width of the content - defaults to {@link MATCH_PARENT}
         * @param contentHeight the height of the content - defaults to {@link WRAP_CONTENT}
         * @param contentPositioning where to place the content - defaults to
         *  {@link POSITION_CONTENT_AUTOMATICALLY}
         */
        public PunchHoleCoachMarkBuilder setContentLayoutParams(
                final int contentWidth,
                final int contentHeight,
                @PunchMarkContentPosition final int contentPositioning) {

            this.contentWidth = contentWidth;
            this.contentHeight = contentHeight;
            this.contentPositioning = contentPositioning;
            return this;
        }

        /**
         * Setting this to a non-zero value wil animate the punch hole moving over the
         * target view from start to end, on APIs Honeycomb and above and where the target view
         * has a ratio that supports a horizontal translation
         *
         * @param horizontalDuration the duration in milliseconds the animation should last for
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public PunchHoleCoachMarkBuilder setHorizontalTranslationDuration(long horizontalDuration) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                return this;
            }

            this.horizontalAnimationDuration = horizontalDuration;
            return this;
        }

        @Override
        public CoachMark build() {
            return new MultiPunchHoleCoachMark(this);
        }
    }
}
