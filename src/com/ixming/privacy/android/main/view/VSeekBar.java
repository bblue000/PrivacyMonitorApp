package com.ixming.privacy.android.main.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VSeekBar extends SeekBar {

	public VSeekBar(Context context) {
		super(context);
	}

	public VSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	protected void onDraw(Canvas c) {
//		c.rotate(-90);
//		c.translate(-getHeight(), 0);
		
		
		c.rotate(90);
		c.translate(0, -getWidth());

		super.onDraw(c);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}
		try {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				int i = 0;
				i = /*getMax() - */(int) (getMax() * event.getY() / getHeight());
				setProgress(i);
				onSizeChanged(getWidth(), getHeight(), 0, 0);
				break;

			case MotionEvent.ACTION_CANCEL:
				break;
			}
			return true;
		} finally {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
				
			} else
			if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
				if (null != mOnSeekBarChangeListener) {
					mOnSeekBarChangeListener.onStopTrackingTouch(this);
				}
			}
		}
		
	}
	
	private OnSeekBarChangeListener mOnSeekBarChangeListener;
	/**
     * Sets a listener to receive notifications of changes to the SeekBar's progress level. Also
     * provides notifications of when the user starts and stops a touch gesture within the SeekBar.
     * 
     * @param l The seek bar notification listener
     * 
     * @see SeekBar.OnSeekBarChangeListener
     */
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
    	super.setOnSeekBarChangeListener(l);
        mOnSeekBarChangeListener = l;
    }

}
