package com.example.icollege.Utilities;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseReference
{
    private FirebaseReference(){}

    public static final FirebaseFirestore getInstance()
    {
        return FirebaseFirestore.getInstance();
    }

}
