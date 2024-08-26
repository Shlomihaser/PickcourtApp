package com.example.pickcourt.Activities;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.pickcourt.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupTouchListener();
    }

    private void setupTouchListener() {
        View rootLayout = findViewById(android.R.id.content);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View focusedView = getCurrentFocus();
                    if (focusedView != null) {
                        Rect outRect = new Rect();
                        focusedView.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            focusedView.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
    }

}