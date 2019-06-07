package com.example.bishopadmin;

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

public class FragmentListOrder extends Fragment {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FragmentListOrder() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_order, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_order);
        orderList = new ArrayList<>();

        ordersAdapter = new OrdersAdapter(getActivity(), orderList, orderList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ordersAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_order);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareAlbums();
            }
        });

        prepareAlbums();

        return rootView;
    }

    private void prepareAlbums() {

        swipeRefreshLayout.setRefreshing(true);

        orderList.clear();

        ApiService apiService = ApiUtils.getApiService();

        apiService.GetOrders(Common.AccessToken).enqueue(
                new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        for (int i = 0; i < response.body().size(); i ++) {
                            Order order = response.body().get(i);
                            orderList.add(order);
                        }
                        ordersAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void filterItem(String query) {
        ordersAdapter.getFilter().filter(query);
    }
}
