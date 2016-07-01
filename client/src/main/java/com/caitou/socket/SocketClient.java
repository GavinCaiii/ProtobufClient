package com.caitou.socket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Socket client, could be used for search and connect to a socket server and transfer a name card to the server.
 *
 * @author GuangzhaoCai
 * @since 2016-06-24
 */
public class SocketClient {
    private static final String TAG = "SocketClient";
    private Socket client = null;

    public SocketClient(String site, int port) throws IOException {
        client = new Socket(site, port);
        Log.d(TAG, "Socket client created! site = " + site + ", port = " + port);
    }

    /**
     * Send a msg to the server.
     *
     * @param msg
     */
    public String sendMsg(String msg) {
        String result = null;
        try {
            InputStream is = client.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            OutputStream os = client.getOutputStream();
            PrintWriter out = new PrintWriter(os);
            out.println(msg);
            out.flush();
            result = in.readLine();
            is.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void sendCommand(TransBean command) {
        try {
            if (client == null)
                return;
            OutputStream os = client.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(command);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the opened socket.
     */
    public void closeSocket() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
