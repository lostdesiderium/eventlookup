package com.app.eventlookup.Event.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.app.eventlookup.Shared.BaseViewHolder;
import com.app.eventlookup.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ImageSliderPageAdapter extends RecyclerView.Adapter<ImageSliderPageAdapter.EventSliderViewHolder>  {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> ListImageUrl;
    private ConstraintLayout CL_sliderIndicator;
    private View thisFragView;
    private int mLastSliderPosition = 0;

    // For testing purposes
    public ImageSliderPageAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.ListImageUrl = new ArrayList<>(  );
    }

    public ImageSliderPageAdapter(Context context, ArrayList<String> views, View view){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.ListImageUrl = new ArrayList<>();
        this.ListImageUrl = views;
        this.thisFragView = view;
        createSliderIndicator( views.size() );
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



    private void createSliderIndicator(int count){
        CL_sliderIndicator = thisFragView.findViewById( R.id.CL_of_slider_indicator );

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );


        for(int i = 0; i < count; i++){
            ImageView imageView = (ImageView) layoutInflater.inflate( R.layout.indicator_active, (ViewGroup) CL_sliderIndicator, false );
            if( i  != 0) {
                imageView.setImageDrawable( context.getDrawable( R.drawable.oval_inactive ) );
            }

            imageView.setPadding( 40 * i, 0 ,0 , 0 );
            imageView.setTag( ListImageUrl.get( i ) );

            CL_sliderIndicator.addView( imageView );
        }
    }

    public void changeIndicator(int position){

        if(position == mLastSliderPosition)
            return;

        if(position < 0)
            position = 0;

        // change current indicator to active
        ImageView imageView = (ImageView) thisFragView.findViewWithTag( ListImageUrl.get( position ) );

        if(imageView != null) {
            imageView.setImageDrawable( context.getDrawable( R.drawable.oval_active ) );
        }

        // change last indicator to inactive
        imageView = (ImageView) thisFragView.findViewWithTag(  ListImageUrl.get(mLastSliderPosition) );

        if(imageView != null) {
            imageView.setImageDrawable( context.getDrawable( R.drawable.oval_inactive ) );
        }

        mLastSliderPosition = position;
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
