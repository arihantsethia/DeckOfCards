package com.enigma.deckofcards.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enigma.deckofcards.Constant;
import com.enigma.deckofcards.Player;
import com.enigma.deckofcards.R;
import com.enigma.deckofcards.ui.UiContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ScoreUpdateActivity extends Activity {

    int totalNoOfCards = 52;
    int noOfCardsPerPlayer = 0;
    int totalCardsDistributed = 0;
    int totalUnusedCards = 0;

    boolean gameStarted = false;

    Context mAppContext;
    UiContext mUiCtxt;

    @InjectView(R.id.player_score_panel)
    LinearLayout playerScorePanel;

    @InjectView(R.id.btn_score_ok)
    Button scoreOk;

    @InjectView(R.id.btn_score_cancel)
    Button scoreCancel;

    ArrayList<Player> player_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mUiCtxt = UiContext.getInstance();
        mAppContext = getApplicationContext();
        mUiCtxt.setContextAndHandler(mAppContext);

        setContentView(R.layout.scoreupdate_layout);

        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        ButterKnife.inject(this);

        if(intent!=null){
            player_names = intent.getParcelableArrayListExtra("PLAYERS");
            for(Player el : player_names) {
                View v = getScoreView(el.getPlayerName());
                playerScorePanel.addView(v);
            }
        }

    }

    private View getScoreView(String name){
        View v = getLayoutInflater().inflate(R.layout.scoreupdateelement_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.score_player_name);
        if(tv!=null){
            tv.setText(name);
        }
        return v;
    }

    @OnClick(R.id.btn_score_ok)
    public void onScoreOk(){
        Intent intent = new Intent();
        for(int i=0;i<playerScorePanel.getChildCount();i++) {
            View v = playerScorePanel.getChildAt(i);
            EditText et = (EditText)v.findViewById(R.id.score_value);
            TextView tv = (TextView) v.findViewById(R.id.score_player_name);
            intent.putExtra(tv.getText().toString(), Integer.parseInt(et.getText().toString()));
        }
        setResult(500, intent);
        finish();
    }

    @OnClick(R.id.btn_score_cancel)
    public void onScoreCancel(){
        Intent intent = new Intent();
        setResult(600, intent);
        finish();
    }

}
