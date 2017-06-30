package com.example.earthshaker.documentaccessapp;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Application {

    public static FirebaseDatabase db;
    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();

    }
}
