package com.example.icollege.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.icollege.Models.Post;
import com.example.icollege.R;
import com.example.icollege.Models.Teacher;
import com.example.icollege.Utilities.CallBack;
import com.example.icollege.Utilities.Constants;
import com.example.icollege.Utilities.Debug;
import com.example.icollege.Utilities.FirebaseReference;
import com.example.icollege.Utilities.FirebaseUtility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class TeacherMainActivity extends AppCompatActivity {

    Post mPost;
    Teacher mTeacher;
    MaterialButton uploadBtn;
    MaterialButton postBtn;
    ImageView image;
    TextView userName;
    TextInputEditText notes;
    private ArrayAdapter<String> adapterClass;
    private MaterialAutoCompleteTextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        Debug.This("Teacher");
        mPost = new Post();

        image = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
        name = (MaterialAutoCompleteTextView) findViewById(R.id.className);
        notes = (TextInputEditText) findViewById(R.id.postNotes);
        uploadBtn = (MaterialButton) findViewById(R.id.uploadBtn);
        postBtn = (MaterialButton) findViewById(R.id.postBtn);

        CollectionReference docRef = FirebaseReference.getInstance()
                .collection("Users").document("Data")
                .collection("Teachers");
        String userEmail = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE)
                .getString(Constants.PrefsUserEmail,"");
        Query query = docRef.whereEqualTo("email", userEmail);

        FirebaseUtility.Query(query, new CallBack() {
            @Override
            public void onSuccess() {
                Debug.This("Error: No email found");
            }

            @Override
            public void onSuccess(DocumentSnapshot snapshot) {

            }

            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                for(DocumentSnapshot doc : snapshot)
                {
                    mTeacher = doc.toObject(Teacher.class);
                    break;
                }
                UpdateUI();
            }

            @Override
            public void onError(Object object) {

            }
        });

        ManageUploadButton();
        ManagePostButton();
    }

    private void ManagePostButton()
    {
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckIfNotEmpty())
                {
                    mPost.GetDataFromTeacher(mTeacher);
                    mPost.className = name.getText().toString();
                    mPost.notes = notes.getText().toString();
                    FirebaseUtility.Insert(GetReferenceFromClassName(mPost.className), mPost, new CallBack() {
                        @Override
                        public void onSuccess() {
                            Debug.ToastLong(getApplicationContext(),"Posted successfully");
                            ClearFields();
                        }

                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {

                        }

                        @Override
                        public void onSuccess(QuerySnapshot snapshot) {

                        }

                        @Override
                        public void onError(Object object) {

                        }
                    });
                }
            }
        });
    }

    private void ClearFields()
    {
        name.setText("");
        notes.setText("");
    }

    private DocumentReference GetReferenceFromClassName(String name)
    {
        String[] split = name.split("Year");
        String[] split2 = split[1].split(" ");
        String year = "Year "+split2[1];
        String group = split2[2];
        DocumentReference ref = FirebaseReference.getInstance()
                .collection("Classes").document(year)
                .collection(group).document();
       return ref;
    }

    private void ManageUploadButton()
    {
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri path = data.getData();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef.child("posts/"+ UUID.randomUUID().toString())
                    .putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Debug.ToastLong(getApplicationContext(), "File uploaded");
                    mPost.documentUrl = taskSnapshot.getMetadata().getPath();
                    Debug.This(mPost.documentUrl);
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    Debug.This("Progress " + (int)progress+"%");
                }
            });
        }
    }

    private boolean CheckIfNotEmpty()
    {
        if(name.getText().toString().isEmpty())
            return false;
        if(notes.getText().toString().isEmpty())
            return false;
        return true;
    }


    private void UpdateUI()
    {
        UpdateHeader();
        adapterClass = new ArrayAdapter<>(
                getApplicationContext(),R.layout.dropdawn_item, mTeacher.GetClasses());
        name.setAdapter(adapterClass);
    }

    private void UpdateHeader()
    {
        Glide.with(this)
                .load(mTeacher.imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(image);

        userName.setText(mTeacher.name);
    }
}