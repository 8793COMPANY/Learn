package com.google.blockly.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.model.Block;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class TestApplication {

    private static TestApplication instance = null;

    public static TestApplication getInstance(Context context) {
        if (instance == null) {
            instance = new TestApplication();

            getStandardSize((Activity) context);
        }
        return instance;
    }

    public static String chapter_check = "none";

    public static String getChapter_check() {
        return chapter_check;
    }

    public static String workspace_name = "none";

    public static void setWorkspace_name(String name) {
        workspace_name = name;
    }

    public static String getWorkspace_name() {
        return workspace_name;
    }

    public static int standardSize_X, standardSize_Y;
    public static float density;

    public static String drag_state = "none";

//    public static Block basicBlock;
//    public static BlocklyController blocklyController;

    public static Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public static void getStandardSize(Activity activity) {
        Point ScreenSize = getScreenSize(activity);
        density  = activity.getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }

//    public static void setBlock() {
//        String str = "<xml xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
//                "  <block type=\"turtle_setup_loop\" x=\"10.0\" y=\"10.0\" />\n" +
//                "</xml>";
//        InputStream is = new ByteArrayInputStream(str.getBytes());
//
//        List<Block> newBlocks = null;
//
//        try {
//            newBlocks = BlocklyXmlHelper.loadFromXml(is, blocklyController.getBlockFactory());
//
//            for (Block item : newBlocks) {
//                basicBlock = item;
//            }
//        } catch (BlockLoadingException e1) {
//            Log.e("TAG", "Failed to load default arduino workspace", e1);
//        }
//    }
}
