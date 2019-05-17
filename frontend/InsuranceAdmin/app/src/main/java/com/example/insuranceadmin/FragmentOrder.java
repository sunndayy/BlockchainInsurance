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

import java.util.ArrayList;
import java.util.List;

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
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }


    private void prepareAlbums() {

        orders.clear();

        Order a = new Order("0001", "Bao hiem A", "12.000");
        orders.add(a);
        a = new Order("0002", "Bao hiem B", "12.000");
        orders.add(a);
        a = new Order("0003", "Bao hiem C", "12.000");
        orders.add(a);
        a = new Order("0004", "Bao hiem D", "12.000");
        orders.add(a);
        a = new Order("0005", "Bao hiem E", "12.000");
        orders.add(a);

        ordersAdapter.notifyDataSetChanged();
    }
}
