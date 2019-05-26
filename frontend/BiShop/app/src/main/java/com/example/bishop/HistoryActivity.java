package com.example.bishop;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoriesAdapter historiesAdapter;
    private List<History> historyList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNotifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvNotifi = (TextView) findViewById(R.id.tv_his_notifi);
        tvNotifi.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_history);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareHistory();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_history);
        historyList = new ArrayList<>();

        historiesAdapter = new HistoriesAdapter(this, historyList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historiesAdapter);

        prepareHistory();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void prepareHistory() {

        historyList.clear();

        tvNotifi.setVisibility(View.GONE);

        ApiService apiService = ApiUtils.getApiService();

        apiService.GetOrders(Common.user.getToken(), Common.user.getUsername())
                .enqueue(new Callback<List<History>>() {
                    @Override
                    public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().size(); i++) {
                                History history = response.body().get(i);
                                historyList.add(history);
                            }
                            historiesAdapter.notifyDataSetChanged();
                            if (historyList.size() == 0) {
                                tvNotifi.setVisibility(View.VISIBLE);
                            }
                            else {
                                tvNotifi.setVisibility(View.GONE);
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<History>> call, Throwable t) {
                        Toast.makeText(HistoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        tvNotifi.setVisibility(View.VISIBLE);
                    }
                });
    }
}