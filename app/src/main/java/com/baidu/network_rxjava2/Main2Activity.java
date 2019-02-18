package com.baidu.network_rxjava2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView textView = findViewById(R.id.tv2);
        set(null);

    }


    public void set(TextView tv){
        tv.setText("xx");
    }


}
