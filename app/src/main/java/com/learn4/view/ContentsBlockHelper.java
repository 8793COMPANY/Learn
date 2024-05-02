package com.learn4.view;

import android.annotation.SuppressLint;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.control.ConnectionManager;
import com.google.blockly.android.ui.BlockGroup;
import com.google.blockly.android.ui.BlockTouchHandler;
import com.google.blockly.android.ui.BlockView;
import com.google.blockly.android.ui.Dragger;
import com.google.blockly.android.ui.FlyoutCallback;
import com.google.blockly.android.ui.ItemSpacingDecoration;
import com.google.blockly.android.ui.PendingDrag;
import com.google.blockly.android.ui.ViewPoint;
import com.google.blockly.android.ui.WorkspaceHelper;
import com.google.blockly.model.Block;
import com.google.blockly.model.WorkspacePoint;
import com.google.blockly.utils.TestApplication;

import java.util.List;

public class ContentsBlockHelper {

    private final Handler mHandler = new Handler();
    private final WorkspacePoint mTempWorkspacePoint = new WorkspacePoint();

    private final RecyclerView mRecyclerView;
    private final Context mContext;
    private final Adapter mAdapter;
    private final LinearLayoutManager mLayoutManager;

    private FlyoutCallback mCallback;
    private WorkspaceHelper mHelper;
    private ConnectionManager mConnectionManager;
    private BlockTouchHandler mTouchHandler, mTouchHandler2;

    BlocklyController blocklyController;

    Boolean blockDrag = true;

    List<Block> blockGroups;

    public ContentsBlockHelper(RecyclerView recyclerView, Context context, BlocklyController blocklyController, List<Block> blockGroups) {
        mRecyclerView = recyclerView;
        mContext = context;
        mAdapter = new Adapter();
        // 상하좌우 스크롤 모두 가능하게
        mLayoutManager = new LinearLayoutManager(context);
        this.blocklyController = blocklyController;
        this.blockGroups = blockGroups;

        Log.e("testtest", "!!!! "+blockGroups);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ItemSpacingDecoration(mAdapter));
    }

    public void init(BlocklyController controller, FlyoutCallback callback) {
        Log.e("testtest", "!!"+controller);
        Log.e("testtest", "!!"+callback);

        TestApplication.flyoutCallback = callback;

        mCallback = callback;
        mHelper = controller.getWorkspaceHelper();
        mConnectionManager = controller.getWorkspace().getConnectionManager();

        mTouchHandler = controller.getDragger()
                .buildImmediateDragBlockTouchHandler(new ContentsBlockHelper.DragHandler());

        mTouchHandler2 = controller.getDragger()
                .buildImmediateDragBlockTouchHandler2(new ContentsBlockHelper.DragHandler2());

        //블록 복사기능
        controller.setOnBlockClickListener(new BlocklyController.OnBlockClickListener() {
            @Override
            public void onBlockClick(PendingDrag pendingDrag) {
                Log.e("block click","in 1 ");

                BlockView touchedBlockView = pendingDrag.getTouchedBlockView();
                Block rootBlock = touchedBlockView.getBlock().getRootBlock();

                // root block이 setup loop 블록과 연결되지 않았을때만 복사 사용 가능
                if (!rootBlock.getType().trim().equals("turtle_setup_loop")){
                    Log.e("block click","in");
                    copyBlock(pendingDrag);
                }
            }
        });
    }

    public class Adapter extends RecyclerView.Adapter<ContentsBlockHelper.BlockViewHolder> {

        @Override
        public int getItemCount() {
            return blockGroups.size();
        }

        @NonNull
        @Override
        public ContentsBlockHelper.BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContentsBlockHelper.BlockViewHolder(mContext);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(ContentsBlockHelper.BlockViewHolder holder, int position) {

            Log.e("testtest", "blockGroups position : " + position);
            Block block = blockGroups.get(position);
            block.setEditable(true);
            BlockGroup bg = mHelper.getParentBlockGroup(block);

            if (bg == null) {
                bg = mHelper.getBlockViewFactory().buildBlockGroupTree(
                        block, mConnectionManager, mTouchHandler);
            } else {
                bg.setTouchHandler(mTouchHandler);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    (int) (ViewGroup.LayoutParams.WRAP_CONTENT ),
                    (int) (ViewGroup.LayoutParams.WRAP_CONTENT ));

            layoutParams.rightMargin = 0;

            if (block.getType().equals("logic_compare") || block.getType().equals("logic_operation")){
                Log.e("here!!!!!","ok");
                bg.setScaleX(1f);
                bg.setScaleY(1f);
                bg.setPivotX(0);
                bg.setPivotY(0);
                layoutParams.bottomMargin = 40;

            }else{
                bg.setScaleX(0.8f);
                bg.setScaleY(0.8f);
                bg.setPivotX(0);
                bg.setPivotY(0);
            }

            if (block.getType().equals("get_weather")){
                bg.setScaleX(0.8f);
                bg.setScaleY(0.8f);
                bg.setPivotX(0);
                bg.setPivotY(0);
            }

            if(bg.getParent() != null) {
                ((ViewGroup)bg.getParent()).removeView(bg); // <- fix
            }

            if (!block.getType().equals("turtle_setup_loop")) {
                holder.mContainer.addView(bg, layoutParams);
                holder.mContainer.setForegroundGravity(0);
            }

            holder.bg = bg;

            bg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("action 확인!", "!touch! !touch!");

                    return false;
                }
            });
        }

        @Override
        public void onViewRecycled(ContentsBlockHelper.BlockViewHolder holder) {
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



    @SuppressLint("ClickableViewAccessibility")
    private class BlockViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout mContainer;
        BlockGroup bg = null;  // Root of the currently attach block views.

        BlockViewHolder(Context context) {
            super(new FrameLayout(context));
            mContainer = (FrameLayout) itemView;

            Log.e("action 확인!", itemView+"!");
            Log.e("action 확인!", mContainer.findFocus()+"!");

            mContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("action 확인!", "!touch!");

                    return false;
                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.e("action 확인!", "!! touch !!");

                    return false;
                }
            });

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("action 확인!", "!click!");
                }
            });

            // 드래그 리스너 설정
