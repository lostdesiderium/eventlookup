package com.example.eventlookup.Event.Adapters;

import com.example.eventlookup.Event.EventInfoFragment;
import com.example.eventlookup.Event.EventMapFragment;
import com.example.eventlookup.Event.EventOverviewFragment;
import com.example.eventlookup.Event.EventTicketsFragment;
import com.example.eventlookup.Event.EventWeathersFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class EventOverviewAdapter extends FragmentStateAdapter {

    private int mFragmentsCount;
    public TabLayout tabLayout;

    public EventOverviewAdapter(@NonNull Fragment fragment, int fragmentsCount, TabLayout tabLayout) {
        super( fragment );
        this.mFragmentsCount = fragmentsCount;
        this.tabLayout = tabLayout;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new EventInfoFragment();
            case 1:
                return new EventMapFragment();
            case 2:
                return new EventWeathersFragment();
            case 3:
                return new EventTicketsFragment();
            default:
                return new EventOverviewFragment();
        }
    }

    @Override
    public int getItemCount() {
        return this.mFragmentsCount;
    }
}
