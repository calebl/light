package com.bigndesign.light;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.UsernameStyle;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;

public class LoginActivity extends AppCompatActivity {

    private Lock mLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        mLock = Lock.newBuilder(auth0, mCallback)
                .withUsernameStyle(UsernameStyle.USERNAME)
                .closable(true)
                // Add parameters to the Lock Builder
                .build(this);
        startActivity(mLock.newIntent(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        mLock.onDestroy(this);
        mLock = null;
    }


    private final LockCallback mCallback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            Toast.makeText(getApplicationContext(), "Log In - Success", Toast.LENGTH_SHORT).show();
            Intent askIntent = new Intent(getApplicationContext(), AskActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString("userId", credentials.getIdToken());

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
}
