package com.zhaoni.findyou.android.main.view;

import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.view.utils.ViewProperties;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Checkable;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.zhaoni.findyou.android.R;

public class SettingsSwicher extends View implements Checkable {

	private final String TAG = SettingsSwicher.class.getSimpleName();
	
	private final int ANIM_DURATION = 150;
	
	// 现在是ON在左，OFF在右
	private int mIntrinsicWidth;
	private int mIntrinsicHeight;
	private Drawable mSwicherOnDrawable;
	private Drawable mSwicherOffDrawable;
	
	private boolean mIsFirstDrawn = false;
	private boolean mIsChecked = false;
	
	private boolean mIsAnimating = false;
	private AnimImpl mAnim;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	
	public SettingsSwicher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initSettingsSwicher();
	}

	public SettingsSwicher(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSettingsSwicher();
	}

	public SettingsSwicher(Context context) {
		super(context);
		initSettingsSwicher();
	}
	
	private void initSettingsSwicher() {
		mSwicherOnDrawable = getResources().getDrawable(R.drawable.settings_button_on);
		mSwicherOffDrawable = getResources().getDrawable(R.drawable.settings_button_off);
		
		mIntrinsicWidth = mSwicherOnDrawable.getIntrinsicWidth();
		mIntrinsicHeight = mSwicherOnDrawable.getIntrinsicHeight();
		
		
		super.setOnClickListener(new OnClickListenerImpl());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		// 需要等比缩放
		int desiredWidth = 0;
		int desiredHeight = 0;
		// 以高度为基准
		if (heightMode == MeasureSpec.EXACTLY) {
			desiredHeight = heightSize;
		} else {
			desiredHeight = getResources().getDimensionPixelSize(R.dimen.spacing_30);
		}
		
		float desiredHeightAsFloat = desiredHeight;
		if (widthMode == MeasureSpec.EXACTLY) {
			desiredWidth = widthSize;
			float desiredWidthAsFloat = desiredWidth;
			float scaleX = desiredWidthAsFloat / mIntrinsicWidth;
			float scaleY = desiredHeightAsFloat / mIntrinsicHeight;
			
			float scale = Math.min(scaleX, scaleY);
			desiredWidth = ViewProperties.getAsInt(mIntrinsicWidth * scale);
			desiredHeight = ViewProperties.getAsInt(mIntrinsicHeight * scale);
		} else {
			float scale = desiredHeightAsFloat / mIntrinsicHeight;
			desiredWidth = ViewProperties.getAsInt(mIntrinsicWidth * scale);
		}
		
		LogUtils.d(TAG, "onMeasure desiredWidth = " + desiredWidth);
		LogUtils.d(TAG, "onMeasure desiredHeight = " + desiredHeight);
		mSwicherOnDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
		mSwicherOffDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
		
//		super.onMeasure(MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.EXACTLY),
//				MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		// align right/ center_vertical
		int left = getWidth() - mSwicherOnDrawable.getBounds().width();
		int top = (getHeight() - mSwicherOnDrawable.getBounds().height()) / 2;
		canvas.clipRect(left, top, left + mSwicherOnDrawable.getBounds().width(), 
				top + mSwicherOnDrawable.getBounds().height());
		
		canvas.translate(getWidth() - mSwicherOnDrawable.getBounds().width(),
				(getHeight() - mSwicherOnDrawable.getBounds().height()) / 2);
		if (mIsAnimating) {
			canvas.save();
			float width = mSwicherOnDrawable.getBounds().width();
			float offsetX = - (1F - mAnim.getCurrentFactor()) * width;
			canvas.translate(offsetX, 0);
			mSwicherOnDrawable.draw(canvas);
			canvas.restore();
			
			canvas.save();
			offsetX = mAnim.getCurrentFactor() * width;
			canvas.translate(offsetX, 0);
			mSwicherOffDrawable.draw(canvas);
			canvas.restore();
		} else {
			if (mIsChecked) {
				mSwicherOnDrawable.draw(canvas);
			} else {
				mSwicherOffDrawable.draw(canvas);
			}
		}
		canvas.restore();
		
		if (!mIsFirstDrawn) {
			mIsFirstDrawn = true;
		}
	}

	@Override
	public void setOnClickListener(OnClickListener l) { }
	
	@Override
	public void setChecked(boolean checked) {
		if (mIsChecked ^ checked) {
			mIsChecked = checked;
			if (mIsFirstDrawn) {
				if (null != mAnim) {
					clearAnimation();
					mAnim = null;
				}
				mIsAnimating = true;
				if (checked) {
					mAnim = new AnimImpl(0F, 1F);
				} else {
					mAnim = new AnimImpl(1F, 0F);
				}
				mAnim.setDuration(ANIM_DURATION);
				startAnimation(mAnim);
			}
		}
	}

	@Override
	public boolean isChecked() {
		return mIsChecked;
	}

	@Override
	public void toggle() {
		setChecked(!mIsChecked);
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}
	
	private class OnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (!mIsAnimating) {
				toggle();
			}
		}
	}
	
	private class AnimImpl extends Animation implements Animation.AnimationListener {
		private float mFromFactor;
		private float mToFactor;
		
		public AnimImpl(float fromFactor, float toFactor) {
			this.mFromFactor = fromFactor;
			this.mToFactor = toFactor;
			setAnimationListener(this);
			mFactor = fromFactor;
		}
		
		private float mFactor;
		public float getCurrentFactor() {
			return mFactor;
		}
		
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			mFactor = mFromFactor + (mToFactor - mFromFactor) * interpolatedTime;
			invalidate();
		}

		@Override
		public void onAnimationStart(Animation animation) { }

		@Override
		public void onAnimationEnd(Animation animation) {
			if (null != mOnCheckedChangeListener) {
				mOnCheckedChangeListener.onCheckedChanged(null, mIsChecked);
			}
			
			mIsAnimating = false;
			invalidate();
		}

		@Override
		public void onAnimationRepeat(Animation animation) { }
	}
	
}
