package com.enigma.deckofcards.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.enigma.deckofcards.Constant;
import com.enigma.deckofcards.Player;
import com.enigma.deckofcards.R;
import com.enigma.deckofcards.Role;
import com.enigma.deckofcards.adapter.PlayerListAdapter;
import com.enigma.deckofcards.bluetooth.mananger.BluetoothManager;
import com.enigma.deckofcards.listener.PlayerListSelectionListener;
import com.enigma.deckofcards.ui.AdminPanelLayout;
import com.enigma.deckofcards.ui.LinearListLayout;
import com.enigma.deckofcards.ui.UiContext;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BluetoothActivity implements PlayerListSelectionListener {

    final int TIME_ANIMATION_EXPAND_COLLAPSE = 500;

    Context mAppContext;
    UiContext mUiCtxt;
    Role role;
    PlayerListAdapter playerListAdapter;

    @InjectView(R.id.btn_admin)
    Button btnAdmin;

    @InjectView(R.id.btn_player)
    Button btnPlayer;

    @InjectView(R.id.btn_start)
    Button btnStart;

    @InjectView(R.id.btn_end)
    Button btnEnd;

    @InjectView(R.id.btn_continue)
    Button btnContinue;

    @InjectView(R.id.list_adminpanel)
    AdminPanelLayout adminPanel;

    @InjectView(R.id.list_players)
    LinearListLayout playerListView;

    ArrayList<Player> players;

    ArrayList<Player> selectedPlayerList;

    boolean adminPanelExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUiCtxt = UiContext.getInstance();
        mAppContext = getApplicationContext();
        mUiCtxt.setContextAndHandler(mAppContext);
        selectedPlayerList = new ArrayList<Player>();

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @Override
    public void onBluetoothDeviceFound(BluetoothDevice device) {
        if (role != null && role == Role.ADMIN && isRelevantDevice(device)) {
            Log.d(Constant.LOG_TAG, device.getName());
            Player player = new Player(device, Role.PLAYER);
            players.add(player);
            playerListAdapter = new PlayerListAdapter(this, players);
            playerListView.setAdapter(playerListAdapter);
        }
    }

    @Override
    public void onClientConnectionSuccess() {

    }

    @Override
    public void onClientConnectionFail() {

    }

    @Override
    public void onServerConnectionSuccess() {

    }

    @Override
    public void onServerConnectionFail() {

    }

    @Override
    public void onBluetoothStartDiscovery() {

    }

    @Override
    public void onBluetoothCommunicator(String messageReceive) {

    }

    @Override
    public void onBluetoothNotAvailable() {

    }

    @OnClick(R.id.btn_admin)
    public void onAdminModeClick() {
        if (adminPanelExpanded) {
            animateCollapseAdminPanel();
        } else {
            animateExpandAdminPanel();
        }
    }

    @OnClick(R.id.btn_start)
    public void adminModeStartClick() {
        //setGameName("DeckOfCards");
        //setPlayerName("Admin");
        updateBluetoothAdapterName("DeckOfCards", "Admin");
        role = Role.ADMIN;
        initializePlayerList();
        setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_600_SEC);
        startDiscovery();
        scanAllBluetoothDevice();
    }

    @OnClick(R.id.btn_continue)
    public void adminModeGameStartClick() {
        continueGame();
    }

    @OnClick(R.id.btn_player)
    public void onPlayerModeClick() {
        //setGameName("DeckOfCards");
        //setPlayerName("Player");
        updateBluetoothAdapterName("DeckOfCards", "Player");
        role = Role.PLAYER;
        setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_600_SEC);
        startDiscovery();
    }

    private void continueGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void animateExpandAdminPanel() {
        if (adminPanel != null) {
            ValueAnimator expand = ValueAnimator.ofInt(0, (int) mUiCtxt.dpToPx(300.0f));
            expand.addUpdateListener(new AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int height = ((Integer) animation.getAnimatedValue()).intValue();
                    adminPanel.setCurrentHeight(height);
                    adminPanel.requestLayout();
                }
            });
            expand.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    adminPanelExpanded = true;
                }
            });
            expand.setDuration(TIME_ANIMATION_EXPAND_COLLAPSE);
            expand.start();
        }
    }

    private void animateCollapseAdminPanel() {
        if (adminPanel != null) {
            ValueAnimator expand = ValueAnimator.ofInt((int) mUiCtxt.dpToPx(300.0f), 0);
            expand.addUpdateListener(new AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int height = ((Integer) animation.getAnimatedValue()).intValue();
                    adminPanel.setCurrentHeight(height);
                    adminPanel.requestLayout();
                }
            });
            expand.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    adminPanelExpanded = false;
                }
            });
            expand.setDuration(TIME_ANIMATION_EXPAND_COLLAPSE);
            expand.start();
        }
    }

    private void initializePlayerList() {
        players = new ArrayList<Player>();
        playerListAdapter = new PlayerListAdapter(this, players);
        playerListView.setAdapter(playerListAdapter);
    }

    @Override
    public void onPerformPlayerSelect(Player player) {
        selectedPlayerList.add(player);
        Toast.makeText(this, player.getPlayerName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPerformPlayerDeselect(Player player) {
        if (selectedPlayerList.contains(player)) {
            selectedPlayerList.remove(player);
            Toast.makeText(this, player.getPlayerName(), Toast.LENGTH_SHORT).show();
        }
    }
}
