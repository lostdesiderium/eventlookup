package com.example.eventlookup.Event;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventlookup.Event.Adapters.ImageSliderPageAdapter;
import com.example.eventlookup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventInfoFragment extends Fragment {

    ViewPager2 imageViewPager;

    public EventInfoFragment() {
        // Required empty public constructor
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

        getData( "TODO data fetch" );

        imageViewPager = view.findViewById( R.id.VP_event_overview_slider );
        ImageSliderPageAdapter sliderAdapter = new ImageSliderPageAdapter( this.getContext() );
        imageViewPager.setAdapter( sliderAdapter );


//        bindDataToView( view );
    }

    public void bindDataToView(@NonNull View view){
        TextView eventTitle = view.findViewById( R.id.event_overview_title );
        eventTitle.setText( getArguments().getString( "testId" ) );
    }


    public void getData(String itemId){

    }

}
