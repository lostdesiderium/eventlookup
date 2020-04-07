package com.example.eventlookup.Account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventlookup.R;
import com.example.eventlookup.Shared.APIRequest;
import com.example.eventlookup.Shared.AppConf;
import com.example.eventlookup.Shared.CacheInterceptor;
import com.example.eventlookup.Shared.MainThreadOkHttpCallback;
import com.example.eventlookup.Shared.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegisterFragment extends Fragment {
    private final String TAG = "RegisterFragment";

    // application classes
    private Utils mUtils;
    private APIRequest apiRequest;

    // framework components
    private NavController mNavController;
    private SharedPreferences mSharedPrefs;
    private OkHttpClient okHttpClient;
    private MediaType mMediaType;

    // layout vars
    private TextView mLoginAction;
    private EditText mETDisplayName;
    private EditText mETEmail;
    private EditText mETPassword;
    private Button mBtnRegister;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_register, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        prepareListeners( view );
    }

    private void prepareLayoutComponents(View view){
        mNavController = Navigation.findNavController(view);
        mLoginAction = view.findViewById( R.id.TV_register_action_login );
        mETDisplayName = view.findViewById( R.id.ET_register_display_name );
        mETEmail = view.findViewById( R.id.ET_register_email );
        mETPassword = view.findViewById( R.id.ET_register_password );
        mBtnRegister = view.findViewById( R.id.BTN_register );
        mMediaType = MediaType.parse(AppConf.JsonMediaTypeString);
        mUtils = new Utils();
        mSharedPrefs = mUtils.getAppSharedPreferences( getContext() );
        apiRequest = new APIRequest( getContext() );
    }

    private void prepareListeners(View view){
        mLoginAction.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavController.navigate( R.id.action_registerFragment_to_action_account );
            }
        } );
        mBtnRegister.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        } );

    }

    private JSONObject formJsonObjectForLogin(){
        JSONObject jsonObject = new JSONObject(  );
        try {
            jsonObject.put( "Email", mETEmail.getText() );
            jsonObject.put( "Password", mETPassword.getText() );
            jsonObject.put( "DisplayName", mETDisplayName.getText() );
            jsonObject.put( "Token", "no-token" );
        }
        catch(JSONException e){
            Log.e(TAG, "LoginFragment -> formJsonObjectForLogin()" + e.toString());
        }

        return jsonObject;
    }

    private void register() {
        okHttpClient = apiRequest.generateOkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String registerApiRoute = apiConf.getACCOUNT_REGISTER_API_ROUTE();

        Request request = apiRequest.getRequestObject( registerApiRoute, false, true, formJsonObjectForLogin().toString(), mMediaType );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    String token = responseRoot.getString( "Token" );
                    String id = responseRoot.getString( "Id" );
                    mUtils.writeAppTokenToSharedPreferences( getContext(), token );
                    mUtils.writeUserIdToSharedPreferences( getContext(), id );

                    mNavController.popBackStack(R.id.accountOverviewFragment, false);
                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/users/register response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/users/register failed: " + e.toString());
            }

        } );
    }

}
