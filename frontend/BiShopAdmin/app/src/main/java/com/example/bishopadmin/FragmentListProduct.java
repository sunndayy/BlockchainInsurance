package com.example.bishopadmin;

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
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FragmentListProduct extends Fragment {

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> itemList;

    private ImageView btnAddProduct;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FragmentListProduct() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_product, container, false);

        btnAddProduct = (ImageView) rootView.findViewById(R.id.btn_add_product);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddProductActivity.class));
            }
        });


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_product);
        itemList = new ArrayList<>();

        itemsAdapter = new ItemsAdapter(getActivity(), itemList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsAdapter);

        prepareAlbums();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_product);
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

        itemList.clear();

        Item a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("1", "AB", "Xe hot 2019", 1, 45000000,
                15, "Honda", R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);

        itemsAdapter.notifyDataSetChanged();
    }
}
