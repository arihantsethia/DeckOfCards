package com.enigma.deckofcards.gameclass;

/**
 * Created by sparshs on 10/17/15.
 */
public class Card {
    Card(CardColor color, CardValue value) {
        this.c = color;
        this.v = value;
        this.b = false;
    }
    public Position getLocation() {
        return l;
    }

    public void setLocation(Position current_location) {
        this.l = current_location;
    }

    Position l;

    public CardColor getCol() {
        return c;
    }

    CardColor c;

    public CardValue getVal() {
        return v;
    }

    CardValue v;

    boolean b;

    public boolean getVisibility(){
        return b;
    }

    public void setVisibility(boolean visible){
        b = visible;
    }

}

