package com.bigndesign.light;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;

public class LoginActivity extends AppCompatActivity {

    private Lock mLock;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Comment out until find replacement for Auth0
        /*Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        mLock = Lock.newBuilder(auth0, mCallback)
                .withUsernameStyle(UsernameStyle.USERNAME)
                .closable(true)
                // Add parameters to the Lock Builder
                .build(this);
        startActivity(mLock.newIntent(this));*/

        // Restore id
        String sinch_id = "sinch_id";
        SharedPreferences settings = getSharedPreferences(sinch_id, 0);
        final String id = settings.getString("sinch_id", "none");

        if(id.equals("none")){
            //Generate id
            SecureRandom random = new SecureRandom();
            String user_id = new BigInteger(130, random).toString(32);
            userId =user_id;

            saveID(sinch_id, user_id);
        } else {
            userId = id;
        }


        Toast.makeText(getApplicationContext(), "Log In - Success", Toast.LENGTH_SHORT).show();
        Intent askIntent = new Intent(getApplicationContext(), AskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        askIntent.putExtras(bundle);

        startActivity(askIntent, bundle);
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        mLock.onDestroy(this);
        mLock = null;
    }*/

    public void saveID(String sinch_id, String id){

        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(sinch_id, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("sinch_id", id);

        // Commit the edits
        editor.commit();
    }

    private final LockCallback mCallback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            String accessToken = credentials.getAccessToken();
            //new GetUserId().execute("https://" + getString(R.string.auth0_domain) + "/userinfo/?access_token=" + accessToken);

            // Restore id
            String sinch_id = "sinch_id";
            SharedPreferences settings = getSharedPreferences(sinch_id, 0);
            final String id = settings.getString("sinch_id", "none");

            if(id.equals("none")){
                //Generate id
                SecureRandom random = new SecureRandom();
                String user_id = new BigInteger(130, random).toString(32);
                userId =user_id;

                saveID(sinch_id, user_id);
            } else {
                userId = id;
            }


            Toast.makeText(getApplicationContext(), "Log In - Success", Toast.LENGTH_SHORT).show();
            Intent askIntent = new Intent(getApplicationContext(), AskActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            askIntent.putExtras(bundle);

            startActivity(askIntent, bundle);

            finish();
        }

        @Override
        public void onCanceled() {
            Toast.makeText(getApplicationContext(), "Log In - Cancelled", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onError(LockException error) {
            Toast.makeText(getApplicationContext(), "Log In - Error Occurred", Toast.LENGTH_SHORT).show();
        }
    };

    //This async class from http://stackoverflow.com/questions/8654876/http-get-using-android-httpurlconnection
    public class GetUserId extends AsyncTask<String , Void ,String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    JSONObject jsonObject = new JSONObject(server_response);
                    userId = jsonObject.getString("user_id");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Response", "" + server_response);


        }
    }

    // Converting InputStream to String
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
