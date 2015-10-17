package com.enigma.deckofcards.bluetooth.mananger;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.enigma.deckofcards.bluetooth.client.BluetoothClient;
import com.enigma.deckofcards.bluetooth.server.BluetoothServer;
import com.enigma.deckofcards.bus.BondedDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Rami MARTIN on 13/04/2014.
 */
public class BluetoothManager extends BroadcastReceiver {

    public static final int REQUEST_DISCOVERABLE_CODE = 114;
    public static final int BLUETOOTH_REQUEST_REFUSED = 0; // NE PAS MODIFIER LA VALEUR
    public static final int BLUETOOTH_TIME_DICOVERY_60_SEC = 60;
    public static final int BLUETOOTH_TIME_DICOVERY_120_SEC = 120;
    public static final int BLUETOOTH_TIME_DICOVERY_300_SEC = 300;
    public static final int BLUETOOTH_TIME_DICOVERY_600_SEC = 600;
    public static final int BLUETOOTH_TIME_DICOVERY_900_SEC = 900;
    public static final int BLUETOOTH_TIME_DICOVERY_1200_SEC = 1200;
    public static final int BLUETOOTH_TIME_DICOVERY_3600_SEC = 3600;
    public static int BLUETOOTH_REQUEST_ACCEPTED;
    private static int BLUETOOTH_NBR_CLIENT_MAX = 7;
    public TypeBluetooth mType;
    public boolean isConnected;
    private Activity mActivity;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothClient mBluetoothClient;
    private ArrayList<String> mAddressListServerWaitingConnection;
    private HashMap<String, BluetoothServer> mServerWaitingConnectionList;
    private ArrayList<BluetoothServer> mServerConnectedList;
    private HashMap<String, Thread> mServerThreadList;
    private int mNbrClientConnection;
    private int mTimeDiscoverable;
    private boolean mBluetoothIsEnableOnStart;
    private String mBluetoothNameSaved;
    private String gameName;
    private String playerName;

    public BluetoothManager(Activity activity) {
        mActivity = activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothNameSaved = mBluetoothAdapter.getName();
        mBluetoothIsEnableOnStart = mBluetoothAdapter.isEnabled();
        mType = TypeBluetooth.None;
        isConnected = false;
        mNbrClientConnection = 0;
        mAddressListServerWaitingConnection = new ArrayList<String>();
        mServerWaitingConnectionList = new HashMap<String, BluetoothServer>();
        mServerConnectedList = new ArrayList<BluetoothServer>();
        mServerThreadList = new HashMap<String, Thread>();
        //setTimeDiscoverable(BLUETOOTH_TIME_DICOVERY_300_SEC);
    }

    public void selectServerMode() {
        startDiscovery();
        mType = TypeBluetooth.Server;
        updateName();
    }

    public void selectClientMode() {
        startDiscovery();
        mType = TypeBluetooth.Client;
        updateName();
    }

    public void updateName(){
        mBluetoothAdapter.setName(getGameName() + " : " + getPlayerName());
    }


