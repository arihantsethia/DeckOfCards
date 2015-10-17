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

    public Player(String playerDeviceName, String playerAddress, int identifier) {
        this.playerDeviceName = playerDeviceName;
        this.playerAddress = playerAddress;
        this.identifier = identifier;
    }

    protected Player(Parcel in) {
        playerDeviceName = in.readString();
        playerAddress = in.readString();
        identifier = in.readInt();
    }

    public int getIdentifier(){
        return identifier;
    }


    public String getPlayerDeviceName(){
        return playerDeviceName;
    }

    public String getPlayerAddress(){
        return playerAddress;
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