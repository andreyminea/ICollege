package com.example.icollege.Models;

public class Class
{
    public String year;
    public String group;
    public String name;

    public Class(){}

    public String ClassToString()
    {
        String fullName="";
        fullName+=name+" "+year+" "+group;
        return fullName;
    }

}
