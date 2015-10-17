package com.enigma.deckofcards.gameclass;

/**
 * Created by sparshs on 10/17/15.
 */
public class Card {
    Card(CardColor color, CardValue value) {
        this.color = color;
        this.value = value;
    }
    public Position getLocation() {
        return location;
    }

    public void setLocation(Position current_location) {
        this.location = current_location;
    }

    Position location;

    public CardColor getColor() {
        return color;
    }

    CardColor color;

    public CardValue getValue() {
        return value;
    }

    CardValue value;

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    boolean visibility;

}

