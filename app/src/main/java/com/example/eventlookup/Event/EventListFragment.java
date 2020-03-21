package com.example.eventlookup.Event;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.eventlookup.Event.Adapters.EventAdapter;
import com.example.eventlookup.Event.POJOs.EventListItemPOJO;
import com.example.eventlookup.R;
import com.example.eventlookup.Shared.AppConf;
import com.example.eventlookup.Shared.CacheInterceptor;
import com.example.eventlookup.Shared.MainThreadOkHttpCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
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

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class EventListFragment extends Fragment {
    private final String TAG = "EventList";
    private final int UPCOMING_EVENTS = 0;
    private final int FINISHED_EVENTS = 1;

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View mThisFragmentView;
    private TabLayout mTabLayout;

    private OkHttpClient okHttpClient;
    private ArrayList<EventListItemPOJO> eventsList;
    private ArrayList<String> eventsTitles;

    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;
    private FrameLayout progressBarHolder;
    private ConstraintLayout mSearchLayout;
    private FloatingActionButton mFABSearch;
    private ImageButton mActionSearchClose;
    private ImageButton mActionSearchContent;
    private AutoCompleteTextView mACTVSearch;

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
        progressBarHolder.setVisibility( View.VISIBLE );
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
        recyclerView.setAdapter( eventAdapter );

        fetchDataForAdapter();

    }

    private void fetchDataForAdapter(){
        try {
            getEventListFromApi();
        }
        catch (Exception e){
            Log.e( "fetchDataForAdapter", "getEventListData api call failed " + e );
        }
    }

    private void getEventListFromApi() throws Exception {
        File httpCacheDirectory = new File(getContext().getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( httpCacheDirectory, cacheSize );

        okHttpClient = new OkHttpClient.Builder(  ).addNetworkInterceptor( new CacheInterceptor() )
                .cache( cache )
                .build();

//        final ArrayList<EventListItemPOJO> eventsList = new ArrayList<>();

        AppConf apiConf = AppConf.getInstance();
        String eventsListApiRoute = apiConf.getEventGetListApiRoute();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(eventsListApiRoute)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        Request request = new Request.Builder(  )
                .url( url )
                .build();

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONArray responseRoot = new JSONArray( body );

                    for(int i = 0; i < responseRoot.length(); i++){

                        JSONObject jObj = responseRoot.getJSONObject( i );
                        EventListItemPOJO eventListItemPOJO = new EventListItemPOJO(
                                jObj.getString( "Id" ),
                                jObj.getString("CoverImagePath"),
                                jObj.getString( "Title" ),
                                jObj.getString( "ShortDescription" ),
                                jObj.getString( "AddressCountryCityStreet1" ),
                                jObj.getString( "StartDate" )
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
                ArrayList<EventListItemPOJO> filteredEvents = filterEventsByName( mACTVSearch.getText().toString() );
                eventAdapter.addItems( filteredEvents );
                recyclerView.setAdapter( eventAdapter );

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


}
