package com.learn4.view.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;

    private int spacing;

    private int outerMargin;

    public ItemDecoration(Context context) {
        spanCount = 4;
        spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12, context.getResources().getDisplayMetrics());
        outerMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int maxCount = parent.getAdapter().getItemCount();

        int position = parent.getChildAdapterPosition(view);

        int column = position % spanCount;

        int row = position / spanCount;

        int lastRow = (maxCount - 1) / spanCount;



        outRect.left = column * spacing / spanCount;
        outRect.right = spacing - (column + 1) * spacing / spanCount;

        outRect.top = 30;



        if (row == lastRow) {

            outRect.bottom = outerMargin;

        }
    }
}
