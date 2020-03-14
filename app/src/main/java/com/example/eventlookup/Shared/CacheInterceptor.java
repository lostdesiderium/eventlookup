package com.example.eventlookup.Shared;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Response response = chain.proceed( chain.request() );

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge( 15, TimeUnit.MINUTES )
                .build();

        return response.newBuilder().removeHeader( "Pragma" ).removeHeader( "Cache-Control" ).header( "Cache-Control", cacheControl.toString() ).build();
    }
}
