package com.enigma.deckofcards.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.enigma.deckofcards.adapter.PlayerListAdapter;

public class LinearListLayout extends LinearLayout {

	float left = 0.0f;
	float top = 0.0f;
	float right = 1000.0f;
	float bottom = 0;

	RectF mRect = new RectF(left, top, right, bottom);

	int mFullWidth, mFullHeight, mCurrentWidth, mCurrentHeight;

	Context  mContext;
	
	public LinearListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public void setAdapter(PlayerListAdapter adapter) {
		//		removeAllViews();
		Toast.makeText(mContext, adapter.getCount() + "", Toast.LENGTH_SHORT).show();
		
		for (int i = 0; i < adapter.getCount(); i++) {
			
			View v = adapter.getView(i, this);
			v.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 150));
			//			settingVault.InfoList.addView(v);
			addView(v);
		}
		//		refreshDrawableState();
	}

	public void setClipRectHeight(float height) {

		bottom = height;
		mRect = new RectF(left, top, right, bottom);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//		canvas.clipRect(mRect);
	}

	public void setCurrentHeight(int height) {
		mCurrentHeight = height;
	}

	public int getCurrentHeight() {
		return mCurrentHeight;
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// Did one of our children change size?
		int newHeight = getMeasuredHeight();
		if (newHeight != mFullHeight) {
			mFullHeight = newHeight;
		}

		heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) mCurrentHeight, MeasureSpec.AT_MOST);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

}
