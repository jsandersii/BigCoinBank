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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.HttpsURLConnection;

public class ShowStatsActivity extends Activity {

    private final String TAG = "TAG"+getClass().getSimpleName();

    private View.OnClickListener mOnClickListener = null;
    private TextView showstatsactivity_tvCurrentperson = null;
    private TextView showstatsactivity_tvGetdifficulty = null;
    private HttpClient mBlockExplorer = null;
    private HttpGet mGetDifficulty = null;
    private HttpGet mGetCurrentHash = null;
    private HttpGet mGetTotalBtm = null;
    private String mDiff_URL = null;
    private String mHash_URL = null;
    private String mBtm_URL = null;

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
            showstatsactivity_tvCurrentperson.setText(welcomePerson);
        }
    }
    // Define Controls here
    private void setupGlobal(){
        mDiff_URL = (HttpsURLConnection)

    }
    private void setupListeners(){
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
    private void setupViews(){
    showstatsactivity_tvCurrentperson = (TextView)findViewById(R.id.showstatsactivity_tvCurrentperson);

    }


}
