package com.example.icollege.Start;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.icollege.Main.StudentMainActivity;
import com.example.icollege.Main.TeacherMainActivity;
import com.example.icollege.R;
import com.example.icollege.Start.UserDetails.NewStudentActivity;
import com.example.icollege.Start.UserDetails.NewTeacherActivity;
import com.example.icollege.Models.Student;
import com.example.icollege.Models.Teacher;
import com.example.icollege.Utilities.CallBack;
import com.example.icollege.Utilities.Constants;
import com.example.icollege.Utilities.Debug;
import com.example.icollege.Utilities.FirebaseReference;
import com.example.icollege.Utilities.FirebaseUtility;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private SignInButton signInButton;
    int RC = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Debug.This("Login Activity");

        CheckIfUserLogged();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.common_google_signin_btn_icon_dark) // replace with logo
                .setTosAndPrivacyPolicyUrls("https://example.com","https://example.com" )
                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .build();
        startActivityForResult(intent, RC);
    }

    private void CheckIfUserLogged()
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        Boolean isLogged = sp.getBoolean(Constants.PrefsLog, false);
        if(isLogged)
        {
            GotoMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC)
        {
            if(resultCode==RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                VerifyUser(user.getEmail(), user);
            }
            else
            {
                Debug.ToastLong(this, "Sign in failed");
            }
        }
    }

    private void VerifyUser(String email, final FirebaseUser user)
    {
        if(GetUserType().equals("student")) {
            CollectionReference ref = FirebaseReference.getInstance()
                    .collection("Users")
                    .document("Data")
                    .collection("Students");
            Query query = ref.whereEqualTo("email", email);
            FirebaseUtility.Query(query, new CallBack() {
                @Override
                public void onSuccess() {
                    //new user
                    SaveUserDetails(user);
                    GotoNewUserActivity();
                }

                @Override
                public void onSuccess(DocumentSnapshot snapshot) {

                }

                @Override
                public void onSuccess(QuerySnapshot snapshot) {
                    GetUserDetails(snapshot);
                    GotoMainActivity();
                    Debug.This("Old user");
                    //returning user
                }

                @Override
                public void onError(Object object) {

                }
            });
        }
        else
        {
            CollectionReference ref = FirebaseReference.getInstance()
                    .collection("Users")
                    .document("Data")
                    .collection("Teachers");
            Query query = ref.whereEqualTo("email", email);
            FirebaseUtility.Query(query, new CallBack() {
                @Override
                public void onSuccess() {
                    //new user
                    SaveUserDetails(user);
                    GotoNewUserActivity();
                }

                @Override
                public void onSuccess(DocumentSnapshot snapshot) {

                }

                @Override
                public void onSuccess(QuerySnapshot snapshot) {
                    GetUserDetails(snapshot);
                    GotoMainActivity();
                    Debug.This("Old user");
                    //returning user
                }

                @Override
                public void onError(Object object) {

                }
            });
        }
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

    private void GotoNewUserActivity()
    {
        Intent intent;
        if(GetUserType().equals("teacher"))
        {
            intent = new Intent(getApplicationContext(), NewTeacherActivity.class);
        }
        else
        {
            intent = new Intent(getApplicationContext(), NewStudentActivity.class);
        }
        startActivity(intent);
        this.finish();
    }

    private String GetUserType()
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        return sp.getString(Constants.PrefsUserType,"");
    }

    private void GetUserDetails(QuerySnapshot snapshot)
    {
        if(GetUserType().equals("student"))
        {
            for (DocumentSnapshot doc : snapshot)
            {
                Student newStudent = doc.toObject(Student.class);
                newStudent.SaveToSharedPrefs(getSharedPreferences(Constants.PrefsName, MODE_PRIVATE));
                Debug.This("Saving user details");
                break;
            }
        }
        if(GetUserType().equals("teacher"))
        {
            for (DocumentSnapshot doc : snapshot)
            {
                Teacher newTeacher = doc.toObject(Teacher.class);
                newTeacher.SaveToSharedPrefs(getSharedPreferences(Constants.PrefsName,MODE_PRIVATE));
                Debug.This("Saving user details");
            }
        }
    }

    private void SaveUserDetails(FirebaseUser user)
    {
        SharedPreferences sp = getSharedPreferences(Constants.PrefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.PrefsUserEmail, user.getEmail());
        editor.putString(Constants.PrefsUserName, user.getDisplayName());
        editor.putString(Constants.PrefsUserImageUrl, user.getPhotoUrl().toString());
        editor.apply();
    }
}