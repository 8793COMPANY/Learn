/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.blockly.android.ui.fieldview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.google.blockly.android.R;
import com.google.blockly.model.Field;
import com.google.blockly.model.FieldColor;
import com.google.blockly.model.FieldJikco;

/**
 * Renders a color field and picker as part of a BlockView.
 */

//여기 참고해서 커스텀 해보자
public class BasicFieldJikcoView extends FrameLayout implements FieldView {
    protected static final int DEFAULT_MIN_WIDTH_DP = 40;
    protected static final int DEFAULT_MIN_HEIGHT_DP = 28;  // Base on styled label text height?

    @VisibleForTesting
    static final int ALPHA_OPAQUE = 0xFF000000;

    private final boolean[] mCheckArray = new boolean[]{
            // 0
            false, false, false, false, false, false, false,
            // 1
            false, false, false, false, false, false, false,
    };

    private Field.Observer mFieldObserver = new Field.Observer() {
        @Override
        public void onValueChanged(Field field, String oldValue, String newValue) {
            Log.e("heelo","in");
//            setColor(mColorField.getColor());
            setImage(drawBitmap(mCheckArray));
        }
    };

    protected FieldJikco mColorField = null;

    protected BasicFieldJikcoView.AutoPositionPopupWindow mColorPopupWindow;
    protected BasicFieldJikcoView.ColorPaletteView mColorPaletteView;

    public BasicFieldJikcoView(Context context) {
        super(context);
        initPostConstructor();
    }

