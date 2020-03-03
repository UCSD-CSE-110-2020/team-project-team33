package com.example.walkwalkrevolution.cloud;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAdapter implements ICloudAdapter {
    private static final String TAG = "[FirebaseAdapter]";

    private static final String USERS_COLLECTION = "USERS";
    private static final String TEAMS_COLLECTION = "TEAMS";

    private static final String TEAM_ID_KEY = "TEAM_ID";
    private static final String FIRST_NAME_KEY = "FIRST";
    private static final String LAST_NAME_KEY = "LAST";
    private static final String GMAIL_KEY = "GMAIL";
    private static final String ROUTES_KEY = "ROUTES";
    private static final String INVITES_KEY = "INVITES";

    private static final String TEAMMATE_IDS_KEY = "TEAMMATE_IDS";

    private FirebaseFirestore db;

    private IAccountInfo user;
    private String userId;

    public FirebaseAdapter() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void addAccount(IAccountInfo account) {
        Map<String, Object> team = new HashMap<>();
        team.put(TEAMMATE_IDS_KEY, new ArrayList<String>());
        db.collection(TEAMS_COLLECTION)
                .add(team)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "Added team with ID: " + documentReference.getId());

                        Gson gson = new Gson();
                        Map<String, Object> user = new HashMap<>();

                        user.put(FIRST_NAME_KEY, account.getFirstName());
                        Log.d(TAG, "Account First Name: " + account.getFirstName());

                        user.put(LAST_NAME_KEY, account.getLastName());
                        Log.d(TAG, "Account Last Name: " + account.getLastName());

                        user.put(GMAIL_KEY, account.getGmail());
                        Log.d(TAG, "Account Gmail: " + account.getGmail());

                        user.put(TEAM_ID_KEY, documentReference.getId());

                        ArrayList<Route> routes = new ArrayList<>();
                        user.put(ROUTES_KEY, gson.toJson(routes));

                        ArrayList<String> invites = new ArrayList<>();
                        user.put(INVITES_KEY, invites);

                        // Prevents the addition of duplicate accounts
                        db.collection(USERS_COLLECTION)
                                .whereEqualTo(GMAIL_KEY, account.getGmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean accountExists = false;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                accountExists = true;
                                                Log.i(TAG, "Account with gmail " + account.getGmail() + " already exists");
                                                break;
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
                                                                Log.w(TAG, "Error adding user", e);
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.w(TAG, "Error getting users: ", task.getException());
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding team", e);
            }
        });
    }

    @Override
    public void setUser(IAccountInfo account) {
        user = account;
        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, account.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, account.getLastName())
                .whereEqualTo(GMAIL_KEY, account.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QueryDocumentSnapshot document = null;
                            if(!task.getResult().isEmpty()) {
                                document = task.getResult().iterator().next();
                            }

                            if(document == null) {
                                Log.i(TAG, account.getGmail() + " not found");
                            } else {
                                userId = document.getId();
                                Log.i(TAG, "User with id " + userId + " set");
                            }

                        } else {
                            Log.w(TAG, "Error getting users: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean userSet() {
        return user != null && userId != null;
    }

    @Override
    public void invite(IAccountInfo recipient, Context context) {
        if(recipient.getGmail().equals(user.getGmail())) {
            Toast.makeText(context, "You cannot invite yourself", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, recipient.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, recipient.getLastName())
                .whereEqualTo(GMAIL_KEY, recipient.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QueryDocumentSnapshot document = null;
                            if(!task.getResult().isEmpty()) {
                                document = task.getResult().iterator().next();
                            }

                            if(document == null) {
                                Log.i(TAG, recipient.getGmail() + " not found");
                                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                            } else {

                                DocumentReference dRecipient = db.collection(USERS_COLLECTION)
                                        .document(document.getId());

                                ArrayList<String> invites = (ArrayList<String>) document.get(INVITES_KEY);
                                if(invites.contains(userId)) {
                                    Toast.makeText(context, "You have already invited this user", Toast.LENGTH_SHORT).show();
                                } else {
                                    invites.add(userId);
                                    dRecipient.update(INVITES_KEY, invites)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.i(TAG, "User found, sending invite...");
                                                    Toast.makeText(context, "Invite Sent", Toast.LENGTH_SHORT).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating invites", e);
                                        }
                                    });
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting users: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void saveRoutes(Iterable<Route> routeManager) {
        Gson gson = new Gson();
        ArrayList<Route> routes = new ArrayList<>();
        for(Route r : routeManager) {
            routes.add(r);
        }

        db.collection(USERS_COLLECTION)
                .document(userId)
                .update(ROUTES_KEY, gson.toJson(routes))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Routes saved to the cloud");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Routes failed to saved: ", e);
            }
        });
    }
}
