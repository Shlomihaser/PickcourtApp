package com.example.pickcourt.Activities.Authentication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pickcourt.Activities.BaseActivity;
import com.example.pickcourt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText signup_TEXTINPUT_email;
    private TextInputEditText signup_TEXTINPUT_password;
    private MaterialTextView signup_TEXTVIEW_login;
    private MaterialButton signup_BTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        findViews();
        initViews();
    }

    private void findViews() {
        signup_TEXTINPUT_email = findViewById(R.id.signup_TEXTINPUT_email);
        signup_TEXTINPUT_password = findViewById(R.id.signup_TEXTINPUT_password);
        signup_TEXTVIEW_login = findViewById(R.id.signup_TEXTVIEW_login);
        signup_BTN = findViewById(R.id.signup_BTN);
    }
    private void initViews() {
        signup_BTN.setOnClickListener((v) -> signUpUser());
        signup_TEXTVIEW_login.setOnClickListener((v) -> redirectToLogin());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            Log.d("Already here","Already Signed SignUp Activity");
    }

    private void signUpUser(){
        String email = "";
        String password = "";
        if(signup_TEXTINPUT_email.getText() != null)
            email = signup_TEXTINPUT_email.getText().toString().trim();
        if(signup_TEXTINPUT_password.getText() != null)
            password = signup_TEXTINPUT_password.getText().toString().trim();

        if (email.isEmpty()) {
            signup_TEXTINPUT_email.setError("Email cannot be empty");
            return;
        }

        if (password.isEmpty()) {
            signup_TEXTINPUT_password.setError("Password cannot be empty");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser(); // user options
                            Toast.makeText(SignUpActivity.this,"Signup Success! Try Login",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                            assert user != null;
                            i.putExtra("email",user.getEmail());
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Signup failed. Try Again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }





}