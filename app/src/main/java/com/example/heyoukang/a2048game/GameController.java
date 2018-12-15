package com.example.heyoukang.a2048game;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.heyoukang.a2048game.view.GameView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameController {

    private static final String TAG = "GameController";
    private int mRow;
    private int mCol;
    private int mSum;
    private int mValueCount;
    private int[][] mValue;
    private String[] mStrValues;
    private Random mRandom;
    private boolean mIsStart;
    private int mScore;
    private Context mContext;
    private GameView mGameView;
    private GameAdapter mGameAdapter;
    private GameListener mGameListener;
    enum Action {
        ACTION_TOP,
        ACTION_BOTTOM,
        ACTION_LEFT,
        ACTION_RIGHT
    }
    public interface GameListener {
        void scoreChange(View v, int newScore);
        void gameOver();
    }

    public GameController(GameView gameView, Context context) {
        mGameView = gameView;
        mContext = context;
        initData();
        initGameView();
    }

    private void initGameView() {
        mGameView.setNumColumns(mCol);
        mGameAdapter = new GameAdapter(getValue(), mContext);
        mGameView.setAdapter(mGameAdapter);
        mGameView.setBackgroundResource(R.drawable.background_rectangle_normal);
        mGameView.setGameViewSlipListener(new GameView.GameViewSlipListener() {
            @Override
            public void slipLeft() {
                action(Action.ACTION_LEFT);
            }

            @Override
            public void slipRight() {
                action(Action.ACTION_RIGHT);
            }

            @Override
            public void slipTop() {
                action(Action.ACTION_TOP);
            }

            @Override
            public void slipBottom() {
                action(Action.ACTION_BOTTOM);
            }
        });
    }

    private void initData() {
        this.mRow = mGameView.getRowNum();
        this.mCol = mGameView.getColNum();
        mSum = mRow * mCol;
        mValueCount = 0;
        mValue = new int[mRow][mCol];
        mStrValues = new String[mSum];
        Arrays.fill(mStrValues,"0");
        mRandom = new Random();
    }

    public void action(Action action) {
        boolean dealRow = false;
        boolean ahead = false;
        switch (action) {
            case ACTION_LEFT:
                dealRow = true;
                ahead = true;
                break;
            case ACTION_RIGHT:
                dealRow = true;
                ahead = false;
                break;
            case ACTION_BOTTOM:
                dealRow = false;
                ahead = false;
                break;
            case ACTION_TOP:
                dealRow = false;
                ahead = true;
                break;
        }
        boolean move = doActionMove(dealRow, ahead);
        if (move) {
            startCreateRandomValue();
            mGameAdapter.updateValues(getValue());
        } else {
            boolean over = isGameOver();
            if (over && mGameListener != null) {
                mGameListener.gameOver();
            }
        }
    }

    public void restart() {
        end();
        start();
    }

    public void start() {
        if (!mIsStart) {
            mIsStart = true;
            startCreateRandomValue();
            startCreateRandomValue();
            mGameAdapter.updateValues(getValue());
        }
    }

    public void end() {
        mScore = 0;
        Arrays.fill(mStrValues, "0");
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                mValue[i][j] = 0;
            }
        }
        mValueCount = 0;
        mIsStart = false;
    }

    public void setGameListener(GameListener gameListener) {
        this.mGameListener = gameListener;
    }

    public List<String> getValue() {
        return Arrays.asList(mStrValues);
    }

    private boolean doActionMove(boolean dealRow, boolean ahead) {
        int count = dealRow ? mRow : mCol;
        boolean result = false;
        for (int i = 0; i < count; i++) {
            if (dealRow) {
                result |= dealRowData(i, ahead);
            } else {
                result |= dealColData(i, ahead);
            }
        }
        return result;
    }

    private boolean dealRowData(int row, boolean ahead) {
        int pos = ahead ? 0 : (mCol - 1);
        int temp = 0;
        int i = pos;
        boolean hasZeroValue = false;
        boolean result = false;
        while (i < mCol && i >= 0) {
            int value = mValue[row][i];
            if (value == 0) {
                i += ahead ? 1 : -1;
                hasZeroValue = true;
                continue;
            }
            result |= hasZeroValue;
            setValue(row, i , 0);
            if (temp == 0) {
                temp = value;
            } else if (value == temp) {
                setValueWithScore(row, pos, temp + value, true);
                result = true;
                pos += ahead ? 1 : -1;
                temp = 0;
            } else {
                setValue(row, pos, temp);
                pos += ahead ? 1 : -1;
                temp = value;
            }
            i += ahead ? 1 : -1;
        }
        if (temp != 0) {
            setValue(row, pos, temp);
        }
        return result;
    }

    private boolean dealColData(int col, boolean ahead) {
        int temp = 0;
        int pos = ahead ? 0 : (mRow - 1);
        int i = pos;
        boolean hasZeroValue = false;
        boolean result = false;
        while (i < mRow && i >= 0) {
            int value = mValue[i][col];
            if (value == 0) {
                i += ahead ? 1 : -1;
                hasZeroValue = true;
                continue;
            }
            result |= hasZeroValue;
            setValue(i, col, 0);
            if (temp == 0) {
                temp = value;
            } else if (value == temp) {
                setValueWithScore(pos, col,temp + value, true);
                result = true;
                pos += ahead ? 1 : -1;
                temp = 0;
            } else {
                setValue(pos, col, temp);
                pos += ahead ? 1 : -1;
                temp = value;
            }
            i += ahead ? 1 : -1;
        }
        if (temp != 0) {
            setValue(pos, col, temp);
        }
        return result;
    }

    private boolean isGameOver() {
        int i = 0;
        int j = 0;
        while (i < mRow && j < mCol) {
            int value = mValue[i][j];
            if (value == 0) {
                return false;
            }
            int leftValue = (j - 1 < 0) ? -1 : mValue[i][j - 1];
            if (leftValue == value) {
                return false;
            }
            int rightValue = (j + 1 >= mCol) ? -1 : mValue[i][j + 1];
            if (rightValue == value) {
                return false;
            }
            int topValue = (i - 1 < 0) ? -1 : mValue[i - 1][j];
            if (topValue == value) {
                return false;
            }
            int bottomValue = (i + 1 >= mRow) ? -1 : mValue[i + 1][j];
            if (bottomValue == value) {
                return false;
            }
            j++;
            if (j == mCol) {
                j = 0;
                i++;
            }
        }
        return true;
    }

    private void setValueWithScore(int row, int col, int value, boolean anim) {
        setValue(row, col, value);
        mScore += value;
        int index = row * mCol + col;
        View child = mGameView.getChildAt(index);
        if (mGameListener != null) {
            mGameListener.scoreChange(child, mScore);
        }
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mGameView.getContext(), R.animator.cell_score_animtor);
        set.setTarget(child);
        set.start();