    public String getYourBtMacAddress() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.getAddress();
        }
        return null;
    }

    public int getNbrClientMax() {
        return BLUETOOTH_NBR_CLIENT_MAX;
    }

    public void setNbrClientMax(int nbrClientMax) {
        if (nbrClientMax <= BLUETOOTH_NBR_CLIENT_MAX) {
            BLUETOOTH_NBR_CLIENT_MAX = nbrClientMax;
        }
    }

    public boolean isNbrMaxReached() {
        return mNbrClientConnection == getNbrClientMax();
    }

    public void setServerWaitingConnection(String address, BluetoothServer bluetoothServer, Thread threadServer) {
        mAddressListServerWaitingConnection.add(address);
        mServerWaitingConnectionList.put(address, bluetoothServer);
        mServerThreadList.put(address, threadServer);
    }

    public void incrementNbrConnection() {
        mNbrClientConnection = mNbrClientConnection + 1;
        //setServerBluetoothName();
        if (mNbrClientConnection == getNbrClientMax()) {
            //resetWaitingThreadServer();
        }
        Log.e("", "===> incrementNbrConnection mNbrClientConnection : " + mNbrClientConnection);
    }

    private void resetWaitingThreadServer() {
        for (Map.Entry<String, Thread> bluetoothThreadServerMap : mServerThreadList.entrySet()) {
            if (mAddressListServerWaitingConnection.contains(bluetoothThreadServerMap.getKey())) {
                Log.e("", "===> resetWaitingThreadServer Thread : " + bluetoothThreadServerMap.getKey());
                bluetoothThreadServerMap.getValue().interrupt();
            }
        }
        for (Map.Entry<String, BluetoothServer> bluetoothServerMap : mServerWaitingConnectionList.entrySet()) {
            Log.e("", "===> resetWaitingThreadServer BluetoothServer : " + bluetoothServerMap.getKey());
            bluetoothServerMap.getValue().closeConnection();
            //mServerThreadList.remove(bluetoothServerMap.getKey());
        }
        mAddressListServerWaitingConnection.clear();
        mServerWaitingConnectionList.clear();
    }

    public void decrementNbrConnection() {
        if (mNbrClientConnection == 0) {
            return;
        }
        mNbrClientConnection = mNbrClientConnection - 1;
        if (mNbrClientConnection == 0) {
            isConnected = false;
        }
        Log.e("", "===> decrementNbrConnection mNbrClientConnection : " + mNbrClientConnection);
        //setServerBluetoothName();
    }

    public void setTimeDiscoverable(int timeInSec) {
        mTimeDiscoverable = timeInSec;
        BLUETOOTH_REQUEST_ACCEPTED = mTimeDiscoverable;
    }

    public boolean checkBluetoothAvailability() {
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            return true;
        }
    }

    public void cancelDiscovery() {
        if (isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    public boolean isDiscovering() {
        return mBluetoothAdapter.isDiscovering();
    }

    public void startDiscovery() {
        if (mBluetoothAdapter == null) {
            return;
        } else {
            if (mBluetoothAdapter.isEnabled() && isDiscovering()) {
                Log.e("", "===> mBluetoothAdapter.isDiscovering()");
                return;
            } else {
                Log.e("", "===> startDiscovery");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, mTimeDiscoverable);
                mActivity.startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE_CODE);
            }
        }
    }

    public void scanAllBluetoothDevice() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mActivity.registerReceiver(this, intentFilter);
        mBluetoothAdapter.startDiscovery();
    }

    public void createClient(String addressMac) {
        if (mType == TypeBluetooth.Client) {
            IntentFilter bondStateIntent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mActivity.registerReceiver(this, bondStateIntent);
            mBluetoothClient = new BluetoothClient(mBluetoothAdapter, addressMac);
            new Thread(mBluetoothClient).start();
        }
    }

    public void createServer(String address) {
        if (mType == TypeBluetooth.Server && !mAddressListServerWaitingConnection.contains(address)) {
            BluetoothServer mBluetoothServer = new BluetoothServer(mBluetoothAdapter, address);
            Thread threadServer = new Thread(mBluetoothServer);
            threadServer.start();
            setServerWaitingConnection(address, mBluetoothServer, threadServer);
            IntentFilter bondStateIntent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mActivity.registerReceiver(this, bondStateIntent);
            Log.e("", "===> createServer address : " + address);
        }
    }

    public void onServerConnectionSuccess(String addressClientConnected) {
        for (Map.Entry<String, BluetoothServer> bluetoothServerMap : mServerWaitingConnectionList.entrySet()) {
            if (addressClientConnected.equals(bluetoothServerMap.getValue().getClientAddress())) {
                mServerConnectedList.add(bluetoothServerMap.getValue());
                incrementNbrConnection();
                Log.e("", "===> onServerConnectionSuccess address : " + addressClientConnected);
                return;
            }
        }
    }

    public void onServerConnectionFailed(String addressClientConnectionFailed) {
        int index = 0;
        for (BluetoothServer bluetoothServer : mServerConnectedList) {
            if (addressClientConnectionFailed.equals(bluetoothServer.getClientAddress())) {
                mServerConnectedList.get(index).closeConnection();
                mServerConnectedList.remove(index);
                mServerWaitingConnectionList.get(addressClientConnectionFailed).closeConnection();
                mServerWaitingConnectionList.remove(addressClientConnectionFailed);
                mServerThreadList.get(addressClientConnectionFailed).interrupt();
                mServerThreadList.remove(addressClientConnectionFailed);
                mAddressListServerWaitingConnection.remove(addressClientConnectionFailed);
                decrementNbrConnection();
                Log.e("", "===> onServerConnectionFailed address : " + addressClientConnectionFailed);
                return;
            }
            index++;
        }
    }

    public void sendMessage(String message) {
        if (mType != null && isConnected) {
            if (mServerConnectedList != null) {
                for (int i = 0; i < mServerConnectedList.size(); i++) {
                    mServerConnectedList.get(i).write(message);
                }
            }
            if (mBluetoothClient != null) {
                mBluetoothClient.write(message);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
            EventBus.getDefault().post(device);
            /*if ((mType == TypeBluetooth.Client && !isConnected)
                    || (mType == TypeBluetooth.Server && !mAddressListServerWaitingConnection.contains(device.getAddress()))) {

                EventBus.getDefault().post(device);
            }*/
        }
        if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            //Log.e("", "===> ACTION_BOND_STATE_CHANGED");
            int prevBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
            int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
            if (prevBondState == BluetoothDevice.BOND_BONDING) {
                // check for both BONDED and NONE here because in some error cases the bonding fails and we need to fail gracefully.
                if (bondState == BluetoothDevice.BOND_BONDED || bondState == BluetoothDevice.BOND_NONE) {
                    //Log.e("", "===> BluetoothDevice.BOND_BONDED");
                    EventBus.getDefault().post(new BondedDevice());
                }
            }
        }
    }

    public void disconnectClient() {
        mType = TypeBluetooth.None;
        cancelDiscovery();
        resetClient();
    }

    public void disconnectServer() {
        mType = TypeBluetooth.None;
        cancelDiscovery();
        resetServer();
    }

    public void resetServer() {
        if (mServerConnectedList != null) {
            for (int i = 0; i < mServerConnectedList.size(); i++) {
                mServerConnectedList.get(i).closeConnection();
            }
        }
        mServerConnectedList.clear();
    }

    public void resetClient() {
        if (mBluetoothClient != null) {
            mBluetoothClient.closeConnexion();
            mBluetoothClient = null;
        }
    }

    public void closeAllConnection() {
        mBluetoothAdapter.setName(mBluetoothNameSaved);

        try {
            mActivity.unregisterReceiver(this);
        } catch (Exception e) {
        }

        cancelDiscovery();

        if (!mBluetoothIsEnableOnStart) {
            mBluetoothAdapter.disable();
        }

        mBluetoothAdapter = null;

        if (mType != null) {
            resetServer();
            resetClient();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public enum TypeBluetooth {
        Client,
        Server,
        None;
    }
}
