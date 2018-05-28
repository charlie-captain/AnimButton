package com.example.thatnight.animbutton;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.animbutton.AnimButton;


public class MainActivity extends AppCompatActivity {

    private AnimButton mButton;
    private Button mBtnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (AnimButton) findViewById(R.id.rl);
        mBtnChange = (Button) findViewById(R.id.btn_change);

        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setNormalColor(Color.GREEN);
                mButton.setPressedColor(Color.BLUE);
                mButton.setPressedColor(Color.BLACK);
                mButton.setDuration(3000);
                mButton.setStartText("改变了");
                mButton.setEndText("改变错误");
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButton.errorAnimation();
            }
        });
    }
}
