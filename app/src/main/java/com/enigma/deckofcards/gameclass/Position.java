package com.enigma.deckofcards.gameclass;

/**
 * Created by sparshs on 10/17/15.
 */
public class Position {
    Place position;
    int player_index;
    public boolean setPosition(Place current_position, int index) {
        if (current_position == Place.PLAYER_HAND || current_position == Place.PLAYER_DECK) {
            if (index == -1) {
                return false;
            } else {
                position = current_position;
                player_index = index;
                return true;
            }
        } else {
            position = current_position;
            return true;
        }
    }
    public Place getPosition() {
        return position;
    }
    public int getPlayer_index() {
        return player_index;
    }
}
