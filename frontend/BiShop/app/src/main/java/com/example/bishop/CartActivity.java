package com.example.bishop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
                startActivity(new Intent(CartActivity.this, PaymentActivity.class));
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
