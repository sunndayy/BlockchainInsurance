package com.example.bishopadmin;

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

public class FragmentListProduct extends Fragment {

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> itemList;

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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_product);
        itemList = new ArrayList<>();

        itemsAdapter = new ItemsAdapter(getActivity(), itemList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsAdapter);

        prepareAlbums();
        return rootView;
    }

    private void prepareAlbums() {
        Item a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);
        a = new Item("AB", 45000000, R.drawable.ic_add_circle_black_24dp);
        itemList.add(a);

        itemsAdapter.notifyDataSetChanged();
    }
}
