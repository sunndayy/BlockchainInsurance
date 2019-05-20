package com.example.bishop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private ItemsCartAdapter itemsCartAdapter;
    private List<Item> itemList;
    private Button btnOrder;
    private TextView tvNotifi, tvSumPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnOrder = (Button) findViewById(R.id.btn_order_cart);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.popup_confirm, null);


                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);

                Button btnYes = (Button) popupView.findViewById(R.id.btn_popup_confirm_yes);
                Button btnNo = (Button) popupView.findViewById(R.id.btn_popup_confirm_no);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                        List<Order> list = new ArrayList<>();

                        for (int i =0; i< Common.cart.size(); i++) {
                            Order order = new Order(Common.cart.get(i).getId(), Common.cart.get(i).getPrice());
                            list.add(order);
                        }

                        Orders orders = new Orders();
                        orders.setItems(list);

                        ApiService apiService = ApiUtils.getApiService();

                        User user = Common.user;

                        apiService.CreateOrder(Common.user.getToken(), orders).enqueue(
                                new Callback<Orders>() {
                                    @Override
                                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                                        if (response.body().getError() == null) {
                                            Toast.makeText(CartActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                            Common.cart.clear();
                                            finish();
                                        }
                                        else  {
                                            Toast.makeText(CartActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Orders> call, Throwable t) {
                                        Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

            }
        });

        tvNotifi = (TextView) findViewById(R.id.tv_cart_notifi);
        tvSumPrice = (TextView) findViewById(R.id.tv_cart_sum_price);


        if (Common.cart.size() == 0) {
            btnOrder.setVisibility(View.GONE);
            tvSumPrice.setVisibility(View.GONE);
        } else {
            tvNotifi.setVisibility(View.GONE);
            Long sumPrice = Long.valueOf(0);
            for (int i = 0; i < Common.cart.size(); i++) {
                sumPrice += Common.cart.get(i).getPrice();
            }

            tvSumPrice.setText("Tổng giá trị: " + Common.beautifyPrice(sumPrice));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_cart);
        itemList = new ArrayList<>();
        itemsCartAdapter = new ItemsCartAdapter(this, itemList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsCartAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        prepareCartView();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ItemsCartAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = itemList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Item deletedItem = itemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            itemsCartAdapter.removeItem(viewHolder.getAdapterPosition());

            Common.cart.remove(position);
            if (Common.cart.size() == 0) {
                btnOrder.setVisibility(View.GONE);
                tvSumPrice.setVisibility(View.GONE);
                tvNotifi.setVisibility(View.VISIBLE);
            } else {
                Long sumPrice = Long.valueOf(0);
                for (int i = 0; i < Common.cart.size(); i++) {
                    sumPrice += Common.cart.get(i).getPrice();
                }

                tvSumPrice.setText("Tổng giá trị: " + Common.beautifyPrice(sumPrice));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void prepareCartView() {
        for (int i = 0; i < Common.cart.size(); i++) {
            itemList.add(Common.cart.get(i));
        }

        itemsCartAdapter.notifyDataSetChanged();
    }
}
