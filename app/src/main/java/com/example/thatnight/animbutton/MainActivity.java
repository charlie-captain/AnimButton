package com.example.thatnight.animbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private AnimButton mButton;
    private ProgressBar mProgress;
    private boolean changeState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (AnimButton) findViewById(R.id.button);
        mProgress = (ProgressBar) findViewById(R.id.pb_button);
        mButton.setWrapper(mButton);
        mButton.setmProgress(mProgress);
        mButton.setmText("login");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (changeState) {
////                    mButton.endAnimation();
//                    mButton.errorAnimation(view);
//                    changeState = false;
//                } else {
                mButton.startAnimation();
//                    changeState = true;
//                }
//                mButton.errorAnimation();
            }
        });
    }
}
