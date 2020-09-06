package com.example.icollege.Start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.icollege.Main.StudentMainActivity;
import com.example.icollege.Main.TeacherMainActivity;
import com.example.icollege.R;
import com.example.icollege.Utilities.CallBack;
import com.example.icollege.Utilities.Constants;
import com.example.icollege.Utilities.Debug;
import com.example.icollege.Utilities.FirebaseReference;
import com.example.icollege.Utilities.FirebaseUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SecurityCheckActivity extends AppCompatActivity implements CallBack {

    private DocumentReference studentRef = FirebaseReference.getInstance().
            collection("Security").document("Student");
    private DocumentReference teacherRef = FirebaseReference.getInstance().
            collection("Security").document("Teacher");
    private String codeStudent;
    private String codeTeacher;
    MaterialButton checkBtn;
    TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);

        CheckUserLogStatus();

        FirebaseUtility.ReadDoc(studentRef, this);
        FirebaseUtility.ReadDoc(teacherRef, this);

        checkBtn = (MaterialButton) findViewById(R.id.checkCodeBtn);
        editText = (TextInputEditText) findViewById(R.id.textInputField);

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userCode = editText.getText().toString();
                if(userCode.equals(codeStudent))
                {
                    Debug.ToastLong(getApplicationContext(),"Student");
                    SaveLocally("student");
                    GotoLoginActivity();
                    return;
                }
                if(userCode.equals(codeTeacher))
                {
                    Debug.ToastLong(getApplicationContext(),"Teacher");
                    SaveLocally("teacher");
                    GotoLoginActivity();
                    return;
                }
                Debug.ToastLong(getApplicationContext(),"Code is invalid!\nTry again");
            }
        });
    }

    private void GotoLoginActivity()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private String GetUserType()
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        return sp.getString(Constants.PrefsUserType,"");
    }

    private void GotoMainActivity()
    {
        Intent intent;
        if(GetUserType().equals("teacher"))
        {
            intent = new Intent(getApplicationContext(), TeacherMainActivity.class);
        }
        else
        {
            intent = new Intent(getApplicationContext(), StudentMainActivity.class);
        }
        startActivity(intent);
        this.finish();
    }

    private void CheckUserLogStatus()
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        boolean isLogged = sp.getBoolean(Constants.PrefsLog, false);
        String type = sp.getString(Constants.PrefsUserType, "");
        if(isLogged)
        {
            GotoMainActivity();
        }
    }
    private void SaveLocally(String value)
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.PrefsUserType, value);
        editor.apply();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onSuccess(DocumentSnapshot snapshot) {
        if(snapshot.getReference().equals(studentRef))
        {
            codeStudent = snapshot.getString("code");
        }
        if(snapshot.getReference().equals(teacherRef))
        {
            codeTeacher = snapshot.getString("code");
        }
    }

    @Override
    public void onSuccess(QuerySnapshot snapshot) {

    }

    @Override
    public void onError(Object object) {

    }
}