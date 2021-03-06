package com.enigma.deckofcards.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.enigma.deckofcards.bluetooth.mananger.BluetoothManager;
import com.enigma.deckofcards.bus.BluetoothCommunicator;
import com.enigma.deckofcards.bus.BondedDevice;
import com.enigma.deckofcards.bus.ClientConnectionFail;
import com.enigma.deckofcards.bus.ClientConnectionSuccess;
import com.enigma.deckofcards.bus.ServerConnectionFail;
import com.enigma.deckofcards.bus.ServerConnectionSuccess;

import de.greenrobot.event.EventBus;

/**
 * Created by Rami MARTIN on 13/04/2014.
 */
public abstract class BluetoothFragment extends Fragment {

    protected BluetoothManager mBluetoothManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBluetoothManager = new BluetoothManager(getActivity());
        checkBluetoothAvailability();
        mBluetoothManager.setNbrClientMax(myNbrClientMax());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        closeAllConnexion();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothManager.REQUEST_DISCOVERABLE_CODE) {
            if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_REFUSED) {
                getActivity().finish();
            } else if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_ACCEPTED) {
                onBluetoothStartDiscovery();
            } else {
                getActivity().finish();
            }
        }
    }

    public void closeAllConnexion() {
        mBluetoothManager.closeAllConnection();
    }

    public void checkBluetoothAvailability() {
        if (!mBluetoothManager.checkBluetoothAvailability()) {
            onBluetoothNotAviable();
        }
    }

    public void setTimeDiscoverable(int timeInSec) {
        mBluetoothManager.setTimeDiscoverable(timeInSec);
    }

    public void startDiscovery() {
        mBluetoothManager.startDiscovery();
    }

    public boolean isConnected() {
        return mBluetoothManager.isConnected;
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

    public void createServeur(String address) {
        mBluetoothManager.createServer(address);
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

    public abstract int myNbrClientMax();

    public abstract void onBluetoothDeviceFound(BluetoothDevice device);

    public abstract void onClientConnectionSuccess();

    public abstract void onClientConnectionFail();

    public abstract void onServeurConnectionSuccess();

    public abstract void onServeurConnectionFail();

    public abstract void onBluetoothStartDiscovery();

    public abstract void onBluetoothCommunicator(String messageReceive);

    public abstract void onBluetoothNotAviable();

    public void onEventMainThread(BluetoothDevice device) {
        if (!mBluetoothManager.isNbrMaxReached()) {
            onBluetoothDeviceFound(device);
            createServeur(device.getAddress());
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
        onServeurConnectionSuccess();
    }

    public void onEventMainThread(ServerConnectionFail event) {
        mBluetoothManager.onServerConnectionFailed(event.mClientAdressConnectionFail);
        onServeurConnectionFail();
    }

    public void onEventMainThread(BluetoothCommunicator event) {
        onBluetoothCommunicator(event.mMessageReceive);
    }

    public void onEventMainThread(BondedDevice event) {
        //mBluetoothManager.sendMessage("BondedDevice");
    }
}
