package com.example.eventlookup.Event.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.eventlookup.BaseViewHolder;
import com.example.eventlookup.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ImageSliderPageAdapter extends RecyclerView.Adapter<ImageSliderPageAdapter.EventSliderViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Integer> images;

    // For testing purposes
    public ImageSliderPageAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        images = new ArrayList<>(  );
        fakeDataFill();
    }

    public ImageSliderPageAdapter(Context context, ArrayList<Integer> views){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.images = new ArrayList<>();
        this.images = views;
    }

    public void fakeDataFill(){
        images.add( R.drawable.event_img3 );
        images.add( R.drawable.event_card_background_gradient );
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
        return images.size();
    }


    public class EventSliderViewHolder extends BaseViewHolder {
        @BindView(R.id.IV_event_slider_item)
        ImageView sliderImage;

        public EventSliderViewHolder(View itemView){
            super(itemView);
        }

        public void onBind(int position){
            super.onBind(position);

            final int imageId = images.get( position );
            if(imageId != 0){
                ImageView img = itemView.findViewById( R.id.IV_event_slider_item );
                img.setImageResource( imageId );
            }
        }

        // Inherited method from BaseViewHolder
        public void clear(){

        };
    }
}
