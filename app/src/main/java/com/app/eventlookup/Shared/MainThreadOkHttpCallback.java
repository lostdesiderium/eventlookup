package com.app.eventlookup.Shared;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class MainThreadOkHttpCallback implements Callback {

    public abstract void apiCallSuccess(final String body);

    public abstract void apiCallFail(final Exception error);

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
            onFailure(call, new IOException("Failed"));
            return;
        }
        final String reponseBody = response.body().string();
        runOnUiThread( new Runnable() {
           @Override
           public void run() {
                apiCallSuccess( reponseBody );
           }
       } );
    }


    @Override
    public void onFailure(final Call call, final IOException e) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                apiCallFail( e );
            }
        } );
    }

    private void runOnUiThread(Runnable task){
        new Handler( Looper.getMainLooper() ).post( task );
    }
}
