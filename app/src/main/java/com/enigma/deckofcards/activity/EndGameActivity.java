package com.enigma.deckofcards.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enigma.deckofcards.Constant;
import com.enigma.deckofcards.R;
import com.enigma.deckofcards.ui.UiContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EndGameActivity extends Activity {

    int totalNoOfCards = 52;
    int noOfCardsPerPlayer = 0;
    int totalCardsDistributed = 0;
    int totalUnusedCards = 0;

    boolean gameStarted = false;

    Context mAppContext;
    UiContext mUiCtxt;

    @InjectView(R.id.btn_end_yes)
    Button btnOK;

    @InjectView(R.id.btn_end_no)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUiCtxt = UiContext.getInstance();
        mAppContext = getApplicationContext();
        mUiCtxt.setContextAndHandler(mAppContext);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        setContentView(R.layout.endgame_layout);

        ButterKnife.inject(this);

    }

    @OnClick(R.id.btn_end_yes)
    public void onEndOk(){
        Intent i = new Intent();
        i.putExtra("END_GAME", true);
        setResult(500, i);
        finish();
    }

    @OnClick(R.id.btn_end_no)
    public void onEndNo(){
        Intent i = new Intent();
        i.putExtra("END_GAME", false);
        setResult(500, i);
        finish();
    }
}
