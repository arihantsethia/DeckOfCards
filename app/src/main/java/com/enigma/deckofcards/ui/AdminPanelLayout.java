package com.enigma.deckofcards.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class AdminPanelLayout extends LinearLayout {

    float left = 0.0f;
    float top = 0.0f;
    float right = 1000.0f;
    float bottom = 0;

    RectF mRect = new RectF(left, top, right, bottom);

    UiContext mUiContext = UiContext.getInstance();

    int mFullWidth, mFullHeight, mCurrentWidth, mCurrentHeight;

    public AdminPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        // TODO Auto-generated constructor stub
    }

    private void initialize() {
        setCurrentHeight((int) mUiContext.dpToPx(0.0f));
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

    public int getCurrentHeight() {
        return mCurrentHeight;
    }

    public void setCurrentHeight(int height) {
        mCurrentHeight = height;
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
