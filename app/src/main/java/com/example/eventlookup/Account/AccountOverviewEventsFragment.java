package com.example.eventlookup.Account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.eventlookup.Account.Adapters.AccountEventsListAdapter;
import com.example.eventlookup.Account.POJOs.AccountEventPOJO;
import com.example.eventlookup.Account.POJOs.AccountOverviewInfoPOJO;
import com.example.eventlookup.R;
import com.example.eventlookup.Shared.APIRequest;
import com.example.eventlookup.Shared.AppConf;
import com.example.eventlookup.Shared.CacheInterceptor;
import com.example.eventlookup.Shared.MainThreadOkHttpCallback;
import com.example.eventlookup.Shared.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AccountOverviewEventsFragment extends Fragment
        implements AccountEventsListAdapter.AELACallback {
    private final String TAG = "AccountEventsFragment";
    private final int INTERESTED_EVENTS = 1; // Matching with TabLayout Tab's position
    private final int GOING_EVENTS = 0; // Matching with TabLayout Tab's position

    // application classes
    private APIRequest apiRequest;
    private Utils mUtils;

    // framework components
    private NavController mNavController;
    private View mThisFrag;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TabLayout mTabLayout;
    private FrameLayout mProgressBarHolder;
    private OkHttpClient mOkHttpClient;
    private MediaType mMediaType;

    // layout vars
    private final int UPCOMING_EVENTS = 0;
    private final int FINISHED_EVENTS = 1;
    private AccountEventsListAdapter mEventAdapter;
    private ArrayList<AccountEventPOJO> mEventsList;
    private FloatingActionButton mBtnEventRemove;
    private OkHttpClient okHttpClient;
    private AccountEventsListAdapter.AELACallback mAdapterCallback;

    public AccountOverviewEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_account_overview_events, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        mProgressBarHolder.setVisibility( View.VISIBLE );
        prepareListeners( view );
        setupRecyclerView();
    }

    private void prepareLayoutComponents(View view){
        mThisFrag = view;
        mNavController = Navigation.findNavController( getParentFragment().getView() );
        mRecyclerView = view.findViewById( R.id.RVL_account_events_list );
        mUtils = new Utils();
        mProgressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );
        mTabLayout = view.findViewById( R.id.TBL_account_events_tab_layout );
        mMediaType = MediaType.parse(AppConf.JsonMediaTypeString);
        apiRequest = new APIRequest( getContext() );
    }

    private void prepareListeners(View view){
        mTabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch(tab.getPosition()){
                    case INTERESTED_EVENTS:
                        mEventAdapter.addItems( filterEventsAttribute( INTERESTED_EVENTS ) );
                        mRecyclerView.setAdapter( mEventAdapter );
                        break;
                    case GOING_EVENTS:
                        mEventAdapter.addItems( filterEventsAttribute( GOING_EVENTS ) );
                        mRecyclerView.setAdapter( mEventAdapter );
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

    private void setupRecyclerView(){
        mLinearLayoutManager = new LinearLayoutManager( getParentFragment().getContext() );
        mLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        mRecyclerView.setLayoutManager( mLinearLayoutManager );
        mRecyclerView.setItemAnimator( new DefaultItemAnimator() );

        mEventAdapter = new AccountEventsListAdapter(  );
        mEventAdapter.addItems( mEventsList );
        mEventAdapter.setParentView( getParentFragment().getView() );
        mEventAdapter.setCallback( this );
        mRecyclerView.setAdapter( mEventAdapter );

        fetchUserData();
    }

    private JSONObject formJsonObjectForLogin(String userId){
        JSONObject jsonObject = new JSONObject(  );
        try {
                jsonObject.put( "EventId", userId );
                jsonObject.put( "UserId", mUtils.getUserId( getContext() ) );
        }
        catch(JSONException e){
            Log.e(TAG, "AccountOverviewEventsFragment -> formJsonObjectForLogin()" + e.toString());
        }

        return jsonObject;
    }

    private void fetchUserData(){
        okHttpClient = apiRequest.generateOkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String getUserDataRoute = apiConf.getACCOUNT_GET_USER_API_ROUTE() + "/" + mUtils.getUserId( getContext() );

        Request request = apiRequest.getRequestObject( getUserDataRoute, true, false, "", null );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    ArrayList<AccountEventPOJO> accountEventPOJOList = new ArrayList<>();
                    JSONObject responseRoot = new JSONObject( body );
                    String id = responseRoot.getString( "UserId" );

                    JSONArray userEvents = responseRoot.getJSONArray( "UserEvents" );
                    for(int i = 0; i < userEvents.length(); i++){
                        JSONObject userEvent = userEvents.getJSONObject( i );
                        String eventId = userEvent.getString( "EventId" );
                        Boolean isInterestedEvent = userEvent.getBoolean( "Interested" );
                        Boolean isGoingEvent = userEvent.getBoolean( "Going" );

                        JSONObject event = userEvent.getJSONObject( "Event" );
                        String eventTitle = event.getString( "Title" );
                        JSONArray eventImages = event.getJSONArray( "Images" );
                        JSONObject eventCoverImageObj = eventImages.getJSONObject( 0 );
                        String eventImagePath = eventCoverImageObj.getString( "PathOnServer" );


                        accountEventPOJOList.add(
                                new AccountEventPOJO(
                                        eventId,
                                        eventImagePath,
                                        eventTitle,
                                        isInterestedEvent,
                                        isGoingEvent
                                )
                        );
                    }
                    mEventsList = accountEventPOJOList;
                    mTabLayout.getTabAt( mTabLayout.getSelectedTabPosition() ).select();
                    mProgressBarHolder.setVisibility( View.GONE );

                }
                catch (JSONException e){
                    mProgressBarHolder.setVisibility( View.GONE );
                    mEventAdapter.setFailedApiCall( true );
                    Log.e("OkHttp", TAG + " Error while parsing api/users/{id} response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                mProgressBarHolder.setVisibility( View.GONE );
                mEventAdapter.setFailedApiCall( true );
                Log.e("OkHttp", TAG + " Api call http://<host>/api/users/{id} failed; " + e.toString());
            }

        } );
    }

    private  ArrayList<AccountEventPOJO> filterEventsAttribute(int tabCase){
        ArrayList<AccountEventPOJO> filteredEvents = new ArrayList<>(  );

        for(AccountEventPOJO event : mEventsList){
            if(tabCase == GOING_EVENTS) {
                if (event.getmGoing())
                    filteredEvents.add( event );
            }
            else{
                if (event.getmInterested())
                    filteredEvents.add( event );
            }
        }

        return filteredEvents;
    }

    public void onEmptyViewRetryClick(){
        fetchUserData();
    }

    @Override
    public void removeEventApiCall(final String eventId) {
        okHttpClient = apiRequest.generateOkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String getUserDataRoute = apiConf.getACCOUNT_REMOVE_USER_EVENT_API_ROUTE();

        Request request = apiRequest.getRequestObject( getUserDataRoute, true, true, formJsonObjectForLogin(eventId).toString(), mMediaType );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );

                    for(AccountEventPOJO event: mEventsList ){
                        if(event.getmEventId().equals(eventId) ) {
                            mEventsList.remove( event );
                            break;
                        }
                    }
                    mTabLayout.getTabAt( mTabLayout.getSelectedTabPosition() ).select();
                    mProgressBarHolder.setVisibility( View.GONE );

                }
                catch (JSONException e){
                    Log.e("OkHttp", TAG + " Error while parsing api/users/remove-event response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", TAG + " Api call http://<host>/api/users/remove-event failed; " + e.toString());
            }

        } );
    }
}
