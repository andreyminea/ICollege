package com.example.icollege.RecyclerFiles;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.icollege.Main.SeeDownloadActivity;
import com.example.icollege.Models.Post;
import com.example.icollege.R;
import com.example.icollege.Utilities.Debug;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostHolder>
{
    Context context;
    ArrayList<Post> posts;

    public PostAdapter(android.content.Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, null);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int pos) {
        Glide.with(context)
                .load(posts.get(pos).userImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userImage);
        holder.userName.setText(posts.get(pos).name);
        holder.className.setText(posts.get(pos).className);
        holder.userPost.setText(posts.get(pos).notes);
        if(!posts.get(pos).documentUrl.isEmpty())
        {
            holder.downloadBtn.setVisibility(View.VISIBLE);
            holder.downloadBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference()
                            .child(posts.get(pos).documentUrl);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Intent intent = new Intent(context, SeeDownloadActivity.class);
                            intent.putExtra("Link",uri.toString());
                            context.startActivity(intent);
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
