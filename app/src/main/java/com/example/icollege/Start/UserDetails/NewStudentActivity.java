package com.example.icollege.Start.UserDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.icollege.Main.StudentMainActivity;
import com.example.icollege.R;
import com.example.icollege.Models.Student;
import com.example.icollege.Utilities.CallBack;
import com.example.icollege.Utilities.Constants;
import com.example.icollege.Utilities.Debug;
import com.example.icollege.Utilities.FirebaseReference;
import com.example.icollege.Utilities.FirebaseUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NewStudentActivity extends AppCompatActivity {

    MaterialAutoCompleteTextView group;
    MaterialAutoCompleteTextView year;
    MaterialButton btn;
    private ArrayAdapter<String> adapterYear;
    private String[] Years = new String[] {"Year 1","Year 2","Year 3","Year 4"};
    private ArrayAdapter<String> adapterGroup;
    private String[] Groups = new String[] {"A","B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);
        Debug.This("New Student Activity");

        group = (MaterialAutoCompleteTextView) findViewById(R.id.exposedDropdownLetter);
        year = (MaterialAutoCompleteTextView) findViewById(R.id.exposedDropdownNumber);
        btn = (MaterialButton) findViewById(R.id.studentDoneBtn);

        adapterYear = new ArrayAdapter<>(
                getApplicationContext(), R.layout.dropdawn_item, Years);
        adapterGroup = new ArrayAdapter<>(
                getApplicationContext(),R.layout.dropdawn_item, Groups);
        group.setAdapter(adapterGroup);
        year.setAdapter(adapterYear);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnButtonClicked();
            }
        });
    }

    private void OnButtonClicked()
    {
        if(CheckIfUserSelected())
        {
            SaveDataToSharedPrefs();
            CreateUser();
            GotoMainActivity();
        }
    }

    private void GotoMainActivity()
    {
        Intent intent = new Intent(this, StudentMainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void CreateUser()
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        Student newStudent = new Student(sp);
        DocumentReference docRef = FirebaseReference.getInstance()
                .collection("Users").document("Data")
                .collection("Students").document();
        FirebaseUtility.Insert(docRef, newStudent, new CallBack() {
            @Override
            public void onSuccess() {
                Debug.This("Data send");
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

    private void SaveDataToSharedPrefs()
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.PrefsUserYear, year.getText().toString());
        editor.putString(Constants.PrefsUserGroup, group.getText().toString());
        editor.putBoolean(Constants.PrefsLog, true);
        editor.apply();
    }

    private boolean CheckIfUserSelected()
    {
        if(group.getText().toString().isEmpty() || year.getText().toString().isEmpty())
        {
            return false;
        }
        return true;
    }
}