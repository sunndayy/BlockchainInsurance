package com.example.bishop;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
    private LinearLayout viewNotifi, viewSearch;
    private Button btnSignIn, btnSignUp;
    private SearchView searchView;
    private TextView tvSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        tvNotifi = (TextView) view.findViewById(R.id.tv_fav_notifi);

        tvNotifi.setVisibility(View.GONE);

        viewNotifi = (LinearLayout) view.findViewById(R.id.view_fav_notifi);

        viewSearch = (LinearLayout) view.findViewById(R.id.view_search_fav);

        btnSignIn = (Button) view.findViewById(R.id.btn_fav_sign_in);

        btnSignUp = (Button) view.findViewById(R.id.btn_fav_sign_up);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                getActivity().finish();
            }
        });

        if (Common.user != null) {
            viewNotifi.setVisibility(View.GONE);
            viewSearch.setVisibility(View.VISIBLE);
        } else {
            viewNotifi.setVisibility(View.VISIBLE);
            viewSearch.setVisibility(View.GONE);
        }

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

        tvSearch = (TextView) view.findViewById(R.id.tv_search_fav);

        searchView = (SearchView) view.findViewById(R.id.search_view_fav);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSearch.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                itemsFavoriteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsFavoriteAdapter.getFilter().filter(newText);
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav);
        itemList = new ArrayList<>();

        itemsFavoriteAdapter = new ItemsFavoriteAdapter(getContext(), itemList, itemList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsFavoriteAdapter);

        if (Common.user != null) {
            prepareFavorite();
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
                                tvNotifi.setVisibility(View.VISIBLE);
                                tvNotifi.setText("Không có sản phẩm yêu thích nào");
                            } else {
                                tvNotifi.setVisibility(View.GONE);
                            }
                        } else {
                            tvNotifi.setVisibility(View.VISIBLE);
                            tvNotifi.setText("Không có sản phẩm yêu thích nào");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        tvNotifi.setVisibility(View.VISIBLE);
                        tvNotifi.setText("Không có sản phẩm yêu thích nào");
                    }
                });
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
