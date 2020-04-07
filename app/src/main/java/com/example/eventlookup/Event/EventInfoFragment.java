package com.example.eventlookup.Event;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventlookup.Event.POJOs.UserEventPOJO;
import com.example.eventlookup.Event.Adapters.ImageSliderPageAdapter;
import com.example.eventlookup.Event.POJOs.EventFullPOJO;
import com.example.eventlookup.R;
import com.example.eventlookup.Shared.AppConf;
import com.example.eventlookup.Shared.CacheInterceptor;
import com.example.eventlookup.Shared.MainThreadOkHttpCallback;
import com.example.eventlookup.Shared.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventInfoFragment extends Fragment {
    private final String TAG = "EventInfoFrag";
    private final int INTERESTED = 0;
    private final int GOING = 1;

    // application classes
    Utils mUtils;

    // framework components
    private ViewPager2 imageViewPager;
    private OkHttpClient okHttpClient;
    private MediaType mMediaType;
    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;
    private FrameLayout progressBarHolder;

    // layout vars
    private Context _thisContext;
    private View _thisView;
    private String _eventId;
    private ImageSliderPageAdapter sliderPageAdapter;
    private FloatingActionButton mFABLikeEvent;
    private FloatingActionButton mFABLoveEvent;


    public EventInfoFragment() {
        // Required empty public constructor
    }

    public EventInfoFragment(String eventId){
        this._eventId = eventId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_event_info, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        prepareListeners( view );

        try{
            progressBarHolder.setVisibility( View.VISIBLE );
            getEventDetailedInfo();
        }
        catch (Exception e){
            Log.e("OkHttp", "Error while trying to fetch data from api/event/{id}");
        }
    }

    private void prepareLayoutComponents(View view){
        progressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );

        _thisContext = this.getContext();
        _thisView = view;
        mFABLikeEvent = view.findViewById( R.id.FAB_event_info_like );
        mFABLoveEvent = view.findViewById( R.id.FAB_event_info_heart );
        mMediaType = MediaType.parse( AppConf.JsonMediaTypeString);
        mUtils = new Utils();
        imageViewPager = view.findViewById( R.id.VP_event_overview_slider );

        sliderPageAdapter = new ImageSliderPageAdapter( _thisContext );
        imageViewPager.setAdapter( sliderPageAdapter);
    }

    private void prepareListeners(View view){
        imageViewPager.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected( position );
                sliderPageAdapter.changeIndicator( position );
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled( position, positionOffset, positionOffsetPixels );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged( state );
            }
        } );

        mFABLikeEvent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUserLoggedIn())
                    markUserEvent( INTERESTED );
            }
        } );
        mFABLoveEvent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUserLoggedIn())
                    markUserEvent( GOING );
            }
        } );
    }

    /*API CALL RESPONSE EXAMPLE
    * "Title": "\"Užsuk\" Restorano atidarymas",
      "ShortDescription": "\"Užsuk\" restoranas patogioje Vilniaus vietoje",
      "LongDescription": "Ne tik patogioje vietoje bet ir su super patraukliomis kainomis",
      "GoingPeopleCount": 247,
      "InterestedPeopleCount": 792,
      "StartDate": "0001-01-01T00:00:00",
      "FinishDate": "0001-02-01T00:00:00",
      "DaysEventActive": 3,
      "Address": {
        "Id": 1,
        "Country": "Lietuva",
        "City": "Vilnius",
        "Street1": "Vokiečių g. 15",
        "District": null,
        "Street2": null,
        "Lat": null,
        "Lng": null,
        "EventId": 1
      },
      "Images": [
        {
          "Id": 1,
          "Caption": "event1",
          "IsCover": true,
          "PathOnServer": "http://127.0.0.1:19422/app-images/event1.jpg",
          "EventId": 1,
          "TicketId": null,
          "Ticket": null
        }
      ],
      "Tickets": []
    * */
    private void getEventDetailedInfo() throws Exception {
        File httpCacheDirectory = new File(getContext().getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( httpCacheDirectory, cacheSize );

//        okHttpClient = new OkHttpClient.Builder(  ).addNetworkInterceptor( new CacheInterceptor() )
//                .cache( cache )
//                .build();
        okHttpClient = new OkHttpClient(  );

        AppConf apiConf = AppConf.getInstance();
        String eventInfoRoute = apiConf.getEventGetEventDetailedApiRoute() + _eventId;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(eventInfoRoute)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        final Request request = new Request.Builder(  )
                .url( url )
                .build();

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject response = new JSONObject( body );

                    String id = response.getString( "EventId" );
                    String title = response.getString( "Title" );
                    String eventShortDesc = response.getString( "ShortDescription" );
                    String eventLongDesc = response.getString( "LongDescription" );
                    String goingPeopleCount = response.getString( "GoingPeopleCount" );
                    String interestedPeopleCount = response.getString( "InterestedPeopleCount" );
                    String startDate = response.getString( "StartDate" );
                    String finishDate = response.getString( "FinishDate" );
                    String daysEventActive = response.getString( "DaysEventActive" );

                    JSONObject address = response.getJSONObject( "Address" );
                    String country = address.getString( "Country" );
                    String city = address.getString( "City" );
                    String street1 = address.getString( "Street1" );
                    String lat = address.getString( "Lat" );
                    String lng = address.getString( "Lng" );

                    JSONArray images = response.getJSONArray( "Images" );
                    ArrayList<String> imagesUrls = new ArrayList<>(  );
                    for(int i = 0; i < images.length(); i++){
                        JSONObject image = images.getJSONObject( i );
                        imagesUrls.add( image.getString( "PathOnServer" ) );
                    }

                    EventFullPOJO eventDetails = new EventFullPOJO(
                            id,
                            title,
                            eventLongDesc,
                            (city + " " + street1 + ", " + country),
                            startDate,
                            interestedPeopleCount,
                            goingPeopleCount,
                            daysEventActive,
                            lat,
                            lng,
                            imagesUrls
                    );
                    String currentUserId = mUtils.getUserId( _thisContext );
                    if(!currentUserId.equals( "" )) {
                        JSONArray markedEvents = response.getJSONArray( "UserEvents" );
                        for (int i = 0; i < markedEvents.length(); i++) {
                            JSONObject markedEvent = markedEvents.getJSONObject( i );
                            String markedEventId = markedEvent.getString( "EventId" );
                            String markedUserId = markedEvent.getString( "UserId" );
                            if(markedEventId.equals( _eventId ) && markedUserId.equals( currentUserId ) ){
                                if(markedEvent.getBoolean( "Interested" ))
                                    changeIcon( INTERESTED );
                                else if(markedEvent.getBoolean( "Going" ))
                                    changeIcon( GOING );
                            }
                        }
                    }

                    sliderPageAdapter = new ImageSliderPageAdapter( _thisContext, imagesUrls, _thisView );
                    imageViewPager.setAdapter( sliderPageAdapter);

                    bindDataToView( eventDetails );
                    progressBarHolder.setVisibility( View.GONE);
                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/event/{id} response data - " + e);
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/event failed");
            }

        } );

    }

    public void bindDataToView(EventFullPOJO eventDetails){
        TextView eventTitle = _thisView.findViewById( R.id.event_overview_title );
        TextView eventLongDescr = _thisView.findViewById( R.id.event_overview_description );
        TextView eventLocation = _thisView.findViewById( R.id.event_overview_location );
        TextView eventDate = _thisView.findViewById( R.id.event_overview_date );
        TextView eventInterestedPeopleCount = _thisView.findViewById( R.id.event_overview_interested_ppl_count );
        TextView eventGoingPeopleCount = _thisView.findViewById( R.id.event_overview_going_ppl_count );

        eventTitle.setText( eventDetails.getEventTitle() );
        eventLongDescr.setText( eventDetails.getEventDescription() );
        eventLocation.setText( eventDetails.getEventLocation() );
        eventDate.setText( eventDetails.getFormattedEventDateFromString(eventDetails.getEventDate()) );
        eventInterestedPeopleCount.setText( eventDetails.getInterestedPeopleCount() );
        eventGoingPeopleCount.setText(eventDetails.getGoingPeopleCount());

    }

    private String formJsonObjectForUserEvent(int action){
        Gson gson = new Gson();

        UserEventPOJO userEventPOJO = new UserEventPOJO(
                false,
                false,
                false,
                _eventId,
                mUtils.getUserId( getContext() )
        );

        // if interested then not going and reverse
        switch(action){
            case GOING:
                userEventPOJO.setGoing( true );
                userEventPOJO.setInterested( false );
                break;
            case INTERESTED:
                userEventPOJO.setInterested( true );
                userEventPOJO.setGoing( false );
                break;
            default:
                return "";
        }

        String jsonString = gson.toJson( userEventPOJO );

        return jsonString;
    }

    /**
     * API call to server to mark user event as interested or going
     * @param action GOING or INTERESTED
     */
    private void markUserEvent(final int action){
        okHttpClient = new OkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String markEventUrl = apiConf.getEVENT_MARK_USER_EVENT();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(markEventUrl)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        RequestBody body = RequestBody.create(formJsonObjectForUserEvent(action), mMediaType );

        Request request = new Request.Builder(  )
                .header("Authorization", "Bearer " + mUtils.getAppToken( getContext() ))
                .url( url )
                .post( body )
                .build();

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    boolean isSuccess = responseRoot.getBoolean( "Success" );
                    if(isSuccess)
                        changeIcon(action);

                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/users/mark-user-event response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/users/mark-user-event failed; " + e.toString());
            }

        } );
    }

    private boolean isUserLoggedIn(){
        String userId = mUtils.getUserId( _thisContext );
        if(userId != "")
            return true;

        Toast.makeText( getContext(), "You must sign in first", Toast.LENGTH_LONG ).show();
        return false;
    }

    /**
     * Change icon to indicate change to user
     * @param action GOING or INTERESTED
     */
    private void changeIcon(int action){
        Drawable likeFillIcon = _thisContext.getResources().getDrawable( R.drawable.like_white_fill );
        Drawable heartFillIcon = _thisContext.getResources().getDrawable( R.drawable.heart_white_fill );
        Drawable heartOutlineIcon = _thisContext.getResources().getDrawable( R.drawable.heart_white );
        Drawable likeOutlineIcon = _thisContext.getResources().getDrawable( R.drawable.like_white );
        switch (action){
            case INTERESTED:
                mFABLoveEvent.setImageDrawable( heartOutlineIcon );
                mFABLikeEvent.setImageDrawable( likeFillIcon );
                break;
            case GOING:
                mFABLikeEvent.setImageDrawable( likeOutlineIcon );
                mFABLoveEvent.setImageDrawable( heartFillIcon );
                break;
            default:
        }

        deactivateMarkButtonsForShortTime( 1000 ); // deactivating buttons for 1second for antispam to server
    }

    private void deactivateMarkButtonsForShortTime(int millis){
        mFABLoveEvent.setClickable( false );
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mFABLoveEvent.setClickable( true );
                    }
                },
                millis);
        mFABLikeEvent.setClickable( false );
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mFABLikeEvent.setClickable( true );
                    }
                },
                millis);
    }
}
