package com.example.eventlookup.Event.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.eventlookup.BaseViewHolder;
import com.example.eventlookup.Event.POJOs.EventPOJO;
import com.example.eventlookup.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.eventlookup.Shared.GlideForApp;

import androidx.navigation.NavController;

public class EventAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final String TAG = "EventAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback callback;
    public List<EventPOJO>  eventsList;

    public EventAdapter(){

    }

    public EventAdapter(ArrayList<EventPOJO> eventsList) {
        this.eventsList = eventsList;
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }


    // ------------------ Overriding methods from default RecyclerView.Adapter but Adapter itself is handling their calls
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder( LayoutInflater.from(parent.getContext()).inflate( R.layout.event_card_2, parent, false) );
            case VIEW_TYPE_EMPTY:
                return new EmptyViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_2, parent, false) );
            default:
                return new EmptyViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_2, parent, false) );
        }
    }

    @Override
    public int getItemViewType(int position){
        if(eventsList != null && eventsList.size() > 0){
            return VIEW_TYPE_NORMAL;
        }
        else{
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind( position );
    }

    @Override
    public int getItemCount() {
        if (eventsList != null && eventsList.size() > 0) {
            return eventsList.size();
        } else {
            return 1; // list is containing one element which shows that list is empty
        }
    }
    // ------------------ Overriding methods from default RecyclerView.Adapter but Adapter itself is handling their calls


    public void addItems(ArrayList<EventPOJO> eventsList){
        this.eventsList = eventsList;
        notifyDataSetChanged(); // Should be used as a last resort only because its not so efficient (notifyItemChanged for item change and notifyItemInserted for structural change
    }

    // Interface for callback
    public interface Callback {
        void onEmptyViewRetryClick();
    }
    // Interface for callback

    public class ViewHolder extends BaseViewHolder{

        @BindView(R.id.IV_event_card_img)
        ImageView eventImage;

        @BindView(R.id.TV_event_title)
        TextView eventTitle;

        @BindView(R.id.TV_event_short_desc)
        TextView eventShortDesc;

        @BindView(R.id.TV_event_location)
        TextView eventLocation;

        @BindView( R.id.TV_event_date )
        TextView eventDate;

        private @Nullable
        NavController navController;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind( this, itemView );
        }

        public void onBind(int position){
            super.onBind(position);

            final EventPOJO event = eventsList.get(position);

            if(event.getImageURL() != null){
                Glide.with(itemView.getContext()).
                        load(event.getImageURL())
                        .into(eventImage);

            }

            if(event.getEventTitle() != null){
                eventTitle.setText( event.getEventTitle() );
            }
            if(event.getEventShortDescription() != null){
                eventShortDesc.setText( event.getEventShortDescription() );
            }
            if(event.getEventLocation() != null){
                eventLocation.setText( event.getEventLocation().toString() );
            }
            if(event.getEventDate() != null){
                eventDate.setText( event.getEventDate().toString() );
            }

            // Here goes item event listener
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("eventId", event.getId());
                     Navigation.findNavController( itemView ).navigate( R.id.action_eventListFragment_to_eventOverviewFragment, bundle );

                }
            } );

        }

        protected void clear(){
            eventImage.setImageDrawable( null );
            eventTitle.setText( "" );
            eventShortDesc.setText( "" );
            eventLocation.setText( "" );
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
}
