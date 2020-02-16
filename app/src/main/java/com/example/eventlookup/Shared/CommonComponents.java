package com.example.eventlookup.Shared;

import android.content.Intent;
import android.view.MenuItem;

import com.example.eventlookup.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

public class CommonComponents extends AppCompatActivity {

//    protected void bottomNavigationConf(BottomNavigationView bottomNavigationView, NavController navController){
//
//        BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                switch(menuItem.getItemId())
//                {
//                    case R.id.action_events:
//                        if (className != EventActivity.class ){
//                            selectedIntent = new Intent( getApplicationContext() , EventActivity.class );
//                            startActivity( selectedIntent );
//                        }
//                        else{
//                            return false;
//                        }
//                        break;
//                    case R.id.action_account:
//                        if (className != AccountActivity.class ){
//                            selectedIntent = new Intent( getApplicationContext() , AccountActivity.class );
//                            startActivity( selectedIntent );
//                        }
//                        else{
//                            return false;
//                        }
//                        break;
//                    case R.id.action_more:
//                        if (className == EventActivity.class ){
//                            return false;
//                        }
//                    default:
//                        return false;
//                }
//
//                return false;
//            }
//        };
//        bottomNavigationView.setOnNavigationItemSelectedListener( bottomNavListener );
//    }



}
