package com.example.earthshaker.documentaccessapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by earthshaker on 29/6/17.
 */

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.StorageAdapterHolder> {

    private List<String> fileUrls;
    private Context context;
    public StorageAdapter(Context context,List<String> fileUrls) {
        this.fileUrls = fileUrls;
        this.context = context;
    }


    @Override
    public StorageAdapter.StorageAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storage_viewer, parent, false);
        return new StorageAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(StorageAdapter.StorageAdapterHolder holder, int position) {
        String url = fileUrls.get(position);
        holder.setData(url);
    }

    @Override
    public int getItemCount() {
        return fileUrls.size();
    }

    public class StorageAdapterHolder extends RecyclerView.ViewHolder {

        private TextView urlTv;
        private LinearLayout share;

        public StorageAdapterHolder(View itemView) {
            super(itemView);
            urlTv = (TextView) itemView.findViewById(R.id.url);
            share = (LinearLayout) itemView.findViewById(R.id.share);
            setListener();
        }

        private void setListener() {
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(Intent.createChooser(
                            sendEMail("prashast.rastogi@gmail.com",
                                    "", ""), "Send email..."));
                }
            });
        }

        private void setData(String url) {
            urlTv.setText(url);
        }

        public Intent sendEMail(String receiver, String subject, String body) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            // emailIntent.setType("message/rfc822");
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[]{receiver});

            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    subject);

            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    body);


            return emailIntent;
        }

    }

}
