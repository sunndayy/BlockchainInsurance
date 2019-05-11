package com.example.insuranceadmin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        return rootView;
    }


    private void prepareAlbums() {
        Order a = new Order("0001", "Bao hiem A", "12.000");
        orders.add(a);
        a = new Order("0001", "Bao hiem A", "12.000");
        orders.add(a);
        a = new Order("0001", "Bao hiem A", "12.000");
        orders.add(a);
        a = new Order("0001", "Bao hiem A", "12.000");
        orders.add(a);
        a = new Order("0001", "Bao hiem A", "12.000");
        orders.add(a);

        ordersAdapter.notifyDataSetChanged();
    }
}
