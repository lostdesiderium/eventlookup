package com.example.eventlookup.Event.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eventlookup.BaseViewHolder;
import com.example.eventlookup.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ImageSliderPageAdapter extends RecyclerView.Adapter<ImageSliderPageAdapter.EventSliderViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> ListImageUrl;

    // For testing purposes
    public ImageSliderPageAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.ListImageUrl = new ArrayList<>(  );
    }

    public ImageSliderPageAdapter(Context context, ArrayList<String> views){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.ListImageUrl = new ArrayList<>();
        this.ListImageUrl = views;
    }

    @NonNull
    @Override
    public EventSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.event_slider_item, parent, false);
        return new EventSliderViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull EventSliderViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return ListImageUrl.size();
    }


    public class EventSliderViewHolder extends BaseViewHolder {
        @BindView(R.id.IV_event_slider_item)
        ImageView sliderImage;

        public EventSliderViewHolder(View itemView){
            super(itemView);
        }

        public void onBind(int position){
            super.onBind(position);

            final String imageUrl = ListImageUrl.get( position );
            if(imageUrl != null){
                ImageView imageHolder = itemView.findViewById( R.id.IV_event_slider_item );
                Glide.with(itemView.getContext())
                        .load( imageUrl )
                        .into(imageHolder);

            }
        }

        // Inherited method from BaseViewHolder
        public void clear(){

        };
    }
}
