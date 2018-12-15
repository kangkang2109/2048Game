package com.example.heyoukang.a2048game.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

import com.example.heyoukang.a2048game.R;

public class GameView extends GridView {

    private static final int DEFAULT_DELAY_LIMITE = 50;
    private static final int DEFAULT_ROW_NUM = 4;
    private static final int DEFAULT_COL_NUM = 4;

    private int mDelayLimite;
    private int mRowNum;
    private int mColNum;
    private int mDownX;
    private int mDownY;
    boolean mFlag = false;
    private GameViewSlipListener mGameViewSlipListener;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GameView);
        mDelayLimite = ta.getInteger(R.styleable.GameView_delay_limite, DEFAULT_DELAY_LIMITE);
        mRowNum = ta.getInteger(R.styleable.GameView_row_num, DEFAULT_ROW_NUM);
        mColNum = ta.getInteger(R.styleable.GameView_col_num, DEFAULT_COL_NUM);
        ta.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mFlag) {
                    break;
                }
                int delayY = y - mDownY;
                int delayX = x - mDownX;
                if (Math.abs(delayX) > Math.abs(delayY) && Math.abs(delayX) > mDelayLimite) {
                    if (mGameViewSlipListener != null) {
                        if (delayX < 0) {
                            mGameViewSlipListener.slipLeft();
                        } else {
                            mGameViewSlipListener.slipRight();
                        }
                        mFlag = false;
                    }
                    break;
                }

                if (Math.abs(delayY) > Math.abs(delayX) && Math.abs(delayY) > mDelayLimite) {
                    if (mGameViewSlipListener != null) {
                        if (delayY < 0) {
                            mGameViewSlipListener.slipTop();
                        } else {
                            mGameViewSlipListener.slipBottom();
                        }
                        mFlag = false;
                    }
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                mFlag = false;
                break;
        }
        return true;
    }

    public int getDelayLimite() {
        return mDelayLimite;
    }

    public void setDelayLimite(int delayLimite) {
        this.mDelayLimite = delayLimite;
    }

    public GameViewSlipListener getGameListener() {
        return mGameViewSlipListener;
    }

    public void setGameViewSlipListener(GameViewSlipListener gameViewSlipListener) {
        this.mGameViewSlipListener = gameViewSlipListener;
    }

    public int getRowNum() {
        return mRowNum;
    }

    public void setRowNum(int rowNum) {
        this.mRowNum = rowNum;
    }

    public int getColNum() {
        return mColNum;
    }

    public void setColNum(int colNum) {
        this.mColNum = colNum;
    }

    public interface GameViewSlipListener {
        void slipLeft();
        void slipRight();
        void slipTop();
        void slipBottom();
    }
}