    public BasicFieldJikcoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPostConstructor();
    }

    public BasicFieldJikcoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPostConstructor();
    }

    private void initPostConstructor() {
        float density = getContext().getResources().getDisplayMetrics().density;
        setMinimumWidth((int) (DEFAULT_MIN_WIDTH_DP * density));
        setMinimumHeight((int) (DEFAULT_MIN_HEIGHT_DP * density));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPickerPopupWindow();
            }
        });
    }

    public Bitmap drawBitmap(boolean[] array){
        Bitmap bm = Bitmap.createBitmap(170,100,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);


        Paint paint = new Paint();
        paint.setStrokeWidth(15f);


        setPaintColor(paint,array[0]);
        c.drawPoint(30, 40, paint);
        setPaintColor(paint,array[1]);
        c.drawPoint(50, 40, paint);
        setPaintColor(paint,array[2]);
        c.drawPoint(70, 40, paint);
        setPaintColor(paint,array[3]);
        c.drawPoint(100, 40, paint);
        setPaintColor(paint,array[4]);
        c.drawPoint(120, 40, paint);
        setPaintColor(paint,array[5]);
        c.drawPoint(140, 40, paint);


        setPaintColor(paint,array[6]);
        c.drawPoint(30, 60, paint);
        setPaintColor(paint,array[7]);
        c.drawPoint(50, 60, paint);
        setPaintColor(paint,array[8]);
        c.drawPoint(70, 60, paint);
        setPaintColor(paint,array[9]);
        c.drawPoint(100, 60, paint);
        setPaintColor(paint,array[10]);
        c.drawPoint(120, 60, paint);
        setPaintColor(paint,array[11]);
        c.drawPoint(140, 60, paint);

//        c.drawPoint();

        return bm;
    }

    public void setPaintColor(Paint paint, boolean check){
        if (check){
            paint.setColor(Color.WHITE);
        }else{
            paint.setColor(Color.parseColor("#f2bc66"));
        }

    }

    @Override
    public void setField(Field field) {
        FieldJikco colorField = (FieldJikco) field;

        if (mColorField == colorField) {
            return;
        }

        if (mColorField != null) {
            mColorField.unregisterObserver(mFieldObserver);
        }



        mColorField = colorField;

        if (mColorField != null) {
//            setColor(mColorField.getColor());
            String[] parts = mColorField.getSerializedValue().split(",");

            boolean[] array = new boolean[parts.length];
            for (int i = 0; i < parts.length; i++) {
                mCheckArray[i] = (parts[i].equals("1") ? true:false);
                Log.e("field check "+i,array[i]+"");
            }

            setImage(drawBitmap(mCheckArray));

            mColorField.registerObserver(mFieldObserver);
        }
    }

    @Override
    public Field getField() {
        return mColorField;
    }

    @Override
    public void unlinkField() {
        setField(null);
    }

    /**
     * Set the selected color represented by this view.  The alpha values will be ignored for
     * rendering.
     *
     * @param color The color in {@code int} format.
     */
    public void setColor(int color) {
        setBackgroundColor(ALPHA_OPAQUE | color);
    }

    public void setImage(Bitmap bitmap){
        setBackground(new BitmapDrawable(bitmap));
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(onMeasureDimension(getSuggestedMinimumWidth(), widthMeasureSpec),
                onMeasureDimension(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    /**
     * Calculates the measured dimension in a single direction (width or height).
     *
     * @param min The minimum size.
     * @param measureSpec The {@link View.MeasureSpec} provided by the parent.
     * @return The calculated size.
     */
    protected int onMeasureDimension(int min, int measureSpec) {
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                return MeasureSpec.getSize(measureSpec);
            case MeasureSpec.AT_MOST:
                return Math.min(min, MeasureSpec.getSize(measureSpec));
            case MeasureSpec.UNSPECIFIED:
            default:
                return min;
        }
    }

    /**
     * Open a {@link PopupWindow} showing a color selection palette.
     */
    protected void openColorPickerPopupWindow() {
        if (mColorPaletteView == null) {
            mColorPaletteView = new BasicFieldJikcoView.ColorPaletteView(this);
        }

        if (mColorPopupWindow == null) {
            mColorPopupWindow = new BasicFieldJikcoView.AutoPositionPopupWindow(mColorPaletteView);
        }

        mColorPopupWindow.show(this);
    }

    /**
     * Popup window that adjusts positioning to the size of the wrapped view.
     */
    protected class AutoPositionPopupWindow extends PopupWindow {
        private final View mWrapView;

        /**
         * Construct popup window wrapping an existing {@link View} object.
         *
         * @param wrapView The view shown inside the popup window.
         */
        public AutoPositionPopupWindow(View wrapView) {
            super(wrapView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mWrapView = wrapView;

            // This is necessary because PopupWindow responds to touch events only with
            // background != null.
            setBackgroundDrawable(new ColorDrawable());
            setOutsideTouchable(true);
        }

        /**
         * Show popup window next to an anchor view.
         *
         * @param anchorView The view that "anchors" the popup window.
         */
        public void show(View anchorView) {
            // Get size of the wrapped view. Since it may not have been measured yet, allow fallback
            // to minimum size.
            int width = mWrapView.getMeasuredWidth();
            if (width == 0) {
                // Note - getMinimumWidth/Height require API 16 and above.
                width = mWrapView.getMinimumWidth();
            }

            int height = mWrapView.getMeasuredHeight();
            if (height == 0) {
                height = mWrapView.getMinimumHeight();
            }

            // Set size of popup window to match wrapped content; this will allow automatic
            // positioning to fit on screen.
            setWidth(width);
            setHeight(height);

            showAsDropDown(anchorView, 0, 0);
        }
    }

    /**
     * View for a color palette that matches Web Blockly's.
     */
    protected class ColorPaletteView extends View {
        private static final int PALETTE_COLUMNS = 6;
        private static final int PALETTE_ROWS = 2;

        private static final int PALETTE_FIELD_WIDTH = 85;
        private static final int PALETTE_FIELD_HEIGHT = 85;
        private static final float GRID_STROKE_WIDTH = 10;

        private final BasicFieldJikcoView mParent;
        private final Paint mAreaPaint = new Paint();
        private final Paint mGridPaint = new Paint();
        private boolean first_check = true;


        // From https://github.com/google/closure-library/blob/master/closure/goog/ui/colorpicker.js
        // TODO(#70): move this table into resources.
        private final int[] mColorArray = new int[]{
                // grays
                0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66,
                // reds
                0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66, 0xfff2bc66,
        };



        private final Rect[] mrectArray = new Rect[]{
                // 0
                null, null, null, null, null, null, null,
                // 1
                null, null, null, null, null, null, null,
        };


        ColorPaletteView(BasicFieldJikcoView parent) {
            super(parent.getContext());
            mParent = parent;



            mGridPaint.setColor(getResources().getColor(R.color.baeulrae_main_color));
            mGridPaint.setStrokeWidth(GRID_STROKE_WIDTH);
            mGridPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        public int getMinimumWidth() {
            return PALETTE_FIELD_WIDTH * PALETTE_COLUMNS +30;
        }

        @Override
        public int getMinimumHeight() {
            return PALETTE_FIELD_HEIGHT * PALETTE_ROWS;
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getMinimumWidth(), getMinimumHeight());
        }

        @Override
        public void onDraw(Canvas canvas) {

            drawPalette(canvas);
            drawGrid(canvas);

        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                int i = Math.min(PALETTE_COLUMNS - 1,
                        (int) motionEvent.getX() / PALETTE_FIELD_WIDTH);
                int j = Math.min(PALETTE_ROWS - 1,
                        (int) motionEvent.getY() / PALETTE_FIELD_HEIGHT);

                int index = i + j * PALETTE_COLUMNS;
                Log.e("onTouchEvent",index+"");

                mCheckArray[index] = !mCheckArray[index];

                Log.e("onTouchEvent checked "+ index,mCheckArray[index]+"");

                //블록 색상 변경
                mParent.setBackgroundColor(mColorArray[index]);
                mParent.mColorField.setColor(mColorArray[index]);



//                mParent.setImage();

                mParent.mColorField.setColor(getCheckList());


                //ondraw() 호출
                invalidate();


//                mParent.mColorP.opupWindow.dismiss();
                return true;
            }

            return false;
        }

        private String getCheckList(){
            String check = "";
            for (int i=0; i < mCheckArray.length - 1; i++){
                check += (mCheckArray[i] ? 1:0)+",";
            }
            check += (mCheckArray[mCheckArray.length-1] ? 1:0);
            return check;
        }

        private void drawPalette(Canvas canvas) {
            Log.e("onTouch","drawPalette");

            int paletteIndex = 0;
            for (int j = 0; j < PALETTE_ROWS; ++j) {
                int y = j * PALETTE_FIELD_HEIGHT;
                for (int i = 0; i < PALETTE_COLUMNS; ++i, ++paletteIndex) {
                    int x = i * PALETTE_FIELD_WIDTH;

                    if (mCheckArray[paletteIndex]){
                        mAreaPaint.setColor(Color.WHITE);
                    }else{
                        mAreaPaint.setColor(mColorArray[paletteIndex]);
                    }

                    Log.e("onTouch palette index",paletteIndex+"");
//                    canvas.drawRoundRect();
//                    if (i == 2 || i ==3)
                    if (i>=3) {
                        Rect rect = new Rect();
                        rect.left = x+25;
                        rect.right = x+25 + PALETTE_FIELD_WIDTH;
                        rect.top = y;
                        rect.bottom = y+PALETTE_FIELD_HEIGHT;

                        canvas.drawRect(rect, mAreaPaint);

                        mrectArray[paletteIndex] = rect;
//                        canvas.drawRect(
//                                x+25, y, x+25 + PALETTE_FIELD_WIDTH, y + PALETTE_FIELD_HEIGHT, mAreaPaint);
                    }else{
                        Rect rect = new Rect();
                        rect.left = x;
                        rect.right = x + PALETTE_FIELD_WIDTH;
                        rect.top = y;
                        rect.bottom = y+PALETTE_FIELD_HEIGHT;

                        mrectArray[paletteIndex] = rect;

                        canvas.drawRect(rect, mAreaPaint);
//                        canvas.drawRect(
//                                x, y, x + PALETTE_FIELD_WIDTH, y + PALETTE_FIELD_HEIGHT, mAreaPaint);
                    }



                }
            }

        }

        private void drawGrid(Canvas canvas) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            canvas.drawRect(0, 0, width - 1, height - 1, mGridPaint);
            for (int j = 0; j < PALETTE_ROWS; ++j) {
                int y = j * PALETTE_FIELD_HEIGHT;
                canvas.drawLine(0, y, width +2, y, mGridPaint);
            }
            for (int i = 0; i < PALETTE_COLUMNS; ++i) {
                int x = i * PALETTE_FIELD_WIDTH;
                Log.e("x",x+", "+i);
                if (i == 3){
                    mGridPaint.setStrokeWidth(38);

//                    canvas.drawLine(x, 0, x, height- 1, mGridPaint);
                }else{
                    mGridPaint.setStrokeWidth(GRID_STROKE_WIDTH);
//                    canvas.drawLine(x, 0, x, height - 1, mGridPaint);
                }

                if (i==3){
                    x += 15;
                    canvas.drawLine(x, 0, x, height- 1, mGridPaint);
                }else if (i > 3){
                    canvas.drawLine(x +30, 0, x+30, height- 1, mGridPaint);
                } else{
                    canvas.drawLine(x, 0, x, height- 1, mGridPaint);
                }

            }
        }
    }





    @VisibleForTesting
    PopupWindow getColorPopupWindow() {
        return mColorPopupWindow;
    }
}
