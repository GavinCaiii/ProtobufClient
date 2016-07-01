package com.caitou.protobufdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


        clr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rev_et.setText("");
            }
        });
    }
}
