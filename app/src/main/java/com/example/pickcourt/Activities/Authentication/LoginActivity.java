package com.example.pickcourt.Activities.Authentication;


import android.content.Intent;
import android.content.IntentSender;


import android.os.Bundle;

import android.util.Log;

import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.pickcourt.Activities.BaseActivity;
import com.example.pickcourt.Activities.MainDashActivity;
import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.R;

import com.example.pickcourt.Utilities.SignalManager;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private TextInputEditText login_TEXTINPUT_email;
    private TextInputEditText login_TEXTINPUT_password;
    private MaterialButton login_BTN;
    private MaterialTextView login_TEXTVIEW_signup;
    private MaterialTextView login_TEXTVIEW_forgot_password;
    private MaterialButton guest_entry_BTN;
    private AppCompatImageButton google_BTN;
    private SignalManager signalManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        signalManager = SignalManager.getInstance();

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        findViews();
        initViews();
     }


    private void findViews() {
        login_TEXTINPUT_email = findViewById(R.id.login_TEXTINPUT_email);
        login_TEXTINPUT_password = findViewById(R.id.login_TEXTINPUT_password);
        login_TEXTVIEW_signup = findViewById(R.id.login_TEXTVIEW_signup);
        login_TEXTVIEW_forgot_password = findViewById(R.id.login_TEXTVIEW_forgot_password);
        google_BTN = findViewById(R.id.google_BTN);
        login_BTN = findViewById(R.id.login_BTN);
        guest_entry_BTN = findViewById(R.id.guest_entry_BTN);
    }

    private void initViews() {
        Intent previousIntent = getIntent();
        if(previousIntent != null){
            String email = previousIntent.getStringExtra("email");
            login_TEXTINPUT_email.setText(email);
        }

        login_BTN.setOnClickListener((v) -> loginUser());
        login_TEXTVIEW_signup.setOnClickListener((v) -> redirectToSignup());
        login_TEXTVIEW_forgot_password.setOnClickListener((v) -> resetPassword());
        guest_entry_BTN.setOnClickListener((v) -> enterAsGuest());
        google_BTN.setOnClickListener((v) -> loginByGoogle());
        login_TEXTINPUT_email.setText("hasshlomi21@gmail.com");
        login_TEXTINPUT_password.setText("yhyh123123");
    }

    private void resetPassword() {
        String email = login_TEXTINPUT_email.getText() != null ? login_TEXTINPUT_email.getText().toString().trim() : "";

        if (email.isEmpty()) {
            login_TEXTINPUT_email.setError("Please enter your email address");
            login_TEXTINPUT_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_TEXTINPUT_email.setError("Please enter a valid email address");
            login_TEXTINPUT_email.requestFocus();
            return;
        }

        // Attempt to create a user to check if the email already exists
        mAuth.createUserWithEmailAndPassword(email, "temporaryPassword")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Delete the temporary user
                        Objects.requireNonNull(mAuth.getCurrentUser()).delete()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        Log.d("ResetPassword", "Temporary user deleted");
                                    } else {
                                        Log.e("ResetPassword", "Error deleting temporary user: " + deleteTask.getException().getMessage());
                                    }
                                });
                        Toast.makeText(LoginActivity.this, "No account found with this email", Toast.LENGTH_LONG).show();
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthUserCollisionException) {
                            sendResetEmail(email);
                        } else {
                            String errorMessage = exception.getMessage();
                            signalManager.toast("Error: " + errorMessage);
                        }
                    }
                });
    }

    private void sendResetEmail(String email) {
        Log.d("ResetPassword", "Sending reset email to: " + email);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        signalManager.toast("Password reset email sent");
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Failed to send reset email";
                        signalManager.toast("Error: " + errorMessage);
                    }
                });
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) Log.d("Already here","Already Signed login Activity");
    }

    private void loginUser() {
        String email = "";
        String password = "";
        if (login_TEXTINPUT_email.getText() != null)
            email = login_TEXTINPUT_email.getText().toString().trim();
        if (login_TEXTINPUT_password.getText() != null)
            password = login_TEXTINPUT_password.getText().toString().trim();

        if (email.isEmpty()) {
            login_TEXTINPUT_email.setError("Email cannot be empty");
            return;
        }
        if (password.isEmpty()) {
            login_TEXTINPUT_password.setError("Password cannot be empty");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        signalManager.toast("Login Successful");
                        navigateToMainDashboard();
                    } else SignalManager.getInstance().toast("Login Failed: " + task.getException().getMessage());
                });
    }



    private void enterAsGuest() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        signalManager.toast("Entered as Guest");
                        navigateToMainDashboard();
                    } else SignalManager.getInstance().toast("Authentication failed.");
                });
    }


    private void loginByGoogle() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), RC_SIGN_IN,
                                null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("GoogleSignIn", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                }).addOnFailureListener(this, e -> Log.d("GoogleSignIn", Objects.requireNonNull(e.getLocalizedMessage())));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) firebaseAuthWithGoogle(idToken);
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                            DataBaseManager.getInstance().setUserExistCallback(this::handleUserExistence);
                            DataBaseManager.getInstance().checkIfUserExists(user.getUid());
                    } else SignalManager.getInstance().toast("Authentication failed.");
                });
    }

    private void handleUserExistence(boolean exists) {
        if (exists) SignalManager.getInstance().toast("Welcome back!");
        else DataBaseManager.getInstance().createNewUser(mAuth.getCurrentUser().getUid());
        navigateToMainDashboard();
    }


    private void navigateToMainDashboard() {
        Intent intent = new Intent(this, MainDashActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToSignup() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }


}