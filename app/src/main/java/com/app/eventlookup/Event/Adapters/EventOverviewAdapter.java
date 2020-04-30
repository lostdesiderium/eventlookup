package com.app.eventlookup.Event.Adapters;

import com.app.eventlookup.Event.EventInfoFragment;
import com.app.eventlookup.Event.EventMapFragment;
import com.app.eventlookup.Event.EventOverviewFragment;
import com.app.eventlookup.Event.EventTicketsFragment;
import com.app.eventlookup.Event.EventWeathersFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class EventOverviewAdapter extends FragmentStateAdapter {

    private int mFragmentsCount;
    private TabLayout tabLayout;
    private String _eventId;

    public EventOverviewAdapter(@NonNull Fragment fragment, int fragmentsCount, TabLayout tabLayout, String eventId) {
        super( fragment );
        this.mFragmentsCount = fragmentsCount;
        this.tabLayout = tabLayout;
        this._eventId = eventId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new EventInfoFragment(_eventId);
            case 1:
                return new EventMapFragment(_eventId);
            case 2:
                return new EventWeathersFragment(_eventId);
            case 3:
                return new EventTicketsFragment(_eventId);
            default:
                return new EventOverviewFragment();
        }
    }

    @Override
    public int getItemCount() {
        return this.mFragmentsCount;
    }
}
