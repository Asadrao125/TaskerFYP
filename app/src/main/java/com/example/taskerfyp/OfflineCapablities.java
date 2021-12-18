package com.example.taskerfyp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineCapablities extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
