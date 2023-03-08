package com.learn4.view.dictionary;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.data.dto.CodeBlock;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.ui.BlockGroup;
import com.google.blockly.android.ui.BlockView;
import com.google.blockly.model.Block;

import java.util.ArrayList;

public class CodeDictionaryAdapter extends RecyclerView.Adapter<CodeDictionaryAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<CodeBlock> listData = new ArrayList<>();
    Context context;
    BlocklyController controller;
    boolean check = true;

    public CodeDictionaryAdapter(Context context, BlocklyController controller, ArrayList<CodeBlock> arrayList){
        listData = arrayList;
        this.context = context;
        this.controller = controller;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_dictionary_itemview, parent, false);
//        view.getLayoutParams().height = 350;

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        Log.e("codeDictionary","onBindeViewHolder in");
        holder.onBind(listData.get(position));

        holder. block_name.setText(listData.get(position).getName());
        holder.block_info.setText(listData.get(position).getInfo());
//        holder.block_img.setBackgroundResource(listData.get(position).getImg());

        holder.block_name.setTextColor(listData.get(position).getBlock().getColor());



        controller.mHelper.getParentBlockGroup(listData.get(position).getBlock());

        Block block = listData.get(position).getBlock();
//        if (controller.getBlockViewFactory().getView(listData.get(position).getBlock()) != null)
//            Log.e("codedictionary","getView not null");
//        else {
//            Log.e("codedictionary", "getView null");
//            controller.addRootBlock(listData.get(position).getBlock());
//        }

        if (holder.blockView != null)
            holder.blockView.removeAllViews();

        BlockView blockView = controller.getBlockViewFactory().getView(listData.get(position).getBlock());
        BlockGroup group = controller.mHelper.getParentBlockGroup(listData.get(position).getBlock());
        if (group != null)
            Log.e("codedictionary","getParentBlockGroup not null");
        else {
            Log.e("codedictionary", "getParentBlockGroup null");
            group = controller.mHelper.getBlockViewFactory().buildBlockGroupTree(block, null,null);
//            controller.addRootBlock(listData.get(position).getBlock());
        }

        if(group.getParent() != null) {
            ((ViewGroup)group.getParent()).removeView(group); // <- fix
        }

        Log.e("codedictionary",block.getType());
//        if (block.getType().equals("turtle_setup_loop")) {
//            group.setScaleX(0.8f);
//            group.setScaleY(0.8f);
//            group.setPadding(0,0,0,0);

        Log.e("위치", "전x : " + group.getX()+"");
        Log.e("위치", "전y : " + holder.block_name.getX()+"");
            group.setScaleX(0.8f);
            group.setScaleY(0.8f);
//        }

        holder.blockView.addView(group);

//        BlockGroup group = new BlockGroup(context,controller.getWorkspaceHelper());

//        if (controller.getBlockFactory())

//        controller.getBlockViewFactory().getView(listData.get(position).getBlock());

//        BlockGroup copyView = controller.mHelper.getParentBlockGroup(listData.get(position).getBlock());

//        BlockGroup copyView  = controller.mHelper.getBlockViewFactory().getView(listData.get(position).getBlock()).getParentBlockGroup();
//        BlockGroup copyView = controller.addRootBlock(listData.get(position).getBlock());
//        BlockGroup copyView = controller.mHelper.getRootBlockGroup(listData.get(position).getBlock());
//        View view = (View) controller.getBlockViewFactory().getView(listData.get(position).getBlock().getRootBlock());
//        if ()
//        if (controller.getBlockViewFactory().getView(listData.get(position).getBlock()) != null) {
//            Log.e("hi", "not null");
//            ((ViewGroup) controller.getBlockViewFactory().getView(listData.get(position).getBlock().getRootBlock()).getParent())
//                    .removeView((ViewGroup) controller.getBlockViewFactory().getView(listData.get(position).getBlock().getRootBlock()));
//        }else
//            Log.e("hi","im null");
//        holder.blockView.addView((View)controller.getBlockViewFactory().getView(listData.get(position).getBlock()));
//        if (holder.blockView != null)
//            Log.e("codeDictionary","blockview not null");
//
//        if(controller.mHelper.getRootBlockGroup(controller.getBlockViewFactory().getView(listData.get(position).getBlock().getRootBlock())).getParent() != null) {
//            Log.e("codeDictionary", "getParent not null");
////            ((ViewGroup)copyView.getParent()).removeView(copyView); // <- fix
//        }
//
//        if (controller.mHelper.getRootBlockGroup(controller.getBlockViewFactory().getView(listData.get(position).getBlock())) != null) {
//            Log.e("codeDictionary", "getRootblockGroup not null in!");
//
////                holder.blockView.removeView(controller.mHelper.getRootBlockGroup(controller.getBlockViewFactory().getView(listData.get(position).getBlock())));
//            if(controller.mHelper.getRootBlockGroup(controller.getBlockViewFactory().getView(listData.get(position).getBlock())).getParent() != null) {
//                Log.e("codeDictionary", "getParent not null");
//            ((ViewGroup)controller.mHelper.getRootBlockGroup(controller.getBlockViewFactory().getView(listData.get(position).getBlock())).getParent())
//                    .removeView(controller.mHelper.getRootBlockGroup(controller.getBlockViewFactory().getView(listData.get(position).getBlock()))); // <- fix
//            }
//            holder.blockView.addView(controller.mHelper.getRootBlockGroup(controller.getBlockViewFactory().getView(listData.get(position).getBlock().getRootBlock())));
//        }else
//            Log.e("hi null","in!");


//        if(copyView.getParent() != null && check) {
//            Log.e("in", "getParent not null");
//            ((ViewGroup)copyView.getParent()).removeView(copyView); // <- fix
//            check = false;
//            holder.blockView.addView(copyView);
//        }



//        holder.blockView.addView(listData.get(position).getBlock());
//        holder.blockView.addView(listData.get(position).getBlock());
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
        private FrameLayout blockView;
        private ConstraintLayout parent_layout;


        ItemViewHolder(View itemView) {
            super(itemView);
            block_name = itemView.findViewById(R.id.block_name);
            block_info = itemView.findViewById(R.id.block_info);
//            block_img = itemView.findViewById(R.id.block_img);
            blockView = itemView.findViewById(R.id.block_view);
//            parent_layout = itemView.findViewById(R.id.parent_layout);

        }

        void onBind(CodeBlock block) {
            Log.e("block hi","hey");



//            blockView.addView();

//            (BlockView)block.getBlock();

        }
    }
}