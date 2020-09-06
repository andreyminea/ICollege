package com.example.icollege.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.icollege.Models.Post;
import com.example.icollege.R;
import com.example.icollege.RecyclerFiles.PostAdapter;
import com.example.icollege.Utilities.CallBack;
import com.example.icollege.Utilities.Constants;
import com.example.icollege.Utilities.Debug;
import com.example.icollege.Utilities.FirebaseReference;
import com.example.icollege.Utilities.FirebaseUtility;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentMainActivity extends AppCompatActivity {

    ImageView image;
    TextView userName;
    MaterialButton scheduleBtn;
    RecyclerView postsRecycler;
    PostAdapter postAdapter;
    ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        Debug.This("Student Activity");

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(this, posts);
        image = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
        scheduleBtn = (MaterialButton) findViewById(R.id.scheduleBtn);
        postsRecycler = (RecyclerView) findViewById(R.id.postsRecycler);
        String userImageUrl = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE)
                .getString(Constants.PrefsUserImageUrl,"");
        String userNameText = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE)
                .getString(Constants.PrefsUserName,"");
        Glide.with(this)
                .load(userImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(image);
        userName.setText(userNameText);

        postsRecycler.setLayoutManager(new LinearLayoutManager(this));
        GetPosts();

        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
                startActivity(intent);
            }
        });
    }

    public void GetPosts()
    {
        String group = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE)
                .getString(Constants.PrefsUserGroup, "");
        String year = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE)
                .getString(Constants.PrefsUserYear, "");

        CollectionReference ref = FirebaseReference.getInstance()
                .collection("Classes").document(year)
                .collection(group);
        Query query = ref.orderBy("className", Query.Direction.ASCENDING);
        FirebaseUtility.Query(query, new CallBack() {
            @Override
            public void onSuccess() {
                Debug.This("Error: Posts not downloaded");
            }

            @Override
            public void onSuccess(DocumentSnapshot snapshot) {

            }

            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                Debug.This("Query received");
                for(DocumentSnapshot doc : snapshot)
                {
                    Post post = doc.toObject(Post.class);
                    Debug.This(post.className);
                    posts.add(post);
                    RefreshAdapter();
                }
            }

            @Override
            public void onError(Object object) {
                Debug.This("There was an error");
            }
        });
    }

    private void RefreshAdapter()
    {
        postAdapter = new PostAdapter(this, posts);
        postsRecycler.setAdapter(postAdapter);
    }
}