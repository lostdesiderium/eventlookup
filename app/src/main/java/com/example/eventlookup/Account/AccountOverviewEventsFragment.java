package com.example.eventlookup.Account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.eventlookup.Account.Adapters.AccountEventsListAdapter;
import com.example.eventlookup.Account.POJOs.AccountEventPOJO;
import com.example.eventlookup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class AccountOverviewEventsFragment extends Fragment {
    private final String TAG = "AccountOverviewEventsFragment";

    // application classes

    // framework components
    private NavController mNavController;
    private View mThisFrag;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TabLayout mTabLayout;
    private FrameLayout mProgressBarHolder;
    private OkHttpClient mOkHttpClient;

    // layout vars
    private final int UPCOMING_EVENTS = 0;
    private final int FINISHED_EVENTS = 1;
    private AccountEventsListAdapter mEventAdapter;
    private ArrayList<AccountEventPOJO> mEventsList;
    private FloatingActionButton mBtnEventRemove;

    public AccountOverviewEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_account_overview_events, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        //        mProgressBarHolder.setVisibility( View.VISIBLE );
        prepareListeners( view );
        setupRecyclerView();

    }

    private void prepareLayoutComponents(View view){
        mThisFrag = view;
        mNavController = Navigation.findNavController( getParentFragment().getView() );
        mRecyclerView = view.findViewById( R.id.RVL_account_events_list );
    }

    private void prepareListeners(View view){

    }

    private void setupRecyclerView(){
        mLinearLayoutManager = new LinearLayoutManager( getParentFragment().getContext() );
        mLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        mRecyclerView.setLayoutManager( mLinearLayoutManager );
        mRecyclerView.setItemAnimator( new DefaultItemAnimator() );

        mEventAdapter = new AccountEventsListAdapter(  );
        mEventAdapter.addItems( mEventsList );
        mRecyclerView.setAdapter( mEventAdapter );

//        fetchDataForAdapter();
    }

}
