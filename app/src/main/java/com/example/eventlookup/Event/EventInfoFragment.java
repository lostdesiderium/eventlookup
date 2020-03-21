package com.example.eventlookup.Event;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.eventlookup.Event.Adapters.ImageSliderPageAdapter;
import com.example.eventlookup.Event.POJOs.EventFullPOJO;
import com.example.eventlookup.R;
import com.example.eventlookup.Shared.AppConf;
import com.example.eventlookup.Shared.CacheInterceptor;
import com.example.eventlookup.Shared.MainThreadOkHttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventInfoFragment extends Fragment {

    private ViewPager2 imageViewPager;
    private String _eventId;
    private OkHttpClient okHttpClient;
    private ImageSliderPageAdapter sliderPageAdapter;
    private Context _thisContext;
    private View _thisView;

    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;
    private FrameLayout progressBarHolder;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    public EventInfoFragment(String eventId){
        this._eventId = eventId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

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
        progressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );

        _thisContext = this.getContext();
        _thisView = view;

        imageViewPager = view.findViewById( R.id.VP_event_overview_slider );
        sliderPageAdapter = new ImageSliderPageAdapter( _thisContext );
        imageViewPager.setAdapter( sliderPageAdapter);

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

        try{
            progressBarHolder.setVisibility( View.VISIBLE );
            getEventDetailedInfo();
        }
        catch (Exception e){
            Log.e("OkHttp", "Error while trying to fetch data from api/event/{id}");
        }
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

    /*
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

        okHttpClient = new OkHttpClient.Builder(  ).addNetworkInterceptor( new CacheInterceptor() )
                .cache( cache )
                .build();
//        okHttpClient = new OkHttpClient(  );

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

                    String id = response.getString( "Id" );
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

}
