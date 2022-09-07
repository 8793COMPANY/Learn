package com.corporation8793.activity;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDecoration_Width extends RecyclerView.ItemDecoration {
    private final int divHeight;
    private final int itemCount;

    public RecyclerDecoration_Width(int divHeight, int itemCount){
        this.divHeight = divHeight;
        this.itemCount = itemCount;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        Log.e("position",parent.getChildAdapterPosition(view)+"");
        Log.e("count",parent.getChildCount()+"");
//            outRect.right = divHeight;
//        Log.e("position",parent.getChildCount()+"");
        if (itemCount >3 && parent.getChildAdapterPosition(view) == 0) {
            Log.e("hello","in!");
            outRect.left = divHeight;
        }
        outRect.right = divHeight;
    }

}