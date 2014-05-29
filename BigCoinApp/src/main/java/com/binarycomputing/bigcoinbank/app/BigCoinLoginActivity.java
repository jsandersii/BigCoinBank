package com.binarycomputing.bigcoinbank.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BigCoinLoginActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public  static final String WELCOME_NAME = "com.binarycomputing.bigcoinbank.app.PERSON";
    private static final String TAG = "BigCoinLoginActivity";

    private static final String KEY_IN_RESOLUTION = "is_in_resolution";

    private com.google.android.gms.common.SignInButton mBtnSignIn = null;
    private View.OnClickListener mOnClickListener = null;
    private ArrayList<String> mAryUIDS = null;

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Determines if the client is in a resolution state, and
     * waiting for resolution intent to return.
     */
    private boolean mIsInResolution;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigcoinbank);
        if (savedInstanceState != null) {
            mIsInResolution = savedInstanceState.getBoolean(KEY_IN_RESOLUTION, false);
        }
        // Setup Controls
        setupGlobal();
        setupListeners();
        setupViews();

    }
    // Define Controls here
    private void setupGlobal(){
        mAryUIDS = new ArrayList<String>();
        mAryUIDS.add("100557922697047334361");
        mAryUIDS.add("101759259863984873240");
        mAryUIDS.add("109218107702170891532");
        mAryUIDS.add("117277818256418508920");
        mAryUIDS.add("111505760421734215603");

    }
    private void setupListeners(){
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleApiClient.connect();
            }
        };
    }
    private void setupViews(){
        mBtnSignIn = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);
        mBtnSignIn.setOnClickListener(mOnClickListener);

    }
    /**
     * Called when the Activity is made visible.
     * A connection to Play Services need to be initiated as
     * soon as the activity is visible. Registers {@code ConnectionCallbacks}
     * and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    // Optionally, add additional APIs and scopes if required.
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    /**
     * Called when activity gets invisible. Connection to Play Services needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

    /**
     * Handles Google Play Services resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQUEST_CODE_RESOLUTION:
            retryConnecting();
            break;
        }
    }

    private void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        //Get the name of the user logging in.
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        String personName = currentPerson.getDisplayName();
        String personID = currentPerson.getId();

        //Check to see if the currentPerson has access
        if (this.mAryUIDS.contains(personID)){
            //Start the next activity with the user information
            Intent intent = new Intent(this,ShowStatsActivity.class);
            intent.putExtra(WELCOME_NAME,personName);
            startActivity(intent);
        } else {
            Toast.makeText(this,"You don't have permission to use this app...sorry :(.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    /**
     * Called when {@code mGoogleApiClient} connection is suspended.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), this, 0, new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    retryConnecting();
                }
            }).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            retryConnecting();
        }
    }

}