//            View.OnDragListener mDragEventListener = new View.OnDragListener() {
//
//                @Override
//                public boolean onDrag(View v, DragEvent event) {
//
//                    Log.e("action 확인!", "!!" + v.toString());
//                    Log.e("action 확인!", "!!" + v.getId());
//
//                    switch (event.getAction()) {
//                        case DragEvent.ACTION_DRAG_STARTED:
//                            Log.e("action 확인!", "ACTION_DRAG_STARTED~");
//
//                            event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
//                        case DragEvent.ACTION_DRAG_ENTERED:
//                            Log.e("action 확인!", "ACTION_DRAG_ENTERED~");
//
//                            return true;
//                        case DragEvent.ACTION_DRAG_LOCATION:
//                            Log.e("action 확인!", "ACTION_DRAG_LOCATION~");
//
//                            return true;
//                        case DragEvent.ACTION_DRAG_EXITED:
//                            Log.e("action 확인!", "ACTION_DRAG_EXITED~");
//
//                            return true;
//                        case DragEvent.ACTION_DROP:
//                            Log.e("action 확인!", "ACTION_DROP~");
//
//                            if (v == mRecyclerView) { // recyclerview
//                                Log.e("action 확인!", "recyclerview~");
//
//                                View rView = (View) event.getLocalState();
//
//                                ((ViewGroup) rView.getParent()).removeView(rView);
//
//                                return true;
//                            } else { // else
//                                return false;
//                            }
//
////                            if (v == context.g) { // workspace
////                                Log.e("action 확인!", "workspace");
////
////                                View wView = (View) event.getLocalState();
////
////                                return true;
////                            } else
//                        case DragEvent.ACTION_DRAG_ENDED:
//                            Log.e("action 확인!", "ACTION_DRAG_ENDED~");
//
//                            if (event.getResult()) {
//                                Log.e("action 확인!", "Success~");
//                            } else {
//                                Log.e("action 확인!", "Failed~");
//                            }
//
//                            return true;
//                        default:
//                            break;
//                    }
//
//                    return false;
//                }
//            };
//
//            // 드래그리스너 이벤트 연결
//            mRecyclerView.setOnDragListener(mDragEventListener);
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
                    Log.e("getworkspace","blockgroupfortouch!!!");
//                    Log.e("getworkspace","blockgroupfortouch!!! + " + pendingDrag.isClick());
                    //Log.e("getworkspace","blockgroupfortouch!!! ++ " + pendingDrag.isDragging());
                    //Log.e("getworkspace","blockgroupfortouch!!! +++ " + pendingDrag.isAlive());

                    // 블록 리스트에서 블록을 드래그하거나 클릭하면 워크스페이스에서 블록이 생성되는 곳
                    // 블록을 드래그했을 때만 블록 생성이 가능하도록 해야 함(블록 클릭시 생성되는 것은 막기)

