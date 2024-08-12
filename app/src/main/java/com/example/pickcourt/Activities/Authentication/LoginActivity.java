package com.example.pickcourt.Activities.Authentication;


import android.content.Intent;
import android.content.IntentSender;

import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.pickcourt.Activities.BaseActivity;
import com.example.pickcourt.Activities.HomeActivity;
import com.example.pickcourt.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private TextInputEditText login_TEXTINPUT_email;
    private TextInputEditText login_TEXTINPUT_password;
    private MaterialButton login_BTN;
    private MaterialTextView login_TEXTVIEW_signup;
    private MaterialButton guest_entry_BTN;

    private AppCompatImageButton facebook_BTN;
    private AppCompatImageButton google_BTN;
    private AppCompatImageButton microsoft_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        findViews();
        initViews();


        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

     }


    private void findViews() {
        login_TEXTINPUT_email = findViewById(R.id.login_TEXTINPUT_email);
        login_TEXTINPUT_password = findViewById(R.id.login_TEXTINPUT_password);
        login_TEXTVIEW_signup = findViewById(R.id.login_TEXTVIEW_signup);
        facebook_BTN = findViewById(R.id.facebook_BTN);
        google_BTN = findViewById(R.id.google_BTN);
        microsoft_BTN = findViewById(R.id.microsoft_BTN);
        login_BTN = findViewById(R.id.login_BTN);
        guest_entry_BTN = findViewById(R.id.guest_entry_BTN);
    }

    private void initViews() {
        login_BTN.setOnClickListener((v) -> loginUser());
        login_TEXTVIEW_signup.setOnClickListener((v) -> redirectToSignup());
        guest_entry_BTN.setOnClickListener((v) -> enterAsGuest());
        google_BTN.setOnClickListener((v) -> loginByGoogle());
        microsoft_BTN.setOnClickListener((v) -> loginByMicrosoft());
        facebook_BTN.setOnClickListener((v) -> loginByFacebook());
        Intent previousIntent = getIntent();
        if(previousIntent != null){
            String email = previousIntent.getStringExtra("email");
            login_TEXTINPUT_email.setText(email);
        }
    }


    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            Log.d("Already here","Already Signed login Activity");
    }

    private void loginUser() {
        String email = login_TEXTINPUT_email.getText().toString().trim();
        String password = login_TEXTINPUT_password.getText().toString().trim();

        if (email.isEmpty()) {
            login_TEXTINPUT_email.setError("Email cannot be empty");
            return;
        }

        if (password.isEmpty()) {
            login_TEXTINPUT_password.setError("Password cannot be empty");
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                           Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void enterAsGuest() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginByFacebook() {
    }

    private void loginByMicrosoft() {
    }

    private void loginByGoogle() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), RC_SIGN_IN,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("GoogleSignIn", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("GoogleSignIn", e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                }
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Signed in as " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void redirectToSignup() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }



}