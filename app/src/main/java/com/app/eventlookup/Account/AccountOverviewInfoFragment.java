package com.app.eventlookup.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.app.eventlookup.Account.POJOs.AccountOverviewInfoPOJO;
import com.app.eventlookup.R;
import com.app.eventlookup.Shared.APIRequest;
import com.app.eventlookup.Shared.AppConf;
import com.app.eventlookup.Shared.MainThreadOkHttpCallback;
import com.app.eventlookup.Shared.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AccountOverviewInfoFragment extends Fragment {
    private final String TAG = "AccountInfoFragment";
    public static final int PICK_IMAGE = 1;
    public static final int THUMBNAIL_SIZE = 512;

    // application classes
    private Utils mUtils;
    private APIRequest apiRequest;

    // framework components
    private OkHttpClient okHttpClient;
    private MediaType mMediaType;

    // layout vars
    private View mThisFrag;
    private ImageView mAccountImage;
    private TextView mTVEmail;
    private TextView mTVDisplayName;
    private TextView mTVGoingCount;
    private TextView mTVInterestedCount;
    private TextView mTVCreatedCount;
    private EditText mETBio;
    private FloatingActionButton mBtnEventsOverview;
    private FloatingActionButton mBtnSettingsOverview;
    private ViewPager2Navigation mNavigationCallback;
    private String mEncodedImage = "";
    private FrameLayout mProgressBarHolder;


    public AccountOverviewInfoFragment() {
        // Required empty public constructor
    }

    public void setCallbackListener(ViewPager2Navigation callback) {
        this.mNavigationCallback = callback;
    }

    public interface ViewPager2Navigation{
        void navigateToFragmentInPosition(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_account_overview_info, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        prepareLayoutComponents( view );
        prepareListeners( view );
        fetchUserData();
    }

    private void prepareLayoutComponents(View view){
        mThisFrag = view;
        mMediaType = MediaType.parse( AppConf.JsonMediaTypeString);
        mUtils = new Utils();
        mAccountImage = view.findViewById( R.id.IV_account_image );
        mTVEmail = view.findViewById( R.id.TV_account_overview_email );
        mTVDisplayName = view.findViewById( R.id.TV_account_display_name );
        mTVInterestedCount = view.findViewById( R.id.TV_account_overview_interested_count );
        mTVGoingCount = view.findViewById( R.id.TV_account_overview_going_count );
        mETBio = view.findViewById( R.id.ET_account_overview_bio );
        mBtnSettingsOverview = view.findViewById( R.id.FAB_account_overview_settings );
        mBtnEventsOverview = view.findViewById( R.id.FAB_account_overview_events );
        apiRequest = new APIRequest( getContext() );
        mProgressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );
    }

    private void prepareListeners(View view){
        mBtnEventsOverview.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigationCallback.navigateToFragmentInPosition( 1 );
            }
        } );
        mBtnSettingsOverview.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigationCallback.navigateToFragmentInPosition( 2 );
            }
        } );
        mETBio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    updateUserBio();
                }
            }
        });
        mAccountImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        } );

    }

    private void bindLayoutData(AccountOverviewInfoPOJO accountInfo){
        mTVDisplayName.setText(accountInfo.getmDisplayName());
        mTVEmail.setText( accountInfo.getmEmail() );
        mTVInterestedCount.setText( accountInfo.getmUserInterestedEventsCount() + "" );
        mTVGoingCount.setText( accountInfo.getmUserGoingEventsCount() + "" );
        if(!accountInfo.getmBiography().equals( "null" ))
            mETBio.setText( accountInfo.getmBiography() );
        else
            mETBio.setText(R.string.account_overview_bio_placeholder_text);
        if(accountInfo.getmImagePath().contains( "png" ) || accountInfo.getmImagePath().contains( "jpg" )
                || accountInfo.getmImagePath().contains( "gif" ) ) {
            Glide.with( mThisFrag )
                    .load( accountInfo.getmImagePath() )
                    .into( mAccountImage );
        }
    }

    private JSONObject formJsonObject(){
        JSONObject jsonObject = new JSONObject(  );
        try {
            jsonObject.put("Email", mTVEmail.getText().toString());
            jsonObject.put( "Biography", mETBio.getText().toString() );
            jsonObject.put( "Token", mUtils.getAppToken( getContext() ) );
            if(!mEncodedImage.equals( "" ))
                jsonObject.put( "ImagePath", mEncodedImage );
        }
        catch(JSONException e){
            Log.e(TAG, "AccountOverviewInfoFragment -> formJsonObject()" + e.toString());
        }

        return jsonObject;
    }


    private void fetchUserData(){
        mProgressBarHolder.setVisibility( View.VISIBLE );
        okHttpClient = apiRequest.generateOkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String getUserDataRoute = apiConf.getACCOUNT_GET_USER_API_ROUTE() + "/" + mUtils.getUserId( getContext() );

        Request request = apiRequest.getRequestObject( getUserDataRoute, true, false, "", null );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    String email = responseRoot.getString( "Email" );
                    String displayName = responseRoot.getString( "DisplayName" );
                    String biography = responseRoot.getString( "Biography" );
                    int userInterestedEventsCount = 0;
                    int userGoingEventsCount = 0;
                    int userCreatedEventsCount = 0;
                    JSONArray userEvents = responseRoot.getJSONArray( "UserEvents" );
                    for(int i = 0; i < userEvents.length(); i++){
                        JSONObject userEvent = userEvents.getJSONObject( i );
                        if(userEvent.getBoolean( "Interested" ))
                            userInterestedEventsCount++;
                        else if(userEvent.getBoolean( "Going" ))
                            userGoingEventsCount++;
                        else if (userEvent.getBoolean( "Created" ))
                            userCreatedEventsCount++;
                    }
                    String imagePath = responseRoot.getString( "ImagePath" );

                    bindLayoutData(
                            new AccountOverviewInfoPOJO(
                              email,
                              displayName,
                              biography,
                              userInterestedEventsCount,
                              userGoingEventsCount,
                              userCreatedEventsCount,
                              imagePath
                            )
                    );
                    mProgressBarHolder.setVisibility( View.GONE );
                }
                catch (JSONException e){
                    mProgressBarHolder.setVisibility( View.GONE );
                    Log.e("OkHttp", "Error while parsing api/users/{id} response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                mProgressBarHolder.setVisibility( View.GONE );
                Log.e("OkHttp", "Api call http://<host>/api/users/{id} failed; " + e.toString());
            }

        } );
    }

    private void updateUserBio(){
        okHttpClient = new OkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String updateUser = apiConf.getACCOUNT_UPDATE_USER_API_ROUTE();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(updateUser)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        RequestBody body = RequestBody.create(formJsonObject().toString(), mMediaType );

        Request request = new Request.Builder(  )
                .header("Authorization", "Bearer " + mUtils.getAppToken( getContext() ))
                .url( url )
                .post( body )
                .build();

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    boolean isSuccess = responseRoot.getBoolean( "Success" );
                    if(isSuccess)
                        Toast.makeText( getContext(), "Updated successfully", Toast.LENGTH_SHORT ).show();

                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/users/update response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/users/update failed; " + e.toString());
            }

        } );
    }

    private void uploadUserImage(String base64String){
        okHttpClient = new OkHttpClient();

        AppConf apiConf = AppConf.getInstance();
        String updateUserImage = apiConf.getACCOUNT_UPLOAD_IMAGE_API_ROUTE();

        Request request = apiRequest.getRequestObject( updateUserImage, true, true, formJsonObject().toString(), mMediaType );

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseRoot = new JSONObject( body );
                    boolean isSuccess = responseRoot.getBoolean( "Success" );
                    if(isSuccess)
                        Toast.makeText( getContext(), "Updated successfully", Toast.LENGTH_SHORT ).show();

                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/users/upload-image response data - " + e.toString());
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/users/upload-image failed; " + e.toString());
            }

        } );
    }

    private String convertBitmapToBase64String(Bitmap bitmapImage){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encodedImage = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    final android.net.Uri imageUri = data.getData();
//                    Bitmap selectedImage = getThumbnail( imageUri );
                    final java.io.InputStream imageStream = getContext().getContentResolver().openInputStream( imageUri );
                    final Bitmap selectedImage = BitmapFactory.decodeStream( imageStream );
                    mAccountImage.setImageBitmap( selectedImage );
                    String encodedImage = convertBitmapToBase64String( selectedImage );
                    mEncodedImage = encodedImage;
                    uploadUserImage( encodedImage );
                } catch (FileNotFoundException e) {
                    Log.e( TAG, "onActivityResult -> " + e.toString() );
                }
            }
        }
    }

    /**
     * @TODO
     * @param uri
     * @return
     * @throws FileNotFoundException
     */
    private Bitmap getThumbnail(android.net.Uri uri) throws FileNotFoundException {
        java.io.InputStream input = getContext().getContentResolver().openInputStream(uri);
        try {
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream( input, null, onlyBoundsOptions );
            input.close();


            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio( ratio );
            bitmapOptions.inDither = true; //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
            input = getContext().getContentResolver().openInputStream( uri );
            Bitmap bitmap = BitmapFactory.decodeStream( input, null, bitmapOptions );
            input.close();
            return bitmap;
        }
        catch (IOException e) {
            Log.e( TAG, e.toString());
        }
        return null;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
}
