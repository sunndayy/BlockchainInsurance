package com.example.garaadmin;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemsAdapter itemsAdapter;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //swipe
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareAlbums();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        items = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsAdapter);

        prepareAlbums();
    }

    private void prepareAlbums() {

        items.clear();

        Item item = new Item("45123", "Phan Van Duong", (long) 0);
        items.add(item);

        item = new Item("45123", "Phan Van Duong", (long) 0);
        items.add(item);

        item = new Item("45123", "Phan Van Duong", (long) 0);
        items.add(item);

        item = new Item("45123", "Phan Van Duong", (long) 0);
        items.add(item);

        item = new Item("45123", "Phan Van Duong", (long) 0);
        items.add(item);

        item = new Item("45123", "Phan Van Duong", (long) 0);
        items.add(item);





    }
}
