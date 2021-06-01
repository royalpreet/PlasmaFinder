package com.example.plasmafinder.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.plasmafinder.Dialogs.DeleteDialog;
import com.example.plasmafinder.Dialogs.LogOutDialog;
import com.example.plasmafinder.Login.LoginActivity;
import com.example.plasmafinder.Models.User;
import com.example.plasmafinder.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpFirebaseAuth();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Plasma Finder");

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.nav_my_donors:
                        fragment = new MyDonorsFragment();
                        break;
                    case R.id.nav_my_requests:
                        fragment = new MyRequestsFragment();
                        break;
                    case R.id.nav_donors:
                        fragment = new DonorsFragment();
                        break;
                    case R.id.nav_requests:
                        fragment = new RequestsFragment();
                        break;
                    case R.id.log_out:
                        LogOutDialog logOutDialog = new LogOutDialog();
                        logOutDialog.show(getSupportFragmentManager(), "Log out Dialog");
                        return true;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if(getIntent().hasExtra("request")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyRequestsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_my_requests);
        }
        else if(getIntent().hasExtra("donor")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyDonorsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_my_donors);
        }
        else if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonorsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_donors);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void setUpFirebaseAuth() {
        Log.d(TAG, "setUpFirebaseAuth: setting up firebase auth");
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        //mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        this.mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String signedIn = "onAuthStateChanged: " + user.getUid() + " signed in";
                    Log.d(TAG, signedIn);
                    return;
                }
                Log.d(TAG, "onAuthStateChanged: signed out");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mAuth.removeAuthStateListener(this.mAuthStateListener);
    }
}