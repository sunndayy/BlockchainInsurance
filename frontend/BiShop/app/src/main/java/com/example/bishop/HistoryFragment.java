package com.example.bishop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoriesAdapter historiesAdapter;
    private List<History> historyList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNotifi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        tvNotifi = (TextView) view.findViewById(R.id.tv_his_notifi);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_history);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.user != null) {
                    prepareHistory();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        historyList = new ArrayList<>();

        historiesAdapter = new HistoriesAdapter(getContext(), historyList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historiesAdapter);

        if (Common.user != null) {
            prepareHistory();
        } else {
            tvNotifi.setText("Xin vui lòng đăng nhập");
        }

        return view;
    }

    private void prepareHistory() {

        historyList.clear();

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
                                tvNotifi.setText("Không có đơn hàng nào bạn đã đặt");
                            } else {
                                tvNotifi.setText("Danh sách đơn hàng của bạn");
                            }
                        } else {
                            tvNotifi.setText("Không có đơn hàng nào bạn đã đặt");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<History>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        tvNotifi.setText("Không có đơn hàng nào bạn đã đặt");
                    }
                });
    }
}
