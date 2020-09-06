package com.example.icollege.Utilities;
import androidx.annotation.NonNull;

import com.example.icollege.Utilities.CallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class FirebaseUtility
{
    /**
     * Insert data on FireStore
     *
     * @param documentReference Document reference of data to be add
     * @param model             Model to insert into Document
     * @param callback          callback for event handling
     */
    public static void Insert(
            final DocumentReference documentReference,
            final Object model,
            final CallBack callback) {

        documentReference.set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * Update data to FireStore
     *
     * @param documentReference Document reference of data to update
     * @param map               Data map to update
     * @param callback          callback for event handling
     */
    public static void Update(
            final DocumentReference documentReference,
            final Map<String, Object> map,
            final CallBack callback) {

        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * FireStore Create or Merge
     *
     * @param documentReference Document reference of data to create update
     * @param model             Model to create or update into Document
     */
    public static void CreateOrMerge(
            final DocumentReference documentReference,
            final Object model,
            final CallBack callback) {

        documentReference.set(model, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * Delete data from FireStore
     *
     * @param documentReference Document reference of data to delete
     * @param callback          callback for event handling
     */
    public static void Delete(
            final DocumentReference documentReference,
            final CallBack callback) {

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * One time data fetch from FireStore with Document reference
     *
     * @param documentReference query of Document reference to fetch data
     * @param callBack          callback for event handling
     */
    public static void ReadDoc(
            final DocumentReference documentReference,
            final CallBack callBack) {

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        callBack.onSuccess(document);
                    } else {
                        callBack.onSuccess();
                    }
                } else {
                    callBack.onError(task.getException());
                }
            }
        });
    }

    /**
     * One time data fetch from FireStore with Query reference
     *
     * @param query    query of Document reference to fetch data
     * @param callBack callback for event handling
     */
    public static void Query(
            final Query query,
            final CallBack callBack) {

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        callBack.onSuccess(querySnapshot);
                    } else {
                        callBack.onSuccess();
                    }
                } else {
                    callBack.onError(task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onError(e);
            }
        });
    }

}
