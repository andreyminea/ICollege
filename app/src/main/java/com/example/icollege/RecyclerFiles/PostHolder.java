package com.example.icollege.RecyclerFiles;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icollege.R;
import com.google.android.material.button.MaterialButton;

public class PostHolder extends RecyclerView.ViewHolder
{
    ImageView userImage;
    TextView userName;
    TextView className;
    TextView userPost;
    MaterialButton downloadBtn;

    public PostHolder(@NonNull View itemView) {
        super(itemView);
        this.userImage = itemView.findViewById(R.id.cardUserImage);
        this.userName = itemView.findViewById(R.id.cardUserName);
        this.userPost = itemView.findViewById(R.id.cardPost);
        this.className = itemView.findViewById(R.id.cardClassName);
        this.downloadBtn = itemView.findViewById(R.id.cardDownloadBtn);
    }
}
