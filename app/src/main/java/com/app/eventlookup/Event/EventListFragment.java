package com.app.eventlookup.Event;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.app.eventlookup.Event.Adapters.EventAdapter;
import com.app.eventlookup.Event.POJOs.EventListItemPOJO;
import com.app.eventlookup.R;
import com.app.eventlookup.Shared.APIRequest;
import com.app.eventlookup.Shared.AppConf;
import com.app.eventlookup.Shared.MainThreadOkHttpCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// With HTTP calls related libs
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class EventListFragment extends Fragment
        implements com.app.eventlookup.Event.Adapters.EventAdapter.EventAdapterCallback {
    private final String TAG = "EventListFragment";
    private final int UPCOMING_EVENTS = 0;
    private final int FINISHED_EVENTS = 1;

    // application classes
    private APIRequest apiRequest;

    // framework components
    private LinearLayoutManager linearLayoutManager;
    private OkHttpClient okHttpClient;

    // layout vars
    private EventAdapter eventAdapter;
    private RecyclerView recyclerView;
    private TabLayout mTabLayout;
    private View mThisFragmentView;
    private ArrayList<String> eventsTitles;
    private ArrayList<EventListItemPOJO> eventsList;
    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;
    private FrameLayout progressBarHolder;
    private ConstraintLayout mSearchLayout;
    private FloatingActionButton mFABSearch;
    private ImageButton mActionSearchClose;
    private ImageButton mActionSearchContent;
    private AutoCompleteTextView mACTVSearch;
    private SeekBar mSeekBar;
    private EditText mETKmRadius;

    public EventListFragment(){
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_event_list, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        mThisFragmentView = view;

        bindViews( view );
        prepareListeners( view );
        setupRecyclerView();
    }

    private void setupRecyclerView(){

        linearLayoutManager = new LinearLayoutManager( this.getContext() );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );

        eventAdapter = new EventAdapter(  );
        eventAdapter.addItems( eventsList );
        eventAdapter.setCallback( this );
        recyclerView.setAdapter( eventAdapter );

        fetchDataForAdapter();

    }

    private void fetchDataForAdapter(){
        try {
            progressBarHolder.setVisibility( View.VISIBLE );
            getEventListFromApi();
        }
        catch (Exception e){
            Log.e( "fetchDataForAdapter", "getEventListData api call failed " + e );
        }
    }

    private void getEventListFromApi() throws Exception {
        okHttpClient = apiRequest.generateOkHttpClient();
        AppConf apiConf = AppConf.getInstance();
        String eventsListApiRoute = apiConf.getEventGetListApiRoute();

        Request request = apiRequest.getRequestObject( eventsListApiRoute, false, false, "", null );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONArray responseRoot = new JSONArray( body );

                    for(int i = 0; i < responseRoot.length(); i++){

                        JSONObject jObj = responseRoot.getJSONObject( i );
                        EventListItemPOJO eventListItemPOJO = new EventListItemPOJO(
                                jObj.getString( "EventId" ),
                                jObj.getString("CoverImagePath"),
                                jObj.getString( "Title" ),
                                jObj.getString( "ShortDescription" ),
                                jObj.getString( "AddressCountryCityStreet1" ),
                                jObj.getString( "StartDate" ),
                                jObj.getString( "FinishDate" ),
                                Float.parseFloat( jObj.getString("AddressLat") ),
                                Float.parseFloat(jObj.getString("AddressLng") )
                        );
                        eventsList.add( eventListItemPOJO );
                        eventsTitles.add(eventListItemPOJO.getEventTitle());
                    }
                    mTabLayout.getTabAt( mTabLayout.getSelectedTabPosition() ).select();

                    progressBarHolder.setVisibility( View.GONE );
                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/event response data - " + e);
                }
            }

            @Override
            public void apiCallFail(Exception e){
                progressBarHolder.setVisibility( View.GONE );
                Log.e("OkHttp", "Api call http://<host>/api/event failed");
            }

        } );

    }

    private void bindViews(View view){
        progressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );
        recyclerView = view.findViewById( R.id.RVL_events_list );
        eventsList = new ArrayList<>(  );
        eventsTitles = new ArrayList<>(  );
        mSearchLayout = view.findViewById( R.id.CL_events_list_search );
        mFABSearch = view.findViewById( R.id.FAB_events_list_search );
        mActionSearchClose = view.findViewById( R.id.IV_event_list_action_close );
        mActionSearchContent = view.findViewById( R.id.IV_event_list_action_search );
        mACTVSearch = view.findViewById( R.id.ACTV_events_list_search );
        mTabLayout = view.findViewById( R.id.TBL_events_tab_layout );
        mSeekBar = view.findViewById( R.id.SeekB_events_radius_seekbar );
        mETKmRadius = view.findViewById( R.id.ET_events_radius );
        apiRequest = new APIRequest( getContext() );
    }

    private void prepareListeners(View view){

        mFABSearch.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSearchLayout.setVisibility( View.VISIBLE );

            }
        });
        mActionSearchClose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchLayout.setVisibility( View.GONE );
            }
        } );

        ArrayAdapter<String> ACTVAdapter = new ArrayAdapter<String>( getContext(), android.R.layout.simple_list_item_1, eventsTitles );
        mACTVSearch.setAdapter( ACTVAdapter );


        mActionSearchContent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterEvents();

                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
                try {
                    inputMethodManager.hideSoftInputFromWindow( mThisFragmentView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
                }
                catch (NullPointerException e){
                    Log.e(TAG, e.toString());
                }
                mActionSearchClose.callOnClick();
            }
        } );

        mTabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch(tab.getPosition()){
                    case UPCOMING_EVENTS:
                        eventAdapter.addItems( filterEventsByDate( UPCOMING_EVENTS ) );
                        recyclerView.setAdapter( eventAdapter );
                        break;
                    case FINISHED_EVENTS:
                        eventAdapter.addItems( filterEventsByDate( FINISHED_EVENTS ) );
                        recyclerView.setAdapter( eventAdapter );
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected( tab );
            }
        } );

        mSeekBar.setMax( 200 );
        mSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mETKmRadius.setText( i + " KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );
    }


    /**
     *
     * @param name (String) Event name
     * @return ArrayList<EventListItemPOJO> list
     */
    private ArrayList<EventListItemPOJO> filterEventsByName(String name){
        ArrayList<EventListItemPOJO> sortedEvents = new ArrayList<>(  );

        for(EventListItemPOJO event : eventsList){
            if(event.getEventTitle().toLowerCase().contains( name.toLowerCase() )){
                sortedEvents.add( event );
            }
        }

        return sortedEvents;
    }

    /**
     *
     * @param tabCase (int) (0 for upcoming events, 1 for finished)
     * @return ArrayList<EventListItemPOJO> list
     */
    private  ArrayList<EventListItemPOJO> filterEventsByDate(int tabCase){
        ArrayList<EventListItemPOJO> filteredEvents = new ArrayList<>(  );
        Date nowDate = new Date();

        for(EventListItemPOJO event : eventsList){
            Date eventDate = event.getFormattedEventDateObject( event.getEventDate() );

            if(tabCase == UPCOMING_EVENTS) {
                if (nowDate.compareTo( eventDate ) < 0)
                    filteredEvents.add( event );
            }
            else{
                if (nowDate.compareTo( eventDate ) > 0)
                    filteredEvents.add( event );
            }

        }

        return filteredEvents;
    }

    private ArrayList<EventListItemPOJO> filterEventByRadius(int radius){
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    AppConf.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        LocationManager mLocationManager = (LocationManager) getContext().getSystemService( Context.LOCATION_SERVICE );
        Location currentLocation = mLocationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
        ArrayList<EventListItemPOJO> filteredEvents = new ArrayList<>(  );

        for(EventListItemPOJO event : eventsList){
            LatLng eventCoords = event.getCoordinatesLatLng();

            float[] results = new float[1];
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                    eventCoords.latitude, eventCoords.longitude, results);

            float distanceBetweenInKM = results[0];
            if((distanceBetweenInKM/1000) < radius){
                filteredEvents.add( event );
            }
        }

        return filteredEvents;
    }

    private void filterEvents() {
        ArrayList<EventListItemPOJO> filteredEvents = new ArrayList<>();
        if (!mACTVSearch.getText().toString().equals( "" )) {
            filteredEvents = filterEventsByName( mACTVSearch.getText().toString() );
        }
        else if( mSeekBar.getProgress() != 0 ){
            filteredEvents = filterEventByRadius(mSeekBar.getProgress());
        }
        else
            return;

        eventAdapter.addItems( filteredEvents );
        recyclerView.setAdapter( eventAdapter );
    }

    public void onEmptyViewRetryClick() {
        fetchDataForAdapter();
    }
}
