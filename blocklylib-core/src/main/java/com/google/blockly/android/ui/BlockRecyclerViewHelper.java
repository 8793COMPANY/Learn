/*
 *  Copyright 2017 Google Inc. All Rights Reserved.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.blockly.android.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.blockly.android.R;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.control.ConnectionManager;
import com.google.blockly.model.Block;
import com.google.blockly.model.BlocklyCategory;
import com.google.blockly.model.WorkspacePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the standard configuration for a {@link RecyclerView}
 * to display a list of blocks, labels, and buttons.
 */

public class BlockRecyclerViewHelper {
    private static final String TAG = "BlockRVHelper";
    static boolean [] check = {true, true, true, true};

    private final Handler mHandler = new Handler();
    private final WorkspacePoint mTempWorkspacePoint = new WorkspacePoint();

    private final RecyclerView mRecyclerView;
    private final Context mContext;
    private final LayoutInflater mHelium;
    private final Adapter mAdapter;
    private final CategoryCallback mCategoryCb;
    private final LinearLayoutManager mLayoutManager;

    private WorkspaceHelper mHelper;
    private ConnectionManager mConnectionManager;
    private FlyoutCallback mCallback;
    private BlocklyCategory mCurrentCategory;
    private BlockTouchHandler mTouchHandler;
    int width = 0;
    int block_width = 0;
    BlockGroup toolbox_bg;
    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    int [] first_check = {0,0,0,0};

