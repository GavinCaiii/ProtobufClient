// Copyright 2011 Google Inc. All Rights Reserved.

package com.caitou.socket;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;


/**
 * A service that process each file transfer request i.e Intent by opening a
 * socket connection with the WiFi Direct Group Owner and writing the file
 */
public class ObjTransferService extends IntentService {

    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE = "com.caitou.socket.SEND_FILE";
    public static final String EXTRAS_OBJECT = "data";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";

    public ObjTransferService(String name) {
        super(name);
    }

    public ObjTransferService() {
        super("ObjTransferService");
    }

    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_SEND_FILE)) {
            TransBean command = (TransBean) intent.getExtras().getSerializable(EXTRAS_OBJECT);
            String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
            int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

            try {
                SocketClient client = new SocketClient(host, port);
                client.sendCommand(command);
                client.closeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}