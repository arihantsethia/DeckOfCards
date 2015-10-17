package com.enigma.deckofcards.listener;

/**
 * Created by sethiaa on 17/10/15.
 */
public interface PlayerListSelectionListener {
    public void onPerformPlayerDeselect(String playerName);
    public void onPerformPlayerSelect(String playerName);
}
