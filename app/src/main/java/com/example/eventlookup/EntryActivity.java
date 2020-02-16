package com.example.eventlookup;

import android.os.Bundle;

import com.example.eventlookup.Shared.CommonComponents;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class EntryActivity extends CommonComponents {

    private @Nullable
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_entry );

        setupNavigation();
    }

    protected void setupNavigation() throws NullPointerException{
        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_navigation );

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById( R.id.nav_host_fragment );
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController() );

    }

}
