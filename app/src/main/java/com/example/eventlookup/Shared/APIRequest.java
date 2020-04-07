package com.example.eventlookup.Shared;

import android.content.Context;

import com.bumptech.glide.RequestBuilder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class APIRequest {

    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private Utils mUtils;

    public APIRequest(Context context){
        mContext = context;
        mUtils = new Utils();
    }

    /**
     * Generate OkHttpClient simple object
     * @return OkHttpClient
     */
    public OkHttpClient generateOkHttpClient(){
        return new OkHttpClient(  );
    }

    /**
     * Generate OkHttpClient object with added cache interceptor
     * @param minutes cache validity in time
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpClientObject(int minutes){
        File httpCacheDirectory = new File(mContext.getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( httpCacheDirectory, cacheSize );

        CacheInterceptor cacheInterceptor = new CacheInterceptor(minutes);
        mOkHttpClient = new OkHttpClient.Builder(  ).addNetworkInterceptor( cacheInterceptor )
                .cache( cache )
                .build();

        return mOkHttpClient;
    }

    /**
     * Generate request object for GET or POST
     * @param apiPath String URL to reach server
     * @param isAuthorized Boolean is JWT token required
     * @param isPost Boolean is this request a POST
     * @param postBody String POST body as JSON to server
     * @param mediaType MediaType
     * @return Request
     */
    public Request getRequestObject(String apiPath, boolean isAuthorized, boolean isPost, String postBody, MediaType mediaType){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiPath)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        Request.Builder requestBuilder = new Request.Builder();

        if(isAuthorized){
            requestBuilder.header("Authorization", "Bearer " + mUtils.getAppToken( mContext ));
        }

        requestBuilder.url(url);

        if(isPost){
            RequestBody requestBody = RequestBody.create( postBody, mediaType );
            requestBuilder.post( requestBody );
        }

        return requestBuilder.build();
    }
}
