package com.example.earthshaker.documentaccessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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

        try {
            //create file to store image in local storage
            final File localFile = File.createTempFile("images", "png");
            //get file helps return local file path and its uri can be used to share the file
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    progressBar.setVisibility(View.GONE);
                    clickTv.setVisibility(View.VISIBLE);

                    final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //path of bitmap to convert it to uri
                            String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                            Uri bmpUri = Uri.parse(pathofBmp);
                            //send uri as intent in mail
                            final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            emailIntent1.setType("image/png");
                            emailIntent1.putExtra(android.content.Intent.EXTRA_EMAIL, "prashast.rastogi@gmail.com");
                            emailIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "Firebase Image");
                            emailIntent1.putExtra(android.content.Intent.EXTRA_TEXT, "view image in attachment");
                            startActivity(Intent.createChooser(emailIntent1, "Send mail..."));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
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
