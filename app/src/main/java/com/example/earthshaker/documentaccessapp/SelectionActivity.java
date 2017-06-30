package com.example.earthshaker.documentaccessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.earthshaker.documentaccessapp.MainActivity.db;

/**
 * Created by earthshaker on 29/6/17.
 */

public class SelectionActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private static HashMap<String, String> userData;
    private Button noAuth, withAuth, authentication;
    private EditText userId;
    boolean flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVies();
        setListeners();
        getDataFromDb();
    }

    private void initializeVies() {
        noAuth = (Button) findViewById(R.id.withoutAuth);
        withAuth = (Button) findViewById(R.id.withAuth);
        authentication = (Button) findViewById(R.id.authenticate);
        userId = (EditText) findViewById(R.id.searchMail);
    }

    private void getDataFromDb() {
        ref = db.getReference("users"); // Key

        // Attach listener
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
                try {
                    userData = (HashMap<String, String>) dataSnapshot.getValue();
                } catch (Exception e) {
                    FirebaseCrash.log(getLocalClassName() + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setListeners() {
        withAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authentication.setVisibility(View.VISIBLE);
                userId.setVisibility(View.VISIBLE);
            }
        });

        authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key : userData.keySet()) {
                    if (userId.getText().toString().equals(key)) {
                        openStorageViewer("Internal/books.png");
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    Toast.makeText(getApplicationContext(), "User Authenticated Successfully", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "You are not authenticated.", Toast.LENGTH_SHORT).show();

            }
        });

        noAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStorageViewer("External/podcast.png");
            }
        });
    }

    public void openStorageViewer(String url) {
        Intent intent = new Intent(SelectionActivity.this, StorageViewerActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

}
