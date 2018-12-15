package com.example.heyoukang.a2048game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyoukang.a2048game.view.CellView;
import com.example.heyoukang.a2048game.view.GameView;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    private GameView mGridView;
    private GameController mGameController;
    private TextView mTextScore;
    private Button mBtRestart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initGrideView();
        initOtherView();
    }

    private void initOtherView() {
        mTextScore = findViewById(R.id.tv_score);
        mBtRestart = findViewById(R.id.bt_restart);
        mBtRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameController.restart();
                mTextScore.setText("0");
            }
        });
    }

    private void initGrideView() {
        mGridView = findViewById(R.id.gv);
        mGameController = new GameController(mGridView, this);
        mGameController.setGameListener(new GameController.GameListener() {
            @Override
            public void scoreChange(View v, int newScore) {
                mTextScore.setText(String.valueOf(newScore));
            }

            @Override
            public void gameOver() {
                Toast.makeText(GameActivity.this, "Over", Toast.LENGTH_SHORT).show();
            }
        });
        mGameController.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameController.end();
    }
}
