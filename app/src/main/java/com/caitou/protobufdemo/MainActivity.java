package com.caitou.protobufdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.caitou.protobufdemo.utils.HexDump;
import com.caitou.socket.ProtobufService;
import com.caitou.socket.SocketServer;
import com.caitou.socket.TransBean;

public class MainActivity extends AppCompatActivity {

    private Button clr_btn;
    private EditText rev_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clr_btn = (Button) findViewById(R.id.clear);
        rev_et = (EditText) findViewById(R.id.received);

        rev_et.setEnabled(false);

        SocketServer.getInstance().startToListen(this, new ProtobufService.DataReceived() {
            @Override
            public void onReceived(TransBean bean) {
                //received the data
//                String data = HexDump.dumpHexString(bean.data);
                String data = bean.toString();
                rev_et.append(data);
                rev_et.append("\n");
            }
        });

        clr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rev_et.setText("");
            }
        });
    }
}
