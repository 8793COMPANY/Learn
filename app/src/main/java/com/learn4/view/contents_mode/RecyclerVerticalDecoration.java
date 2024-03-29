package com.learn4.view.contents_mode;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerVerticalDecoration extends RecyclerView.ItemDecoration {

    private final int divHeight;


    public RecyclerVerticalDecoration(int divHeight) {
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        Log.e("divHeight",divHeight+"");
        int position = parent.getChildAdapterPosition(view);
        if (position != parent.getAdapter().getItemCount() - 1)

            outRect.bottom = divHeight;

        if (position !=0){
            outRect.top = -12;
        }

    }
}
