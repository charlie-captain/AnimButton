package com.example.thatnight.animbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.animbutton.AnimButton;


public class MainActivity extends AppCompatActivity {

    private AnimButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (AnimButton) findViewById(R.id.rl);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButton.startAnimation();
                mButton.errorAnimation();
            }
        });
    }
}
