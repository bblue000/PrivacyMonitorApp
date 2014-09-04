package com.zhaoni.findyou.android.main.view;

import org.ixming.base.view.utils.ViewProperties;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.zhaoni.findyou.android.R;

public class CustomVSeekBar extends ViewGroup {
	
	private float mDensity;

	private ProgressBar mProgressBar;
	private Rect mProgressBarRect = new Rect();
	private int mProgressBarHeight;
	
	private int mProgressBarOffset;
	
	private Drawable mCursor;
	private Rect mCursorRect = new Rect();
	
	private final int PROGRESSBAR_OFFSET = 3; // dp
	private final int PROGRESSBAR_HEIGHT = 8; // dp
	
	public CustomVSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomVSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomVSeekBar(Context context) {
		super(context);
		init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		mDensity = getResources().getDisplayMetrics().density;
		
		mProgressBarHeight = ViewProperties.getAsInt(PROGRESSBAR_HEIGHT * mDensity);
		mProgressBarOffset = ViewProperties.getAsInt(PROGRESSBAR_OFFSET * mDensity);
		
		
		mProgressBar = new ProgressBar(getContext(), null, android.R.attr.seekBarStyle);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, mProgressBarHeight);
		mProgressBar.setLayoutParams(lp);
		mProgressBar.setProgressDrawable(tileify(getResources().getDrawable(R.drawable.vseekbar_progress), false));
		mProgressBar.setIndeterminate(false);
		mProgressBar.setMax(100);
		mProgressBar.setBackgroundDrawable(null);
		addView(mProgressBar);
		
		
		mCursor = getResources().getDrawable(R.drawable.seekbar_thumb);
		mCursor.setBounds(0, 0, mCursor.getIntrinsicWidth(), mCursor.getIntrinsicHeight());
		
		setClipToPadding(false);
	}
	
	/**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    private Drawable tileify(Drawable drawable, boolean clip) {
        if (drawable instanceof LayerDrawable) {
            LayerDrawable background = (LayerDrawable) drawable;
            final int N = background.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];
            
            for (int i = 0; i < N; i++) {
                int id = background.getId(i);
                outDrawables[i] = tileify(background.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }
            LayerDrawable newBg = new LayerDrawable(outDrawables);
            
            for (int i = 0; i < N; i++) {
                newBg.setId(i, background.getId(i));
            }
            return newBg;
        } else if (drawable instanceof BitmapDrawable) {
            final Bitmap tileBitmap = ((BitmapDrawable) drawable).getBitmap();
            
            final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());

            final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader(bitmapShader);

            return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL) : shapeDrawable;
        }
        return drawable;
    }
    
    Shape getDrawableShape() {
        final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
        return new RoundRectShape(roundedCorners, null, null);
    }
    
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		
		int width = MeasureSpec.getSize(heightMeasureSpec);
		int thumbWidth = mCursor == null ? 0 : mCursor.getIntrinsicWidth();
		int thumbHeight = mCursor == null ? 0 : mCursor.getIntrinsicHeight();
		int dh = thumbHeight + mProgressBarOffset + mProgressBarHeight + paddingLeft + paddingRight;
		
		int progressBarLeft = paddingLeft + thumbWidth / 2;
		int progressBarTop = paddingTop + thumbHeight + mProgressBarOffset;
		int progressBarRight = Math.max(0, width - paddingRight - thumbWidth / 2);
		int progressBarBottom = Math.max(0, dh - paddingBottom);
		
		mProgressBarRect.set(progressBarLeft, progressBarTop, progressBarRight, progressBarBottom);
		mProgressBar.measure(MeasureSpec.makeMeasureSpec(mProgressBarRect.width(), MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(mProgressBarRect.height(), MeasureSpec.EXACTLY));
		
		refreshCursorBounds();
		
//		super.onMeasure(widthMeasureSpec,
//				MeasureSpec.makeMeasureSpec(dh, MeasureSpec.getMode(heightMeasureSpec)));
		
        super.setMeasuredDimension(MeasureSpec.makeMeasureSpec(dh, MeasureSpec.EXACTLY),
        		heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mProgressBar.layout(mProgressBarRect.left, mProgressBarRect.top, mProgressBarRect.right, mProgressBarRect.bottom);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(90);
		canvas.translate(0, -getWidth());
		super.dispatchDraw(canvas);
		mCursor.draw(canvas);
		canvas.restore();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
		
		refreshCursorBounds();
		invalidate();
	}
	
	private void refreshCursorBounds() {
		int thumbLeft = getPaddingLeft() + ViewProperties.getAsInt(
				mProgressBarRect.width() * (((float) mProgressBar.getProgress()) / ((float) mProgressBar.getMax())));
		int thumbTop = getPaddingTop();
		mCursorRect.set(thumbLeft, thumbTop, thumbLeft + mCursor.getIntrinsicWidth(), thumbTop + mCursor.getIntrinsicHeight());
		mCursor.setBounds(thumbLeft, thumbTop, thumbLeft + mCursor.getIntrinsicWidth(), thumbTop + mCursor.getIntrinsicHeight());
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
				float eventY = event.getY();
				float y = Math.min(Math.max(event.getY() - mProgressBarRect.left, 0), mProgressBarRect.width());
				float deltaY = y - eventY;
				int i = 0;
				i = /*getMax() - */(int) (mProgressBar.getMax() * y / mProgressBarRect.width());
				mProgressBar.setProgress(i);
				if (mChangingWhenDragging && null != mOnSeekBarChangeListener) {
					mOnSeekBarChangeListener.onProgressChanged(this, mProgressBar.getProgress(), mProgressBar.getMax());
				}
				if (null != mOnSeekBarChangeListener) {
					mOnSeekBarChangeListener.onCursorPositionChanged(this, (int) (event.getRawY() + deltaY));
				}
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
					mOnSeekBarChangeListener.onProgressChanged(this, mProgressBar.getProgress(), mProgressBar.getMax());
				}
			}
		}
		
	}
	
	private boolean mChangingWhenDragging;
	public void setChangingWhenDragging(boolean value) {
		mChangingWhenDragging = value;
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
        mOnSeekBarChangeListener = l;
    }
    
    /**
     * A callback that notifies clients when the progress level has been
     * changed. This includes changes that were initiated by the user through a
     * touch gesture or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnSeekBarChangeListener {
        
        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         * 
         * @param seekBar The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range 0..max where max
         *        was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        void onProgressChanged(CustomVSeekBar seekBar, int progress, int max);
    
        
        void onCursorPositionChanged(CustomVSeekBar seekBar, int yPosInGlobal);
    }
}
