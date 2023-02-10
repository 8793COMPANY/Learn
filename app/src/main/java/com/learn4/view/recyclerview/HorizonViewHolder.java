package com.learn4.view.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;

public class HorizonViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView recyclerView;
    public TextView level;

    public HorizonViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.level_contents);
        level = itemView.findViewById(R.id.level);
    }
}
