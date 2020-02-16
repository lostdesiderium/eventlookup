package com.example.eventlookup;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int CurrentPosition;

    public BaseViewHolder (View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        CurrentPosition = position;
        clear();
    }

    public int getCurrentPosition() {
        return CurrentPosition;
    }

}
