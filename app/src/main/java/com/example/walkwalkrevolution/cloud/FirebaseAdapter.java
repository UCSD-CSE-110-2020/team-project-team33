package com.example.walkwalkrevolution.cloud;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAdapter implements ICloudAdapter {
    private static final String TAG = "[FirebaseAdapter]";

    private static final String USERS_COLLECTION = "USERS";

    private static final String FIRST_NAME_KEY = "FIRST";
    private static final String LAST_NAME_KEY = "LAST";
    private static final String GMAIL_KEY = "GMAIL";
    private static final String ROUTES_COLLECTION_KEY = "ROUTES";

    private FirebaseFirestore db;

    private QueryDocumentSnapshot user;

    public FirebaseAdapter() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void addAccount(IAccountInfo account) {
        Map<String, Object> user = new HashMap<>();
        user.put(FIRST_NAME_KEY, account.getFirstName());
        Log.d(TAG, "Account First Name: " + account.getFirstName());
        user.put(LAST_NAME_KEY, account.getLastName());
        Log.d(TAG, "Account Last Name: " + account.getLastName());
        user.put(GMAIL_KEY, account.getGmail());
        Log.d(TAG, "Account Gmail: " + account.getGmail());
        ArrayList<Route> routes = new ArrayList<>();
        user.put(ROUTES_COLLECTION_KEY, routes);

        // Prevents the addition of duplicate accounts
        db.collection(USERS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            boolean accountExists = false;
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(((String)document.get(GMAIL_KEY)).equals(account.getGmail())) {
                                    accountExists = true;
                                    Log.i(TAG, "Account with gmail " + account.getGmail() + " already exists");
                                    break;
                                }
                            }
                            if(!accountExists) {
                                db.collection(USERS_COLLECTION)
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting users: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void setUser(IAccountInfo account) {
        db.collection(USERS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(((String)document.get(GMAIL_KEY)).equals(account.getGmail())) {
                                    Log.i(TAG, "User found, setting...");
                                    user = document;
                                    break;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting users: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean userSet() {
        return user != null;
    }
}
