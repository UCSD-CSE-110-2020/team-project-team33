package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.AccountInfo;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.CloudAdapterFactory;
import com.example.walkwalkrevolution.cloud.FirebaseAdapter;
import com.example.walkwalkrevolution.fitness.*;
import com.example.walkwalkrevolution.routemanagement.RoutesManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private String ACCOUNT_INFO_KEY = "GMAIL_ACCOUNT";
    private String FIREBASE_KEY = "FIREBASE";
    private int userHeight;
    private GoogleSignInAccount account;
    private SharedPreferences sharedPreferences;
    private RoutesManager routesManager;

    private static final String TAG = "[MainActivity]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FitnessServiceFactory.put(fitnessServiceKey, GoogleFitAdapter::new);
        AccountFactory.put(ACCOUNT_INFO_KEY, new AccountFactory.BluePrint() {
            @Override
            public IAccountInfo create(Context context) {
                return new AccountInfo(context);
            }

            @Override
            public IAccountInfo create(String first, String last, String gmail) {
                return new AccountInfo(first, last, gmail);
            }
        });
        CloudAdapterFactory.put(FIREBASE_KEY, FirebaseAdapter::new);

        sharedPreferences = getSharedPreferences(DataKeys.USER_NAME_KEY, MODE_PRIVATE);
        routesManager = new RoutesManager(sharedPreferences);

        userHeight = sharedPreferences.getInt(DataKeys.USER_HEIGHT_KEY, -1);
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null || userHeight == -1) {
            launchHeightActivity();
        } else {
            launchStepCountActivity();
        }
    }

    public void launchStepCountActivity() {
        Log.i(TAG, "Launching step count activity");
        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, fitnessServiceKey);
        intent.putExtra(DataKeys.ACCOUNT_KEY, ACCOUNT_INFO_KEY);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, userHeight);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        intent.putExtra(DataKeys.CLOUD_KEY, FIREBASE_KEY);
        startActivity(intent);
    }

    public void launchHeightActivity() {
        Log.i(TAG, "Launching height activity");
        Intent intent = new Intent(this, HeightActivity.class);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, fitnessServiceKey);
        intent.putExtra(DataKeys.ACCOUNT_KEY, ACCOUNT_INFO_KEY);
        intent.putExtra(DataKeys.CLOUD_KEY, FIREBASE_KEY);
        startActivity(intent);

        if(account == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            startActivity(mGoogleSignInClient.getSignInIntent());
        }
    }

    @Override
    public void onBackPressed() { }
}