package smartdevelop.ir.eram.showcaseviewlib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Mohammad Reza Eram  on 20/01/2018.
 */

class GuideMessageView extends LinearLayout {

    // 기본 사이즈 5 >> 20 변경
    private static final int RADIUS_SIZE = 20;
    private static final int PADDING_SIZE = 10;
    private static final int BOTTOM_PADDING_SIZE = 5;
    private static final int DEFAULT_TITLE_TEXT_SIZE = 18;
    private static final int DEFAULT_CONTENT_TEXT_SIZE = 14;

    private final Paint mPaint;
    private final RectF mRect;

    private final TextView mTitleTextView;
    private final TextView mContentTextView;
    int[] location = new int[2];

    GuideMessageView(Context context) {
        super(context);

        float density = context.getResources().getDisplayMetrics().density;
        setWillNotDraw(false);
        setOrientation(VERTICAL);
//        setGravity(Gravity.CENTER);
        setGravity(Gravity.LEFT);

        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        final int padding = (int) (PADDING_SIZE * density);
        final int paddingBottom = (int) (BOTTOM_PADDING_SIZE * density);

        mTitleTextView = new TextView(context);
//        mTitleTextView.setPadding(padding, padding, padding, paddingBottom);
        mTitleTextView.setPadding((int) (padding * 2), padding*2, (int) (padding * 2), 0);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TITLE_TEXT_SIZE);
        //mTitleTextView.setTextColor(Color.BLACK);
        addView(
            mTitleTextView,
            new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        );

        mContentTextView = new TextView(context);
        mContentTextView.setTextColor(Color.BLACK);
        mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CONTENT_TEXT_SIZE);
//        mContentTextView.setPadding(padding, paddingBottom, padding, padding);
        mContentTextView.setPadding((int) (padding * 2), 0, (int) (padding * 2), (int) (padding * 2));
        mContentTextView.setGravity(Gravity.CENTER);
        addView(
            mContentTextView,
            new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        );

        //추후 다른 뷰를 추가하고 싶으면 addView를 통해 추가
    }

    public void setTitle(String title) {
        if (title == null) {
            removeView(mTitleTextView);
            return;
        }
        mTitleTextView.setText(title);
    }

    public void setContentText(String content) {
        mContentTextView.setText(content);
    }

    public void setContentSpan(Spannable content) {
        mContentTextView.setText(content);
    }

    public void setContentTypeFace(Typeface typeFace) {
        mContentTextView.setTypeface(typeFace);
    }

    public void setTitleTypeFace(Typeface typeFace) {
        mTitleTextView.setTypeface(typeFace);
    }

    public void setTitleTextSize(int size) {
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTitleTextColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public void setContentTextSize(int size) {
        mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setContentTextColor(int color) {
        mContentTextView.setTextColor(color);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.getLocationOnScreen(location);

        Log.e("testtest", location[0] + "!!!!!!!!!!!!!!!");
        Log.e("testtest", location[1] + "!!!!!!!!!!!!!!!");

        mRect.set(
            getPaddingLeft(),
            getPaddingTop(),
            getWidth() - getPaddingRight(),
            getHeight() - getPaddingBottom()
        );

        final int density = (int) getResources().getDisplayMetrics().density;
        final int radiusSize = RADIUS_SIZE * density;
        canvas.drawRoundRect(mRect, radiusSize, radiusSize, mPaint);
    }

}
