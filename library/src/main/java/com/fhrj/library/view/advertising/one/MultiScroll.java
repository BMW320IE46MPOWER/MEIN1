package com.fhrj.library.view.advertising.one;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Scroller;

/***
 * Android开发ScrollView上下左右滑动事件冲突整理一（根据事件）
 * @author ZhangGuoHao
 * @date 2016年5月15日 下午5:55:11
 */
public class MultiScroll extends ScrollView {  
  
    @SuppressWarnings("unused")
	private static final int ANIMATION_SCREEN_SET_DURATION_MILLIS = 500;  
    // What fraction (1/x) of the screen the user must swipe to indicate a page change  
    @SuppressWarnings("unused")
	private static final int FRACTION_OF_SCREEN_WIDTH_FOR_SWIPE = 4;  
    private static final int INVALID_SCREEN = -1;  
    /* 
     * Velocity of a swipe (in density-independent pixels per second) to force a swipe to the 
     * next/previous screen. Adjusted into mDensityAdjustedSnapVelocity on init. 
     */  
    private static final int SNAP_VELOCITY_DIP_PER_SECOND = 600;  
    // Argument to getVelocity for units to give pixels per second (1 = pixels per millisecond).  
    @SuppressWarnings("unused")
	private static final int VELOCITY_UNIT_PIXELS_PER_SECOND = 1000;  
  
    private static final int TOUCH_STATE_REST = 0;  
    private static final int TOUCH_STATE_HORIZONTAL_SCROLLING = 1;  
    private static final int TOUCH_STATE_VERTICAL_SCROLLING = -1;  
    @SuppressWarnings("unused")
	private int mCurrentScreen;  
    @SuppressWarnings("unused")
	private int mDensityAdjustedSnapVelocity;  
    @SuppressWarnings("unused")
	private boolean mFirstLayout = true;  
    private float mLastMotionX;  
    private float mLastMotionY;  
    //private OnScreenSwitchListener mOnScreenSwitchListener;  
    @SuppressWarnings("unused")
	private int mMaximumVelocity;  
    @SuppressWarnings("unused")
	private int mNextScreen = INVALID_SCREEN;  
    @SuppressWarnings("unused")
	private Scroller mScroller;  
    private int mTouchSlop;  
    private int mTouchState = TOUCH_STATE_REST;  
    @SuppressWarnings("unused")
	private VelocityTracker mVelocityTracker;  
    @SuppressWarnings("unused")
	private int mLastSeenLayoutWidth = -1;  
      
    public MultiScroll(Context context) {  
        super(context);  
        init();  
    }  
  
    public MultiScroll(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init();  
    }  
  
    public MultiScroll(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();  
    }  
      
    private void init() {  
        mScroller = new Scroller(getContext());  
        DisplayMetrics displayMetrics = new DisplayMetrics();  
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()  
                .getMetrics(displayMetrics);  
        mDensityAdjustedSnapVelocity =  
                (int) (displayMetrics.density * SNAP_VELOCITY_DIP_PER_SECOND);  
  
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());  
        mTouchSlop = configuration.getScaledTouchSlop();  
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();  
    }  
      
  
    @Override  
    public boolean onInterceptTouchEvent(final MotionEvent ev) {  
        final int action = ev.getAction();  
        boolean intercept = false;  
  
        switch (action) {  
            case MotionEvent.ACTION_MOVE:  
                  
                /* 
                 * If we're in a horizontal scroll event, take it (intercept further events). But if 
                 * we're mid-vertical-scroll, don't even try; let the children deal with it. If we 
                 * haven't found a scroll event yet, check for one. 
                 */  
                if (mTouchState == TOUCH_STATE_HORIZONTAL_SCROLLING) {  
                    intercept = true;  
                    // Let children handle the events for the duration of the scroll event.  
                    intercept = false;  
                } else if (mTouchState == TOUCH_STATE_VERTICAL_SCROLLING) {  
                       /* 
                     * We've already started a horizontal scroll; set intercept to true so we can 
                     * take the remainder of all touch events in onTouchEvent. 
                     */  
                    intercept = true;  
                } else { // We haven't picked up a scroll event yet; check for one.  
  
                    /* 
                     * If we detected a horizontal scroll event, start stealing touch events (mark 
                     * as scrolling). Otherwise, see if we had a vertical scroll event -- if so, let 
                     * the children handle it and don't look to intercept again until the motion is 
                     * done. 
                     */  
  
                    final float x = ev.getX();  
                    final int xDiff = (int) Math.abs(x - mLastMotionX);  
                    boolean xMoved = xDiff > mTouchSlop;  
                      
                    final float y = ev.getY();  
                    final int yDiff = (int) Math.abs(y - mLastMotionY);  
                    boolean yMoved = yDiff > mTouchSlop;  
  
                      
                    if (xMoved) {  
                        // Scroll if the user moved far enough along the X axis  
                        if(xDiff>=yDiff)  
                        mTouchState = TOUCH_STATE_HORIZONTAL_SCROLLING;  
                        mLastMotionX = x;  

                    }  
  
    
  
                    if (yMoved) {  
                        if(yDiff>xDiff)  
                        mTouchState = TOUCH_STATE_VERTICAL_SCROLLING;  
                        mLastMotionY = y; 
                     
                    }  
                }  
                break;  
            case MotionEvent.ACTION_CANCEL:  
            case MotionEvent.ACTION_UP:  
                // Release the drag.  
                mTouchState = TOUCH_STATE_REST;  
                intercept = false;  
                break;  
            case MotionEvent.ACTION_DOWN:  
                /* 
                 * No motion yet, but register the coordinates so we can check for intercept at the 
                 * next MOVE event. 
                 */  
                //Log.i("ViewPager-->", "Action_Down");  /
                mTouchState = TOUCH_STATE_REST;  
                mLastMotionY = ev.getY();  
                mLastMotionX = ev.getX();  
                
                break;  
            default:  
                break;  
            }  
  
        return intercept;  
    }  
      
}  