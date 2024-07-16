package com.example.pickcourt.Activities.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pickcourt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText login_TEXTINPUT_username;
    private TextInputEditText login_TEXTINPUT_password;
    private MaterialButton login_BTN;
    private MaterialTextView login_TEXTVIEW_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        findViews();
        initViews();
    }

    private void findViews() {
        login_TEXTINPUT_username = findViewById(R.id.login_TEXTINPUT_username);
        login_TEXTINPUT_password = findViewById(R.id.login_TEXTINPUT_password);
        login_TEXTVIEW_signup = findViewById(R.id.login_TEXTVIEW_signup);
        login_BTN = findViewById(R.id.login_BTN);
    }

    private void initViews() {
        login_BTN.setOnClickListener((v) -> loginUser());
        login_TEXTVIEW_signup.setOnClickListener((v) -> redirectToSignup());
    }


    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            Log.d("Already here","Already Signed login Activity");
    }

    private void loginUser() {
        String email = login_TEXTINPUT_username.getText().toString().trim();
        String password = login_TEXTINPUT_password.getText().toString().trim();

        if (email.isEmpty()) {
            login_TEXTINPUT_username.setError("Email cannot be empty");
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
                        } else {
                           Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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