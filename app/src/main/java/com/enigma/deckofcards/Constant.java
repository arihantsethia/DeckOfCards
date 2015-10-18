package com.enigma.deckofcards;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sethiaa on 17/10/15.
 */
public class Constant {
    public static final String APP_NAME = "Deck Of Cards";
    public static final String LOG_TAG = "DeckOfCards";

    public static ArrayList<String> CARD_IMAGE_NAMES = new ArrayList<String>(){
        {
            add("ACE_OF_HEARTS");
            add("TWO_OF_HEARTS");
            add("THREE_OF_HEARTS");
            add("FOUR_OF_HEARTS");
            add("FIVE_OF_HEARTS");
            add("SIX_OF_HEARTS");
            add("SEVEN_OF_HEARTS");
            add("EIGHT_OF_HEARTS");
            add("NINE_OF_HEARTS");
            add("TEN_OF_HEARTS");
            add("JACK_OF_HEARTS");
            add("QUEEN_OF_HEARTS");
            add("KING_OF_HEARTS");

            add("ACE_OF_SPADES");
            add("TWO_OF_SPADES");
            add("THREE_OF_SPADES");
            add("FOUR_OF_SPADES");
            add("FIVE_OF_SPADES");
            add("SIX_OF_SPADES");
            add("SEVEN_OF_SPADES");
            add("EIGHT_OF_SPADES");
            add("NINE_OF_SPADES");
            add("TEN_OF_SPADES");
            add("JACK_OF_SPADES");
            add("QUEEN_OF_SPADES");
            add("KING_OF_SPADES");

            add("ACE_OF_CLUBS");
            add("TWO_OF_CLUBS");
            add("THREE_OF_CLUBS");
            add("FOUR_OF_CLUBS");
            add("FIVE_OF_CLUBS");
            add("SIX_OF_CLUBS");
            add("SEVEN_OF_CLUBS");
            add("EIGHT_OF_CLUBS");
            add("NINE_OF_CLUBS");
            add("TEN_OF_CLUBS");
            add("JACK_OF_CLUBS");
            add("QUEEN_OF_CLUBS");
            add("KING_OF_CLUBS");

            add("ACE_OF_DIAMONDS");
            add("TWO_OF_DIAMONDS");
            add("THREE_OF_DIAMONDS");
            add("FOUR_OF_DIAMONDS");
            add("FIVE_OF_DIAMONDS");
            add("SIX_OF_DIAMONDS");
            add("SEVEN_OF_DIAMONDS");
            add("EIGHT_OF_DIAMONDS");
            add("NINE_OF_DIAMONDS");
            add("TEN_OF_DIAMONDS");
            add("JACK_OF_DIAMONDS");
            add("QUEEN_OF_DIAMONDS");
            add("KING_OF_DIAMONDS");
        }
    };

    public static HashMap<String, Integer> CARD_IMAGE_RESOURCE = new HashMap<String, Integer>(){
        {
            put("ACE_OF_HEARTS", R.drawable.ace_of_hearts);
            put("TWO_OF_HEARTS", R.drawable.two_of_hearts);
            put("THREE_OF_HEARTS", R.drawable.three_of_hearts);
            put("FOUR_OF_HEARTS", R.drawable.four_of_hearts);
            put("FIVE_OF_HEARTS", R.drawable.five_of_hearts);
            put("SIX_OF_HEARTS", R.drawable.six_of_hearts);
            put("SEVEN_OF_HEARTS", R.drawable.seven_of_hearts);
            put("EIGHT_OF_HEARTS", R.drawable.eight_of_hearts);
            put("NINE_OF_HEARTS", R.drawable.nine_of_hearts);
            put("TEN_OF_HEARTS", R.drawable.ten_of_hearts);
            put("JACK_OF_HEARTS", R.drawable.jack_of_hearts);
            put("QUEEN_OF_HEARTS", R.drawable.queen_of_hearts);
            put("KING_OF_HEARTS", R.drawable.king_of_hearts);

            put("ACE_OF_SPADES", R.drawable.ace_of_spades);
            put("TWO_OF_SPADES", R.drawable.two_of_spades);
            put("THREE_OF_SPADES", R.drawable.three_of_spades);
            put("FOUR_OF_SPADES", R.drawable.four_of_spades);
            put("FIVE_OF_SPADES", R.drawable.five_of_spades);
            put("SIX_OF_SPADES", R.drawable.six_of_spades);
            put("SEVEN_OF_SPADES", R.drawable.seven_of_spades);
            put("EIGHT_OF_SPADES", R.drawable.eight_of_spades);
            put("NINE_OF_SPADES", R.drawable.nine_of_spades);
            put("TEN_OF_SPADES", R.drawable.ten_of_spades);
            put("JACK_OF_SPADES", R.drawable.jack_of_spades);
            put("QUEEN_OF_SPADES", R.drawable.queen_of_spades);
            put("KING_OF_SPADES", R.drawable.king_of_spades);

            put("ACE_OF_CLUBS", R.drawable.ace_of_clubs);
            put("TWO_OF_CLUBS", R.drawable.two_of_clubs);
            put("THREE_OF_CLUBS", R.drawable.three_of_clubs);
            put("FOUR_OF_CLUBS", R.drawable.four_of_clubs);
            put("FIVE_OF_CLUBS", R.drawable.five_of_clubs);
            put("SIX_OF_CLUBS", R.drawable.six_of_clubs);
            put("SEVEN_OF_CLUBS", R.drawable.seven_of_clubs);
            put("EIGHT_OF_CLUBS", R.drawable.eight_of_clubs);
            put("NINE_OF_CLUBS", R.drawable.nine_of_clubs);
            put("TEN_OF_CLUBS", R.drawable.ten_of_clubs);
            put("JACK_OF_CLUBS", R.drawable.jack_of_clubs);
            put("QUEEN_OF_CLUBS", R.drawable.queen_of_clubs);
            put("KING_OF_CLUBS", R.drawable.king_of_clubs);

            put("ACE_OF_DIAMONDS", R.drawable.ace_of_diamonds);
            put("TWO_OF_DIAMONDS", R.drawable.two_of_diamonds);
            put("THREE_OF_DIAMONDS", R.drawable.three_of_diamonds);
            put("FOUR_OF_DIAMONDS", R.drawable.four_of_diamonds);
            put("FIVE_OF_DIAMONDS", R.drawable.five_of_diamonds);
            put("SIX_OF_DIAMONDS", R.drawable.six_of_diamonds);
            put("SEVEN_OF_DIAMONDS", R.drawable.seven_of_diamonds);
            put("EIGHT_OF_DIAMONDS", R.drawable.eight_of_diamonds);
            put("NINE_OF_DIAMONDS", R.drawable.nine_of_diamonds);
            put("TEN_OF_DIAMONDS", R.drawable.ten_of_diamonds);
            put("JACK_OF_DIAMONDS", R.drawable.jack_of_diamonds);
            put("QUEEN_OF_DIAMONDS", R.drawable.queen_of_diamonds);
            put("KING_OF_DIAMONDS", R.drawable.king_of_diamonds);

            put("CARD_BACK_FACE", R.drawable.card_back_face);
        }
    };

}
