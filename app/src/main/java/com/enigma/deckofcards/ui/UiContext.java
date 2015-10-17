package com.enigma.deckofcards.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;

/**
 * This class implements the methods by which the other components/modules in
 * the application shall interact with the UI thread. Note that the application
 * architecture is a variant of Model/View/Controller architecture. So, the
 * postEvent function implemented here plays a critical part in the realization
 * of the architecture.
 *
 * @author Arun T A
 */
public class UiContext {


    private static UiContext mThis;
    DisplayMetrics mDisplayMetrics;
    private Context mContext;

    private UiContext() {
    }

    public static UiContext getInstance() {
        if (mThis == null) {
            mThis = new UiContext();
        }
        return mThis;
    }

    public void setContextAndHandler(Context context) {
        if (null != mContext) {
            Log.wtf("UiContext", "Changing previously set context");
        }

        mContext = context;
        Resources mResources = mContext.getResources();
        mDisplayMetrics = mResources.getDisplayMetrics();
    }


    public final Context getAppContext() {
        return mContext;
    }

    /**
     * Very important function. This function is used to convert device
     * independent pixel values to device dependent value. It is critical in all
     * UI related calculations.
     *
     * @param dp
     * @return px
     */

    public float dpToPx(float dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        if (displayMetrics.widthPixels == 480) {
            dp = dp * (320.0f / 360.0f);
        }

        if (displayMetrics.widthPixels == 768) {
            dp = dp * (384.0f / 360.0f);
        }

        if (displayMetrics.density == 3.5f) {
            dp = dp * (1.1428571429f);
        }
        float px = dp * displayMetrics.density;
        return px;
    }

    public float getDensity() {
        return mDisplayMetrics.density;
    }

    public float getDisplayHeight() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels - (int) dpToPx(25.0f);
    }

    public float getDispalyMetrics() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public boolean isPermanentMenuPresent() {
        return ViewConfiguration.get(getAppContext()).hasPermanentMenuKey();
    }

}
