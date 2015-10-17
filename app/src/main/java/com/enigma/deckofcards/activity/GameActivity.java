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
import android.widget.FrameLayout;
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
    int totalCardsDistributed = 0;
    int totalUnusedCards = 0;

    boolean gameStarted = false;

    Context mAppContext;
    UiContext mUiCtxt;
    Role role;
    Player admin;
    ArrayList<Player> selectedPlayerList;
    String gameUBID = null;
    int lastHandledMessage = -1;


    @InjectView(R.id.center_container)
    LinearLayout centerContainer;

    @InjectView(R.id.hand_container)
    FrameLayout handContainer;

    @InjectView(R.id.distribute)
    Button distribute;

    @InjectView(R.id.show_cards)
    Button showCards;

    @InjectView(R.id.start_game)
    Button startGame;

    @InjectView(R.id.from_unused)
    Button fromUnused;

    @InjectView(R.id.place_card)
    Button placeCard;

    @InjectView(R.id.from_table_to_deck)
    Button fromTableToDeck;

    @InjectView(R.id.from_table_to_hand)
    Button fromTableToHand;

    @InjectView(R.id.btn_unused)
    Button unused;

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
    ArrayList<String> unusedCardsList = new ArrayList<String>();
    ArrayList<String> centerCardCollection = new ArrayList<String>();
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
        totalCardsDistributed = 52;
    }

    @OnClick(R.id.show_cards)
    public void onShowCards() {
        //TODO: Logic For Show Cards
    }

    @OnClick(R.id.from_table_to_deck)
    public void onFromTableToDeck(){
        //TODO: Logic For Take Cards
        if(centerCardCollection.isEmpty())
            return;

        int childCount = centerContainer.getChildCount();

        for(int i=0;i<childCount;i++){
            ImageView card = (ImageView) centerContainer.getChildAt(0);
            centerContainer.removeView(card);
            card.setTranslationY(0.0f);
            cardPanel.addView(card, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        }

        centerCardCollection.clear();
    }

    @OnClick(R.id.from_table_to_hand)
    public void onFromTableToHand(){
        //TODO: Logic For Take Cards
        if(centerCardCollection.isEmpty())
            return;

        int childCount = centerContainer.getChildCount();

        for(int i=0;i<childCount;i++){
            ImageView card = (ImageView) centerContainer.getChildAt(0);
            centerContainer.removeView(card);
            card.setTranslationY(0.0f);
            card.setTranslationX(0.0f);

            handContainer.addView(card, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        }

        centerCardCollection.clear();
    }

    @OnClick(R.id.from_unused)
    public void onFromUnused() {
        if(unusedCardsList.isEmpty())
            return;

        String cardName = unusedCardsList.get(0);
        --totalUnusedCards;
        unusedCardsList.remove(0);
        ImageView card = new ImageView(getApplicationContext());
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
        unused.setText(totalUnusedCards + " cards");
    }

    @OnClick(R.id.start_game)
    public void onGameStart(){
        gameStarted = true;

        initializeCardImages();

        distribute.setVisibility(View.GONE);
        showCards.setVisibility(View.GONE);
        startGame.setVisibility(View.GONE);

        fromUnused.setVisibility(View.VISIBLE);
        fromTableToDeck.setVisibility(View.VISIBLE);
        fromTableToHand.setVisibility(View.VISIBLE);
        placeCard.setVisibility(View.VISIBLE);

        Button btn = (Button) playerPanel.getChildAt(0);
        int val = collectionPlayers.get(btn).intValue();

        totalUnusedCards = totalNoOfCards - totalCardsDistributed;

        for(int i=0;i<totalUnusedCards;i++) {
            unusedCardsList.add(getNextCard());
        }

        unused.setText(totalUnusedCards + " cards");
        ImageView tempCard1 = new ImageView(getApplicationContext());
        ImageView tempCard2 = new ImageView(getApplicationContext());
        ImageView tempCard3 = new ImageView(getApplicationContext());
        ImageView tempCard4 = new ImageView(getApplicationContext());
        cardPanel.addView(tempCard1, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        cardPanel.addView(tempCard2, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        cardPanel.addView(tempCard3, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        cardPanel.addView(tempCard4, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        for (int i = 0; i < val; i++) {
            ImageView card = new ImageView(getApplicationContext());
            String cardName = getNextCard();
//            Log.i(cardName, getNextCardImage(cardName) + "");
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
        selectedCardColor.clear();
        selectedCardValue.clear();
        for (ImageView v : selectedCardImages) {
            cardPanel.removeView(v);
            centerContainer.addView(v);
            v.setTranslationX((centerContainer.getChildCount()-1)*-mUiCtxt.dpToPx(40.0f));
            centerCardCollection.add((String)v.getTag());
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
        ++totalCardsDistributed;
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
        Toast.makeText(this, "" + tempCardsImageNames.size(), Toast.LENGTH_SHORT).show();
    }

    private String getNextCard() {
//        unused.setText(tempCardsImageNames.size() + "");
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
        if(selectedCardValue.contains(split[0]) && selectedCardColor.contains(split[split.length-1])){
            deselectCard(v);
            selectedCardValue.remove(split[0]);
            selectedCardColor.remove(split[split.length-1]);
            selectedCardImages.remove(v);
        }
        else{
            selectedCardValue.add(split[0]);
            selectedCardColor.add(split[split.length - 1]);
            selectedCardImages.add(v);
        }
    }

    private void deselectCard(ImageView v){
        v.setTranslationY(mUiCtxt.dpToPx(0.0f));
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
