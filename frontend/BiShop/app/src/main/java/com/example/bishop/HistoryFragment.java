package com.example.bishop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
    private TextView tvNotifi, tvSearch;
    private LinearLayout viewNotifi;
    private Button btnSignIn, btnSignUp;
    private LinearLayout viewSearch;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        tvNotifi = (TextView) view.findViewById(R.id.tv_his_notifi);

        tvNotifi.setVisibility(View.GONE);

        viewSearch = (LinearLayout) view.findViewById(R.id.view_search_his);

        viewNotifi = (LinearLayout) view.findViewById(R.id.view_his_notifi);

        btnSignIn = (Button) view.findViewById(R.id.btn_his_sign_in);

        btnSignUp = (Button) view.findViewById(R.id.btn_his_sign_up);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                getActivity().finish();
            }
        });

        if (Common.user != null) {
            viewNotifi.setVisibility(View.GONE);
            viewSearch.setVisibility(View.VISIBLE);
        } else {
            viewNotifi.setVisibility(View.VISIBLE);
            viewSearch.setVisibility(View.GONE);
        }

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

        tvSearch = (TextView) view.findViewById(R.id.tv_search_his);

        searchView = (SearchView) view.findViewById(R.id.search_view_his);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSearch.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                historiesAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                historiesAdapter.getFilter().filter(newText);
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        historyList = new ArrayList<>();

        historiesAdapter = new HistoriesAdapter(getContext(), historyList, historyList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historiesAdapter);

        if (Common.user != null) {
            prepareHistory();
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
                                tvNotifi.setVisibility(View.VISIBLE);
                                tvNotifi.setText("Không có đơn hàng nào bạn đã đặt");
                            } else {
                                tvNotifi.setVisibility(View.GONE);
                            }
                        } else {
                            tvNotifi.setVisibility(View.VISIBLE);
                            tvNotifi.setText("Không có đơn hàng nào bạn đã đặt");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<History>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        tvNotifi.setVisibility(View.VISIBLE);
                        tvNotifi.setText("Không có đơn hàng nào bạn đã đặt");
                    }
                });
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