    public BlockRecyclerViewHelper(RecyclerView recyclerView, Context context, int width) {
        mRecyclerView = recyclerView;
        mContext = context;
        mHelium = LayoutInflater.from(mContext);
        mAdapter = new Adapter();
        mCategoryCb = new CategoryCallback();
        mLayoutManager = new LinearLayoutManager(context);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ItemSpacingDecoration(mAdapter));
        this.width = width;
    }

    /**
     * Initialize this helper. The controller and callback allows user interactions to
     * be handled correctly.
     *
     * @param controller The controller to use for view creation and drag handling.
     * @param callback The {@link FlyoutCallback} that defines how the block list will respond to
     * user events.
     */
    public void init(BlocklyController controller, FlyoutCallback callback) {
        mCallback = callback;
        mHelper = controller.getWorkspaceHelper();
        mConnectionManager = controller.getWorkspace().getConnectionManager();

        mTouchHandler = controller.getDragger()
                .buildImmediateDragBlockTouchHandler(new DragHandler());


    }

    /**
     * Reset all the initialized components so this object may be attached to a new
     * controller or callback. The context and recycler view this was created with will
     * be retained.
     */
    public void reset() {
        mCallback = null;
        mHelper = null;
        mConnectionManager = null;
        mTouchHandler = null;
    }

    /**
     * Change the direction blocks should be laid out and scrolled in the RecyclerView.
     * @param scrollOrientation {@link OrientationHelper#HORIZONTAL} or
     * {@link OrientationHelper#VERTICAL}.
     */
    public void setScrollOrientation(int scrollOrientation) {
        mLayoutManager.setOrientation(scrollOrientation);
    }

    /**
     * Sets the category to connect to the {@link RecyclerView}.
     *
     * @param category The category to display blocks for.
     */
    public void setCurrentCategory(@Nullable BlocklyCategory category) {
        Log.e("setCurrent","click");


        if (mCurrentCategory == category) {
            return;
        }
        if (mCurrentCategory != null) {
            mCurrentCategory.setCallback(null);
        }
        mCurrentCategory = category;
        mAdapter.notifyDataSetChanged();



//        }catch (IndexOutOfBoundsException e){
//            e.printStackTrace();
//        }


        try{

//            List<BlocklyCategory.CategoryItem> items = mCurrentCategory == null
//                    ? new ArrayList<BlocklyCategory.CategoryItem>()
//                    : mCurrentCategory.getItems();
//
//                BlocklyCategory.CategoryItem item = items.get(0);
//                final Block block = ((BlocklyCategory.BlockItem) item).getBlock();
//                block.setEditable(true);
//                toolbox_bg = mHelper.getParentBlockGroup(block);
//
//
//                if (toolbox_bg == null) {
//                    toolbox_bg = mHelper.getBlockViewFactory().buildBlockGroupTree(
//                            block, mConnectionManager, mTouchHandler);
//                }

//            toolbox_bg.setScaleX(0.8f);
//            toolbox_bg.setScaleY(0.8f);



//                mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//
//                    public void onGlobalLayout() {
//
//
//                        block_width= toolbox_bg.getWidth();
//
//                        Log.e(TAG, "width = " + block_width);
//
//                        removeOnGlobalLayoutListener(toolbox_bg.getViewTreeObserver(),mGlobalLayoutListener);
//
//                    }
//
//                };
//
//                toolbox_bg.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);


            RelativeLayout.MarginLayoutParams marginLayoutParams = (RelativeLayout.MarginLayoutParams) mRecyclerView.getLayoutParams();
        if (mCurrentCategory.getCategoryName().toString().equals("Logic") ){
            Log.e("current","logic");
//            if(width != 1280 ){
////                marginLayoutParams.width =(int)(width / 1280 * 991);
//                marginLayoutParams.width =(int)(width / 1280 * 1400);
//            }else{
//                marginLayoutParams.width =1007;
//            }

//            if (getLargeSize(1) > 900)
//                marginLayoutParams.width = getLargeSize(1);
//            marginLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getLargeSize(0);
            marginLayoutParams.setMargins((int)(width / 1280 * 55), 0, 0, 0);
//            mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(991, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        else if ( mCurrentCategory.getCategoryName().toString().equals("Loops")){
            Log.e("current","loops");
//            if(width != 1280 ){
//                marginLayoutParams.width =(int)(width / 1280 * 1400)  ;
//            }else{
//                marginLayoutParams.width =1007;
//            }
//            if (getLargeSize(0) > 800)
//                marginLayoutParams.width = getLargeSize(0);
            getLargeSize(0);
//            marginLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            marginLayoutParams.setMargins((int)(width / 1280 * 55), 0, 0, 0);
//            mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(1307, ViewGroup.LayoutParams.MATCH_PARENT));
        }else if (mCurrentCategory.getCategoryName().toString().equals("Math")){
            Log.e("current","math");
            marginLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            if(width != 1280 ){
//                if (getLargeSize(1) > 1200)
//                    marginLayoutParams.width = getLargeSize(1);
//                marginLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                getLargeSize(1);
                marginLayoutParams.setMargins((int)(width / 1280 * 55), 0,
                        0, 0);
            }else{
                marginLayoutParams.width =1280;
                marginLayoutParams.setMargins(width - 1253, 0, 0, 0);
            }

//                marginLayoutParams.width = getLargeSize(1);

//            mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(1240 , ViewGroup.LayoutParams.MATCH_PARENT));

        }else if (mCurrentCategory.getCategoryName().toString().equals("Text")){
            Log.e("current","Text");
//            marginLayoutParams.width =(int)(width /1280.0 * 900) ;
//            if (getLargeSize(2) > 600)
//                marginLayoutParams.width = getLargeSize(2);
//            marginLayoutParams.width = getLargeSize(1);
//            marginLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getLargeSize(2);
            marginLayoutParams.setMargins((int)(width / 1280 * 55), 0, 0, 0);
        }

//
//
            mRecyclerView.setLayoutParams(marginLayoutParams);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }




        if (mCurrentCategory != null) {
            mCurrentCategory.setCallback(mCategoryCb);
        }
    }





    private static void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        Log.e("remove","!!");

        if (observer == null) {
            Log.e("remove","null");

            return ;

        }


        if (Build.VERSION.SDK_INT >= 16){
            Log.e("remove","JELLY");
            observer.removeGlobalOnLayoutListener(listener);
        }else{
            Log.e("remove","INT");
            observer.removeOnGlobalLayoutListener(listener);
        }


    }


    public void getLargeSize(int pos){
        Log.e("come","getLargeSize");

        List<BlocklyCategory.CategoryItem> items = mCurrentCategory == null
                ? new ArrayList<BlocklyCategory.CategoryItem>()
                : mCurrentCategory.getItems();
//        Log.e("mCurrent",mCurrentCategory.getCategoryName().toString());



        try {
            BlocklyCategory.CategoryItem item = items.get(pos);
            final Block block = ((BlocklyCategory.BlockItem) item).getBlock();
            block.setEditable(true);
            toolbox_bg = mHelper.getParentBlockGroup(block);


            if (toolbox_bg == null) {
                toolbox_bg = mHelper.getBlockViewFactory().buildBlockGroupTree(
                        block, mConnectionManager, mTouchHandler);
            }

            toolbox_bg.setScaleX(0.8f);
            toolbox_bg.setScaleY(0.8f);


                mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override

                    public void onGlobalLayout() {
                        block_width = toolbox_bg.getWidth();
                        Log.e(TAG, "width = " + block_width);
                        try {
                            RelativeLayout.MarginLayoutParams marginLayoutParams = (RelativeLayout.MarginLayoutParams) mRecyclerView.getLayoutParams();
                            marginLayoutParams.width = block_width;
                            marginLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            mRecyclerView.setLayoutParams(marginLayoutParams);
                            removeOnGlobalLayoutListener(toolbox_bg.getViewTreeObserver(), mGlobalLayoutListener);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }

                };


            toolbox_bg.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
            Log.e("hello","addOnGlobalLayout");

        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

//        return block_width;
    }

    /**
     * @return The currently set category.
     */
    public @Nullable
    BlocklyCategory getCurrentCategory() {
        return mCurrentCategory;
    }

    /**
     * Calculates the workspace point for a {@link PendingDrag}, such that the
     * {@link MotionEvent#ACTION_DOWN} location remains in the same location on the screen
     * (i.e., under the user's finger), and calls
     * {@link FlyoutCallback#getDraggableBlockGroup} with the location. The workspace
     * point accounts for the {@link WorkspaceView}'s location, pan, and scale.
     *
     * @param pendingDrag The {@link PendingDrag} for the gesture.
     * @return The pair of {@link BlockGroup} and the view relative touch point returned by
     *         {@link FlyoutCallback#getDraggableBlockGroup}.
     */
    @NonNull
    private Pair<BlockGroup, ViewPoint> getWorkspaceBlockGroupForTouch(PendingDrag pendingDrag) {
        Log.e("getworkspace","blockgroupfortouch");
        BlockView touchedBlockView = pendingDrag.getTouchedBlockView();
        Block rootBlock = touchedBlockView.getBlock().getRootBlock();
        BlockView rootTouchedBlockView = mHelper.getView(rootBlock);
        BlockGroup rootTouchedGroup = rootTouchedBlockView.getParentBlockGroup();

        // Calculate the offset from rootTouchedGroup to touchedBlockView in view
        // pixels. We are assuming the only transforms between BlockViews are the
        // child offsets.
        View view = (View) touchedBlockView;
//        view.setBackgroundColor(Color.parseColor("#FF007F"));


        float offsetX = view.getX() + pendingDrag.getTouchDownViewOffsetX();
        float offsetY = view.getY() + pendingDrag.getTouchDownViewOffsetY();
        ViewGroup parent = (ViewGroup) view.getParent();
        while (parent != rootTouchedGroup) {
            view = parent;
            offsetX += view.getX();
            offsetY += view.getY();
            parent = (ViewGroup) view.getParent();
        }
        ViewPoint touchOffset = new ViewPoint((int) Math.ceil(offsetX),
                (int) Math.ceil(offsetY));

        // Adjust for RTL, where the block workspace coordinate will be in the top right
        if (mHelper.useRtl()) {
            offsetX = rootTouchedGroup.getWidth() - offsetX;
        }

        // Scale into workspace coordinates.
        int wsOffsetX = mHelper.virtualViewToWorkspaceUnits(offsetX);
        int wsOffsetY = mHelper.virtualViewToWorkspaceUnits(offsetY);

        // Offset the workspace coord by the BlockGroup's touch offset.
        mTempWorkspacePoint.setFrom(
                pendingDrag.getTouchDownWorkspaceCoordinates());
        mTempWorkspacePoint.offset(-wsOffsetX, -wsOffsetY);

        int itemIndex = mCurrentCategory.indexOf(rootBlock); // Should never be -1
        BlockGroup dragGroup = mCallback.getDraggableBlockGroup(itemIndex, rootBlock,
                mTempWorkspacePoint);
        return Pair.create(dragGroup, touchOffset);
    }

    /**
     * Internal implementation that listens to changes to the category and refreshes
     * the recycler view if it changes.
     */
    protected class CategoryCallback extends BlocklyCategory.Callback {

        @Override
        public void onItemAdded(int index, BlocklyCategory.CategoryItem item) {
            mAdapter.notifyItemInserted(index);
            Log.e("??","gg");
        }

        @Override
        public void onItemRemoved(int index, BlocklyCategory.CategoryItem block) {
            mAdapter.notifyItemRemoved(index);
        }

        @Override
        public void onCategoryCleared() {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Adapts {@link Block}s in list into {@link BlockGroup}s inside {@Link FrameLayout}.
     */
    public class Adapter extends RecyclerView.Adapter<BlockViewHolder> {

        @Override
        public int getItemCount() {

            return mCurrentCategory == null ? 0 : mCurrentCategory.getItems().size();
        }

        @Override
        public BlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new BlockViewHolder(mContext);
        }

        @Override
        public int getItemViewType(int position) {
            if (mCurrentCategory == null) {
                return -1;
            }
            Log.e("position",position+"");

//
//            mRecyclerView.setLayoutParams(marginLayoutParams);
            return mCurrentCategory.getItems().get(position).getType();
        }

        @Override
        public void onBindViewHolder(BlockViewHolder holder, int position) {
            List<BlocklyCategory.CategoryItem> items = mCurrentCategory == null
                    ? new ArrayList<BlocklyCategory.CategoryItem>()
                    : mCurrentCategory.getItems();

            BlocklyCategory.CategoryItem item = items.get(position);
            if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BLOCK) {
                Block block = ((BlocklyCategory.BlockItem) item).getBlock();
                block.setEditable(true);
                BlockGroup bg = mHelper.getParentBlockGroup(block);

                if (bg == null) {
                    bg = mHelper.getBlockViewFactory().buildBlockGroupTree(
                            block, mConnectionManager, mTouchHandler);
                } else {
                    bg.setTouchHandler(mTouchHandler);
                }


                bg.setScaleX(0.8f);
                bg.setScaleY(0.8f);
                bg.setPivotX(0);
                bg.setPivotY(0);


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        (int) (ViewGroup.LayoutParams.WRAP_CONTENT ),
                        (int) (ViewGroup.LayoutParams.WRAP_CONTENT ));

                layoutParams.rightMargin = 0;

                holder.mContainer.addView(bg, layoutParams);
                holder.mContainer.setForegroundGravity(0);

//                holder.mContainer.setScaleX(0.8f);
//                holder.mContainer.setScaleY(0.8f);


//                holder.mContainer.setPivotX(0);
//                holder.mContainer.setPivotY(0);


                holder.bg = bg;



            } else if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BUTTON) {
                BlocklyCategory.ButtonItem bItem = (BlocklyCategory.ButtonItem) item;
                final String action = bItem.getAction();
                Button button = (Button) mHelium.inflate(R.layout.simple_button, holder.mContainer,
                        false);
                holder.mContainer.addView(button);
                button.setText(bItem.getText());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallback != null) {
                            mCallback.onButtonClicked(v, action, mCurrentCategory);
                        }
                    }
                });
            } else if (item.getType() == BlocklyCategory.CategoryItem.TYPE_LABEL) {
                BlocklyCategory.LabelItem lItem = (BlocklyCategory.LabelItem) item;
                TextView label = (TextView) mHelium.inflate(R.layout.simple_label,
                        holder.mContainer, false);
                holder.mContainer.addView(label);
                label.setText(lItem.getText());
            } else {
                Log.e(TAG, "Tried to bind unknown item type " + item.getType());
            }
        }


        @Override
        public void onViewRecycled(BlockViewHolder holder) {
            // If this was a block item BlockGroup may be reused under a new parent.
            // Only clear if it is still a child of mContainer.
            Log.e("recycled","holder");
            if (holder.bg != null && holder.bg.getParent() == holder.mContainer) {
                holder.bg.unlinkModel();
                holder.bg = null;
                holder.mContainer.removeAllViews();
            }

            super.onViewRecycled(holder);
        }
    }

    private class BlockViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout mContainer;
        BlockGroup bg = null;  // Root of the currently attach block views.
        ImageView imageView;
        BlockViewHolder(Context context) {
            super(new FrameLayout(context));
            mContainer = (FrameLayout) itemView;
        }
    }

    /** {@link Dragger.DragHandler} implementation for BlockListViews. */
    private class DragHandler implements Dragger.DragHandler {

        @Override
        public Runnable maybeGetDragGroupCreator(final PendingDrag pendingDrag) {
            return new Runnable() {
                @Override
                public void run() {
                    // Acquire the draggable BlockGroup on the Workspace from the
                    // {@link OnDragListBlock}.
                    Pair<BlockGroup, ViewPoint> dragGroupAndTouchOffset =
                            getWorkspaceBlockGroupForTouch(pendingDrag);
                    if (dragGroupAndTouchOffset != null) {
                        pendingDrag.startDrag(
                                mRecyclerView,
                                dragGroupAndTouchOffset.first,
                                dragGroupAndTouchOffset.second);
                        Log.e("hi~","maybegetdraggroupcreator");
                    }

                }
            };
        }

        @Override
        public boolean onBlockClicked(final PendingDrag pendingDrag) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Identify and process the clicked BlockGroup.
                    getWorkspaceBlockGroupForTouch(pendingDrag);
                    Log.e("onBlock","clicked");
                }
            });
            return true;
        }
    }

}
