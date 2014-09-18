package com.zhaoni.findyou.android.main.view;

import org.ixming.base.view.utils.ViewProperties;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.main.activity.PersonLocationV3;
import com.zhaoni.findyou.android.main.model.RespLocation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimeSeekView extends ViewGroup {

	private final int LINE_WIDTH = 2; //DP
	private final int UNSEL_DOT_RADIUS = 2; //DP
	private final int SEL_DOT_RADIUS = 5; //DP
	private final int DOT_TEXT_GAP = 5; //DP
	private final int MIN_PER_HEIGHT = 10; //DP
	
	private float mDensity;
	
	private int mLineWidth;
	
	private int mUnSelDotRadius;
	private int mSelDotRadius;
	private int mDotTextGap;
	private int mMinTimeSepHeight;
	
	private TextView mText;
	
	private Rect mTimeLineRect = new Rect();
	private Rect mTextRect = new Rect();
	
	private int mTimeLineWidth;
	private int mTextXOffset;
	
	private Paint mTimeLinePaint;
	private Paint mUnSelDotPaint;
	private Paint mSelDotPaint;
	private int mTimeLineColor;
	private int mUnSelDotColor;
	private int mSelDotColor;
	public TimeSeekView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TimeSeekView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimeSeekView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		mDensity = getResources().getDisplayMetrics().density;
		
		mLineWidth = ViewProperties.getAsInt(mDensity * LINE_WIDTH);
		mUnSelDotRadius = ViewProperties.getAsInt(mDensity * UNSEL_DOT_RADIUS);
		mSelDotRadius = ViewProperties.getAsInt(mDensity * SEL_DOT_RADIUS);
		mDotTextGap = ViewProperties.getAsInt(mDensity * DOT_TEXT_GAP);
		mMinTimeSepHeight = ViewProperties.getAsInt(mDensity * MIN_PER_HEIGHT);
		
		mTimeLineColor = getResources().getColor(R.color.appbase_blue_disable);
		mUnSelDotColor = getResources().getColor(R.color.appbase_blue_pressed);
		mSelDotColor = getResources().getColor(R.color.appbase_blue);
		
		mTimeLinePaint = new Paint();
		mTimeLinePaint.setColor(mTimeLineColor);
		mTimeLinePaint.setAntiAlias(true);
		mTimeLinePaint.setStrokeWidth(mLineWidth);
		
		mUnSelDotPaint = new Paint();
		mUnSelDotPaint.setAntiAlias(true);
		mUnSelDotPaint.setColor(mUnSelDotColor);
		
		mSelDotPaint = new Paint();
		mSelDotPaint.setAntiAlias(true);
		mSelDotPaint.setColor(mSelDotColor);
		
		mText = new TextView(getContext());
		mText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addView(mText);
		
		mText.setText("00:00");
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		measureChild(mText, widthMeasureSpec, heightMeasureSpec);
		
		mTimeLineWidth = mSelDotRadius * 2;
		mTimeLineRect.set(getPaddingLeft(),
				getPaddingTop(),
				getPaddingLeft() + mTimeLineWidth,
				heightSize - getPaddingBottom());
		mTextRect.set(mTimeLineRect.left + mDotTextGap, 0,
				mTimeLineRect.left + mDotTextGap + mText.getWidth(),
				mText.getHeight());
		
		int widthSize = getPaddingLeft() + mTimeLineRect.width() + mTextRect.width() + getPaddingRight();
		super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mText.layout(mTextRect.left, mTextRect.top, mTextRect.right, mTextRect.bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (null == mPersonLocationV3) {
			return ;
		}
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		
		int halfLineWidth = mLineWidth / 2;
		canvas.drawLine(mTimeLineRect.left + mSelDotRadius, paddingTop,
				mTimeLineRect.left + mSelDotRadius, getHeight() - paddingBottom, mTimeLinePaint);
		
		// draw unselected dots
		
		
		// draw selected dot
		canvas.drawCircle(mTimeLineRect.left + mSelDotRadius, paddingTop + getHeight() / 2, mSelDotRadius, mSelDotPaint);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (null == mPersonLocationV3) {
			return ;
		}
		canvas.save();
		canvas.translate(mTextRect.left, getPaddingTop() + getHeight() / 2);
		super.dispatchDraw(canvas);
		canvas.restore();
	}
	
	private float mLastY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
			mLastY = event.getY();
			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			// 寻找最近的一项
			
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	private PersonLocationV3 mPersonLocationV3;
	private int mPerHeight;
	private int mSelectedIndex;
	private int mYs[];
	public void setPersonLocationV3(PersonLocationV3 personLocationV3) {
		mPersonLocationV3 = personLocationV3;
//		float totalTimeSep = mPersonLocationV3.endTime - mPersonLocationV3.startTime;
//		float minTimeSep = mPersonLocationV3.minTimeSep;
//		float minTimeSepHeight = (minTimeSep / totalTimeSep) * getHeight();
//		if (minTimeSepHeight < mMinTimeSepHeight) {
//			minTimeSepHeight = mMinTimeSepHeight;
//		}
		
//		mPersonLocationV3.timeSepPerHeight = minTimeSepHeight / minTimeSep;
		
		mYs = null;
		if (mPersonLocationV3.infoList.isEmpty()) {
			return ;
		}
		
		int size = mPersonLocationV3.infoList.size();
		mYs = new int[size];
		float perHeight = (float) getHeight() / (float) size;
		if (perHeight < mMinTimeSepHeight) {
			perHeight = mMinTimeSepHeight;
		}
		mPerHeight = ViewProperties.getAsInt(perHeight);
		
		mYs[0] = getPaddingTop() + getHeight() / 2;
//		RespLocation lastLocation = mPersonLocationV3.infoList.get(0);
		for (int i = 1; i < size; i++) {
//			RespLocation next = mPersonLocationV3.infoList.get(i);
			mYs[i] = mYs[i - 1] + mPerHeight;
		}
		
		mSelectedIndex = 0;
		invalidate();
	}
}
