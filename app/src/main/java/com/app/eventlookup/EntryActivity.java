package com.app.eventlookup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.eventlookup.Shared.AppConf;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class EntryActivity extends AppCompatActivity {

    private @Nullable
    NavController navController;

    private boolean mLocationPermissionGranted = false;
    private boolean mNetworkPermissionGranted = false;
    private final String TAG = "EntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_entry );

        setupNavigation();
        checkAppServices();
    }

    protected void setupNavigation() throws NullPointerException{
        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_navigation );

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById( R.id.nav_host_fragment );
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController() );
    }

    private boolean checkAppServices(){
        return isGooglePlayServiceWorking() && isLocationEnabled();
    }

    public boolean isGooglePlayServiceWorking(){
        GoogleApiAvailability gApiAv = GoogleApiAvailability.getInstance();
        int resultCode = gApiAv.isGooglePlayServicesAvailable(EntryActivity.this);

        if(resultCode == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            return true;
        }
        else if(gApiAv.isUserResolvableError(resultCode)){
            //an error occured but we can resolve it
            gApiAv.getErrorDialog(EntryActivity.this, resultCode, AppConf.ERROR_DIALOG_REQUEST).show();
        }else{
            Toast.makeText(this, "Error with google play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isLocationEnabled(){
        final LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        try{
            // Check if GPS and Network are enabled
            if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )
                    && !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {
                buildAlertMessageNoGpsOrNetwork();
                return false;
            }
        }
        catch(NullPointerException e){
            Log.d(TAG, "isLocationEnabled provider error" + e);
        }

        return true;
    }

    private void buildAlertMessageNoGpsOrNetwork() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS access to work properly")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick( final DialogInterface dialog, final int id ) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, AppConf.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppConf.PERMISSIONS_REQUEST_ENABLE_GPS){
            if(!mLocationPermissionGranted)
                getLocationPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkAppServices()){
            if(!mLocationPermissionGranted){
                getLocationPermission();
            }
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    AppConf.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if(requestCode == AppConf.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
    }

}
