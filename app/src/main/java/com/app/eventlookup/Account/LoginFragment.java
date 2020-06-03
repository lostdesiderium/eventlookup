package com.app.eventlookup.Account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.eventlookup.R;
import com.app.eventlookup.Shared.APIRequest;
import com.app.eventlookup.Shared.AppConf;
import com.app.eventlookup.Shared.MainThreadOkHttpCallback;
import com.app.eventlookup.Shared.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LoginFragment extends Fragment {
    private final String TAG = "LoginFragment";

    // application classes
    private Utils mUtils;
    private APIRequest apiRequest;

    // framework components
    private NavController mNavController;
    private SharedPreferences mSharedPrefs;
    private OkHttpClient okHttpClient;
    private MediaType mMediaType;

    // layout vars
    private View mThisFrag;
    private TextView mRegisterAction;
    private EditText mETEmail;
    private EditText mETPassword;
    private Button mBtnLogin;
    private TextView mTVErrorText;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_login, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        prepareListeners( view );
        if(checkIfTokenExists()){
            loginWithToken();
        }
    }


    private void prepareLayoutComponents(View view){
        mThisFrag = view;
        mNavController = Navigation.findNavController(view);
        mRegisterAction = view.findViewById( R.id.TV_login_action_register );
        mETEmail = view.findViewById( R.id.ET_login_email );
        mETPassword = view.findViewById( R.id.ET_login_password );
        mBtnLogin = view.findViewById( R.id.BTN_login );
        mMediaType = MediaType.parse(AppConf.JsonMediaTypeString);
        mUtils = new Utils();
        mSharedPrefs = mUtils.getAppSharedPreferences( getContext() );
        apiRequest = new APIRequest( getContext() );
        mTVErrorText = view.findViewById( R.id.TV_login_error_box );
    }

    private void prepareListeners(View view){
        mRegisterAction.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavController.navigate( R.id.action_loginFragment_to_registerFragment );
            }
        } );

        mBtnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        } );
    }

    private Boolean checkIfTokenExists(){
        return !mUtils.getAppToken( getContext() ).equals( "" );
    }

    private void login(){
        loginWithCreds();
    }

    private JSONObject formJsonObjectForLogin(){
        String token = mUtils.getAppToken( getContext() );
        JSONObject jsonObject = new JSONObject(  );
        try {
            if (mETEmail.getText().toString().equals( "" ) ) {
                jsonObject.put( "token", token );
            } else {
                jsonObject.put( "Email", mETEmail.getText() );
                jsonObject.put( "Password", mETPassword.getText() );
                jsonObject.put( "DisplayName", "" );
                jsonObject.put( "Token", "" );
            }
        }
        catch(JSONException e){
            Log.e(TAG, "LoginFragment -> formJsonObjectForLogin()" + e.toString());
        }

        return jsonObject;
    }

    private void loginWithCreds() {
        okHttpClient = apiRequest.generateOkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String loginRoute = apiConf.getACCOUNT_LOGIN_API_ROUTE();

        Request request = apiRequest.getRequestObject( loginRoute, false, true, formJsonObjectForLogin().toString(), mMediaType );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    String token = responseRoot.getString( "Token" );
                    String id = responseRoot.getString( "Id" );
//                    mTVErrorText.setText( token );

                    if(!token.equals( "" ) && !id.equals( "0" )) {
                        mUtils.writeAppTokenToSharedPreferences( getContext(), token );
                        mUtils.writeUserIdToSharedPreferences( getContext(), id );
                        mNavController.popBackStack( R.id.accountOverviewFragment, false );
                    }
                    else{
                        JSONObject message = responseRoot.getJSONObject( "Message" );
                        String messageText = message.getString( "Item2" );
                        mTVErrorText.setText( messageText );
                        mTVErrorText.setVisibility( View.VISIBLE );
                    }
                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/login response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/login failed: " + e.toString());
            }

        } );
    }

    private void loginWithToken() {
        okHttpClient = apiRequest.generateOkHttpClient(  );

        AppConf apiConf = AppConf.getInstance();
        String loginRoute = apiConf.getACCOUNT_TOKEN_LOGIN_API_ROUTE();

        Request request = apiRequest.getRequestObject( loginRoute, true, true, "", null );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    String id = responseRoot.getString( "Id" );
                    mUtils.writeUserIdToSharedPreferences( getContext(), id );
                    mNavController.popBackStack(R.id.accountOverviewFragment, false);
                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/token-login response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/token-login failed; " + e.toString());
            }

        } );
    }

}
