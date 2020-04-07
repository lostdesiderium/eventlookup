package com.example.eventlookup.Account;


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

import com.example.eventlookup.Account.Adapters.AccountOverviewAdapter;
import com.example.eventlookup.R;


public class AccountOverviewFragment extends Fragment
        implements com.example.eventlookup.Account.AccountOverviewInfoFragment.ViewPager2Navigation {
    private final String TAG = "AccountOverviewFragment";
    private final int FRAGMENTS_COUNT = 3;

    // layout vars
    private NavController mNavController;
    private View mThisFrag;
    private ViewPager2 mViewPager2;

    public AccountOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_account_overview, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        prepareListeners( view );
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof AccountOverviewInfoFragment) {
            AccountOverviewInfoFragment accountOverviewInfoFragment = (AccountOverviewInfoFragment) fragment;
            accountOverviewInfoFragment.setCallbackListener(this);
        }
    }


    private void prepareLayoutComponents(View view){
        mThisFrag = view;
        mNavController = Navigation.findNavController(view);
        setupViewPager2( view );
    }

    private void setupViewPager2(View view){
        mViewPager2 = view.findViewById( R.id.VP2_account_overview );
        mViewPager2.setAdapter( new AccountOverviewAdapter( this, FRAGMENTS_COUNT, mNavController ) );
        mViewPager2.setNestedScrollingEnabled( true );
        mViewPager2.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
    }

    private void prepareListeners(View view){

    }

    public void navigateToFragmentInPosition(int position){
        mViewPager2.setCurrentItem( position );
    }
}
