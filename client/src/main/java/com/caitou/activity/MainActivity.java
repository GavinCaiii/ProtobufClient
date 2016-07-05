package com.caitou.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caitou.data.IntRequestData;
import com.caitou.data.StrRequestData;
import com.caitou.protocol.Protocol;
import com.caitou.socket.ObjTransferService;
import com.caitou.socket.TransBean;
import com.caitou.utils.HexDump;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText ip_et;
    private EditText data_et;
    private EditText received_et;
    private Button send_btn;
    private Button send_pro_int;
    private Button send_pro_str;

    private static final int PORT = 8888;

    private static final String IP = "10.1.1.84";

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

        ip_et.setText(IP);

        received_et.setEnabled(false);

    }

    private void sendCommand(String ip, byte[] data){
        Intent serviceIntent = new Intent(this, ObjTransferService.class);
        serviceIntent.setAction(ObjTransferService.ACTION_SEND_FILE);
        serviceIntent.putExtra(ObjTransferService.EXTRAS_OBJECT, data);
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
                    sendCommand(ipAdd, frame.toByteArray());
                }
                break;
            case R.id.send_protobuf_int:
                if (ipAdd.equals(""))
                    Toast.makeText(this, "请正确的输入ip地址", Toast.LENGTH_SHORT).show();
                else {
                    Protocol.Frame frame = IntRequestData.create().toFrame();
                    String str = HexDump.dumpHexString(frame.toByteArray());
                    System.out.println("==================================================");
                    System.out.println(str);
                    sendCommand(ipAdd, frame.toByteArray());
                    System.out.println("==================================================");
                }
                break;
            case R.id.send_protobuf_str:
                if (ipAdd.equals(""))
                    Toast.makeText(this, "请正确的输入ip地址", Toast.LENGTH_SHORT).show();
                else {
                    Protocol.Frame frame = StrRequestData.create().toFrame();
                    String str = HexDump.dumpHexString(frame.toByteArray());
                    System.out.println("==================================================");
                    System.out.println(str);
                    sendCommand(ipAdd, frame.toByteArray());
                    System.out.println("==================================================");
                }
                break;
        }
    }
}
