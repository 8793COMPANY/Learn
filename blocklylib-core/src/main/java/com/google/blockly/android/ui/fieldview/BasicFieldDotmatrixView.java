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
import android.view.Gravity;
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
import com.google.blockly.model.FieldDotmatrix;

public class BasicFieldDotmatrixView extends FrameLayout implements FieldView {

    protected static final int DEFAULT_MIN_WIDTH_DP = 50;
    protected static final int DEFAULT_MIN_HEIGHT_DP = 50;

    @VisibleForTesting
    static final int ALPHA_OPAQUE = 0xFF000000;

    private final boolean[] mCheckArray = new boolean[]{
            // 0      1      2      3      4      5      6      7
            false, false, false, false, false, false, false, false,
            // 1
            false, false, false, false, false, false, false, false,
            // 2
            false, false, false, false, false, false, false, false,
            // 3
            false, false, false, false, false, false, false, false,
            // 4
            false, false, false, false, false, false, false, false,
            // 5
            false, false, false, false, false, false, false, false,
            // 6
            false, false, false, false, false, false, false, false,
            // 7
            false, false, false, false, false, false, false, false

//            // 0      1      2      3      4      5      6      7      8
//            false, false, false, false, false, false, false, false, false,
//            // 1
//            false, false, false, false, false, false, false, false, false,
//            // 2
//            false, false, false, false, false, false, false, false, false,
//            // 3
//            false, false, false, false, false, false, false, false, false,
//            // 4
//            false, false, false, false, false, false, false, false, false,
//            // 5
//            false, false, false, false, false, false, false, false, false,
//            // 6
//            false, false, false, false, false, false, false, false, false,
//            // 7
//            false, false, false, false, false, false, false, false, false
    };

    private Field.Observer mFieldObserver = new Field.Observer() {
        @Override
        public void onValueChanged(Field field, String oldValue, String newValue) {
            Log.e("hello", "in");

            setImage(drawBitmap(mCheckArray));
        }
    };

    protected FieldDotmatrix mColorField = null;

    protected BasicFieldDotmatrixView.AutoPositionPopupWindow mColorPopupWindow;
    protected BasicFieldDotmatrixView.ColorPaletteView mColorPaletteView;

    public BasicFieldDotmatrixView(@NonNull Context context) {
        super(context);
        initPostConstructor();
    }

