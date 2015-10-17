package com.enigma.deckofcards;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sethiaa on 18/10/15.
 */
public class MessageGenerator {

    private static int counter = 0;

    public static String getPlayerListMessage(String gameUBID, ArrayList<Player> players){
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("GameUBID", gameUBID);
            jsonObject.put("MessageType", MessageType.PlayerList);
            jsonObject.put("MessageCounter", counter++);
            jsonObject.put("PlayerList", (new Gson()).toJson(players, new TypeToken<ArrayList<Player>>() {
            }.getType()));
            message = jsonObject.toString();
        }catch (JSONException e) {
            Log.e(Constant.LOG_TAG, e.getMessage());
        }
        return message;
    }

    public static String getCurrentDeck(String gameUBID){
        return "";
    }
}
