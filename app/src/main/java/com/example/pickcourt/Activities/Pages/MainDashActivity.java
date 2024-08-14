package com.example.pickcourt.Activities.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pickcourt.Activities.Authentication.LoginActivity;
import com.example.pickcourt.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainDashActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private AppCompatImageView menu_ICON;
    private String idToken;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        findViews();
        initViews();
    }



    private void findViews() {
        drawer_layout = findViewById(R.id.drawer_layout);
        navigation_view = findViewById(R.id.navigation_view);
        menu_ICON = findViewById(R.id.menu_ICON);
    }

    private void initViews() {
        navigation_view.bringToFront();
        navigation_view.setNavigationItemSelectedListener(this);
        navigation_view.setCheckedItem(R.id.nav_home);
        menu_ICON.setOnClickListener((v) -> openNavDrawer());
        updateNavigationMenu(navigation_view.getMenu(),currentUser.isAnonymous());
    }

    private void updateNavigationMenu(Menu menu, boolean isAnonymous) {
        MenuItem loginItem = menu.findItem(R.id.nav_login);
        MenuItem logoutItem = menu.findItem(R.id.nav_logout);
        MenuItem profileItem = menu.findItem(R.id.nav_profile);

        if (isAnonymous) {
            // User is anonymous, show login, hide logout and profile
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
            profileItem.setVisible(false);
        } else {
            // User is authenticated, hide login, show logout and profile
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
            profileItem.setVisible(true);
        }

    }


    private void openNavDrawer() {
        if(drawer_layout.isDrawerVisible(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START);
        else drawer_layout.openDrawer(GravityCompat.START);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.nav_login || itemId == R.id.nav_logout){
            Intent loginIntent = new Intent(MainDashActivity.this, LoginActivity.class);
            mAuth.signOut();
            startActivity(loginIntent);
            finish();
        }




        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer_layout.isDrawerVisible(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}