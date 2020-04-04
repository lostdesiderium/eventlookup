package com.example.eventlookup.Account;

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

public class AccountOverviewSettingsFragment extends Fragment {
    private final String TAG = "AccountOverviewEventsFragment";

    // layout vars
    private NavController mNavController;
    private View mThisFrag;
    private Button mTestButton;

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
//        mNavController = Navigation.findNavController( view );
        mTestButton = view.findViewById( R.id.accountEventTestButton );
    }


    private void prepareListeners(View view){
        mTestButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("eventId", "1");
//                Navigation.findNavController( mThisFrag ).navigate( R.id.action_accountOverviewSettingsFragment2_to_eventOverviewFragment, bundle );
            }
        } );
    }
}
