package com.corporation8793.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corporation8793.R;
import com.corporation8793.dto.CodeBlock;

import java.util.ArrayList;
import java.util.List;

public class CodeDictionaryAdapter extends RecyclerView.Adapter<CodeDictionaryAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<CodeBlock> listData = new ArrayList<>();
    Context context;

    public CodeDictionaryAdapter(Context context, ArrayList<CodeBlock> arrayList){
        listData = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_dictionary_itemview, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.

//        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }



    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView block_name;
        private TextView block_info;
        private ImageView block_img;


        ItemViewHolder(View itemView) {
            super(itemView);
            block_name = itemView.findViewById(R.id.block_name);
            block_info = itemView.findViewById(R.id.block_info);
            block_img = itemView.findViewById(R.id.block_img);

        }

        void onBind(CodeBlock block) {
            block_name.setText(block.getName());
            block_info.setText(block.getInfo());
            block_img.setBackgroundResource(block.getImg());

        }
    }
}