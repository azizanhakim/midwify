package com.azizanhakim.midwife.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.azizanhakim.midwife.R;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread thread = new Thread() {
            public void run(){
                try{
                    sleep(1800);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}
