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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        itemsAdapter = new ItemsAdapter(getActivity(), itemList, itemList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_product);
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

        itemList.clear();

        ApiService apiService = ApiUtils.getApiService();

        apiService.GetProducts().enqueue(
                new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                        if (response.body().size() != 0) {
                            for (int i = 0; i < response.body().size(); i++) {
                                Item item = response.body().get(i);
                                item.setImage(ApiUtils.BASE_URL + "/product-image/" + item.getId());
                                itemList.add(item);
                            }
                            itemsAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    public void filterItem(String query) {
        itemsAdapter.getFilter().filter(query);
    }
}
