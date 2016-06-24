package com.caitou.client;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caitou.socket.SocketClient;
import com.caitou.socket.TransBean;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText ip_et;
    private EditText data_et;
    private EditText received_et;
    private Button connect_btn;
    private Button disconnect_btn;
    private Button send_btn;

    private SocketClient client;
    private String ipAdd;
    private static final int PORT = 8888;

    TransBean bean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip_et = (EditText) findViewById(R.id.ip);
        data_et = (EditText) findViewById(R.id.send_data_area);
        received_et = (EditText) findViewById(R.id.received_area);
        connect_btn = (Button) findViewById(R.id.connect);
        disconnect_btn = (Button) findViewById(R.id.disconnect);
        send_btn = (Button) findViewById(R.id.send);

        connect_btn.setOnClickListener(this);
        send_btn.setOnClickListener(this);

        send_btn.setClickable(false);
        disconnect_btn.setClickable(false);

        received_et.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connect:
                //获取socket ip地址
                ipAdd = ip_et.getText().toString();
                if (ipAdd.equals("")){
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error!")
                            .setMessage("请输入ip地址")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "== ip地址错误 ==", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                } else {
                    try {
                        client = new SocketClient(ipAdd, PORT);
                        if (client != null){
                            //连接成功
                            Toast.makeText(MainActivity.this, "== 连接成功 ==", Toast.LENGTH_SHORT).show();
                            connect_btn.setClickable(false);
                            disconnect_btn.setClickable(true);
                            send_btn.setClickable(true);
                        } else {
                            //连接失败
                            Toast.makeText(MainActivity.this, "== 连接失败 ==", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.disconnect:
                client.closeSocket();
                Toast.makeText(MainActivity.this, "== 断开连接 ==", Toast.LENGTH_SHORT).show();
                connect_btn.setClickable(true);
                disconnect_btn.setClickable(false);
                send_btn.setClickable(false);
                break;

            case R.id.send:
                String data = data_et.getText().toString();
                if (data.equals("")){
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error!")
                            .setMessage("发送数据为空，请输入发送数据")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "== 数据错误 ==", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    bean.data = data.getBytes();
                    client.sendCommand(bean);
                }
                break;
        }
    }
}
