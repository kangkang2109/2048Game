package com.example.heyoukang.a2048game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.heyoukang.a2048game.R;


public class CellView extends View{

    private String mText;
    private int mBackgroundRes;
    private Paint mPaint;
    Paint.FontMetricsInt mFontMetricsInt;

    public CellView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mText = "0";
        mBackgroundRes = R.drawable.cell_rectangle_normal;
        mPaint = new Paint();
        mPaint.setTextSize(80);
        //mPaint.setFakeBoldText(true);
        mFontMetricsInt = mPaint.getFontMetricsInt();
        setBackgroundResource(mBackgroundRes);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
        updateRes();
    }

    private void updateRes() {
        int resId = -1;
        switch (mText) {
            case "2":
                resId = R.drawable.cell_rectangle_2;
                break;
            case "4":
                resId = R.drawable.cell_rectangle_4;
                break;
            case "8":
                resId = R.drawable.cell_rectangle_8;
                break;
            case "16":
                resId = R.drawable.cell_rectangle_16;
                break;
            case "32":
                resId = R.drawable.cell_rectangle_32;
                break;
            case "64":
                resId = R.drawable.cell_rectangle_64;
                break;
            case "128":
                resId = R.drawable.cell_rectangle_128;
                break;
            case "256":
                resId = R.drawable.cell_rectangle_256;
                break;
            case "1024":
                resId = R.drawable.cell_rectangle_1024;
                break;
            case "2048":
                resId = R.drawable.cell_rectangle_2048;
                break;
            default:
                resId = R.drawable.cell_rectangle_normal;
        }
        setBackgroundResource(resId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mText.equals("0")) {
            return;
        }
        int i = Integer.parseInt(mText);
        if (i > 4) {
            mPaint.setColor(Color.WHITE);
        } else {
            mPaint.setColor(Color.BLACK);
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int x = (int) ((width - mPaint.measureText(mText)) / 2);
        int y = (mFontMetricsInt.bottom - mFontMetricsInt.top) / 2 - mFontMetricsInt.bottom + height / 2;
        canvas.drawText(mText, x, y, mPaint);
    }
}
