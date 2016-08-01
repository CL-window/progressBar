package com.cl.slack.sample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cl.slack.lib.ProgressbarView;

public class MainActivity extends AppCompatActivity {

    private ProgressbarView mProgressbarView;
    private int progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressbarView = ((ProgressbarView) findViewById(R.id.progressbar));
        mProgressbarView.setProgress(progress += 10);//  ,true);
        mProgressbarView.setProgress(progress += 10);//  ,true);
        mProgressbarView.setProgress(progress += 10);//  ,true);


    }

    public void delProgress(View view) {
        if(progress > 0){
            progress -= 10;
            mProgressbarView.removeProgress(progress);
        }
    }

    public void addProgress(View view) {
        if(progress < 100){
            mProgressbarView.setProgress(progress += 10);// ,true);
        }
    }
}
