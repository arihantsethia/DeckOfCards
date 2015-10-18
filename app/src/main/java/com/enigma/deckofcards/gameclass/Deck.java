package com.enigma.deckofcards.gameclass;

import java.util.ArrayList;

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
            if (index == 52) {
                break;
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
    public ArrayList<Card> GetCardForPlayer(int player_index) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Card card : card_list) {
            if (card.l.getPosition() == Place.D || card.l.getPosition() == Place.H) {
                if (card.l.getPlayer_index() == player_index) {
                    cards.add(0, card);
                }
            }
        }
        return cards;
    }
    public ArrayList<Card> GetCardForArena() {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Card card : card_list) {
            if (card.l.getPosition() == Place.A) {
                cards.add(0, card);
            }
        }
        return cards;
    }
}
