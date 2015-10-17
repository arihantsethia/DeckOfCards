package com.enigma.deckofcards.adapter;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enigma.deckofcards.Player;
import com.enigma.deckofcards.ui.UiContext;

import java.util.ArrayList;

import com.enigma.deckofcards.R;

/**
 * This important class handles the category-wise mirror and mirror+ related
 * stuff as a list adapter. Be careful while modifying. Read in-line comments.
 *
 * @author Arun T A
 */

@SuppressLint("UseSparseArrays")
public class PlayerListAdapter {

    UiContext mUiCtxt = UiContext.getInstance();
    ViewConfiguration vc = ViewConfiguration.get(mUiCtxt.getAppContext());
    ArrayList<Player> player_list;
    Activity mActivity;
    private Context mContext = UiContext.getInstance().getAppContext();
    private int SWIPE_MIN_DISTANCE = vc.getScaledTouchSlop();
    private int SWIPE_MAX_OFF_PATH = vc.getScaledTouchSlop();
    private int TAP_TIME = ViewConfiguration.getTapTimeout();
    private DisplayMetrics dm = mUiCtxt.getAppContext().getResources().getDisplayMetrics();
    private int REL_SWIPE_MIN_DISTANCE = (int) (SWIPE_MIN_DISTANCE * dm.densityDpi / 160.0f);
    @SuppressWarnings("unused")
    private int REL_SWIPE_MAX_OFF_PATH = (int) (SWIPE_MAX_OFF_PATH * dm.densityDpi / 160.0f);
    private float downX, downY, upX, upY, deltaX, deltaY;
    private long timeDown, deltaTime;

    public PlayerListAdapter(Activity activity, ArrayList<Player> list) {
        player_list = new ArrayList<Player>();
        mActivity = activity;
        for (Player el : list) {
            player_list.add(el);
        }
    }

    public int getCount() {
        return player_list.size();
    }

    public Object getItem(int position) {
        return player_list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = null;
        TextView player_name;

        v = inflater.inflate(R.layout.listrow_vault_data_cat, parent, false);
        player_name = (TextView) v.findViewById(R.id.text_playername);
        if (player_name != null) {
            player_name.setText(player_list.get(position).toString());
        }
        /*v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//					ViewHolder vh = (ViewHolder) v.getTag();
				//					if(vh.isselected == true){
				//						vh.isselected = false;
				//						v.setBackgroundColor(Color.argb(255, 241, 241, 241));
				//						mActivity.performPlayerDeselect(player_list.get(position));
				//					}
				//					else{
				//						vh.isselected = true;
				//						v.setBackgroundColor(Color.argb(255, 236, 234, 104));
				//						mActivity.performPlayerSelect(player_list.get(position));
				//					}

				if (((String) v.getTag()).equalsIgnoreCase("NOTSELECTED")) {
					v.setTag(new String("SELECTED"));
					v.setBackgroundColor(Color.argb(255, 236, 234, 104));
					mActivity.performPlayerSelect(player_list.get(position));
				} else {
					v.setTag(new String("NOTSELECTED"));
					v.setBackgroundColor(Color.argb(255, 241, 241, 241));
					mActivity.performPlayerDeselect(player_list.get(position));
				}

			}
		});
		v.setBackgroundColor(Color.argb(255, 241, 241, 241));*/

		/*if (null != viewHolder) {
			updateView(position, viewHolder);

		}*/
        v.setVisibility(View.VISIBLE);
        return v;
    }

    private void updateView(int pos, final ViewHolder vh, boolean existing) {
        vh.player.setText(player_list.get(pos).toString());

        if (existing == true) {
            //			if(vh.isselected == true){
            //				v.setBackgroundColor(Color.argb(255, 236, 234, 104));
            //			}
            //			else{
            //				v.setBackgroundColor(Color.argb(255, 241, 241, 241));
            //			}
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

    public static class ViewHolder {
        TextView player;
        boolean isselected = false;
    }


}
