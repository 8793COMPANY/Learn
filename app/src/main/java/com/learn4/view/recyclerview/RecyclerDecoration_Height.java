package com.learn4.view.recyclerview;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDecoration_Height extends RecyclerView.ItemDecoration {
    private final int divHeight;

    public RecyclerDecoration_Height(int divHeight){
        this.divHeight = divHeight;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        Log.e("position",parent.getChildAdapterPosition(view)+"");
        if (parent.getChildAdapterPosition(view) != 0){
            outRect.left = divHeight;
        }
    }

}