//                    if (blockDrag) {
//                        Pair<BlockGroup, ViewPoint> dragGroupAndTouchOffset =

//                                getWorkspaceBlockGroupForTouch(pendingDrag);
//
//                        if (dragGroupAndTouchOffset != null) {
//                            pendingDrag.startDrag(
//                                    mRecyclerView,
//                                    dragGroupAndTouchOffset.first,
//                                    dragGroupAndTouchOffset.second);
//                        }
//                    } else {
//                        blockDrag = true;
//                    }

                    Log.e("getworkspace~", "maybeGetDragGroupCreator~~");

                    Pair<BlockGroup, ViewPoint> dragGroupAndTouchOffset =
                            getWorkspaceBlockGroupForTouch(pendingDrag);

                    if (dragGroupAndTouchOffset != null) {
                        pendingDrag.startDrag(
                                mRecyclerView,
                                dragGroupAndTouchOffset.first,
                                dragGroupAndTouchOffset.second);
                    }

//                    if (!pendingDrag.isClick()) {
//                        Log.e("getworkspace","~block drag!!!");
//
//                        Pair<BlockGroup, ViewPoint> dragGroupAndTouchOffset =
//                                getWorkspaceBlockGroupForTouch(pendingDrag);
//
//                        if (dragGroupAndTouchOffset != null) {
//                            pendingDrag.startDrag(
//                                    mRecyclerView,
//                                    dragGroupAndTouchOffset.first,
//                                    dragGroupAndTouchOffset.second);
//                        }
//                    } else {
//                        Log.e("getworkspace","~block click!!!");
//                    }
                }
            };
        }

        @Override
        public boolean onBlockClicked(final PendingDrag pendingDrag) {
            Log.e("getworkspace","blockgroupfortouch~~");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Identify and process the clicked BlockGroup.
                    // getWorkspaceBlockGroupForTouch(pendingDrag);
                    Log.e("onBlock","clicked");

                    blockDrag = false;
                }
            });
            return true;
        }
    }

    /** {@link Dragger.DragHandler} implementation for BlockListViews. */
    private class DragHandler2 implements Dragger.DragHandler {

        @Override
        public Runnable maybeGetDragGroupCreator(final PendingDrag pendingDrag) {
            return new Runnable() {
                @Override
                public void run() {
                    Log.e("getworkspace","blockgroupfortouch!!!");
                    //Log.e("getworkspace","blockgroupfortouch!!! + " + pendingDrag.isClick());

                    Log.e("getworkspace~", "maybeGetDragGroupCreator~~");

                    Pair<BlockGroup, ViewPoint> dragGroupAndTouchOffset =
                            getWorkspaceBlockGroupForTouch(pendingDrag);

                    if (dragGroupAndTouchOffset != null) {
                        pendingDrag.startDrag(
                                mRecyclerView,
                                dragGroupAndTouchOffset.first,
                                dragGroupAndTouchOffset.second);
                    }

//                    if (!pendingDrag.isClick()) {
//                        Log.e("getworkspace","~block drag!!!");
//
//                        Pair<BlockGroup, ViewPoint> dragGroupAndTouchOffset =
//                                getWorkspaceBlockGroupForTouch(pendingDrag);
//
//                        if (dragGroupAndTouchOffset != null) {
//                            pendingDrag.startDrag(
//                                    mRecyclerView,
//                                    dragGroupAndTouchOffset.first,
//                                    dragGroupAndTouchOffset.second);
//                        }
//                    } else {
//                        Log.e("getworkspace","~block click!!!");
//                    }
                }
            };
        }

        @Override
        public boolean onBlockClicked(final PendingDrag pendingDrag) {
            Log.e("getworkspace","blockgroupfortouch~~");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Identify and process the clicked BlockGroup.
                    // getWorkspaceBlockGroupForTouch(pendingDrag);
                    Log.e("onBlock","clicked");

                    blockDrag = false;
                }
            });
            return true;
        }
    }


    //블록 복사
    @NonNull
    private Pair<BlockGroup, ViewPoint> copyBlock(PendingDrag pendingDrag) {
        Log.e("getworkspace","blockgroupfortouch!");
        BlockView touchedBlockView = pendingDrag.getTouchedBlockView();
        Block rootBlock = touchedBlockView.getBlock().getRootBlock();
        Log.e("rootBlock",rootBlock.getType());
        BlockView rootTouchedBlockView = mHelper.getView(rootBlock);
        BlockGroup rootTouchedGroup = rootTouchedBlockView.getParentBlockGroup();

        // Calculate the offset from rootTouchedGroup to touchedBlockView in view
        // pixels. We are assuming the only transforms between BlockViews are the
        // child offsets.
        View view = (View) touchedBlockView;

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

        //복사한 블록 workspace에 띄워줄 위치 조정은 여기서 하시면 돼요
        int wsOffsetX = mHelper.virtualViewToWorkspaceUnits(offsetX);
        int wsOffsetY = mHelper.virtualViewToWorkspaceUnits(offsetY)-100;

        // Offset the workspace coord by the BlockGroup's touch offset.
        mTempWorkspacePoint.setFrom(
                pendingDrag.getTouchDownWorkspaceCoordinates());
        mTempWorkspacePoint.offset(-wsOffsetX, -wsOffsetY);

        int itemIndex =0;

        BlockGroup dragGroup = mCallback.getDraggableBlockGroup(itemIndex, rootBlock,
                mTempWorkspacePoint);
        return Pair.create(dragGroup, touchOffset);
    }

    // 블록 삭제 기능
    void removeBlock(PendingDrag pendingDrag) {
        Log.e("getworkspace", "blockgroupfortouch");
        BlockView touchedBlockView = pendingDrag.getTouchedBlockView();
        Block rootBlock = touchedBlockView.getBlock().getRootBlock();
        Log.e("rootBlock", rootBlock.getType());
        BlockView rootTouchedBlockView = mHelper.getView(rootBlock);
        BlockGroup rootTouchedGroup = rootTouchedBlockView.getParentBlockGroup();
    }

    @NonNull
    private Pair<BlockGroup, ViewPoint> getWorkspaceBlockGroupForTouch(PendingDrag pendingDrag) {

        Log.e("getworkspace","blockgroupfortouch!!");

        BlockView touchedBlockView = pendingDrag.getTouchedBlockView();
        Block rootBlock = touchedBlockView.getBlock().getRootBlock();
        Log.e("rootBlock",rootBlock.getType());
        BlockView rootTouchedBlockView = mHelper.getView(rootBlock);
        Log.e("rootBlock", rootTouchedBlockView+"");
        BlockGroup rootTouchedGroup = rootTouchedBlockView.getParentBlockGroup();

        Log.e("action 확인","getWorkspaceBlockGroupForTouch");
        Log.e("action 확인",rootBlock+"");

        // Calculate the offset from rootTouchedGroup to touchedBlockView in view
        // pixels. We are assuming the only transforms between BlockViews are the
        // child offsets.
        View view = (View) touchedBlockView;

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

        Log.e("action 확인",offsetX+"");
        Log.e("action 확인",offsetY+"");

        Log.e("action 확인",wsOffsetX+"");
        Log.e("action 확인",wsOffsetY+"");

        int itemIndex =0;

        // getDraggableBlockGroup : Workspace 및 WorkspaceView 에 블록을 추가하는 것을 포함하여 드래그 가능한 BlockGroup 의 선택을 처리
        BlockGroup dragGroup = mCallback.getDraggableBlockGroup(itemIndex, rootBlock,
                mTempWorkspacePoint);

        Log.e("getworkspace~", "~~" + dragGroup);
        Log.e("getworkspace~", "~~" + touchOffset);

        TestApplication.block_check = dragGroup.getFirstBlock();
        TestApplication.workspacePoint = mTempWorkspacePoint;

        pendingDrag.getDragTouchOffset();

        int[] location = new int[2];
        mRecyclerView.getLocationOnScreen(location);

        int[] new_location = new int[2];

        Log.e("getworkspace~", "check" + location[0]);
        Log.e("getworkspace~", "check" + location[1]);

        if (pendingDrag.getTouchDownViewOffsetX() >= location[0]) {
            if (pendingDrag.getTouchDownViewOffsetX() <= location[0] + mRecyclerView.getWidth()) {
                if (pendingDrag.getTouchDownViewOffsetY() >= location[1]) {
                    if (pendingDrag.getTouchDownViewOffsetY() <= location[1] + mRecyclerView.getHeight()) {
                        Log.e("getworkspace~", "true");
                    } else {
                        Log.e("getworkspace~", "false1");
                    }
                } else {
                    Log.e("getworkspace~", "false2");
                }
            } else {
                Log.e("getworkspace~", "false3");
            }
        } else {
            Log.e("getworkspace~", "false4");
        }

        return Pair.create(dragGroup, touchOffset);
    }

}