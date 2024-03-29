package com.learn4.view.contents_mode;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.view.recyclerview.RecyclerDecoration;

public class VerticalViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView recyclerView;
    public TextView level;

    public View bottom_section;

    public VerticalViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.level_contents);
        level = itemView.findViewById(R.id.level);
        bottom_section = itemView.findViewById(R.id.bottom_section);

        recyclerView.addItemDecoration(new RecyclerVerticalDecoration(0));
    }
}
