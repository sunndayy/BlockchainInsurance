package com.example.bishop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView, recyclerView1, recyclerView2, recyclerView3;
    private ItemsMainAdapter itemsMainAdapter, itemsMainAdapter1, itemsMainAdapter2, itemsMainAdapter3;
    private List<Item> itemList, itemList1, itemList2, itemList3;
    private SliderLayout sliderLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private RecyclerView recyclerViewSearch;
    private List<ItemSearchMain> itemSearchMains;
    private ItemsSearchMainAdapter itemsSearchMainAdapter;
    private TextView tvSearch;
    private FrameLayout tvBanner1, tvBanner2, tvBanner3, tvBanner4;
    private ImageView btnCart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //btn cart
        btnCart = (ImageView) view.findViewById(R.id.btn_cart_main);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.user != null) {
                    startActivity(new Intent(getActivity(), CartActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Hãy đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // tv banner
        tvBanner1 = (FrameLayout) view.findViewById(R.id.tv_banner1_main);
        tvBanner2 = (FrameLayout) view.findViewById(R.id.tv_banner2_main);
        tvBanner3 = (FrameLayout) view.findViewById(R.id.tv_banner3_main);
        tvBanner4 = (FrameLayout) view.findViewById(R.id.tv_banner4_main);

        tvBanner1.setVisibility(View.GONE);
        tvBanner2.setVisibility(View.GONE);
        tvBanner3.setVisibility(View.GONE);
        tvBanner4.setVisibility(View.GONE);

        // search view
        searchView = (SearchView) view.findViewById(R.id.search_view_main);

        tvSearch = (TextView) view.findViewById(R.id.tv_search_main);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemSearchMains.clear();

                for (Item item : Common.allItems) {
                    itemSearchMains.add(new ItemSearchMain(item.getImage(), item.getName(),
                            Common.beautifyPrice(item.getPrice())));
                }

                itemsSearchMainAdapter.notifyDataSetChanged();

                tvSearch.setVisibility(View.GONE);
                recyclerViewSearch.setVisibility(View.VISIBLE);
                sliderLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvSearch.setVisibility(View.VISIBLE);
                recyclerViewSearch.setVisibility(View.GONE);
                sliderLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                itemsSearchMainAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsSearchMainAdapter.getFilter().filter(newText);
                return false;
            }
        });

        recyclerViewSearch = (RecyclerView) view.findViewById(R.id.recycler_view_search_main);
        itemSearchMains = new ArrayList<>();

        itemsSearchMainAdapter = new ItemsSearchMainAdapter(getContext(), itemSearchMains, itemSearchMains);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSearch.setLayoutManager(layoutManager);
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSearch.setAdapter(itemsSearchMainAdapter);

        recyclerViewSearch.setVisibility(View.GONE);

        // slider
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider_main);
        prepareBanner();

        //swipe
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareAlbums();
            }
        });

        // recycle view

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_view_main_1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view_main_2);
        recyclerView3 = (RecyclerView) view.findViewById(R.id.recycler_view_main_3);


        itemList = new ArrayList<>();
        itemList1 = new ArrayList<>();
        itemList2 = new ArrayList<>();
        itemList3 = new ArrayList<>();

        itemsMainAdapter = new ItemsMainAdapter(getContext(), itemList);
        LinearLayoutManager l = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(l);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsMainAdapter);

        itemsMainAdapter1 = new ItemsMainAdapter(getContext(), itemList1);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(itemsMainAdapter1);

        itemsMainAdapter2 = new ItemsMainAdapter(getContext(), itemList2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(itemsMainAdapter2);

        itemsMainAdapter3 = new ItemsMainAdapter(getContext(), itemList3);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(itemsMainAdapter3);

        prepareAlbums();


        return view;
    }


    private void prepareAlbums() {

        swipeRefreshLayout.setRefreshing(true);

        Common.allItems.clear();
        itemList.clear();
        itemList1.clear();
        itemList2.clear();
        itemList3.clear();


        ApiService apiService = ApiUtils.getApiService();

        apiService.GetProductsBestSellers().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Item item = response.body().get(i);
                        item.setImage(ApiUtils.BASE_URL + "/product-image/" + item.getId());
                        itemList.add(item);
                    }

                    itemsMainAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                    tvBanner1.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getContext(), "Không thể lấy danh sách sản phẩm",
                            Toast.LENGTH_SHORT).show();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        apiService.GetProducts().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Item item = response.body().get(i);
                        item.setImage(ApiUtils.BASE_URL + "/product-image/" + item.getId());
                        Common.allItems.add(item);
                        if (item.getType() == 0) {
                            itemList1.add(item);
                        } else {
                            if (item.getType() == 1) {
                                itemList2.add(item);
                            } else {
                                if (item.getType() == 2) {
                                    itemList3.add(item);
                                }
                            }
                        }
                    }

                    itemsMainAdapter1.notifyDataSetChanged();
                    itemsMainAdapter2.notifyDataSetChanged();
                    itemsMainAdapter3.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);

                    tvBanner2.setVisibility(View.VISIBLE);
                    tvBanner3.setVisibility(View.VISIBLE);
                    tvBanner4.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy danh sách sản phẩm",
                            Toast.LENGTH_SHORT).show();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void prepareBanner() {
        TextSliderView textSliderView1 = new TextSliderView(getContext());
        textSliderView1.image(R.drawable.banner1);

        TextSliderView textSliderView2 = new TextSliderView(getContext());
        textSliderView2.image(R.drawable.banner2);

        TextSliderView textSliderView3 = new TextSliderView(getContext());
        textSliderView3.image(R.drawable.banner3);

        TextSliderView textSliderView4 = new TextSliderView(getContext());
        textSliderView4.image(R.drawable.banner4);

        sliderLayout.addSlider(textSliderView1);
        sliderLayout.addSlider(textSliderView2);
        sliderLayout.addSlider(textSliderView3);
        sliderLayout.addSlider(textSliderView4);

        sliderLayout.setDuration(5000);
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
