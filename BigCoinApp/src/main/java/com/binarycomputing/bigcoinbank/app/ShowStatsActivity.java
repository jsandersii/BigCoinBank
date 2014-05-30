package com.binarycomputing.bigcoinbank.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

public class ShowStatsActivity extends Activity {

    private final String TAG = "TAG"+getClass().getSimpleName();
    private final String bigCoinStatus = "http://www.binary-computing.com/bigcoin/status.asp";

    private View.OnClickListener mOnClickListener = null;

    private TextView showstatsactivity_tvCurrentperson = null;
    private TextView showstatsactivity_tvGethash = null;
    private TextView showstatsactivity_tvGettotalbtm = null;

    private String statusHash = null;
    private String statusTotal = null;
    private String statusJamesCoin = null;
    private String statusHaroldCoin = null;
    private String statusMelvinCoin = null;
    private String statusNakiaCoin = null;
    private String statusCalebCoin = null;


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

        //Using AsyncTask to grab data
        new CoinAsyncTask().execute();
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
    showstatsactivity_tvCurrentperson = (TextView)findViewById(R.id.showstatsactivity_tvCurrentperson);
    showstatsactivity_tvGethash = (TextView)findViewById(R.id.showstatsactivity_tvGethash);
    showstatsactivity_tvGettotalbtm = (TextView)findViewById(R.id.showstatsactivity_tvGettotalbtm);

    }
    //Define Async Class
    private class CoinAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String...Params){

            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            HttpPost httppost = new HttpPost(bigCoinStatus);

            httppost.setHeader("Content-Type","application/json");

            InputStream resultStream = null;

            String result = null;

            try {
                HttpResponse response = httpClient.execute(httppost);
                HttpEntity entity = response.getEntity();
                resultStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(resultStream, "UTF-8"),8);
                StringBuilder theStringBuilder = new StringBuilder();

                String line = null;

                while((line = reader.readLine()) != null){
                    theStringBuilder.append(line + "\n");
                }

                result = theStringBuilder.toString();
            }

            catch (Exception e){
                e.printStackTrace();
            }

            finally {

                try {
                    if(resultStream != null)resultStream.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            JSONObject jsonObject;

            try{

                //Log.v("JSONParser RESULT",result);

                jsonObject = new JSONObject(result);

                statusHash = jsonObject.getString("hash");
                statusTotal = jsonObject.getString("totalcoin");
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result){
            showstatsactivity_tvGethash.setText(statusHash);
            showstatsactivity_tvGettotalbtm.setText(statusTotal);

        }
    }


}
