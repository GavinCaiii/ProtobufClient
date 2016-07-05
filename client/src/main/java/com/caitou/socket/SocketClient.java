package com.caitou.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public void sendCommand(byte[] data) {
        try {
            if (client == null)
                return;
            OutputStream os = client.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
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

    private class ReadThread extends Thread{
        boolean isRunning = false;
        String ip = null;
        int port = 0;
        @Override
        public void run() {
            try {
                client = new Socket(ip, port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (isRunning){

            }
        }
    }
}
