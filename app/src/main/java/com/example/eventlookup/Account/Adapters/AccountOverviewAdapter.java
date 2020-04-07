package com.example.eventlookup.Account.Adapters;

import com.example.eventlookup.Account.AccountOverviewInfoFragment;
import com.example.eventlookup.Account.AccountOverviewSettingsFragment;
import com.example.eventlookup.Account.AccountOverviewEventsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.eventlookup.R;

public class AccountOverviewAdapter extends FragmentStateAdapter {

    private int mFragmentsCount;
    private String mEventId;
    private NavController mNavController;
    private ViewPager2 mViewPager2;

    public AccountOverviewAdapter(@NonNull Fragment fragment, int fragmentsCount, NavController navController) {
        super( fragment );
        this.mFragmentsCount = fragmentsCount;
        mNavController = navController;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new AccountOverviewInfoFragment();
            case 1:
                return new AccountOverviewEventsFragment();
            case 2:
                return new AccountOverviewSettingsFragment();
        }
        return new AccountOverviewInfoFragment();
    }

    @Override
    public int getItemCount() {
        return this.mFragmentsCount;
    }

    public void setEventId(String id){
        this.mEventId = id;
    }
}
