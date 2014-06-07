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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

public class ShowStatsActivity extends Activity {

    private final String TAG = "TAG"+getClass().getSimpleName();
    private final String bigCoinStatus = "http://www.binary-computing.com/bigcoin/status.asp";

    private View.OnClickListener mOnClickListener = null;

    private TextView showstatsactivity_tvCurrentperson = null;
    private TextView showstatsactivity_tvCurrentprice = null;
    private TextView showstatsactivity_tvGethash = null;
    private TextView showstatsactivity_tvGettotalbtm = null;
    private TextView showstatsactivity_tvHaroldcoin = null;
    private TextView showstatsactivity_tvJamescoin = null;
    private TextView showstatsactivity_tvMelvincoin = null;
    private TextView showstatsactivity_tvNakiacoin = null;
    private TextView showstatsactivity_tvCalebcoin = null;

    private String statusHash = null;
    private String statusTotal = null;
    private String statusCurrentPrice = null;
    private String statusJamesCoin = null;
    private String statusHaroldCoin = null;
    private String statusMelvinCoin = null;
    private String statusNakiaCoin = null;
    private String statusCalebCoin = null;

    private ProgressBar spinner = null;



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
            showstatsactivity_tvCurrentperson.setText("Welcome " + welcomePerson +"!");
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
    showstatsactivity_tvCurrentprice = (TextView)findViewById(R.id.showstatsactivity_tvCurrentprice);
    showstatsactivity_tvGethash = (TextView)findViewById(R.id.showstatsactivity_tvGethash);
    showstatsactivity_tvGettotalbtm = (TextView)findViewById(R.id.showstatsactivity_tvGettotalbtm);
    showstatsactivity_tvHaroldcoin = (TextView)findViewById(R.id.showstatsactivity_tvHaroldcoin);
    showstatsactivity_tvJamescoin = (TextView)findViewById(R.id.showstatsactivity_tvJamescoin);
    showstatsactivity_tvMelvincoin = (TextView)findViewById(R.id.showstatsactiviy_tvMelvincoin);
    showstatsactivity_tvNakiacoin = (TextView)findViewById(R.id.showstatsactivity_tvNakiacoin);
    showstatsactivity_tvCalebcoin = (TextView)findViewById(R.id.showstatsactivity_tvCalebcoin);
    spinner = (ProgressBar)findViewById(R.id.showstatsactivity_progressbar);


    }
    //Define Async Class
    private class CoinAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String...Params){

            // Activate loading progress bar
            spinner.setVisibility(View.VISIBLE);

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
                statusHaroldCoin = jsonObject.getString("haroldcoin");
                statusJamesCoin = jsonObject.getString("jamescoin");
                statusMelvinCoin = jsonObject.getString("melvincoin");
                statusNakiaCoin = jsonObject.getString("nakiacoin");
                statusCalebCoin = jsonObject.getString("calebcoin");
                statusCurrentPrice = jsonObject.getString("currentprice");
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result){
            showstatsactivity_tvCurrentprice.setText("Current Price: " + statusCurrentPrice);
            showstatsactivity_tvGethash.setText("Hash Rate: " + "\n" + statusHash);
            showstatsactivity_tvGettotalbtm.setText("Total Bit Coins: " + "\n" + statusTotal);
            showstatsactivity_tvHaroldcoin.setText("Harold's coins: " + statusHaroldCoin);
            showstatsactivity_tvJamescoin.setText("James' coins: " + statusJamesCoin);
            showstatsactivity_tvMelvincoin.setText("Melvin's coins: " + statusMelvinCoin);
            showstatsactivity_tvNakiacoin.setText("Nakia's coins: " + statusNakiaCoin);
            showstatsactivity_tvCalebcoin.setText("Caleb's coins: " + statusCalebCoin);

            // Deactivate progress bar
            spinner.setVisibility(View.GONE);

        }
    }


}
