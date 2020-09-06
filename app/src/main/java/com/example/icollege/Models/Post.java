package com.example.icollege.Models;

public class Post
{
    public String name;
    public String className;
    public String userImageUrl;
    public String notes;
    public String documentUrl = "";

    public Post(){}

    public void GetDataFromTeacher(Teacher mTeacher)
    {
        name = mTeacher.name;
        userImageUrl = mTeacher.imageUrl;
    }

}
