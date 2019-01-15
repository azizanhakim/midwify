package com.azizanhakim.midwife.Service;

import android.app.Application;

import com.firebase.client.Firebase;

public class Fbase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }

}
