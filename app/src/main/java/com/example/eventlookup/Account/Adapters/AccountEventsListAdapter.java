package com.example.eventlookup.Account.Adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventlookup.Account.POJOs.AccountEventPOJO;
import com.example.eventlookup.R;
import com.example.eventlookup.Shared.BaseViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountEventsListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final String TAG = "EventAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    public List<AccountEventPOJO> mEventsList;

    public void setCallback(Callback callback){
        this.mCallback = callback;
    }

    public void addItems(List<AccountEventPOJO> list){
        this.mEventsList = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder( inflater.inflate( R.layout.account_overview_event_card, parent, false) );
            case VIEW_TYPE_EMPTY:
                return new EmptyViewHolder( inflater.inflate( R.layout.account_overview_event_card, parent, false) );
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

    // Interface for callback
    public interface Callback {
        void onEmptyViewRetryClick();
    }
    // Interface for callback

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

                }
            } );
        }
    }

    public class EmptyViewHolder extends BaseViewHolder{

        public EmptyViewHolder(View view){
            super(view);
        }

        @Override
        protected void clear() {

        }
    }
}
