package com.learn4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.blockly.android.BlockClickDialog;
import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.control.ConnectionManager;
import com.google.blockly.android.control.FlyoutController;
import com.google.blockly.android.ui.BlockGroup;
import com.google.blockly.android.ui.BlockRecyclerViewHelper;
import com.google.blockly.android.ui.BlockTouchHandler;
import com.google.blockly.android.ui.BlockView;
import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.android.ui.Dragger;
import com.google.blockly.android.ui.FlyoutCallback;
import com.google.blockly.android.ui.ItemSpacingDecoration;
import com.google.blockly.android.ui.PendingDrag;
import com.google.blockly.android.ui.ViewPoint;
import com.google.blockly.android.ui.WorkspaceHelper;
import com.google.blockly.model.Block;
import com.google.blockly.model.BlocklyCategory;
import com.google.blockly.model.DefaultBlocks;
import com.google.blockly.model.VariableCustomCategory;
import com.google.blockly.model.WorkspacePoint;
import com.learn4.data.dto.CodeBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTestActivity extends BlocklySectionsActivity {
    private static final String TAG = "BlockTest";
    CategoryView mCategoryView;
    FrameLayout frameLayout;
    RecyclerView block_view_list_test;
    View blockly_workspace;
    BlocklyController controller;
    private WorkspaceHelper mHelper;

    public Dragger mDragger;
    FlyoutController mFlyoutController;

    private ConnectionManager mConnectionManager;
    private BlockTouchHandler mTouchHandler;
    Adapter mAdapter;

    String [] usage_block = {"inout_analog_write","inout_analog_read","base_delay"};
    List<String> block_list = new ArrayList<>(Arrays.asList(usage_block));

//    private FlyoutCallback mCallback;

    private final WorkspacePoint mTempWorkspacePoint = new WorkspacePoint();

    private final Handler mHandler = new Handler();
    static final List<String> TURTLE_BLOCK_DEFINITIONS = Arrays.asList(
            DefaultBlocks.COLOR_BLOCKS_PATH,
            DefaultBlocks.LOGIC_BLOCKS_PATH,
            DefaultBlocks.LOOP_BLOCKS_PATH,
            DefaultBlocks.MATH_BLOCKS_PATH,
            DefaultBlocks.TEXT_BLOCKS_PATH,
            DefaultBlocks.VARIABLE_BLOCKS_PATH,
            "turtle/turtle_blocks.json"
    );

    private static final int MAX_LEVELS = 2;
    private static final String[] LEVEL_TOOLBOX = new String[MAX_LEVELS];

    static {
        LEVEL_TOOLBOX[0] = "arduino_basic.xml";
        LEVEL_TOOLBOX[1] = "arduino_advanced.xml";
    }

    ConstraintLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_test);
        background = findViewById(R.id.background);
//        blockly_workspace = findViewById(R.id.blockly_workspace);
        frameLayout = findViewById(R.id.block_view_test);
        block_view_list_test = findViewById(R.id.block_view_list_test);


        mCategoryView = mBlocklyActivityHelper.getmCategoryView();

        controller = getController();
        mHelper = controller.getWorkspaceHelper();


//        mCallback = controller.call
        mConnectionManager = controller.getWorkspace().getConnectionManager();

        mAdapter = new Adapter();
        block_view_list_test.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        block_view_list_test.setAdapter(mAdapter);
        block_view_list_test.addItemDecoration(new ItemSpacingDecoration(mAdapter));
