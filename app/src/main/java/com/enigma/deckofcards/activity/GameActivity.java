package com.enigma.deckofcards.activity;

import java.util.ArrayList;

import com.enigma.deckofcards.adapter.PlayerListAdapter.ViewHolder;
import com.enigma.deckofcards.ui.LinearListLayout;
import com.enigma.deckofcards.ui.UiContext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import enigma.deckofcards.R;

public class GameActivity extends Activity {

	final int TIME_ANIMATION_EXPAND_COLLAPSE = 500;

	Context mAppContext;
	UiContext mUiCtxt;

	Button distribute;
	Button move_cards;
	Button take_cards;
	
	Button player_1;
	Button player_2;
	Button player_3;
	Button player_4;

	LinearListLayout player_listview;
	
	ArrayList<String> players;

	boolean admin_panel_expanded = false;
	
	ArrayList<String> selected_player_list = new ArrayList<String>();
	
	
	int selected_player_index = -1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mUiCtxt = UiContext.getInstance();
		mAppContext = getApplicationContext();
		mUiCtxt.setContextAndHandler(mAppContext);

		setContentView(R.layout.game_main);
		
		
		player_1 = (Button) findViewById(R.id.player_1);
		player_2 = (Button) findViewById(R.id.player_2);
		player_3 = (Button) findViewById(R.id.player_3);
		player_4 = (Button) findViewById(R.id.player_4);
		
		distribute = (Button) findViewById(R.id.distribute);
		move_cards = (Button) findViewById(R.id.move_cards);
		take_cards = (Button) findViewById(R.id.take_cards);
		
		
		if(player_1 !=null){
			player_1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selected_player_index = 1;
				}
			});
		}
		
		if(player_2 !=null){
			player_2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selected_player_index = 2;
				}
			});
		}
		
		if(player_3 !=null){
			player_3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selected_player_index = 3;
				}
			});
		}
		
		if(player_4 !=null){
			player_4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selected_player_index = 4;
				}
			});
		}
		
		if(distribute !=null){
			distribute.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					distribute_cards();
				}
			});
		}
		
		if(move_cards !=null){
			move_cards.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					moveCards();
				}
			});
		}
		
		if(take_cards !=null){
			take_cards.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					takeCards();
				}

				
			});
		}
		

	}
	
	private void takeCards() {
		Toast.makeText(this, "Take Cards: " + selected_player_index, Toast.LENGTH_SHORT).show();
	}
	
	private void moveCards() {
		Toast.makeText(this, "Make Cards: " + selected_player_index, Toast.LENGTH_SHORT).show();
	}
	
	private void distribute_cards() {
		Toast.makeText(this, "Distribute Cards: " + selected_player_index, Toast.LENGTH_SHORT).show();
	}
}
