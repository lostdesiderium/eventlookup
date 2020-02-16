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

    ViewPager2 fragmentsPager;
    TabLayout tabLayout;
    NavController navController;
    private ArrayList<String> mTabLayoutLabels;

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
        fragmentsPager.setAdapter( new EventOverviewAdapter( this, 4, tabLayout ) );
        fragmentsPager.setNestedScrollingEnabled( true );
        fragmentsPager.setOrientation( ViewPager2.ORIENTATION_VERTICAL );

        tabLayout = view.findViewById( R.id.TBL_events_overview_tab_layout );
//        setupTabLayout(tabLayout);

        new TabLayoutMediator( tabLayout, fragmentsPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText( mTabLayoutLabels.get(position) );
            }
        }).attach();
    }



//    public void setupTabLayout(TabLayout tabLayout){
//        tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                switch(tab.getPosition()){
//                    case 0:
//                        return;
//                    case 1:
//                        navController.navigate( R.id.action_eventOverviewFragment_to_eventMapFragment );
//                        break;
//                    case 2:
//                        return;
//                    case 3:
//                        return;
//                    default:
//
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        } );
//    }


}
