package com.example.icollege.Models;

import android.content.SharedPreferences;

import com.example.icollege.Utilities.Constants;

import java.util.ArrayList;

public class Teacher
{
    public ArrayList<String> classesNames;
    public ArrayList<String> classesYears;
    public ArrayList<String> classesGroups;
    public String email;
    public String name;
    public String imageUrl;

    public Teacher(){}

    public Teacher(SharedPreferences preferences)
    {
        classesNames = new ArrayList<>();
        classesGroups = new ArrayList<>();
        classesYears = new ArrayList<>();
        email = preferences.getString(Constants.PrefsUserEmail, "");
        name = preferences.getString(Constants.PrefsUserName, "");
        imageUrl = preferences.getString(Constants.PrefsUserImageUrl, "");
    }

    public ArrayList<String> GetClasses()
    {
        ArrayList<String> fullNameClasses = new ArrayList<>();
        ArrayList<Class> classes = new ArrayList<>();
        for(int i =0; i<classesNames.size(); i++)
        {
            Class mClass = new Class();
            mClass.name = classesNames.get(i);
            mClass.year = classesYears.get(i);
            mClass.group = classesGroups.get(i);
            classes.add(mClass);
            fullNameClasses.add(mClass.ClassToString());
        }
        return fullNameClasses;
    }

    public void SaveToSharedPrefs(SharedPreferences preferences)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PrefsUserEmail, email);
        editor.putString(Constants.PrefsUserName, name);
        editor.putString(Constants.PrefsUserImageUrl, imageUrl);
        editor.putString(Constants.PrefsUserType, "teacher");
        editor.putBoolean(Constants.PrefsLog, true);
        editor.apply();
    }
}
