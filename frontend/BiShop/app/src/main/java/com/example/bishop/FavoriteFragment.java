package com.example.bishop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemsFavoriteAdapter itemsFavoriteAdapter;
    private List<Item> itemList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNotifi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        tvNotifi = (TextView) view.findViewById(R.id.tv_fav_notifi);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_fav);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.user != null) {
                    prepareFavorite();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav);
        itemList = new ArrayList<>();

        itemsFavoriteAdapter = new ItemsFavoriteAdapter(getContext(), itemList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsFavoriteAdapter);

        if (Common.user != null) {
            prepareFavorite();
        } else {
            tvNotifi.setText("Xin vui lòng đăng nhập");
        }

        return view;
    }


    private void prepareFavorite() {

        itemList.clear();

        ApiService apiService = ApiUtils.getApiService();

        apiService.GetFavoriteProducts(Common.user.getToken())
                .enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().size(); i++) {
                                Item item = response.body().get(i);
                                item.setImage(ApiUtils.BASE_URL + "/product-image/" + item.getId());
                                itemList.add(item);
                            }
                            itemsFavoriteAdapter.notifyDataSetChanged();
                            if (itemList.size() == 0) {
                                tvNotifi.setText("Không có sản phẩm yêu thích nào");
                            } else {
                                tvNotifi.setText("Danh sách sản phẩm yêu thích");
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        tvNotifi.setText("Không có sản phẩm yêu thích nào");
                    }
                });
    }

}
