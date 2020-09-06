package com.example.icollege.Start.UserDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.icollege.Main.TeacherMainActivity;
import com.example.icollege.R;
import com.example.icollege.Models.Teacher;
import com.example.icollege.Utilities.CallBack;
import com.example.icollege.Utilities.Constants;
import com.example.icollege.Utilities.Debug;
import com.example.icollege.Utilities.FirebaseReference;
import com.example.icollege.Utilities.FirebaseUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NewTeacherActivity extends AppCompatActivity {

    Teacher mTeacher;
    ChipGroup chipGroup;
    MaterialAutoCompleteTextView group;
    MaterialAutoCompleteTextView year;
    MaterialButton addBtn;
    MaterialButton finishBtn;
    TextInputEditText name;
    private ArrayAdapter<String> adapterYear;
    private String[] Years = new String[] {"Year 1","Year 2","Year 3","Year 4"};
    private ArrayAdapter<String> adapterGroup;
    private String[] Groups = new String[] {"A","B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_teacher);
        Debug.This("New Teacher Activity");

        mTeacher = new Teacher(getSharedPreferences(Constants.PrefsName, MODE_PRIVATE));

        group = (MaterialAutoCompleteTextView) findViewById(R.id.classLetter);
        year = (MaterialAutoCompleteTextView) findViewById(R.id.classYear);
        chipGroup = (ChipGroup) findViewById(R.id.chipGroup);
        addBtn = (MaterialButton) findViewById(R.id.addButton);
        finishBtn = (MaterialButton) findViewById(R.id.teacherFinishBtn);
        name = (TextInputEditText) findViewById(R.id.className);

        adapterYear = new ArrayAdapter<>(
                getApplicationContext(), R.layout.dropdawn_item, Years);
        adapterGroup = new ArrayAdapter<>(
                getApplicationContext(),R.layout.dropdawn_item, Groups);
        group.setAdapter(adapterGroup);
        year.setAdapter(adapterYear);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckFields())
                {
                    SaveFields();
                    Chip mChip = new Chip(chipGroup.getContext());
                    mChip.setText(GetNewClass());
                    chipGroup.addView(mChip);
                    ClearFields();
                }
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitToFirebase();
                GotoMainActivity();
            }
        });
    }

    private void GotoMainActivity()
    {
        Intent intent = new Intent(this, TeacherMainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void SubmitToFirebase()
    {
        DocumentReference docRef = FirebaseReference.getInstance()
                .collection("Users").document("Data")
                .collection("Teachers").document();

        FirebaseUtility.Insert(docRef, mTeacher, new CallBack() {
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

    private void SaveFields()
    {
        mTeacher.classesGroups.add(group.getText().toString());
        mTeacher.classesYears.add(year.getText().toString());
        mTeacher.classesNames.add(name.getText().toString());
    }

    private void ClearFields()
    {
        group.setText("");
        year.setText("");
        name.setText("");
    }

    private String GetNewClass()
    {
        String newClass = "";
        newClass += name.getText().toString()+ " ";
        newClass += year.getText().toString()+ " ";
        newClass += group.getText().toString();
        return newClass;
    }

    private boolean CheckFields()
    {
        if(group.getText().toString().isEmpty())
            return false;
        if(year.getText().toString().isEmpty())
            return false;
        if(name.getText().toString().isEmpty())
            return false;
        return true;
    }
}