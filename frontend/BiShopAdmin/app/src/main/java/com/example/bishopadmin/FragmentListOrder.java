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

import java.util.ArrayList;
import java.util.List;

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

        ordersAdapter = new OrdersAdapter(getActivity(), orderList);
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

        orderList.clear();

        Order a = new Order("AB", "45.000.000d", "20/12/2018");
        orderList.add(a);

        a = new Order("AB", "45.000.000d", "20/12/2018");
        orderList.add(a);

        a = new Order("AB", "45.000.000d", "20/12/2018");
        orderList.add(a);

        a = new Order("AB", "45.000.000d", "20/12/2018");
        orderList.add(a);

        a = new Order("AB", "45.000.000d", "20/12/2018");
        orderList.add(a);

        a = new Order("AB", "45.000.000d", "20/12/2018");
        orderList.add(a);

        ordersAdapter.notifyDataSetChanged();
    }
}
