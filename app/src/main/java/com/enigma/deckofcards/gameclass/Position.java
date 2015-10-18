package com.enigma.deckofcards.gameclass;

/**
 * Created by sparshs on 10/17/15.
 */
public class Position {
    Place p;
    int i;
    public boolean setPosition(Place current_position, int index) {
        if (current_position == Place.H || current_position == Place.D) {
            if (index == -1) {
                return false;
            } else {
                p = current_position;
                i = index;
                return true;
            }
        } else {
            p = current_position;
            return true;
        }
    }
    public Place getPosition() {
        return p;
    }
    public int getPlayer_index() {
        return i;
    }
}
