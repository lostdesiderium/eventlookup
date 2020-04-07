package com.example.eventlookup.Account;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventlookup.R;
import com.example.eventlookup.Shared.Utils;

public class AccountOverviewSettingsFragment extends Fragment {
    private final String TAG = "AccountOverviewEventsFragment";

    // application classes
    private Utils mUtils;

    // framework components
    private Context mContext;

    // layout vars
    private NavController mNavController;
    private View mThisFrag;
    private Button mBtnLogout;


    public AccountOverviewSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_account_overview_settings, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        prepareListeners( view );
    }


    private void prepareLayoutComponents(View view){
        mThisFrag = view;
        mNavController = Navigation.findNavController( getParentFragment().getView() );
        mBtnLogout = view.findViewById( R.id.BTN_account_settings_logout );
        mUtils = new Utils();
        mContext = getContext();
    }


    private void prepareListeners(View view) {
        mBtnLogout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUtils.writeUserIdToSharedPreferences( mContext, "" );
                mUtils.writeAppTokenToSharedPreferences( mContext, "" );
                mNavController.navigate( R.id.action_accountOverviewFragment_to_action_account );
            }
        } );
    }
}
