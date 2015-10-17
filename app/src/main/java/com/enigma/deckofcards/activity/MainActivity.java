package com.enigma.deckofcards.activity;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.enigma.deckofcards.adapter.PlayerListAdapter;
import com.enigma.deckofcards.ui.AdminPanelLayout;
import com.enigma.deckofcards.ui.LinearListLayout;
import com.enigma.deckofcards.ui.UiContext;

import enigma.deckofcards.R;

public class MainActivity extends Activity {

	final int TIME_ANIMATION_EXPAND_COLLAPSE = 500;

	Context mAppContext;
	UiContext mUiCtxt;

	Button btn_admin;
	Button btn_player;
	Button btn_start;
	Button btn_end;
	Button btn_continue;

	AdminPanelLayout admin_panel;
	
	LinearListLayout player_listview;
	
	ArrayList<String> players;

	boolean admin_panel_expanded = false;
	
	ArrayList<String> selected_player_list = new ArrayList<String>();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mUiCtxt = UiContext.getInstance();
		mAppContext = getApplicationContext();
		mUiCtxt.setContextAndHandler(mAppContext);

		setContentView(R.layout.activity_main);
		
		
		btn_admin = (Button) findViewById(R.id.btn_admin);
		btn_player = (Button) findViewById(R.id.btn_player);
		admin_panel = (AdminPanelLayout) findViewById(R.id.list_adminpanel);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_end = (Button) findViewById(R.id.btn_end);
		btn_continue = (Button) findViewById(R.id.btn_continue);
		player_listview = (LinearListLayout) findViewById(R.id.list_players);

		if (btn_admin != null) {
			btn_admin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (admin_panel_expanded) {
						animate_collapse_adminpanel();
					} else {
						animate_expand_adminpanel();
					}
				}
			});
		}
		
		if(btn_start!=null){
			btn_start.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					initialize_playerlist();
				}
			});
		}
		
		if(btn_continue!=null){
			btn_continue.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					continue_game();
				}
			});
		}
		
//		if(player_listview !=null){
//			player_listview.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					perform_player_select(players.get(position));
//				}
//			});
//		}
		

	}
	
	private void continue_game(){
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	private void animate_expand_adminpanel() {
		if (admin_panel != null) {
			ValueAnimator expand = ValueAnimator.ofInt(0, (int) mUiCtxt.dpToPx(300.0f));
			expand.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int height = ((Integer) animation.getAnimatedValue()).intValue();
					admin_panel.setCurrentHeight(height);
					admin_panel.requestLayout();
				}
			});
			expand.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					admin_panel_expanded = true;
				}
			});
			expand.setDuration(TIME_ANIMATION_EXPAND_COLLAPSE);
			expand.start();
		}
	}

	private void animate_collapse_adminpanel() {
		if (admin_panel != null) {
			ValueAnimator expand = ValueAnimator.ofInt((int) mUiCtxt.dpToPx(300.0f), 0);
			expand.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int height = ((Integer) animation.getAnimatedValue()).intValue();
					admin_panel.setCurrentHeight(height);
					admin_panel.requestLayout();
				}
			});
			expand.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					admin_panel_expanded = false;
				}
			});
			expand.setDuration(TIME_ANIMATION_EXPAND_COLLAPSE);
			expand.start();
		}
	}

	private void initialize_playerlist(){
		players = new ArrayList<String>();
		players.add("Player 1");
		players.add("Player 2");
		players.add("Player 3");
		players.add("Player 4");
		players.add("Player 5");
		players.add("Player 6");
		players.add("Player 7");
		
		Toast.makeText(this, "Init", Toast.LENGTH_SHORT).show();
		
		
		PlayerListAdapter player_list_adapter = new PlayerListAdapter(this, players);
		player_listview.setAdapter(player_list_adapter);
	}
	
	public void perform_player_select(String player_name){
		selected_player_list.add(player_name);
		Toast.makeText(this, player_name, Toast.LENGTH_SHORT).show();
	}
	
	public void perform_player_deselect(String player_name){
		if(selected_player_list.contains(player_name)){
			selected_player_list.remove(player_name);
			Toast.makeText(this, player_name, Toast.LENGTH_SHORT).show();
		}
	}
}
