package com.example.proyecto_ed_vd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashBoardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Perfil");

        firebaseAuth = FirebaseAuth.getInstance();

        //bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        HomeFragment fragment = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment,"");
        ft1.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        actionBar.setTitle("Home");
                        HomeFragment fragment = new HomeFragment();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content,fragment,"");
                        ft1.commit();
                        return true;
                    case R.id.nav_profile:
                        actionBar.setTitle("Profile");
                        ProfileFragment fragment1 = new ProfileFragment();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.content,fragment1,"");
                        ft2.commit();
                        return true;
                    case R.id.nav_users:
                        actionBar.setTitle("Users");
                        UsersFragment fragment2 = new UsersFragment();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.content,fragment2,"");
                        ft3.commit();
                        return true;
                    case R.id.nav_chat:
                        actionBar.setTitle("Chat");
                        ChatListFragment fragment3 = new ChatListFragment();
                        FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.content,fragment3,"");
                        ft4.commit();
                        return true;
                    case R.id.nav_more:
                        actionBar.setTitle("More");
                        MoreFragment fragment4 = new MoreFragment();
                        FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                        ft5.replace(R.id.content,fragment4,"");
                        ft5.commit();
                        return true;
                }
                return false;
            }
        };

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
        }
        else {
            startActivity(new Intent(DashBoardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }


}