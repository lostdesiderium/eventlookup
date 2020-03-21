package com.example.eventlookup.Event;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eventlookup.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventWeathersFragment extends Fragment {

    private int mDaysActiveEvent;
    private ViewGroup thisFragView;
    private String _eventId;
    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;
    private FrameLayout progressBarHolder;

    public EventWeathersFragment() {
        // Required empty public constructor
    }

    public EventWeathersFragment(String eventId){
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
        thisFragView = container;
        return inflater.inflate( R.layout.fragment_event_weathers, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        LinearLayout linearLayoutParent = view.findViewById( R.id.LL_event_weathers );

        progressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );
        mDaysActiveEvent = 3; // Fake data

        progressBarHolder.setVisibility( View.VISIBLE );
        attachDataToLayout( linearLayoutParent, view );
    }

    private void attachDataToLayout(LinearLayout linearLayout, View view ){

        int weightForLayout = 10 / mDaysActiveEvent;

        for(int i = 0; i < mDaysActiveEvent; i++){

            View eventWeatherForecast = getLayoutInflater().inflate(R.layout.event_weather_row, (ViewGroup)view, false);

            TextView eventForecastTitleTV = eventWeatherForecast.findViewById( R.id.TV_event_weather_day_forecast_title );
            eventForecastTitleTV.setText( "Day " + (i + 1) );

            TextView eventForecastValueTV = eventWeatherForecast.findViewById( R.id.TV_event_weather_day_forecast_value );
            eventForecastValueTV.setText( (17 + i )+ "°" );

            linearLayout.addView( eventWeatherForecast );

        }
        progressBarHolder.setVisibility( View.GONE );

    }


}
