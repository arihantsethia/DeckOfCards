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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enigma.deckofcards.Constant;
import com.enigma.deckofcards.MessageGenerator;
import com.enigma.deckofcards.MessageType;
import com.enigma.deckofcards.Player;
import com.enigma.deckofcards.R;
import com.enigma.deckofcards.Role;
import com.enigma.deckofcards.gameclass.Card;
import com.enigma.deckofcards.gameclass.CardColor;
import com.enigma.deckofcards.gameclass.CardValue;
import com.enigma.deckofcards.gameclass.Deck;
import com.enigma.deckofcards.gameclass.Game;
import com.enigma.deckofcards.gameclass.Place;
import com.enigma.deckofcards.ui.UiContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

    String backFace = "CARD_BACK_FACE";

    boolean gameStarted = false;

    Game gameState;

    Context mAppContext;
    UiContext mUiCtxt;
    Role role;
    Player admin;
    ArrayList<Player> selectedPlayerList;
    String gameUBID = null;
    int lastHandledMessage = -1;
    String bMsg="";


    @InjectView(R.id.center_container)
    LinearLayout centerContainer;

    @InjectView(R.id.hand_container)
    FrameLayout handContainer;

    @InjectView(R.id.unused_container)
    FrameLayout unusedContainer;

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

    @InjectView(R.id.btn_score)
    Button scoreUpdate;

    @InjectView(R.id.btn_endGame)
    Button btnEndGame;

    @InjectView(R.id.from_table_to_hand)
    Button fromTableToHand;

