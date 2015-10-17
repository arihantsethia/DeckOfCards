package com.enigma.deckofcards.listener;

import com.enigma.deckofcards.Player;

/**
 * Created by sethiaa on 17/10/15.
 */
public interface PlayerListSelectionListener {
    public void onPerformPlayerDeselect(Player player);
    public void onPerformPlayerSelect(Player player);
}
