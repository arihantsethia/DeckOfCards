package com.enigma.deckofcards.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.enigma.deckofcards.Constant;
import com.enigma.deckofcards.MessageGenerator;
import com.enigma.deckofcards.MessageType;
import com.enigma.deckofcards.Player;
import com.enigma.deckofcards.R;
import com.enigma.deckofcards.Role;
import com.enigma.deckofcards.ui.UiContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GameActivity extends BluetoothActivity {

    int totalNoOfCards = 52;
    int noOfCardsPerPlayer = 0;

    boolean gameStarted = false;

    Context mAppContext;
    UiContext mUiCtxt;
    Role role;
    Player admin;
    ArrayList<Player> selectedPlayerList;
    String gameUBID = null;
    int lastHandledMessage = -1;


    @InjectView(R.id.distribute)
    Button distribute;

    @InjectView(R.id.show_cards)
    Button showCards;

    @InjectView(R.id.start_game)
    Button startGame;

    @InjectView(R.id.take_card)
    Button takeCard;

    @InjectView(R.id.place_card)
    Button placeCard;

    @InjectView(R.id.move_to_trash)
    Button moveTrash;

    @InjectView(R.id.player_panel)
    LinearLayout playerPanel;

    @InjectView(R.id.card_panel)
    LinearLayout cardPanel;

    @InjectView(R.id.scroller_card_panel)
    HorizontalScrollView scrollerCardPanel;

    ArrayList<String> tempCardsImageNames;

    HashMap<Button, Integer> collectionPlayers = new HashMap<Button, Integer>();
    ArrayList<String> selectedCardValue = new ArrayList<String>();
    ArrayList<String> selectedCardColor = new ArrayList<String>();
    ArrayList<ImageView> selectedCardImages = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mUiCtxt = UiContext.getInstance();
        mAppContext = getApplicationContext();
        mUiCtxt.setContextAndHandler(mAppContext);

        setContentView(R.layout.game_main);

        ButterKnife.inject(this);

        if (intent != null) {
            role = (Role) intent.getSerializableExtra("ROLE");
            if(role == Role.ADMIN){
                gameUBID = UUID.randomUUID().toString();
                selectedPlayerList = intent.getParcelableArrayListExtra("PLAYERS");
                connectToClients();
                createPlayerButtons();
            }else{
                admin = intent.getParcelableExtra("ADMIN");
                connectToServer();
            }
        }
    }

    @OnClick(R.id.distribute)
    public void onDistributeCards() {
        noOfCardsPerPlayer = totalNoOfCards / collectionPlayers.size();
        Set<Button> set = collectionPlayers.keySet();
        for (int i = 0; i < playerPanel.getChildCount(); i++) {
            Button btn = (Button) playerPanel.getChildAt(i);
            collectionPlayers.remove(btn);
            collectionPlayers.put(btn, noOfCardsPerPlayer);
            int tag = ((Integer) btn.getTag()).intValue();
            String text = selectedPlayerList.get(tag) + " (" + noOfCardsPerPlayer + ")";
            btn.setText(text);
        }
    }

    @OnClick(R.id.show_cards)
    public void onShowCards() {
        //TODO: Logic For Show Cards
    }

    @OnClick(R.id.take_card)
    public void onTakeCard(){
        //TODO: Logic For Take Cards
    }

    @OnClick(R.id.move_to_trash)
    public void onMoveTrash() {

    }

    @OnClick(R.id.show_cards)
    public void onGameStart(){
        gameStarted = true;

        initializeCardImages();

        distribute.setVisibility(View.GONE);
        showCards.setVisibility(View.GONE);
        startGame.setVisibility(View.GONE);

        takeCard.setVisibility(View.VISIBLE);
        moveTrash.setVisibility(View.VISIBLE);
        placeCard.setVisibility(View.VISIBLE);



        for (int i = 0; i < noOfCardsPerPlayer; i++) {
            ImageView card = new ImageView(getApplicationContext());
            String cardName = getNextCard();
            card.setImageResource(getNextCardImage(cardName));
            card.setTag(cardName);
            if (card != null) {
                card.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        v.setTranslationY(-mUiCtxt.dpToPx(10.0f));
                        selectCard((ImageView) v);
                    }
                });
            }
            cardPanel.addView(card, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        }
    }

    @OnClick(R.id.place_card)
    public void placeCard() {
        for (int i = 0; i < selectedCardValue.size(); i++) {
            Toast.makeText(this, selectedCardValue.get(i) + " OF " + selectedCardColor.get(i), Toast.LENGTH_SHORT).show();
        }
        selectedCardColor.clear();
        selectedCardValue.clear();
        for (ImageView v : selectedCardImages) {
            cardPanel.removeView(v);
        }
        selectedCardImages.clear();
    }

    private void createPlayerButtons(){
        for (int i = 0; i < selectedPlayerList.size(); i++) {
            Button player = new Button(this);
            player.setText(selectedPlayerList.get(i).getPlayerName());
            player.setTag(i);
            if (player != null) {
                player.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        giveCards((Button) v);
                    }
                });
            }
            playerPanel.addView(player);
            collectionPlayers.put(player, 0);
        }
    }
    private void giveCards(Button btn) {
        if (gameStarted) {
            return;
        }
        int val = collectionPlayers.get(btn).intValue();
        ++val;
        collectionPlayers.remove(btn);
        collectionPlayers.put(btn, val);
        int tag = ((Integer) btn.getTag()).intValue();
        String text = selectedPlayerList.get(tag) + " (" + val + ")";
        btn.setText(text);
    }

    private void initializeCardImages() {
        tempCardsImageNames = new ArrayList<String>();
        for (String el : Constant.CARD_IMAGE_NAMES) {
            tempCardsImageNames.add(el);
        }
    }

    private String getNextCard() {
        Random rnd = new Random();
        int index = rnd.nextInt(tempCardsImageNames.size());
        String res_string = tempCardsImageNames.get(index);
        tempCardsImageNames.remove(index);
        return res_string;
    }

    private int getNextCardImage(String str) {
        int res = 0;
        if (Constant.CARD_IMAGE_RESOURCE.containsKey(str)) {
            res = Constant.CARD_IMAGE_RESOURCE.get(str);
        }
        return res;
    }

    private void selectCard(ImageView v) {
        String cardName = (String) v.getTag();
        String split[] = cardName.split("_");
        selectedCardValue.add(split[0]);
        selectedCardColor.add(split[split.length - 1]);
        selectedCardImages.add(v);

    }

    private void distributeCards() {

    }

    private void connectToClients(){
        selectServerMode();
        for(Player player : selectedPlayerList){
            if(player.getRole() != Role.ADMIN) {
                createServer(player.getPlayerAddress());
            }
        }
    }

    private void connectToServer(){
        selectClientMode();
        createClient(admin.getPlayerAddress());
    }


    @Override
    public void onBluetoothDeviceFound(BluetoothDevice device) {

    }

    @Override
    public void onClientConnectionSuccess() {
        Log.d("Client Connection ", mBluetoothManager.getYourBtMacAddress() + mBluetoothManager.getDeviceName());
    }

    @Override
    public void onClientConnectionFail() {

    }

    @Override
    public void onServerConnectionSuccess() {
        sendMessage(MessageGenerator.getPlayerListMessage(gameUBID, selectedPlayerList));
    }

    @Override
    public void onServerConnectionFail() {

    }

    @Override
    public void onBluetoothStartDiscovery() {

    }

    @Override
    public void onBluetoothCommunicator(String messageReceive) {
        Log.d("MessageReceived", messageReceive);
        if(role == Role.ADMIN){
            sendMessage(messageReceive);
        }
        actOnMessage(messageReceive);
    }

    private void actOnMessage(String message){
        try {
            JSONObject jsonObject = new JSONObject(message);
            MessageType messageType = MessageType.valueOf(jsonObject.getString("MessageType"));
            int receivedMessageCounter = jsonObject.getInt("MessageCounter");
            String receivedGameUBID = jsonObject.getString("GameUBID");
            if(lastHandledMessage < receivedMessageCounter && (gameUBID == null || gameUBID == receivedGameUBID)) {
                if (messageType == MessageType.PlayerList) {
                    if (role == Role.PLAYER && gameUBID == null) {
                        gameUBID = receivedGameUBID;
                        lastHandledMessage = receivedMessageCounter;
                        selectedPlayerList = (new Gson()).fromJson(jsonObject.get("PlayerList").toString(), new TypeToken<ArrayList<Player>>() {
                        }.getType());
                        createPlayerButtons();
                    }
                } else if (messageType == MessageType.Deck && gameUBID != null) {
                    lastHandledMessage = receivedMessageCounter;
                    //TODO:
                }
            }
        } catch(Exception e){
            Log.e(Constant.LOG_TAG, e.getMessage());
        }
    }

    @Override
    public void onBluetoothNotAvailable() {

    }
}
