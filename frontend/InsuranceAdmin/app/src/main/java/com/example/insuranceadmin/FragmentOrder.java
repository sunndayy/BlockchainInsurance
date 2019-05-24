package com.example.insuranceadmin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOrder extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orders;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FragmentOrder() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_order);
        orders = new ArrayList<>();

        ordersAdapter = new OrdersAdapter(getActivity(), orders);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ordersAdapter);

        prepareAlbums();


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_order);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareAlbums();
            }
        });

        return rootView;
    }


    private void prepareAlbums() {

        orders.clear();

        ApiService apiService = ApiUtils.getApiService();
        apiService.GetContracts(Common.AccessToken)
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        for (int i = 0; i <response.body().size(); i++) {
                            Order order = response.body().get(i);
                            orders.add(order);
                        }
                        ordersAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
