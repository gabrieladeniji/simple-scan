package com.tech104.isreal.simplescan;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class EthScanActivity extends AppCompatActivity {

    EditText ethAddressET;
    ProgressBar progressBar;
    TextView resultTitle;
    TextView resultMsg;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethscan);
        setTitle("Scan Ethereum Address");
        //
        ethAddressET = (EditText) findViewById(R.id.ethAddressEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBarRound);
        resultTitle = (TextView) findViewById(R.id.resultTitleTextView);
        resultMsg = (TextView) findViewById(R.id.resultMsgTextView);
    }

    public void scanEthAddressFunction(View view) {
        progressBar.setVisibility(View.VISIBLE);
        resultTitle.setVisibility(View.INVISIBLE);
        resultMsg.setVisibility(View.INVISIBLE);
        if(isValid(ethAddressET)) {
            if(validateETHAddress(ethAddressET.getText().toString())) {
                Toast.makeText(this, "Address is valid", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                resultTitle.setVisibility(View.VISIBLE);
                resultTitle.setText("Address is not valid");
                resultMsg.setVisibility(View.VISIBLE);
                resultMsg.setText(ethAddressET.getText().toString());
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            resultTitle.setVisibility(View.VISIBLE);
            resultTitle.setText("Address filed is empty");
        }
    }

    public boolean validateETHAddress(String address) {
        ValidateAddress validateAddress = new ValidateAddress();
        try {
            boolean result = validateAddress.execute("https://balidator.io/api/ethereum/"+address).get();
            Log.i("Result", String.valueOf(result) );
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isValid(EditText e) {
        if(e.getText().toString().equals("") || e.getText().toString().trim().equals("") || e.getText().toString().trim().length() < 0) {
            return false;
        } else {
            return true;
        }
    }

     public class ValidateAddress extends AsyncTask<String, Void, Boolean>  {
         @Override
         protected Boolean doInBackground(String... url) {
             URL Url = null;
             String response = "";
             try {
                 Url = new URL(url[0]);
                 HttpURLConnection con = (HttpURLConnection) Url.openConnection();
                 InputStreamReader reader = new InputStreamReader(con.getInputStream());
                 int data = reader.read();
                 String result = "";
                 while(data != -1) {
                     char c = (char) data;
                     result += c;
                     data = reader.read();
                 }
                 JSONObject jo = new JSONObject(result);
                 response = jo.getString("valid_address");
             } catch (Exception e) {
                 e.printStackTrace();
             }
             if(response.equals("true")) {
                 return true;
             } else {
                 return false;
             }
         }
     }
}
