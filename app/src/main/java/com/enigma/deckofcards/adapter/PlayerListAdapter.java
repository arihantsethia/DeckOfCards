package com.enigma.deckofcards.adapter;

import java.util.ArrayList;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enigma.deckofcards.Player;
import com.enigma.deckofcards.R;
import com.enigma.deckofcards.listener.PlayerListSelectionListener;
import com.enigma.deckofcards.ui.UiContext;

/**
 * This important class handles the category-wise mirror and mirror+ related
 * stuff as a list adapter. Be careful while modifying. Read in-line comments.
 *
 * @author Arun T A
 *
 */

@SuppressLint("UseSparseArrays")
public class PlayerListAdapter {

    private Context mContext = UiContext.getInstance().getAppContext();
    UiContext mUiCtxt = UiContext.getInstance();

    ViewConfiguration vc = ViewConfiguration.get(mUiCtxt.getAppContext());

    private int SWIPE_MIN_DISTANCE = vc.getScaledTouchSlop();
    private int SWIPE_MAX_OFF_PATH = vc.getScaledTouchSlop();
    private int TAP_TIME = ViewConfiguration.getTapTimeout();

    private DisplayMetrics dm = mUiCtxt.getAppContext().getResources().getDisplayMetrics();
    private int REL_SWIPE_MIN_DISTANCE = (int) (SWIPE_MIN_DISTANCE * dm.densityDpi / 160.0f);

    @SuppressWarnings("unused")
    private int REL_SWIPE_MAX_OFF_PATH = (int) (SWIPE_MAX_OFF_PATH * dm.densityDpi / 160.0f);

    private float downX, downY, upX, upY, deltaX, deltaY;
    private long timeDown, deltaTime;

    ArrayList<Player> playerList;

    Activity mActivity;

    public PlayerListAdapter(Activity activity, ArrayList<Player> list) {
        playerList = new ArrayList<Player>();
        mActivity = activity;
        for (Player el : list) {
            playerList.add(el);
        }
    }

    public int getCount() {
        return playerList.size();
    }

    public Object getItem(int position) {
        return playerList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView player;
        boolean isselected = false;
    }

    public View getView(final int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        TextView playerName;

        View v = inflater.inflate(R.layout.listrow_vault_data_cat, parent, false);
        playerName = (TextView) v.findViewById(R.id.text_playername);
        if (playerName != null) {
            playerName.setText(playerList.get(position).getPlayerName());
        }
        v.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((String) v.getTag()).equalsIgnoreCase("NOTSELECTED")) {
                    v.setTag(new String("SELECTED"));
                    v.setBackgroundColor(Color.argb(255, 236, 234, 104));
                    ((PlayerListSelectionListener)mActivity).onPerformPlayerSelect(playerList.get(position));
                } else {
                    v.setTag(new String("NOTSELECTED"));
                    v.setBackgroundColor(Color.argb(255, 241, 241, 241));
                    ((PlayerListSelectionListener)mActivity).onPerformPlayerDeselect(playerList.get(position));
                }

            }
        });
        v.setTag(new String("NOTSELECTED"));
        v.setBackgroundColor(Color.argb(255, 241, 241, 241));

        v.setVisibility(View.VISIBLE);
        return v;
    }

    private void updateView(int pos, final ViewHolder vh, boolean existing) {
        vh.player.setText(playerList.get(pos).toString());

        if (existing == true) {

        } else {
            ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
            anim.addUpdateListener(new AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = ((Float) animation.getAnimatedValue()).floatValue();
                    vh.player.setScaleX(scale);
                    vh.player.setScaleY(scale);
                }
            });
            anim.setDuration(500);
            anim.start();
        }
    }
}
