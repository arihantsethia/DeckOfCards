package com.enigma.deckofcards.gameclass;

/**
 * Created by sparshs on 10/17/15.
 */
public class Deck {
    Card[] card_list;
    Deck() {
        card_list = new Card[52];
        int index = 0;
        for (CardColor cardColor : CardColor.values()) {
            for (CardValue cardValue : CardValue.values()) {
                card_list[index] = new Card(cardColor, cardValue);
                ++index;
            }
        }
    }
    public boolean ChangeCardLocation(int index, Place place, int player_index) {
        Position currentLocation = new Position();
        if (currentLocation.setPosition(place, player_index)) {
            card_list[index].setLocation(currentLocation);
            return true;
        } else {
            return false;
        }
    }
}
