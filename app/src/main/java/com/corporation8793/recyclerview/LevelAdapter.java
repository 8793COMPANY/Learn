package com.corporation8793.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corporation8793.R;
import com.corporation8793.data.Level;

import java.util.ArrayList;

public class LevelAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Level> levels;
    private LayoutInflater inflater;

    public LevelAdapter(Context context, ArrayList<Level> levels) {
        this.context = context;
        this.levels = levels;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.level_item_layout, parent, false);

        return new HorizonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((HorizonViewHolder) holder).recyclerView.setAdapter(new ChapterAdapter(context, levels.get(position).chapters));
        ((HorizonViewHolder) holder).recyclerView.addItemDecoration(new RecyclerDecoration(36));
        ((HorizonViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        ((HorizonViewHolder) holder).recyclerView.setHasFixedSize(true);
        String [] level_split = levels.get(position).level.split(" ");
        SpannableStringBuilder ssb = new SpannableStringBuilder(levels.get(position).level);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ff953a")), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new RelativeSizeSpan(1.2f), 5, 7, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((HorizonViewHolder) holder).level.setText(ssb);

    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return levels.get(position).id;
    }
}