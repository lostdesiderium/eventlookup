package com.example.eventlookup.Event;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventlookup.Event.Adapters.EventAdapter;
import com.example.eventlookup.Event.POJOs.EventPOJO;
import com.example.eventlookup.R;

import java.util.ArrayList;;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class EventListFragment extends Fragment {

    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    LinearLayoutManager linearLayoutManager;



    public EventListFragment(){
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_event_list, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        recyclerView = view.findViewById( R.id.RVL_events_list );
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        linearLayoutManager = new LinearLayoutManager( this.getContext() );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );

        eventAdapter = new EventAdapter( );
        fetchDataForAdapter();
    }

    private void fetchDataForAdapter(){

        // FAKE DATA
        ArrayList<EventPOJO> eventsList = new ArrayList<>();
        eventsList.add(new EventPOJO(
            "https://lh3.googleusercontent.com/proxy/Ee-omx7kHLdt6zJSKeAdtJynt0H6ONDkq4mEIekbHTjc6ZJuFSpg6lpk0WMMak4xomQs9DcDKXfwdRSMZfcMu-ssZdfc0SrToONk0aZqJgV76Gs_0Vl6RpIxaVPaOYKYrLl8K9XuhOZqEHHw",
            "First fake event",
"This is a short description of First fake event. Lets begin",
    "Long description, not used",
        "Street 1",
            "2020-02-20"
        ));

        eventsList.add(new EventPOJO(
                "https://i.pinimg.com/originals/12/5d/12/125d12f09b112290307bc00e9c38400f.jpg",
                "Second fake event",
                "This is a short description of SECOND fake event. Lets begin",
                "Long description, not used",
                "Street 2",
                "2020-03-27"
        ));

        eventsList.add(new EventPOJO(
                "https://lh3.googleusercontent.com/proxy/_yc-HIg6CWR_Qr7w_2TV02vabV08Z-5W_yS7B1L-4d0Xqw1yya8j9Xw1bFysftVWi5Q3FBqth2ta1UF-YyRNWN_cIBs8vPQT125Mxj2AcWloSPNGvNhwpipzr8NRfhm-1NiE_0lKsj09K9oYsev30gVwx9feW3Fs9B4L9QggcV3NrSdAgGxRSAJBWKj6N8J20S3KIU38P4Xy1X_x8Q3Za6JX5RrtHtG1Vn7zrvioaqzg-2-jtOp3zS_4GGvxNRBuZeHNACxQ3V5ThDnrEuNvXg_JrZbnd3oLVBaoBsnTt4aSV1U",
                "Third fake event",
                "This is a short description of THIRD fake event. Lets begin",
                "Long description, not used",
                "Street 3",
                "2020-04-25"
        ));

        eventsList.add(new EventPOJO(
                "https://previews.123rf.com/images/smileus/smileus1612/smileus161200007/69529862-gorgeous-fireworks-on-black-background-ideal-for-new-year-or-other-celebration-events-vertical-forma.jpg",
                "Fourth fake event",
                "This is a short description of FOURTH fake event. Lets begin",
                "Long description, not used",
                "Street 4",
                "2020-05-10"
        ));

        eventAdapter.addItems( eventsList );
        recyclerView.setAdapter( eventAdapter );
    }


}
