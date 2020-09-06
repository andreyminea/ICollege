package com.example.icollege.Utilities;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface CallBack {
    public void onSuccess();
    public void onSuccess(DocumentSnapshot snapshot);
    public void onSuccess(QuerySnapshot snapshot);
    public void onError(Object object);
}
