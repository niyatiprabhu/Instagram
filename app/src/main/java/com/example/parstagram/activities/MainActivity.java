package com.example.parstagram.activities;

import static com.example.parstagram.R.*;
import static com.example.parstagram.R.layout.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.parstagram.R;
import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.FeedFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.example.parstagram.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "Main Activity";

    private Button btnLogout;

    FeedFragment feedFragment = new FeedFragment();
    ComposeFragment composeFragment = new ComposeFragment(MainActivity.this);
    ProfileFragment profileFragment = new ProfileFragment();


    public BottomNavigationView bottomNavigationView;

    public void goToFeedFrag() {
        bottomNavigationView.setSelectedItemId(id.action_home);
    }

    public void goToProfileFrag(User user) {
        bottomNavigationView.setSelectedItemId(id.action_profile);
        profileFragment.user = user;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);



        final FragmentManager fragmentManager = getSupportFragmentManager();

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        // Find the toolbar view inside the activity layout
        //Toolbar toolbar = findViewById(toolbar_main);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case id.action_home:
                        fragment = feedFragment;
                        break;
                    case id.action_compose:
                        fragment = composeFragment;
                        break;
                    case id.action_profile:
                        profileFragment.user = (User) ParseUser.getCurrentUser();
                        fragment = profileFragment;
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(id.flContainer, fragment).commit();
                return true;
            }
        });
        // this sets the default selection
        bottomNavigationView.setSelectedItemId(id.action_home);
    }





}