//
//
//        mTouchHandler = controller.getDragger()
//                .buildImmediateDragBlockTouchHandler(new DragHandler());


        mDragger = new Dragger(controller);
        mTouchHandler = mDragger.buildSloppyBlockTouchHandler(mWorkspaceDragHandler);

        mFlyoutController = new FlyoutController(controller);
        int count = 0;


        List<BlocklyCategory.CategoryItem> blocks = mCategoryView.mRootCategory.getSubcategories().get(0).getItems();
        Log.e("test", "size : " + blocks.size()+"");

        for (BlocklyCategory.CategoryItem item : blocks) {

            if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BLOCK) {
                BlocklyCategory.BlockItem blockItem = (BlocklyCategory.BlockItem) item;
                Block block = blockItem.getBlock();
                block.setEditable(true);
                Log.e("block name check",block.getType());
//                Log.e("block comment check",block.getComment());
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

                    //이부분 수정
                    if(bg.getParent() != null) {
                        ((ViewGroup)bg.getParent()).removeView(bg); // <- fix
                    }





                if (!block.getType().equals("turtle_setup_loop")) {
                   frameLayout.addView(bg, layoutParams);
                    frameLayout.setForegroundGravity(0);
                    break;
                }
            }
        }

        Log.e("------------------------","----------------------------");

