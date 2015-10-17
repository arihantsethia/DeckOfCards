package com.enigma.deckofcards.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import butterknife.ButterKnife;
import enigma.deckofcards.R;

/**
 * Created by sethiaa on 17/10/15.
 */
public class MainActivity2 extends BluetoothActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @Override
    public void onBluetoothDeviceFound(BluetoothDevice device) {

    }

    @Override
    public void onClientConnectionSuccess() {

    }

    @Override
    public void onClientConnectionFail() {

    }

    @Override
    public void onServerConnectionSuccess() {

    }

    @Override
    public void onServerConnectionFail() {

    }

    @Override
    public void onBluetoothStartDiscovery() {

    }

    @Override
    public void onBluetoothCommunicator(String messageReceive) {

    }

    @Override
    public void onBluetoothNotAvailable() {

    }
}
