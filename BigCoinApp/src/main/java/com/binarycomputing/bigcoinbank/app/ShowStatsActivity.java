package com.binarycomputing.bigcoinbank.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

public class ShowStatsActivity extends Activity {

    private final String TAG = "TAG"+getClass().getSimpleName();

    private View.OnClickListener mOnClickListener = null;
    private TextView showstatsactivity_tvcurrentperson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showstats);

        // Setup Controls
        setupGlobal();
        setupListeners();
        setupViews();
        Intent intent = null;
        String welcomePerson = null;

        // Get Intent message
        if (getIntent().getExtras() !=null) {
            intent = getIntent();
            welcomePerson = intent.getStringExtra (BigCoinLoginActivity.WELCOME_NAME);

            // Use the received intent message to personalize the welcome message
            showstatsactivity_tvcurrentperson.setText(welcomePerson);
        }
    }
    // Define Controls here
    private void setupGlobal(){

    }
    private void setupListeners(){
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
    private void setupViews(){
    showstatsactivity_tvcurrentperson = (TextView)findViewById(R.id.showstatsactivity_tvcurrentperson);

    }


}
