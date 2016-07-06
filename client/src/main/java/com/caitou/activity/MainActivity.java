package com.caitou.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caitou.data.BaseFrame;
import com.caitou.data.IntRequestData;
import com.caitou.data.StrRequestData;
import com.caitou.protocol.Protocol;
import com.caitou.socket.SocketClient;
import com.caitou.socket.TransferService;
import com.google.protobuf.InvalidProtocolBufferException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText ip_et;
    private EditText data_et;
    private EditText received_et;
    private Button send_btn;
    private Button send_pro_int;
    private Button send_pro_str;
    private Button connect_btn;
    private Button clear_btn;
    private Button disconnect_btn;

    private Handler mHandler = new Handler();

    private static final int PORT = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip_et = (EditText) findViewById(R.id.ip);
        data_et = (EditText) findViewById(R.id.send_data_area);
        received_et = (EditText) findViewById(R.id.received_area);

        send_btn = (Button) findViewById(R.id.send);
        send_pro_int = (Button) findViewById(R.id.send_protobuf_int);
        send_pro_str = (Button) findViewById(R.id.send_protobuf_str);
        connect_btn = (Button) findViewById(R.id.connect);
        clear_btn = (Button) findViewById(R.id.clear);
        disconnect_btn = (Button) findViewById(R.id.disconnect);

        send_btn.setOnClickListener(this);
        send_pro_int.setOnClickListener(this);
        send_pro_str.setOnClickListener(this);
        connect_btn.setOnClickListener(this);
        clear_btn.setOnClickListener(this);
        disconnect_btn.setOnClickListener(this);

        received_et.setFocusable(false);
        received_et.setClickable(false);

        send_btn.setClickable(false);
        send_pro_int.setClickable(false);
        send_pro_str.setClickable(false);
        disconnect_btn.setClickable(false);
    }

    @Override
    public void onClick(View view) {
        String ipAdd = ip_et.getText().toString();
        switch (view.getId()){
            case R.id.connect:
                SocketClient.getInstance().startToListen(ipAdd, PORT, new TransferService.DataReceived() {
                    @Override
                    public void onReceived(byte[] data) {
                        try {
                            Protocol.Frame frame = Protocol.Frame.parseFrom(data);
                            int ctlCode = frame.getHeader().getControlCode();
                            final int funCode = frame.getHeader().getFunctionCode();
                            if (ctlCode == BaseFrame.CTRL_SERVER_TO_CLIENT){
                                if (funCode == BaseFrame.FUNC_INT){
                                    final int int32 = frame.getRequest().getIntRequest().getInt32Data();
                                    final long int64 = frame.getRequest().getIntRequest().getInt64Data();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    received_et.append("functionCode = " + funCode + "\n");
                                                    received_et.append("int32 = " + int32 + "; int64 = " + int64 + "\n");
                                                }
                                            });
                                        }
                                    }).start();
                                }
                                if (funCode == BaseFrame.FUNC_STRING) {
                                    final String str = frame.getRequest().getStringRequest().getStrData();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    received_et.append("functionCode = " + funCode + "\n");
                                                    received_et.append("strData =  " + str + "\n");
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            }

                        } catch (InvalidProtocolBufferException e) {
                            e.printStackTrace();
                        }
                    }
                });

                send_btn.setClickable(true);
                send_pro_int.setClickable(true);
                send_pro_str.setClickable(true);
                disconnect_btn.setClickable(true);
                connect_btn.setClickable(false);
                break;
            case R.id.send:
                String data = data_et.getText().toString();
                if (ipAdd.equals("")){
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error!")
                            .setMessage("请输入ip地址")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "== IP地址出错 ==", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    break;
                }
                if (data.equals("")){
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error!")
                            .setMessage("发送数据为空，请输入发送数据")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "== 请填写发送的数据 ==", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                } else {
                    Protocol.Frame frame = StrRequestData.initData(data).toFrame();
                    SocketClient.getInstance().sendMsg(frame.toByteArray());
                }
                break;
            case R.id.send_protobuf_int:
                if (ipAdd.equals(""))
                    Toast.makeText(this, "请正确的输入ip地址", Toast.LENGTH_SHORT).show();
                else {
                    Protocol.Frame frame = IntRequestData.create().toFrame();
                    SocketClient.getInstance().sendMsg(frame.toByteArray());
                }
                break;
            case R.id.send_protobuf_str:
                if (ipAdd.equals(""))
                    Toast.makeText(this, "请正确的输入ip地址", Toast.LENGTH_SHORT).show();
                else {
                    Protocol.Frame frame = StrRequestData.create().toFrame();
                    SocketClient.getInstance().sendMsg(frame.toByteArray());
                }
                break;
            case R.id.clear:
                received_et.setText("");
                break;
            case R.id.disconnect:
                SocketClient.getInstance().closeSocket();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                received_et.append("disconnect the server ...\n");
                            }
                        });
                    }
                }).start();
                disconnect_btn.setClickable(false);
                send_btn.setClickable(false);
                send_pro_int.setClickable(false);
                send_pro_str.setClickable(false);
                connect_btn.setClickable(true);
        }
    }
}
