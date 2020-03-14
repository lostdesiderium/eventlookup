package com.example.eventlookup.Event;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventlookup.Event.Adapters.EventAdapter;
import com.example.eventlookup.Event.POJOs.EventPOJO;
import com.example.eventlookup.R;
import com.example.eventlookup.Shared.AppConf;
import com.example.eventlookup.Shared.CacheInterceptor;
import com.example.eventlookup.Shared.MainThreadOkHttpCallback;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// With HTTP calls related libs
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class EventListFragment extends Fragment {

    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    LinearLayoutManager linearLayoutManager;
    private OkHttpClient okHttpClient;
    private ArrayList<EventPOJO> eventsList;

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
        eventsList = new ArrayList<>(  );

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        linearLayoutManager = new LinearLayoutManager( this.getContext() );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );

        eventAdapter = new EventAdapter(  );
        eventAdapter.addItems( eventsList );
        recyclerView.setAdapter( eventAdapter );

        fetchDataForAdapter();

    }

    private void fetchDataForAdapter(){
        try {
            getEventListFromApi();
        }
        catch (Exception e){
            Log.e( "fetchDataForAdapter", "getEventListData api call failed " + e );
        }
    }



    private void getEventListFromApi() throws Exception {
        File httpCacheDirectory = new File(getContext().getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( httpCacheDirectory, cacheSize );

        okHttpClient = new OkHttpClient.Builder(  ).addNetworkInterceptor( new CacheInterceptor() )
                .cache( cache )
                .build();

        final ArrayList<EventPOJO> eventsList = new ArrayList<>();

        AppConf apiConf = AppConf.getInstance();
        String eventsListApiRoute = apiConf.getEventGetListApiRoute();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(eventsListApiRoute)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        Request request = new Request.Builder(  )
                .url( url )
                .build();

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONArray responseRoot = new JSONArray( body );

                    for(int i = 0; i < responseRoot.length(); i++){

                        JSONObject jObj = responseRoot.getJSONObject( i );
                        EventPOJO eventPOJO = new EventPOJO(
                                jObj.getString( "Id" ),
                                jObj.getString("CoverImagePath"),
                                jObj.getString( "Title" ),
                                jObj.getString( "ShortDescription" ),
                                jObj.getString( "AddressCountryCityStreet1" ),
                                jObj.getString( "StartDate" )
                        );
                        eventsList.add( eventPOJO );
                    }

                    eventAdapter.addItems( eventsList );
                    recyclerView.setAdapter( eventAdapter );
                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/event response data - " + e);
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/event failed");
            }

        } );

    }




}