    public BasicFieldDotmatrixView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPostConstructor();
    }

    public BasicFieldDotmatrixView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public Bitmap drawBitmap(boolean[] array) {
        Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);

        Paint paint = new Paint();
        paint.setStrokeWidth(10f);

        int mul_num = 10;
        int check_num = 0;
        int total_num;

        for (int i = 0; i < array.length; i++) {
            setPaintColor(paint, array[i]);

            if (i < 8) {
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 15, paint);
            } else if (i < 16) {
                if (i == 8) check_num = 0;
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 25, paint);
            } else if (i < 24) {
                if (i == 16) check_num = 0;
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 35, paint);
            } else if (i < 32) {
                if (i == 24) check_num = 0;
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 45, paint);
            } else if (i < 40) {
                if (i == 32) check_num = 0;
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 55, paint);
            } else if (i < 48) {
                if (i == 40) check_num = 0;
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 65, paint);
            } else if (i < 56) {
                if (i == 48) check_num = 0;
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 75, paint);
            } else if (i < 64) {
                if (i == 56) check_num = 0;
                check_num++;
                total_num = mul_num * check_num;
                c.drawPoint((total_num + 5), 85, paint);
            }
        }

        return bm;
    }

    public void setPaintColor(Paint paint, boolean check) {
        if (check) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.BLACK);
        }
    }

    @Override
    public Field getField() {
        return mColorField;
    }

    @Override
    public void setField(Field field) {
        FieldDotmatrix colorField = (FieldDotmatrix) field;

        if (mColorField == colorField) {
            return;
        }

        if (mColorField != null) {
            mColorField.unregisterObserver(mFieldObserver);
        }

        mColorField = colorField;

        if (mColorField != null) {
            String[] parts = mColorField.getSerializedValue().split(",");
            boolean[] array = new boolean[parts.length];

            for (int i = 0; i < parts.length; i++) {
                // 여기가 제대로 작성되어야 비트맵이 그려짐
                mCheckArray[i] = (parts[i].equals("1") ? true : false);

                Log.e("field check " + i, array[i] + "");
            }

            setImage(drawBitmap(mCheckArray));

            mColorField.registerObserver(mFieldObserver);
        }
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

    public void setImage(Bitmap bitmap) {
        setBackground(new BitmapDrawable(bitmap));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(onMeasureDimension(getSuggestedMinimumWidth(), widthMeasureSpec),
                onMeasureDimension(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    /**
     * Calculates the measured dimension in a single direction (width or height).
     *
     * @param min         The minimum size.
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
            mColorPaletteView = new BasicFieldDotmatrixView.ColorPaletteView(this);
        }

        if (mColorPopupWindow == null) {
            mColorPopupWindow = new BasicFieldDotmatrixView.AutoPositionPopupWindow(mColorPaletteView);
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

            // yoff : 팝업창 y축 위치 지정
            showAsDropDown(anchorView, 0, 0);
        }
    }

    /**
     * View for a color palette that matches Web Blockly's.
     */
    protected class ColorPaletteView extends View {
        private static final int PALETTE_COLUMNS = 8;
        private static final int PALETTE_ROWS = 8;

        private static final int PALETTE_FIELD_WIDTH = 40;
        private static final int PALETTE_FIELD_HEIGHT = 40;
        private static final float GRID_STROKE_WIDTH = 5;

        private final BasicFieldDotmatrixView mParent;
        private final Paint mAreaPaint = new Paint();
        private final Paint mGridPaint = new Paint();
        private boolean first_check = true;

        // From https://github.com/google/closure-library/blob/master/closure/goog/ui/colorpicker.js
        // TODO(#70): move this table into resources.
        private final int[] mColorArray = new int[]{
                // 0            1           2           3           4           5           6           7
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
                // 1
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
                // 2
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
                // 3
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
                // 4
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
                // 5
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
                // 6
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
                // 7
                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666

//                // 0            1           2           3           4           5           6           7           8
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
//                // 1
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
//                // 2
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
//                // 3
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
//                // 4
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
//                // 5
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
//                // 6
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666,
//                // 7
//                0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666, 0xff666666
        };

        private final Rect[] mrectArray = new Rect[]{
                // 0    1     2     3    4     5     6     7
                null, null, null, null, null, null, null, null,
                // 1
                null, null, null, null, null, null, null, null,
                // 2
                null, null, null, null, null, null, null, null,
                // 3
                null, null, null, null, null, null, null, null,
                // 4
                null, null, null, null, null, null, null, null,
                // 5
                null, null, null, null, null, null, null, null,
                // 6
                null, null, null, null, null, null, null, null,
                // 7
                null, null, null, null, null, null, null, null

//                // 0    1     2     3    4     5     6     7     8
//                null, null, null, null, null, null, null, null, null,
//                // 1
//                null, null, null, null, null, null, null, null, null,
//                // 2
//                null, null, null, null, null, null, null, null, null,
//                // 3
//                null, null, null, null, null, null, null, null, null,
//                // 4
//                null, null, null, null, null, null, null, null, null,
//                // 5
//                null, null, null, null, null, null, null, null, null,
//                // 6
//                null, null, null, null, null, null, null, null, null,
//                // 7
//                null, null, null, null, null, null, null, null, null
        };

        ColorPaletteView(BasicFieldDotmatrixView parent) {
            super(parent.getContext());
            mParent = parent;

            mGridPaint.setColor(getResources().getColor(R.color.orange_ffc865));
            mGridPaint.setStrokeWidth(GRID_STROKE_WIDTH);
            mGridPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        public int getMinimumWidth() {
            return PALETTE_FIELD_WIDTH * PALETTE_COLUMNS;
        }
        // public int getMinimumWidth() {
        //            return PALETTE_FIELD_WIDTH * PALETTE_COLUMNS +30;
        //        }

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
                Log.e("onTouchEvent", index + "");

                mCheckArray[index] = !mCheckArray[index];
                Log.e("onTouchEvent checked " + index, mCheckArray[index] + "");

                //블록 색상 변경
                mParent.setBackgroundColor(mColorArray[index]);
                mParent.mColorField.setColor(mColorArray[index]);

                mParent.mColorField.setColor(getCheckList());

                //ondraw() 호출
                invalidate();

                return true;
            }
            return false;
        }

        private String getCheckList() {
            String check = "";

            for (int i = 0; i < mCheckArray.length - 1; i++) {
                check += (mCheckArray[i] ? 1 : 0) + ",";
            }

            check += (mCheckArray[mCheckArray.length - 1] ? 1 : 0);

            return check;
        }

        private void drawPalette(Canvas canvas) {
            Log.e("onTouch", "drawPalette");

            int paletteIndex = 0;

            for (int j = 0; j < PALETTE_ROWS; ++j) {
                int y = j * PALETTE_FIELD_HEIGHT;
                for (int i = 0; i < PALETTE_COLUMNS; ++i, ++paletteIndex) {
                    int x = i * PALETTE_FIELD_WIDTH;

                    if (mCheckArray[paletteIndex]) {
                        mAreaPaint.setColor(Color.RED);
                    } else {
                        mAreaPaint.setColor(mColorArray[paletteIndex]);
                    }

                    Log.e("onTouch palette index", paletteIndex + "");

                    Rect rect = new Rect();
                    rect.left = x;
                    rect.right = x + PALETTE_FIELD_WIDTH;
                    rect.top = y;
                    rect.bottom = y + PALETTE_FIELD_HEIGHT;

                    mrectArray[paletteIndex] = rect;

                    canvas.drawRect(rect, mAreaPaint);
                }
            }
        }

        private void drawGrid(Canvas canvas) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            canvas.drawRect(0, 0, width - 1, height - 1, mGridPaint);

            for (int j = 0; j < PALETTE_ROWS; ++j) {
                int y = j * PALETTE_FIELD_HEIGHT;
                canvas.drawLine(0, y, width + 2, y, mGridPaint);
            }

            for (int i = 0; i < PALETTE_COLUMNS; ++i) {
                int x = i * PALETTE_FIELD_WIDTH;
                Log.e("x", x + ", " + i);

                mGridPaint.setStrokeWidth(GRID_STROKE_WIDTH);

                canvas.drawLine(x, 0, x, height - 1, mGridPaint);
            }
        }
    }

    @VisibleForTesting
    PopupWindow getColorPopupWindow() {
        return mColorPopupWindow;
    }
}
