package com.app.eventlookup.Event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.eventlookup.R;
import com.app.eventlookup.Shared.APIRequest;
import com.app.eventlookup.Shared.AppConf;
import com.app.eventlookup.Shared.MainThreadOkHttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTicketsFragment extends Fragment {
    // application classes
    private APIRequest apiRequest;

    // framework components
    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;
    private FrameLayout progressBarHolder;
    private OkHttpClient okHttpClient;
    private MediaType mMediaType;

    // layout vars
    private Button mBtnOpenTicketsWebView;
    private String mTicketsUrl;
    private String _eventId;


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

        prepareLayoutComponents( view );
        progressBarHolder.setVisibility( View.VISIBLE );
        prepareListeners( view );

        try{
            getEventDetailedInfo();
        }
        catch (Exception e){
            Log.e("OkHttp", "Error while trying to fetch data from api/event/{id}");
        }

        progressBarHolder.setVisibility( View.GONE );
    }

    private void prepareLayoutComponents(View view){
        mTicketsUrl = "";
        apiRequest = new APIRequest( this.getContext() );
        progressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );
        mBtnOpenTicketsWebView = view.findViewById( R.id.Btn_event_buy_ticket );
    }

    private void prepareListeners(View view){
        mBtnOpenTicketsWebView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTicketsWebView();
            }
        } );
    }

    private void openTicketsWebView(){
        if(mTicketsUrl.contains( "http" ) ) {
            startActivity( new Intent( Intent.ACTION_VIEW ).setData( Uri.parse( mTicketsUrl ) ) );
        }
        else{
            Toast.makeText( this.getContext(), "Apologies, there are no tickets for sale to this event", Toast.LENGTH_SHORT ).show();
        }
    }

    private void getEventDetailedInfo() throws Exception {
        okHttpClient = new OkHttpClient(  );

        AppConf apiConf = AppConf.getInstance();
        String eventInfoRoute = apiConf.getEventGetEventDetailedApiRoute() + _eventId;

        Request request = apiRequest.getRequestObject( eventInfoRoute, false, false, "", null );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject response = new JSONObject( body );
                    JSONArray tickets = response.getJSONArray( "Tickets" );
                    if (tickets.length() > 0) {
                        JSONObject ticket = tickets.getJSONObject( 0 );
                        mTicketsUrl = ticket.getString( "DistributorUrl" );
                    }

                    progressBarHolder.setVisibility( View.GONE );
                }
                catch (JSONException e){
                    progressBarHolder.setVisibility( View.GONE );
                    Log.e("OkHttp", "Error while parsing api/event/{id} response data - " + e);
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/event failed");
            }

        } );

    }
}
