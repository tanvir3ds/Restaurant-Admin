package com.example.restadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progress= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {

                doWork();
                startApp();
            }
        });
        thread.start();

    }
    public void doWork(){



        for (progress=1; progress<=10; progress=progress+1){
            try {
                Thread.sleep(90);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void startApp(){

        Intent intent = new Intent(MainActivity.this, Admin_Login.class);
        startActivity(intent);
        finish();
    }
}
