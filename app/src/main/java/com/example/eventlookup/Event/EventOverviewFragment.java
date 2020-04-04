package com.example.eventlookup.Event;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventlookup.Event.Adapters.EventOverviewAdapter;
import com.example.eventlookup.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventOverviewFragment extends Fragment {

    private ViewPager2 fragmentsPager;
    private TabLayout tabLayout;
    private NavController navController;
    private ArrayList<String> mTabLayoutLabels;
    private final int FRAGMENTS_COUNT = 4;

    public EventOverviewFragment() {
        // Required empty public constructor
        mTabLayoutLabels = new ArrayList<>(  );
        mTabLayoutLabels.add( "Overview" );
        mTabLayoutLabels.add("Directions");
        mTabLayoutLabels.add("Weather Forecasts");
        mTabLayoutLabels.add( "Tickets" );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_event_overview, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        navController = Navigation.findNavController( view );

        fragmentsPager = view.findViewById( R.id.VP2_event_overview );
        fragmentsPager.setAdapter( new EventOverviewAdapter( this, FRAGMENTS_COUNT, tabLayout, getArguments().getString( "eventId" ) ) );
        fragmentsPager.setNestedScrollingEnabled( true );
        fragmentsPager.setOrientation( ViewPager2.ORIENTATION_VERTICAL );

        tabLayout = view.findViewById( R.id.TBL_events_overview_tab_layout );

        new TabLayoutMediator( tabLayout, fragmentsPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText( mTabLayoutLabels.get(position) );
            }
        }).attach();
    }



}
