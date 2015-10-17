package com.enigma.deckofcards.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.enigma.deckofcards.bluetooth.mananger.BluetoothManager;
import com.enigma.deckofcards.bus.BluetoothCommunicator;
import com.enigma.deckofcards.bus.BondedDevice;
import com.enigma.deckofcards.bus.ClientConnectionFail;
import com.enigma.deckofcards.bus.ClientConnectionSuccess;
import com.enigma.deckofcards.bus.ServerConnectionFail;
import com.enigma.deckofcards.bus.ServerConnectionSuccess;

import de.greenrobot.event.EventBus;

public abstract class BluetoothActivity extends AppCompatActivity {

    protected BluetoothManager mBluetoothManager;
    protected String mGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothManager = new BluetoothManager(this);
        checkBluetoothAviability();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        mBluetoothManager.setNbrClientMax(myNbrClientMax());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        closeAllConnexion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothManager.REQUEST_DISCOVERABLE_CODE) {
            if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_REFUSED) {
            } else if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_ACCEPTED) {
                onBluetoothStartDiscovery();
            } else {
            }
        }
    }

    private boolean isRelevantDevice(BluetoothDevice device) {
        return !device.getName().isEmpty() && device.getName().startsWith(getGameName());
    }

    private String getGameName() {
        return mGameName;
    }

    private void setGameName(String gameName) {
        mGameName = gameName;
    }

    public void closeAllConnexion() {
        mBluetoothManager.closeAllConnexion();
    }

    public void checkBluetoothAviability() {
        if (!mBluetoothManager.checkBluetoothAviability()) {
            onBluetoothNotAvailable();
        }
    }

    public void setTimeDiscoverable(int timeInSec) {
        mBluetoothManager.setTimeDiscoverable(timeInSec);
    }

    public void startDiscovery() {
        mBluetoothManager.startDiscovery();
    }

    public void scanAllBluetoothDevice() {
        mBluetoothManager.scanAllBluetoothDevice();
    }

    public void disconnectClient() {
        mBluetoothManager.disconnectClient();
    }

    public void disconnectServer() {
        mBluetoothManager.disconnectServer();
    }

    public void createServer(String address) {
        mBluetoothManager.createServeur(address);
    }

    public void selectServerMode() {
        mBluetoothManager.selectServerMode();
    }

    public void selectClientMode() {
        mBluetoothManager.selectClientMode();
    }

    public BluetoothManager.TypeBluetooth getTypeBluetooth() {
        return mBluetoothManager.mType;
    }

    public BluetoothManager.TypeBluetooth getBluetoothMode() {
        return mBluetoothManager.mType;
    }

    public void createClient(String addressMac) {
        mBluetoothManager.createClient(addressMac);
    }

    public void sendMessage(String message) {
        mBluetoothManager.sendMessage(message);
    }

    public boolean isConnected() {
        return mBluetoothManager.isConnected;
    }

    public int myNbrClientMax() {
        return 7;
    }

    public abstract void onBluetoothDeviceFound(BluetoothDevice device);

    public abstract void onClientConnectionSuccess();

    public abstract void onClientConnectionFail();

    public abstract void onServerConnectionSuccess();

    public abstract void onServerConnectionFail();

    public abstract void onBluetoothStartDiscovery();

    public abstract void onBluetoothCommunicator(String messageReceive);

    public abstract void onBluetoothNotAvailable();

    public void onEventMainThread(BluetoothDevice device) {
        onBluetoothDeviceFound(device);
        if (isRelevantDevice(device)) {
            createServer(device.getAddress());
        }
    }

    public void onEventMainThread(ClientConnectionSuccess event) {
        mBluetoothManager.isConnected = true;
        onClientConnectionSuccess();
    }

    public void onEventMainThread(ClientConnectionFail event) {
        mBluetoothManager.isConnected = false;
        onClientConnectionFail();
    }

    public void onEventMainThread(ServerConnectionSuccess event) {
        mBluetoothManager.isConnected = true;
        mBluetoothManager.onServerConnectionSuccess(event.mClientAdressConnected);
        onServerConnectionSuccess();
    }

    public void onEventMainThread(ServerConnectionFail event) {
        mBluetoothManager.onServerConnectionFailed(event.mClientAdressConnectionFail);
        onServerConnectionFail();
    }

    public void onEventMainThread(BluetoothCommunicator event) {
        onBluetoothCommunicator(event.mMessageReceive);
    }

    public void onEventMainThread(BondedDevice event) {
        //mBluetoothManager.sendMessage("BondedDevice");
    }

}