//        if (anim) {
//            child.startAnimation(AnimationUtils.loadAnimation(mGameView.getContext(), R.anim.cell_score_anim));
//        } else {
//            child.setAnimation(null);
//        }
    }

    private void setValueFromRamdom(int row, int col, int value) {
        setValue(row, col, value);
        int index = row * mCol + col;
        View child = mGameView.getChildAt(index);
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mGameView.getContext(), R.animator.cell_ramdom_animtor);
        set.setTarget(child);
        set.start();
    }

    private void setValue(int row, int col, int value) {
        int oldValue = mValue[row][col];
        mValue[row][col] = value;
        int index = row * mCol + col;
        mStrValues[index] =  String.valueOf(value);

        if (oldValue != 0 && value == 0) {
            mValueCount--;
        } else if (oldValue == 0 && value != 0) {
            mValueCount++;
        }
    }

    private void startCreateRandomValue() {
        int randomValue = (mRandom.nextInt(2) + 1) * 2;
        int leave = mSum - mValueCount;
        if (leave == 0) {
            return;
        }
        int randomIndex = mRandom.nextInt(leave);
        int count = 0;
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                if (mValue[i][j] == 0) {
                    if (count == randomIndex) {
                        setValueFromRamdom(i, j, randomValue);
                        return;
                    }
                    count++;
                }
            }
        }
    }
}
