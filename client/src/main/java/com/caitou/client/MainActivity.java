package com.caitou.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caitou.socket.ObjTransferService;
import com.caitou.socket.TransBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText ip_et;
    private EditText data_et;
    private EditText received_et;
    private Button send_btn;
    private Button send_pro_int;
    private Button send_pro_str;

    private static final int PORT = 8888;

    TransBean bean = new TransBean();


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

        send_btn.setOnClickListener(this);
        send_pro_int.setOnClickListener(this);
        send_pro_str.setOnClickListener(this);

        received_et.setEnabled(false);

    }

    private void sendCommand(String ip, TransBean command){
        Intent serviceIntent = new Intent(this, ObjTransferService.class);
        serviceIntent.setAction(ObjTransferService.ACTION_SEND_FILE);
        serviceIntent.putExtra(ObjTransferService.EXTRAS_OBJECT, command);
        serviceIntent.putExtra(ObjTransferService.EXTRAS_GROUP_OWNER_ADDRESS, ip);
        serviceIntent.putExtra(ObjTransferService.EXTRAS_GROUP_OWNER_PORT, PORT);
        startService(serviceIntent);
    }

    @Override
    public void onClick(View view) {
        String ipAdd = ip_et.getText().toString();
        switch (view.getId()){
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
                    return;
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
                    bean.data = data.getBytes();
                    sendCommand(ipAdd, bean);
                }
            case R.id.send_protobuf_int:
                if (ipAdd.equals(""))
                    Toast.makeText(this, "请正确的输入ip地址", Toast.LENGTH_SHORT).show();
                else {

                }
                break;
            case R.id.send_protobuf_str:
                break;
        }
    }
}
