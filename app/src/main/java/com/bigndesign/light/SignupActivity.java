package com.bigndesign.light;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class SignupActivity extends AppCompatActivity {

    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
    public static final String SHARED_PREFS_FILENAME = "lqd-prefs";
    private String TAG = "Liquid.LoginActivity";

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;
    private String mUsername;
    private String mPasswordVerify;

    // UI references.
    private EditText mEmailView;
    private EditText mUsernameView;

    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    private View focusView;
    private EditText mPasswordVerifyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        MeteorSingleton.getInstance().connect();

        // Set up the signup form.
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordVerifyView = (EditText) findViewById(R.id.password_verify);
        mPasswordVerifyView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.sign_up_button || id == EditorInfo.IME_NULL) {
                            attemptSignup();
                            return true;
                        }
                        return false;
                    }
                });

        mLoginFormView = findViewById(R.id.signup_form);
        mLoginStatusView = findViewById(R.id.login_status);


        findViewById(R.id.sign_up_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptSignup();
                    }
                }
        );

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    public void attemptSignup() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mPasswordVerifyView.setError(null);


        if(!MeteorSingleton.getInstance().isConnected()){
            Toast.makeText(SignupActivity.this, "Unable to connect to Liquid server. Please try again when you have an internet connection.", Toast.LENGTH_LONG).show();
            return;
        }

        // Store values at the time of the login attempt.
        mUsername = mUsernameView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        mPasswordVerify = mPasswordVerifyView.getText().toString();

        boolean cancel = false;
        focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(mUsername)){
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!mPassword.equals(mPasswordVerify)){
            mPasswordVerifyView.setError("Password must match.");
            focusView = mPasswordVerifyView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


            final Intent askIntent = new Intent(this, AskActivity.class);
            //signup with DDP


            MeteorSingleton.getInstance().registerAndLogin(mUsername, null, mPassword, new ResultListener() {
                @Override
                public void onSuccess(String s) {
                    Log.i(TAG, "signup successful!");

//                    LiquidUser.setUsername(mEmail);
//                    LiquidUser.setUserId(MeteorSingleton.getInstance().getUserId());

                    startActivity(askIntent);
                    finish();
                }

                @Override
                public void onError(String s, String reason, String s2) {
                    showProgress(false);
                    switch(reason){
                        case "Incorrect password":
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                    }

                    Toast.makeText(SignupActivity.this, reason, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Used to display error dialogs
     *
     * @param title title of dialog
     * @param msg   details of error
     */
    private void showError(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Called on resume of activity and initial startup
     */

    protected void onResume() {
        super.onResume();
    }


}
