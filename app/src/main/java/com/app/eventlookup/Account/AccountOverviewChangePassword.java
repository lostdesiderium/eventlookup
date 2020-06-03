package com.app.eventlookup.Account;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.eventlookup.R;
import com.app.eventlookup.Shared.APIRequest;
import com.app.eventlookup.Shared.AppConf;
import com.app.eventlookup.Shared.MainThreadOkHttpCallback;
import com.app.eventlookup.Shared.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountOverviewChangePassword extends Fragment {
    private final String TAG = "AccountChangePWFragment";

    // application classes
    private Utils mUtils;

    // framework components
    private Context mContext;
    private OkHttpClient okHttpClient;
    private APIRequest apiRequest;
    private MediaType mMediaType;

    // layout vars
    private NavController mNavController;
    private View mThisFrag;
    private EditText mETCurrentPassword;
    private EditText mETNewPassword;
    private EditText mETRepeatNewPassword;
    private Button mBtnChangePw;
    private TextView mErrorBox;

    public AccountOverviewChangePassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_account_overview_change_password, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents(view);
        prepareListeners( view );
    }
    private void prepareLayoutComponents(View view){
        mThisFrag = view;
        mNavController = Navigation.findNavController( getParentFragment().getView() );
        mETCurrentPassword = view.findViewById( R.id.ET_account_overview_current_password );
        mETNewPassword = view.findViewById( R.id.ET_account_overview_new_password );
        mETRepeatNewPassword = view.findViewById( R.id.ET_account_overview_new_repeat_password );
        mBtnChangePw = view.findViewById( R.id.BTN_account_settings_change_password );
        mErrorBox = view.findViewById( R.id.TV_account_settings_error_box );
        mUtils = new Utils();
        mContext = getContext();
        apiRequest = new APIRequest( mContext );
        mMediaType = MediaType.parse( AppConf.JsonMediaTypeString);
    }


    private void prepareListeners(View view) {
        mBtnChangePw.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        } );
    }

    private JSONObject formJsonObject(){
        JSONObject jsonObject = new JSONObject(  );
        try {
            jsonObject.put("id", mUtils.getUserId( mContext ));
            jsonObject.put("oldPassword", mETCurrentPassword.getText().toString());
            jsonObject.put( "newPassword", mETNewPassword.getText().toString() );
        }
        catch(JSONException e){
            Log.e(TAG, "AccountOverviewChangePassword -> formJsonObject()" + e.toString());
        }

        return jsonObject;
    }

    private void changePasswordApiCall(){
        okHttpClient = new OkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String changePasswordURL = apiConf.getACCOUNT_CHANGE_PASSWORD();

        Request request = apiRequest.getRequestObject( changePasswordURL, true, true, formJsonObject().toString(), mMediaType );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    boolean isSuccess = responseRoot.getBoolean( "Success" );
                    if(isSuccess) {
                        Toast.makeText( getContext(), "Password changed", Toast.LENGTH_SHORT ).show();
                        mNavController.popBackStack();
                    }
                    else{
                        showError( "Incorrect current password" );
                    }

                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/users/change-password response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/users/change-password failed; " + e.toString());
            }

        } );
    }

    private void changePassword(){
        if(validateFields())
            changePasswordApiCall();
    }

    private boolean validateFields(){
        String newPw = mETNewPassword.getText().toString();
        String newPwRepeat = mETRepeatNewPassword.getText().toString();
        if(newPw.length() < 6){
            showError( "Password must be at least 6 symbols" );

            if(!newPw.equals( newPwRepeat )){
                showError( "Passwords do not match" );
                return false;
            }
            return false;
        }

        return true;
    }

    private void showError(String errorText){
        mErrorBox.setText( errorText );
        mErrorBox.setVisibility( View.VISIBLE );
    }

}
