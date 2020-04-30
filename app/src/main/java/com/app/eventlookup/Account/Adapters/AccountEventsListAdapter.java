package com.app.eventlookup.Account.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.eventlookup.Account.POJOs.AccountEventPOJO;
import com.app.eventlookup.R;
import com.app.eventlookup.Shared.BaseViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountEventsListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final String TAG = "EventAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_FAILED = 2;

    private static AELACallback mCallback;
    protected View mParentView;
    public List<AccountEventPOJO> mEventsList;
    public static boolean mFailedApiCall = false;

    // Interface for callback
    public interface AELACallback {
        void onEmptyViewRetryClick();
        void removeEventApiCall(String eventId);
    }
    // Interface for callback

    public void setCallback(AELACallback callback)
    {
        this.mCallback = callback;
    }

    public void addItems(List<AccountEventPOJO> list){
        this.mEventsList = list;
    }

    public void setParentView(View view){
        mParentView = view;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder( inflater.inflate( R.layout.account_overview_event_card, parent, false) );
            case VIEW_TYPE_EMPTY:
                return new EmptyViewHolder( inflater.inflate( R.layout.placeholder_no_marked_event, parent, false) );
            case VIEW_TYPE_FAILED:
                return new FailedApiViewHolder( inflater.inflate( R.layout.placeholder_unsuccessful_call, parent, false) );
            default:
                return new EmptyViewHolder( inflater.inflate( R.layout.account_overview_event_card, parent, false) );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind( position );
    }

    @Override
    public int getItemViewType(int position) {
        if(mEventsList != null && mEventsList.size() > 0){
            return VIEW_TYPE_NORMAL;
        }
        else if(mFailedApiCall == true)
            return VIEW_TYPE_FAILED;
        else{
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if(mEventsList != null && mEventsList.size() > 0){
            return mEventsList.size();
        }
        else{
            return 1; // return 1 indicating that list is empty
        }
    }

    public void setFailedApiCall(boolean value){
        mFailedApiCall = value;
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.TV_account_event_title)
        TextView mEventTitle;

        @BindView(R.id.FAB_account_overview_event_remove)
        FloatingActionButton mBtnEventRemove;

        @BindView(R.id.IV_account_event_card_img)
        ImageView mEventImage;

        protected NavController mNavController;
        private String mEventId;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind( position );

            final AccountEventPOJO mEvent = mEventsList.get( position );

            if(!mEvent.getmImagePath().equals( "" )){
                Glide.with(itemView.getContext())
                        .load( mEvent.getmImagePath() )
                        .into(mEventImage);
            }
            if(!mEventTitle.equals( "" ))
                mEventTitle.setText( mEvent.getmTitle() );

            mBtnEventRemove.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.removeEventApiCall(mEvent.getmEventId());
                }
            } );

            // Here goes item event listener
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("eventId", mEvent.getmEventId());
                    Navigation.findNavController( mParentView ).navigate( R.id.action_accountOverviewFragment_to_eventOverviewFragment, bundle );
                }
            } );
        }

    }

    public class EmptyViewHolder extends BaseViewHolder{
        public EmptyViewHolder(View itemView){
            super(itemView);
        }

        @Override
        protected void clear(){

        }
    }

    public class FailedApiViewHolder extends BaseViewHolder{
        @BindView( R.id.BTN_event_retry )
        Button mBtnRetry;

        public FailedApiViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind( this, itemView );
        }

        @Override
        public void onBind(int position) {
            super.onBind( position );

            mBtnRetry.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mCallback != null)
                        mCallback.onEmptyViewRetryClick();
                }
            } );
        }

        @Override
        protected void clear(){

        }
    }
}
