package com.example.walkwalkrevolution.cloud;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private static final String PENDING_KEY = "PENDING";

    private FirebaseFirestore db;
    private final String accountInfoKey;

    private IAccountInfo user;

    public FirebaseAdapter(String ACKey) {
        accountInfoKey = ACKey;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void addAccount(IAccountInfo account) {
        user = account;

        Gson gson = new Gson();
        Map<String, Object> user = new HashMap<>();

        user.put(FIRST_NAME_KEY, account.getFirstName());
        Log.d(TAG, "Account First Name: " + account.getFirstName());

        user.put(LAST_NAME_KEY, account.getLastName());
        Log.d(TAG, "Account Last Name: " + account.getLastName());

        user.put(GMAIL_KEY, account.getGmail());
        Log.d(TAG, "Account Gmail: " + account.getGmail());

        user.put(TEAM_ID_KEY, "");

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
                            if (!accountExists) {
                                db.collection(USERS_COLLECTION)
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "User added with ID: " + documentReference.getId());
                                                String userId = documentReference.getId();

                                                // Add the team the user is a part of
                                                Map<String, Object> team = new HashMap<>();
                                                ArrayList<String> ids = new ArrayList<>();
                                                ids.add(documentReference.getId());
                                                team.put(TEAMMATE_IDS_KEY, ids);
                                                team.put(PENDING_KEY, new ArrayList<String>());
                                                db.collection(TEAMS_COLLECTION)
                                                        .add(team)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.i(TAG, "Added team with ID: " + documentReference.getId());

                                                                db.collection(USERS_COLLECTION)
                                                                        .document(userId)
                                                                        .update(TEAM_ID_KEY, documentReference.getId());
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding team", e);
                                                    }
                                                });
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

    @Override
    public void setUser(IAccountInfo account) {
        user = account;
    }
    
    @Override
    public void getTeam(ITeamSubject teamSubject) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, user.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, user.getLastName())
                .whereEqualTo(GMAIL_KEY, user.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().isEmpty()) {
                                String userId = task.getResult().iterator().next().getId();

                                db.collection(USERS_COLLECTION)
                                        .document(userId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                db.collection(TEAMS_COLLECTION)
                                                        .document(task.getResult().getString(TEAM_ID_KEY))
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                DocumentSnapshot teamSnapshot = task.getResult();
                                                                ArrayList<String> teammateIds = (ArrayList<String>) teamSnapshot.get(TEAMMATE_IDS_KEY);
                                                                ArrayList<String> pendingIds = (ArrayList<String>) teamSnapshot.get(PENDING_KEY);
                                                                db.collection(USERS_COLLECTION)
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                ArrayList<Teammate> teammates = new ArrayList<>();
                                                                                for(QueryDocumentSnapshot user : task.getResult()) {
                                                                                    boolean isTeammate = teammateIds.contains(user.getId());
                                                                                    boolean isPending = pendingIds.contains(user.getId());
                                                                                    if(isPending || isTeammate) {
                                                                                        teammates.add(new Teammate(AccountFactory.create(accountInfoKey,
                                                                                                user.getString(FIRST_NAME_KEY),
                                                                                                user.getString(LAST_NAME_KEY),
                                                                                                user.getString(GMAIL_KEY)), isPending));
                                                                                    }
                                                                                }
                                                                                Log.i(TAG, "Teammates successfully found");
                                                                                teamSubject.update(teammates);
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error retrieving user collection", e);
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error retrieving teams collection", e);
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error retrieving user collection", e);
                                    }
                                });
                            }
                        }
                    }
                });

    }
    
    @Override
    public void getInvites(IInviteSubject inviteSubject) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, user.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, user.getLastName())
                .whereEqualTo(GMAIL_KEY, user.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().isEmpty()) {
                                String userId = task.getResult().iterator().next().getId();
                                db.collection(USERS_COLLECTION)
                                        .document(userId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                ArrayList<String> invites = (ArrayList<String>) task.getResult().get(INVITES_KEY);
                                                db.collection(USERS_COLLECTION)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                ArrayList<IAccountInfo> inviters = new ArrayList<>();
                                                                for (QueryDocumentSnapshot user : task.getResult()) {
                                                                    if (invites.contains(user.getId())) {
                                                                        inviters.add(AccountFactory.create(accountInfoKey,
                                                                                user.getString(FIRST_NAME_KEY),
                                                                                user.getString(LAST_NAME_KEY),
                                                                                user.getString(GMAIL_KEY)));
                                                                    }
                                                                }
                                                                Log.i(TAG, "Invites successfully retrieved");
                                                                inviteSubject.update(inviters);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {

                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error retrieving users collection", e);
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error retrieving users collection", e);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    @Override
    public boolean userSet() {
        return user != null;
    }

    @Override
    public void invite(IAccountInfo recipient, Context context) {
        if (recipient.getGmail().equals(user.getGmail())) {
            Toast.makeText(context, "You cannot invite yourself", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, user.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, user.getLastName())
                .whereEqualTo(GMAIL_KEY, user.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().isEmpty()) {
                                QueryDocumentSnapshot userSnapshot = task.getResult().iterator().next();

                                db.collection(USERS_COLLECTION)
                                        .whereEqualTo(FIRST_NAME_KEY, recipient.getFirstName())
                                        .whereEqualTo(LAST_NAME_KEY, recipient.getLastName())
                                        .whereEqualTo(GMAIL_KEY, recipient.getGmail())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    if (task.getResult().isEmpty()) {
                                                        Log.i(TAG, recipient.getGmail() + " not found");
                                                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        QueryDocumentSnapshot recipientSnapshot = task.getResult().iterator().next();

                                                        DocumentReference dRecipient = db.collection(USERS_COLLECTION)
                                                                .document(recipientSnapshot.getId());
                                                        DocumentReference dTeam = db.collection(TEAMS_COLLECTION)
                                                                .document(recipientSnapshot.getString(TEAM_ID_KEY));

                                                        ArrayList<String> invites = (ArrayList<String>) recipientSnapshot.get(INVITES_KEY);
                                                        if (invites.contains(userSnapshot.getId())) {
                                                            Toast.makeText(context, "You have already invited this user", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            invites.add(userSnapshot.getId());
                                                            dRecipient.update(INVITES_KEY, invites)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            dTeam.get()
                                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                            Log.i(TAG, "User found, sending invite...");
                                                                                            Toast.makeText(context, "Invite Sent", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });

                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error updating invites", e);
                                                                }
                                                            });

                                                            db.collection(TEAMS_COLLECTION)
                                                                    .document(userSnapshot.getString(TEAM_ID_KEY))
                                                                    .get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            ArrayList<String> pending = (ArrayList<String>) documentSnapshot.get(PENDING_KEY);
                                                                            pending.add(recipientSnapshot.getId());
                                                                            db.collection(TEAMS_COLLECTION)
                                                                                    .document(userSnapshot.getString(TEAM_ID_KEY))
                                                                                    .update(PENDING_KEY, pending);
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
                        }
                    }
                });
    }

    @Override
    public void saveRoutes(Iterable<Route> routeManager) {
        Gson gson = new Gson();
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : routeManager) {
            routes.add(r);
        }

        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, user.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, user.getLastName())
                .whereEqualTo(GMAIL_KEY, user.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().isEmpty()) {
                                String userId = task.getResult().iterator().next().getId();
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
                    }
                });
    }

    @Override
    public void getTeamRoutes(ITeammateRoutesSubject teammateRoutesSubject) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, user.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, user.getLastName())
                .whereEqualTo(GMAIL_KEY, user.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().isEmpty()) {
                                String userId = task.getResult().iterator().next().getId();
                                db.collection(USERS_COLLECTION)
                                        .document(userId)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                System.out.println("Inside Get TeamRoutes 2");

                                                db.collection(TEAMS_COLLECTION)
                                                        .document(documentSnapshot.getString(TEAM_ID_KEY))
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                System.out.println("Inside Get TeamRoutes 3");
                                                                ArrayList<String> teammateIds = (ArrayList<String>) documentSnapshot.get(TEAMMATE_IDS_KEY);

                                                                db.collection(USERS_COLLECTION)
                                                                        .get()
                                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                                                                            @Override
                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                System.out.println("Inside Get TeamRoutes 4");
                                                                                ArrayList<TeammateRoute> teamRoutes = new ArrayList<>();

                                                                                for (String teammateId : teammateIds) {
                                                                                    for (QueryDocumentSnapshot user : queryDocumentSnapshots) {
                                                                                        System.out.println("Printing TeammateID:" + teammateId);
                                                                                        if (teammateId.equals(user.getId()) && !teammateId.equals(userId)) {
                                                                                            System.out.println("I should be calling strToRoutes");

                                                                                            teamRoutes.addAll(stringToRoutes(user.getString(ROUTES_KEY),
                                                                                                    AccountFactory.create(accountInfoKey,
                                                                                                            user.getString(FIRST_NAME_KEY),
                                                                                                            user.getString(LAST_NAME_KEY),
                                                                                                            user.getString(GMAIL_KEY))));
                                                                                        }
                                                                                    }
                                                                                }
                                                                                teammateRoutesSubject.update(teamRoutes);

                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                });
    }


    private ArrayList<TeammateRoute> stringToRoutes(String str, IAccountInfo info){
        System.out.print("Inside StrToRoutes");
        Gson gson = new Gson();
        TypeToken<ArrayList<Route>> token = new TypeToken<ArrayList<Route>>(){};
        ArrayList<Route> routesList = gson.fromJson(str, token.getType());

        ArrayList<TeammateRoute> teamRoutes = new ArrayList<TeammateRoute>();

        for(Route route : routesList){
            teamRoutes.add(new TeammateRoute(route, info));
        }

        return teamRoutes;
    }

    public void acceptInvite(IAccountInfo account, IAcceptSubject acceptSubject) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, user.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, user.getLastName())
                .whereEqualTo(GMAIL_KEY, user.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().isEmpty()) {
                                String userId = task.getResult().iterator().next().getId();
                                db.collection(USERS_COLLECTION)
                                        .whereEqualTo(FIRST_NAME_KEY, account.getFirstName())
                                        .whereEqualTo(LAST_NAME_KEY, account.getLastName())
                                        .whereEqualTo(GMAIL_KEY, account.getGmail())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    QueryDocumentSnapshot hostUser;
                                                    hostUser = task.getResult().iterator().next();

                                                    db.collection(TEAMS_COLLECTION)
                                                            .document(hostUser.getString(TEAM_ID_KEY))
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    ArrayList<String> teammateIds = (ArrayList<String>) documentSnapshot.get(TEAMMATE_IDS_KEY);
                                                                    ArrayList<String> pendingIds = (ArrayList<String>) documentSnapshot.get(PENDING_KEY);
                                                                    teammateIds.add(userId);
                                                                    pendingIds.remove(userId);
                                                                    db.collection(TEAMS_COLLECTION)
                                                                            .document(hostUser.getString(TEAM_ID_KEY))
                                                                            .update(TEAMMATE_IDS_KEY, teammateIds);
                                                                    db.collection(TEAMS_COLLECTION)
                                                                            .document(hostUser.getString(TEAM_ID_KEY))
                                                                            .update(PENDING_KEY, pendingIds);
                                                                }
                                                            });

                                                    db.collection(USERS_COLLECTION)
                                                            .document(userId)
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    String teamId = documentSnapshot.getString(TEAM_ID_KEY);
                                                                    ArrayList<String> invites = (ArrayList<String>) documentSnapshot.get(INVITES_KEY);
                                                                    db.collection(TEAMS_COLLECTION)
                                                                            .document(teamId)
                                                                            .get()
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                    ArrayList<String> teammateIds = (ArrayList<String>) documentSnapshot.get(TEAMMATE_IDS_KEY);
                                                                                    teammateIds.remove(userId);
                                                                                    db.collection(TEAMS_COLLECTION)
                                                                                            .document(teamId)
                                                                                            .update(TEAMMATE_IDS_KEY, teammateIds);

                                                                                    invites.remove(hostUser.getId());
                                                                                    db.collection(USERS_COLLECTION)
                                                                                            .document(userId)
                                                                                            .update(INVITES_KEY, invites);

                                                                                    db.collection(USERS_COLLECTION)
                                                                                            .document(userId)
                                                                                            .update(TEAM_ID_KEY, hostUser.getString(TEAM_ID_KEY))
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    acceptSubject.update("Team Joined");
                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public void declineInvite(IAccountInfo account, IAcceptSubject acceptSubject) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo(FIRST_NAME_KEY, user.getFirstName())
                .whereEqualTo(LAST_NAME_KEY, user.getLastName())
                .whereEqualTo(GMAIL_KEY, user.getGmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().isEmpty()) {
                                String userId = task.getResult().iterator().next().getId();
                                db.collection(USERS_COLLECTION)
                                        .whereEqualTo(FIRST_NAME_KEY, account.getFirstName())
                                        .whereEqualTo(LAST_NAME_KEY, account.getLastName())
                                        .whereEqualTo(GMAIL_KEY, account.getGmail())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    QueryDocumentSnapshot hostUser;
                                                    hostUser = task.getResult().iterator().next();

                                                    db.collection(TEAMS_COLLECTION)
                                                            .document(hostUser.getString(TEAM_ID_KEY))
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    ArrayList<String> pendingIds = (ArrayList<String>) documentSnapshot.get(PENDING_KEY);
                                                                    pendingIds.remove(userId);
                                                                    db.collection(TEAMS_COLLECTION)
                                                                            .document(hostUser.getString(TEAM_ID_KEY))
                                                                            .update(PENDING_KEY, pendingIds);
                                                                }
                                                            });

                                                    db.collection(USERS_COLLECTION)
                                                            .document(userId)
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    ArrayList<String> invites = (ArrayList<String>) documentSnapshot.get(INVITES_KEY);
                                                                    invites.remove(hostUser.getId());
                                                                    db.collection(USERS_COLLECTION)
                                                                            .document(userId)
                                                                            .update(INVITES_KEY, invites);
                                                                    acceptSubject.update("Invite Declined");
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}


