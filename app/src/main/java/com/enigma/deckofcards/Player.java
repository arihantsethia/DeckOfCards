package com.enigma.deckofcards;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by sethiaa on 17/10/15.
 */

public class Player implements Parcelable {
    String playerDeviceName;
    String playerAddress;
    int identifier;
    int playerScore;
    float totalAmountWon;
    int amountInPlay;

    public Player(String playerDeviceName, String playerAddress, int identifier) {
        this.playerDeviceName = playerDeviceName;
        this.playerAddress = playerAddress;
        this.identifier = identifier;
        this.playerScore = 0;
        this.totalAmountWon = 0;
        this.amountInPlay = 0;
    }

    protected Player(Parcel in) {
        playerDeviceName = in.readString();
        playerAddress = in.readString();
        identifier = in.readInt();
        playerScore = in.readInt();
        totalAmountWon = in.readFloat();
        amountInPlay = in.readInt();
    }

    public int getIdentifier(){
        return identifier;
    }

    public int getPlayerScore(){
        return playerScore;
    }

    public float getTotalAmountWon(){
        return totalAmountWon;
    }

    public void setAmountInPlay(int amount){
        this.amountInPlay = amount;
    }

    public void updateAmountWon(float multiplier){
        totalAmountWon = totalAmountWon + multiplier*amountInPlay;
    }

    public void updateScore(int change){
        playerScore =  playerScore + change;
    }

    public String getPlayerDeviceName(){
        return playerDeviceName;
    }

    public String getPlayerAddress(){
        return playerAddress;
    }

    public String getGameName() {
        return playerDeviceName.split(":")[0].trim();
    }

    public String getPlayerName(){
        return playerDeviceName.split(":")[1].trim();
    }

    public Role getRole() {
        return Role.valueOf(playerDeviceName.split(":")[2].trim());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerDeviceName);
        dest.writeString(playerAddress);
        dest.writeInt(identifier);
        dest.writeInt(playerScore);
        dest.writeFloat(totalAmountWon);
        dest.writeInt(amountInPlay);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int hashCode() {
        return getPlayerAddress().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player))
            return false;
        if (obj == this)
            return true;

        Player rhs = (Player) obj;
        return rhs.getPlayerAddress().equals(getPlayerAddress());
    }
}