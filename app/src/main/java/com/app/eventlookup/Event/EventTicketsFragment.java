package com.app.eventlookup.Event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.app.eventlookup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTicketsFragment extends Fragment {

    private String _eventId;
    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;
    private FrameLayout progressBarHolder;

    public EventTicketsFragment() {
        // Required empty public constructor
    }

    public EventTicketsFragment(String eventId){
        this._eventId = eventId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_event_tickets, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        progressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );
        progressBarHolder.setVisibility( View.VISIBLE );
        progressBarHolder.setVisibility( View.GONE );
    }

}