//        List<BlocklyCategory.CategoryItem> blocks2 = mCategoryView.mRootCategory.getSubcategories().get(1).getItems();
////        Log.e("test", "size : " + blocks.size()+"");
//
//        for (BlocklyCategory.CategoryItem item : blocks2) {
//
//            if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BLOCK) {
//                BlocklyCategory.BlockItem blockItem = (BlocklyCategory.BlockItem) item;
//                Block block = blockItem.getBlock();
//                block.setEditable(true);
//                Log.e("block name check",block.getType());
////                Log.e("block comment check",block.getComment());
//                BlockGroup bg = mHelper.getParentBlockGroup(block);
//
////                bg = mHelper.getView(block).getParentBlockGroup();
//
//                if (bg == null) {
//                    bg = mHelper.getBlockViewFactory().buildBlockGroupTree(
//                            block, mConnectionManager, mTouchHandler);
//                } else {
//                    bg.setTouchHandler(mTouchHandler);
//                }
//
//
//
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                        (int) (ViewGroup.LayoutParams.WRAP_CONTENT ),
//                        (int) (ViewGroup.LayoutParams.WRAP_CONTENT ));
//
//                layoutParams.rightMargin = 0;
//
////                //이부분 수정
//                if(bg.getParent() != null) {
//                    ((ViewGroup)bg.getParent()).removeView(bg); // <- fix
//                }
//
//
//
//                if (block.getType().equals("inout_digital_write")) {
//                    Log.e("hi","block in");
//                    block_view_list_test.addView(bg, layoutParams);
//                    block_view_list_test.setForegroundGravity(0);
//                    break;
//                }
//            }
//        }
    }

    @NonNull
    @Override
    protected ListAdapter onCreateSectionsListAdapter() {
        return null;
    }

    @Override
    protected boolean onSectionChanged(int oldSection, int newSection) {
        return false;
    }

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        return "turtle/" + LEVEL_TOOLBOX[getCurrentSectionIndex()];
    }

    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        return TURTLE_BLOCK_DEFINITIONS;
    }

    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        return null;
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        return null;
    }

    public final Dragger.DragHandler mWorkspaceDragHandler = new Dragger.DragHandler() {
        @Override
        public Runnable maybeGetDragGroupCreator(final PendingDrag pendingDrag) {

            BlockView touchedView = pendingDrag.getTouchedBlockView();;

            // If a shadow or other undraggable block is touched, and it is attached to a draggable
            // parent block, drag that block instead.
            final BlockView activeTouchedView = mHelper.getNearestActiveView(touchedView);
            if (activeTouchedView == null) {
                Log.e("error!", "User touched a stack of blocks that may not be dragged");
                return null;
            }



            return new Runnable() {
                @Override
                public void run() {
                    // extractBlockAsRoot() fires MoveEvent immediately.
                    controller.extractBlockAsRoot(activeTouchedView.getBlock());

                    // Since this block was already on the workspace, the block's position should
                    // have been assigned correctly during the most recent layout pass.
                    BlockGroup bg = mHelper.getRootBlockGroup(activeTouchedView);
                    bg.bringToFront();

                    // Measure and layout the block group to get the correct touch offset.
                    bg.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    bg.layout(0, 0, bg.getMeasuredWidth(), bg.getMeasuredHeight());

                    ViewPoint touchOffset = new ViewPoint(
                            (int) (activeTouchedView.getX()
                                    + pendingDrag.getTouchDownViewOffsetX()),
                            (int) (activeTouchedView.getY()
                                    + pendingDrag.getTouchDownViewOffsetY()));
                    pendingDrag.startDrag(background, bg, touchOffset);
                }
            };
        }

        @Override
        public boolean onBlockClicked(PendingDrag pendingDrag) {
            // TODO(#35): Mark block as focused / selected.

            Log.e("block clicked","in !!!");
            return false;
        }
    };


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
                                background,
                                dragGroupAndTouchOffset.first,
                                dragGroupAndTouchOffset.second);
                        //Log.e("hi~","maybegetdraggroupcreator");
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



    private Pair<BlockGroup, ViewPoint> getWorkspaceBlockGroupForTouch(PendingDrag pendingDrag) {
        Log.e("getworkspace","blockgroupfortouch");
        BlockView touchedBlockView = pendingDrag.getTouchedBlockView();
        Block rootBlock = touchedBlockView.getBlock().getRootBlock();
        Log.e("rootBlock",rootBlock.getType());
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


        ;
//        int itemIndex = getCurrentCategory().indexOf(rootBlock); // Should never be -1
        int itemIndex =0;

        BlockGroup dragGroup = mCallback.getDraggableBlockGroup(itemIndex, rootBlock,
                mTempWorkspacePoint);
        return Pair.create(dragGroup, touchOffset);
    }

    protected FlyoutCallback mCallback = new FlyoutCallback() {
        @Override
        public void onButtonClicked(View v, String action, BlocklyCategory category) {
            if (action == VariableCustomCategory.ACTION_CREATE_VARIABLE && controller != null) {

                controller.requestAddVariable("item");
            }
        }

        @Override
        public BlockGroup getDraggableBlockGroup(int index, Block blockInList,
                                                 WorkspacePoint initialBlockPosition) {
            Log.e("in block","drag");
            Block copy = blockInList.deepCopy();
            copy.setPosition(initialBlockPosition.x, initialBlockPosition.y);
            BlockGroup copyView = controller.addRootBlock(copy);
//            mTrashCategory.removeItem(index);
//            closeTrash();
            return copyView;
        }
    };


    public class Adapter extends RecyclerView.Adapter<BlockViewHolder> {

        @Override
        public int getItemCount() {

            return mCategoryView == null ? 0 :  mCategoryView.mRootCategory.getSubcategories().get(1).getItems().size();
        }

        @Override
        public BlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new BlockViewHolder(getApplicationContext());
        }


        @Override
        public int getItemViewType(int position) {
            if (mCategoryView == null) {
                return -1;
            }
            Log.e("position",position+"");

//
//            mRecyclerView.setLayoutParams(marginLayoutParams);
            return mCategoryView.mRootCategory.getSubcategories().get(1).getItems().get(position).getType();
        }

        @Override
        public void onBindViewHolder(BlockViewHolder holder, int position) {

            List<BlocklyCategory.CategoryItem> items =
                  mCategoryView.mRootCategory.getSubcategories().get(1).getItems();

//            holder.mContainer.setBackgroundColor(Color.parseColor("#B5B2FF"));

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


//                bg.setBackgroundColor(Color.parseColor("#B2CCFF"));
                //이부분 수정
                if(bg.getParent() != null) {
                    ((ViewGroup)bg.getParent()).removeView(bg); // <- fix
                }


                if (block_list.contains(block.getType())) {
                    holder.mContainer.addView(bg, layoutParams);
                    holder.mContainer.setForegroundGravity(0);
                }



//                    ViewGroup.LayoutParams params = holder.mContainer.getLayoutParams();
//                    holder.mContainer.setTouchDelegate(new TouchDelegate(new Rect(holder.mContainer.getWidth(),0,holder.mContainer.getWidth(),holder.mContainer.getHeight()), bg));
//                holder.mContainer.setScaleX(0.8f);
//                holder.mContainer.setScaleY(0.8f);


//                holder.mContainer.setPivotX(0);
//                holder.mContainer.setPivotY(0);


                holder.bg = bg;



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
}