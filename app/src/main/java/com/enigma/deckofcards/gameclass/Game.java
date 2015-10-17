package com.enigma.deckofcards.gameclass;

import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

/**
 * Created by sparshs on 10/17/15.
 */
public class Game {
    Deck whole_deck;
    Deck previous_snapshot;
    int no_of_players;
    int current_player;
    int admin_index;
    Gson gson;
    public Game(int no_of_players, int current_player, boolean is_admin) {
        this.no_of_players = no_of_players;
        this.current_player = current_player;
        if (is_admin) {
            this.admin_index = current_player;
        }
        whole_deck = new Deck();
        previous_snapshot = whole_deck;
        gson = new Gson();
    }
    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public boolean Distribute() {
        if (this.current_player != this.admin_index) {
            return false;
        }
        int[] shuffle = new int[52];
        for (int i = 0; i < 52; ++i) {
            shuffle[i] = i;
        }
        shuffleArray(shuffle);
        for (int i = 0; i < 52; ++i) {
            whole_deck.ChangeCardLocation(shuffle[i], Place.PLAYER_HAND, i%no_of_players);
        }
        return true;
    }

    public String ConvertDeckToJsonString() {
        return gson.toJson(whole_deck);
    }

    public void updateDeckfromJson(String json_string) {
        previous_snapshot = whole_deck;
        whole_deck = gson.fromJson(json_string, Deck.class);
    }

    public void Undo() {
        whole_deck = previous_snapshot;
    }

    public void ChangeLocationOfCard(CardColor cardColor, CardValue cardValue, Place place) {
        int card_index = cardColor.ordinal() * 13 + cardValue.ordinal();
        whole_deck.ChangeCardLocation(card_index, place, current_player);
    }

    public boolean Move(List<Card> cards, Place to) {
        for (Card card : cards) {
            Position new_location = new Position();
            if (new_location.setPosition(to, current_player)) {
                card.setLocation(new_location);
            } else {
                return false;
            }
        }
        return true;
    }
}
