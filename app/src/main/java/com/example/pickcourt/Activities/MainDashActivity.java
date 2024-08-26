package com.example.pickcourt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pickcourt.Activities.Authentication.LoginActivity;
import com.example.pickcourt.Fragments.AboutFragment;
import com.example.pickcourt.Fragments.ContactUsFragment;
import com.example.pickcourt.Fragments.FavoritesFragment;
import com.example.pickcourt.Fragments.HomeFragment;
import com.example.pickcourt.Fragments.LoginGuidanceFragment;
import com.example.pickcourt.Fragments.PaymentsFragment;
import com.example.pickcourt.Fragments.ReservationsFragment;
import com.example.pickcourt.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainDashActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private AppCompatImageView menu_ICON;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FrameLayout dash_frame;
    private LinearLayout toolbar;

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
      //  menu_ICON = findViewById(R.id.menu_ICON);
        toolbar = findViewById(R.id.toolbar);
    }

    private void initViews() {
        navigation_view.bringToFront();
        navigation_view.setNavigationItemSelectedListener(this);
        navigation_view.setCheckedItem(R.id.nav_home);
        updateNavigationMenu(navigation_view.getMenu(),currentUser.isAnonymous());
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dash_frame, homeFragment)
                .commit();
        homeFragment.setOpenDrawerCallback(this::openNavDrawer);
    }

    private void updateNavigationMenu(Menu menu, boolean isAnonymous) {
        MenuItem loginItem = menu.findItem(R.id.nav_login);
        MenuItem logoutItem = menu.findItem(R.id.nav_logout);

        if (isAnonymous) { // User is anonymous, show login, hide logout and profile
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
        } else { // User is authenticated, hide login, show logout and profile
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
        }
    }


    private void openNavDrawer() {
        if(drawer_layout.isDrawerVisible(GravityCompat.START)) drawer_layout.closeDrawer(GravityCompat.START);
        else drawer_layout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        FrameLayout frameLayout = findViewById(R.id.dash_frame);
        frameLayout.removeAllViews();

        if(itemId == R.id.nav_login || itemId == R.id.nav_logout) {
            Intent loginIntent = new Intent(MainDashActivity.this, LoginActivity.class);
            mAuth.signOut();
            startActivity(loginIntent);
            finish();
        } else if(itemId == R.id.nav_payments) {
            if (!currentUser.isAnonymous()) {
                PaymentsFragment paymentsFragment = new PaymentsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dash_frame, paymentsFragment)
                        .commit();
                paymentsFragment.setOpenDrawerCallback(this::openNavDrawer);
            } else {
                LoginGuidanceFragment loginGuidanceFragment = new LoginGuidanceFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dash_frame, loginGuidanceFragment)
                        .commit();
                loginGuidanceFragment.setOpenDrawerCallback(this::openNavDrawer);
            }
        } else if(itemId == R.id.nav_about){
            AboutFragment aboutFragment = new AboutFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dash_frame, aboutFragment)
                    .commit();
            aboutFragment.setOpenDrawerCallback(this::openNavDrawer);
        } else if(itemId == R.id.nav_contact_us){
            ContactUsFragment contactUsFragment = new ContactUsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dash_frame, contactUsFragment)
                    .commit();
            contactUsFragment.setOpenDrawerCallback(this::openNavDrawer);
        } else if(itemId == R.id.nav_home){
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dash_frame, homeFragment)
                    .commit();
            homeFragment.setOpenDrawerCallback(this::openNavDrawer);
        } else if(itemId == R.id.nav_favorites){
            FavoritesFragment favoritesFragment = new FavoritesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dash_frame, favoritesFragment)
                    .commit();
            favoritesFragment.setOpenDrawerCallback(this::openNavDrawer);
        } else if(itemId == R.id.nav_reservations){
            ReservationsFragment reservationsFragment = new ReservationsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dash_frame, reservationsFragment)
                    .commit();
            reservationsFragment.setOpenDrawerCallback(this::openNavDrawer);
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