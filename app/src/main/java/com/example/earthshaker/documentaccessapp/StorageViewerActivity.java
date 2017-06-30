package com.example.earthshaker.documentaccessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by earthshaker on 29/6/17.
 */

public class StorageViewerActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView clickTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_viewer);
        initializeViews();
        fetchUrls();
    }

    private void fetchUrls() {
        String folderUrl = getIntent().getStringExtra("url");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://documentaccessapp.appspot.com").child(folderUrl);

        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("application/image");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "prashast.rastogi@gmail.com");
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Subject");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri.toString()));
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    }
                });
            }
        });
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                progressBar.setVisibility(View.GONE);
                clickTv.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);

            }
        });

    }

    private void initializeViews() {
        //shareableLinks = (RecyclerView) findViewById(R.id.shareable_links);
        imageView = (ImageView) findViewById(R.id.image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        clickTv = (TextView) findViewById(R.id.clickTv);
        progressBar.setVisibility(View.VISIBLE);
        clickTv.setVisibility(View.GONE);
    }
}