//    @InjectView(R.id.btn_unused)
//    Button unused;

    @InjectView(R.id.player_panel)
    LinearLayout playerPanel;

    @InjectView(R.id.card_panel)
    LinearLayout cardPanel;

    @InjectView(R.id.scroller_card_panel)
    HorizontalScrollView scrollerCardPanel;


    ArrayList<String> tempCardsImageNames;

    HashMap<View, Integer> collectionPlayers = new HashMap<View, Integer>();
    HashMap<Player, View> playerViews = new HashMap<Player, View>();
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
            if (role == Role.ADMIN) {
                gameUBID = UUID.randomUUID().toString();
                selectedPlayerList = intent.getParcelableArrayListExtra("PLAYERS");
                connectToClients();
                gameState = new Game(selectedPlayerList.size(), getMyListIndex(), (getMyListIndex() == 0));
                createPlayerButtons();
            } else {
                distribute.setVisibility(View.GONE);
                admin = intent.getParcelableExtra("ADMIN");
                connectToServer();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            if(resultCode == 500){
                for(Player el : selectedPlayerList) {
                    int val = data.getIntExtra(el.getPlayerName(), 0);
                    el.updateScore(val);
                    String text = el.getPlayerName() + " : " + el.getPlayerScore();
                    TextView tv = (TextView) playerViews.get(el).findViewById(R.id.text_playername);
                    tv.setText(text);
                    Toast.makeText(this, el + " : " + val, Toast.LENGTH_SHORT).show();
                }
            }

            if(resultCode == 600){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == 200){
            if(resultCode == 500){
                boolean finish = data.getBooleanExtra("END_GAME", false);
                Toast.makeText(this, "End game : " + finish, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private View getPlayerView(Player player){
        View v = getLayoutInflater().inflate(R.layout.player_view_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.text_playername);
        tv.setText(player.getPlayerName() + " : " + player.getPlayerScore());
        return v;
    }

    @OnClick(R.id.distribute)
    public void onDistributeCards() {
        noOfCardsPerPlayer = totalNoOfCards / collectionPlayers.size();
        Set<View> set = collectionPlayers.keySet();
        for (int i = 0; i < playerPanel.getChildCount(); i++) {
            View btn = playerPanel.getChildAt(i);
            collectionPlayers.remove(btn);
            collectionPlayers.put(btn, noOfCardsPerPlayer);
            int tag = ((Integer) btn.getTag()).intValue();
            String text = selectedPlayerList.get(tag).getPlayerName() + " (" + noOfCardsPerPlayer + ")";
            TextView tv = (TextView) btn.findViewById(R.id.text_playername);
            tv.setText(text);
        }
        totalCardsDistributed = 52;
        gameState.Distribute();
    }

    @OnClick(R.id.show_cards)
    public void onShowCards() {
        ArrayList<Card> cards = gameState.getDeck().GetCardForArena();
        for (Card card : cards){
            card.setVisibility(true);
        }
        sendMessage(MessageGenerator.getCurrentDeck(gameUBID, gameState));
        updateUI();
    }

    @OnClick(R.id.btn_endGame)
    public void onEndGame() {
        Intent intent = new Intent(this, EndGameActivity.class);
        startActivityForResult(intent, 200);
    }

    @OnClick(R.id.btn_score)
    public void onScoreUpdate() {
        Intent intent = new Intent(this, ScoreUpdateActivity.class);
        intent.putParcelableArrayListExtra("PLAYERS", selectedPlayerList);
        startActivityForResult(intent, 100);
    }

    @OnClick(R.id.from_table_to_deck)
    public void onFromTableToDeck(){
        ArrayList<Card> cards = gameState.getDeck().GetCardForArena();

        for(int i=0;i<cards.size();i++){
            cards.get(i).setVisibility(false);
            String cardName = getCardName(cards.get(i).getCol(), cards.get(i).getVal());
            gameState.ChangeLocationOfCard(getCardColor(cardName), getCardValue(cardName), Place.D);
        }
        sendMessage(MessageGenerator.getCurrentDeck(gameUBID, gameState));
        updateUI();
    }

    @OnClick(R.id.from_table_to_hand)
    public void onFromTableToHand(){
        //TODO: Logic For Take Cards
        if(centerCardCollection.isEmpty())
            return;

        // int childCount = centerContainer.getChildCount();

        ArrayList<Card> cards = gameState.getDeck().GetCardForArena();

        for(Card card: cards){
            card.setVisibility(true);
            String cardName = getCardName(card.getCol(), card.getVal());
            gameState.ChangeLocationOfCard(getCardColor(cardName), getCardValue(cardName), Place.H);
        }

        sendMessage(MessageGenerator.getCurrentDeck(gameUBID, gameState));
        updateUI();
    }

    @OnClick(R.id.from_unused)
    public void onFromUnused() {
        if(unusedCardsList.isEmpty())
            return;

        ImageView card = getCardFromUnused();
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

    private ImageView getCardFromUnused(){
        String cardName = unusedCardsList.get(unusedCardsList.size()-1);
        --totalUnusedCards;
        unusedCardsList.remove(unusedCardsList.size()-1);
        ImageView v = (ImageView)unusedContainer.findViewWithTag(cardName);
        unusedContainer.removeView(v);
        return v;
    }

    @OnClick(R.id.start_game)
    public void onGameStart(){
        gameStarted = true;

        initializeCardImages();

        distribute.setVisibility(View.GONE);
        startGame.setVisibility(View.GONE);

        fromUnused.setVisibility(View.VISIBLE);
        fromTableToDeck.setVisibility(View.VISIBLE);
        fromTableToHand.setVisibility(View.VISIBLE);
        placeCard.setVisibility(View.VISIBLE);
        showCards.setVisibility(View.VISIBLE);
        //showCardCheck.setVisibility(View.VISIBLE);

        View btn =  playerPanel.getChildAt(0);
        int val = collectionPlayers.get(btn).intValue();

        totalUnusedCards = totalNoOfCards - totalCardsDistributed;
/*
        for(int i=0;i<totalUnusedCards;i++) {
            String cardName = getNextCard();
            unusedCardsList.add(cardName);
            ImageView card = new ImageView(getApplicationContext());
            card.setImageResource(getNextCardImage(cardName));
            card.setTag(cardName);

            unusedContainer.addView(card, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        }
*/
        ImageView tempCard1 = new ImageView(getApplicationContext());
        ImageView tempCard2 = new ImageView(getApplicationContext());
        ImageView tempCard3 = new ImageView(getApplicationContext());
        ImageView tempCard4 = new ImageView(getApplicationContext());
        cardPanel.addView(tempCard1, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        cardPanel.addView(tempCard2, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        cardPanel.addView(tempCard3, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
        cardPanel.addView(tempCard4, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));

        sendMessage(MessageGenerator.getCurrentDeck(gameUBID, gameState));
        updateUI();
    }

    private void ShowCardsForPlayer(int index) {
        Log.e("Calling Show for Player", "yes" + Integer.toString(index));
        cardPanel.removeAllViews();
        ArrayList<Card> cardsOfCurrentPlayer = gameState.getDeck().GetCardForPlayer(index);
        for (int i = 0; i < cardsOfCurrentPlayer.size(); i++) {
            Card currentCard = cardsOfCurrentPlayer.get(i);
            ImageView card = new ImageView(getApplicationContext());
            String cardName = getCardName(currentCard.getCol(), currentCard.getVal());
            card.setImageResource(getNextCardImage(cardName));
            card.setTag(cardName);
            Log.e("CardName", cardName);
            if(currentCard.getLocation().getPosition() == Place.D) {
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
            else {
                card.setTranslationY(0.0f);
                card.setTranslationX(0.0f);
                handContainer.addView(card, (int) mUiCtxt.dpToPx(50.0f), (int) mUiCtxt.dpToPx(100.0f));
            }
        }

    }

    private void ShowCardsForArena() {
        Log.e("Calling Show for Arena", "yes");
        centerContainer.removeAllViews();
        centerCardCollection.clear();
        ArrayList<Card> cardsOfArena = gameState.getDeck().GetCardForArena();
        for (int i = 0; i < cardsOfArena.size(); i++) {
            Card currentCard = cardsOfArena.get(i);
            ImageView card = new ImageView(getApplicationContext());
            String cardName = getCardName(currentCard.getCol(), currentCard.getVal());
            if(currentCard.getVisibility()) {
                card.setImageResource(getNextCardImage(cardName));
            }else{
                card.setImageResource(getNextCardImage(backFace));
            }
            card.setTag(cardName);
            Log.e("CardName", cardName);
            centerContainer.addView(card, (int)mUiCtxt.dpToPx(50.0f), (int)mUiCtxt.dpToPx(100.0f));
            card.setTranslationX((centerContainer.getChildCount() - 1) * -mUiCtxt.dpToPx(40.0f));
            centerCardCollection.add(cardName);
        }
    }
    private static final String[] cardColors = {"DIAMONDS", "HEARTS", "SPADES", "CLUBS"};
    private static final String[] cardValues = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "KING", "QUEEN"};
    private String getCardName(CardColor cardColor, CardValue cardValue) {
        return cardValues[cardValue.ordinal()] + "_OF_" + cardColors[cardColor.ordinal()];
    }
    private  CardValue getCardValue(String cardName) {
        String cv = cardName.substring(0, cardName.indexOf('_'));
        int ord = Arrays.asList(cardValues).indexOf(cv);
        return CardValue.values()[ord];
    }
    private  CardColor getCardColor(String cardName) {
        String cc = cardName.substring(cardName.indexOf('_')+4);
        int ord = Arrays.asList(cardColors).indexOf(cc);
        return CardColor.values()[ord];
    }

    @OnClick(R.id.place_card)
    public void placeCard() {


        selectedCardColor.clear();
        selectedCardValue.clear();
        Log.e("before deck", gameState.ConvertDeckToJsonString());
        for (ImageView v : selectedCardImages) {
            String cardName = (String) v.getTag();
            gameState.ChangeLocationOfCard(getCardColor(cardName), getCardValue(cardName), Place.A);

            /*if(!showCardCheck.isChecked()) {
                String tempName = (String) v.getTag();
                v.setImageResource(getNextCardImage(backFace));
            }*/
            cardPanel.removeView(v);
            centerContainer.addView(v);
            v.setTranslationX((centerContainer.getChildCount()-1)*-mUiCtxt.dpToPx(40.0f));
            centerCardCollection.add((String)v.getTag());
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        sendMessage(MessageGenerator.getCurrentDeck(gameUBID, gameState));
        updateUI();
        selectedCardImages.clear();
    }

    private void createPlayerButtons(){

        for (int i = 0; i < selectedPlayerList.size(); i++) {
            View player = getPlayerView(selectedPlayerList.get(i));
            player.setTag(i);
            if (player != null) {
                player.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        giveCards( v);
                    }
                });
            }
            playerPanel.addView(player);
            collectionPlayers.put(player, 0);
            playerViews.put(selectedPlayerList.get(i), player);
        }
    }

    private void giveCards(View btn) {
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
        TextView tv = (TextView) btn.findViewById(R.id.text_playername);
        tv.setText(text);
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

    private void actOnMessage(String message) {
        bMsg+=message;
        try {
            Log.e("act on", bMsg);
            JSONObject jsonObject = new JSONObject(bMsg);
            MessageType messageType = MessageType.valueOf(jsonObject.getString("MessageType"));
            int receivedMessageCounter = jsonObject.getInt("MessageCounter");
            String receivedGameUBID = jsonObject.getString("GameUBID");
            if(lastHandledMessage < receivedMessageCounter && (gameUBID == null || gameUBID.equals(receivedGameUBID))) {
                if (messageType == MessageType.PlayerList) {
                    if (role == Role.PLAYER && gameUBID == null) {
                        gameUBID = receivedGameUBID;
                        lastHandledMessage = receivedMessageCounter;
                        selectedPlayerList = (new Gson()).fromJson(jsonObject.get("PlayerList").toString(), new TypeToken<ArrayList<Player>>() {
                        }.getType());
                        gameState = new Game(selectedPlayerList.size(), getMyListIndex(), (getMyListIndex() == 0));
                        createPlayerButtons();
                    }
                } else if (messageType == MessageType.Deck && gameUBID != null) {
                    lastHandledMessage = receivedMessageCounter;
                    gameState.updateDeckfromJson(jsonObject.get("Deck").toString());
                    updateUI();
                }
            }
            bMsg="";
        } catch(Exception e){
            Log.e(Constant.LOG_TAG, e.getMessage());
        }
    }

    @Override
    public void onBluetoothNotAvailable() {

    }

    private void updateUI(){
        ShowCardsForArena();
        ShowCardsForPlayer(getMyListIndex());
    }

    private int getMyListIndex(){
        for(int i=0;i<selectedPlayerList.size();i++){
            if(mBluetoothManager.getYourBtMacAddress().equals(selectedPlayerList.get(i).getPlayerAddress())) {
                return selectedPlayerList.get(i).getIdentifier();
            }
        }
        return -1;
    }
}
