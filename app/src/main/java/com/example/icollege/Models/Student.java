package com.example.icollege.Models;

import android.content.SharedPreferences;

import com.example.icollege.Utilities.Constants;

public class Student
{
    public String email;
    public String name;
    public String imageUrl;
    public String year;
    public String group;

    // for Firebase
    public Student(){}

    public Student(SharedPreferences preferences)
    {
        email = preferences.getString(Constants.PrefsUserEmail, "");
        name = preferences.getString(Constants.PrefsUserName, "");
        imageUrl = preferences.getString(Constants.PrefsUserImageUrl, "");
        year = preferences.getString(Constants.PrefsUserYear, "");
        group = preferences.getString(Constants.PrefsUserGroup, "");
    }

    public void SaveToSharedPrefs(SharedPreferences preferences)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PrefsUserEmail, email);
        editor.putString(Constants.PrefsUserName, name);
        editor.putString(Constants.PrefsUserImageUrl, imageUrl);
        editor.putString(Constants.PrefsUserYear, year);
        editor.putString(Constants.PrefsUserGroup, group);
        editor.putString(Constants.PrefsUserType, "student");
        editor.putBoolean(Constants.PrefsLog, true);
        editor.apply();
    }
}
