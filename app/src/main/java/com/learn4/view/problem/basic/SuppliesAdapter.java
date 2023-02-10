package com.learn4.view.problem.basic;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.view.custom.view.AnswerItem;
import com.learn4.data.dto.Supplies;

import java.util.ArrayList;
import java.util.List;

public class SuppliesAdapter extends RecyclerView.Adapter<SuppliesAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Supplies> supplies;
    private LayoutInflater inflater;

    public interface OnItemClickEventListener {
        void onItemClick(int position, Boolean click);
    }

    private OnItemClickEventListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        mItemClickListener = a_listener;
    }

    public SuppliesAdapter(Context context, ArrayList<Supplies> supplies) {
        this.context = context;
        this.supplies = supplies;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.supplies_itemview, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//        Chapter chapter = chapters.get(position);
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)holder.itemView.getLayoutParams();
        layoutParams.width = 520;
        holder.itemView.requestLayout();

        holder.aImg.setBackgroundResource(supplies.get(position).getImg());

        holder.aImg.setSelected(supplies.get(position).getClick());
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position, @NonNull List<Object> payloads) {
//        Chapter chapter = chapters.get(position);
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }else {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    String type = (String) payload;
                    if (TextUtils.equals(type, "click")) {
                        Log.e("click","adapter");
                        supplies.get(position).setClick(true);
                        holder.aImg.setSelected(true);
                    }
                    if (TextUtils.equals(type, "no")) {
                        Log.e("click","adapter");
                        supplies.get(position).setClick(false);
                        holder.aImg.setSelected(false);
                    }
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return supplies.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public AnswerItem aImg;

        public CustomViewHolder(View itemView) {
            super(itemView);
            aImg = itemView.findViewById(R.id.supplies);


            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition() ;
                if (pos != RecyclerView.NO_POSITION) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(getAdapterPosition(),supplies.get(pos).getClick()) ;
                    }
                }
            });
        }
    }
}