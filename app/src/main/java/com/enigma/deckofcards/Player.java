package com.enigma.deckofcards;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sethiaa on 17/10/15.
 */
public class Player  implements Parcelable {
    BluetoothDevice playerDevice;
    Role role;

    public Player(BluetoothDevice playerDevice, Role role) {
        this.playerDevice = playerDevice;
        this.role = role;
    }

    public String getPlayerBluetoothName(){
        return playerDevice.getName();
    }

    public String getPlayerAddress(){
        return playerDevice.getAddress();
    }

    public String getPlayerName(){
        return playerDevice.getName();
    }

    public BluetoothDevice getPlayerDevice() {
        return playerDevice;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
