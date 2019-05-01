package com.example.bishop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoriesAdapter historiesAdapter;
    private List<History> historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_history);
        historyList = new ArrayList<>();

        historiesAdapter = new HistoriesAdapter(this, historyList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historiesAdapter);

        prepareHistory();
    }

    private void prepareHistory() {
        historyList.add(new History("1", "20/12/2018", 23000000));
        historyList.add(new History("2", "21/12/2018", 24000000));
        historyList.add(new History("3", "22/12/2018", 25000000));
        historyList.add(new History("4", "23/12/2018", 26000000));

        historiesAdapter.notifyDataSetChanged();
    }
}
