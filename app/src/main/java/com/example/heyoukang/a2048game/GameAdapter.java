package com.example.heyoukang.a2048game;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.heyoukang.a2048game.view.CellView;

import java.util.List;

public class GameAdapter extends BaseAdapter {

    private List<String> values;
    private Context mContext;

    public GameAdapter(List<String> value, Context context) {
        this.values = value;
        mContext = context;
    }

    public void updateValues(List<String> values) {
        this.values = values;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        return values.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.game_item, viewGroup, false);
        }
        CellView t = v.findViewById(R.id.text);
        t.setText(values.get(i));
        t.invalidate();
        return v;
    }
}
