package com.example.bishop;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvType, tvProducer, tvPrice, tvDescribe;
    private ImageView imgItem;
    private LinearLayout btnAddCart;
    private int type;
    private RecyclerView recyclerView;
    private List<Item> items;
    private ItemsDetailAdapter itemsDetailAdapter;
    private NestedScrollView scrollView;
    private LinearLayout toolBar;
    private TextView tvToolbarTitle;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        getSupportActionBar().hide();

        tvId = (TextView) findViewById(R.id.tv_detail_id);
        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvType = (TextView) findViewById(R.id.tv_detail_type);
        tvProducer = (TextView) findViewById(R.id.tv_detail_producer);
        tvPrice = (TextView) findViewById(R.id.tv_detail_price);
        tvDescribe = (TextView) findViewById(R.id.tv_detail_describe);

        imgItem = (ImageView) findViewById(R.id.ic_detail_item);

        Glide.with(this).load(getIntent().getExtras().getString("image"))
                .into(imgItem);

        btnAddCart = (LinearLayout) findViewById(R.id.btn_detail_addcart);

        toolBar = (LinearLayout) findViewById(R.id.tool_bar_detal);

        tvToolbarTitle = (TextView) findViewById(R.id.tv_detail_toolbar_title);

        scrollView = (NestedScrollView) findViewById(R.id.scroll_detail);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int height = view.getBottom() - nestedScrollView.getHeight();
                double alpha = (double) (height - i1) / (double) height;
                int intAlpha = (int) ((1 - alpha) * 255);
                toolBar.setBackgroundColor(Color.argb(intAlpha, 22, 154, 241));

                if (alpha < 0.7) {
                    tvToolbarTitle.setTextColor(Color.WHITE);
                    tvToolbarTitle.setText("Chi tiết sản phẩm");
                    btnBack.setColorFilter(Color.WHITE);
                } else {
                    tvToolbarTitle.setText("");
                    btnBack.setColorFilter(Color.GRAY);
                }
            }
        });

        btnBack = (ImageView) findViewById(R.id.btn_back_detail);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvId.setText(getIntent().getExtras().getString("id"));
        tvName.setText("Xe máy " + getIntent().getExtras().getString("producer") + " "
                + getIntent().getExtras().getString("name"));

        type = getIntent().getExtras().getInt("type");

        if (type == 0) {
            tvType.setText("Xe số");
        }

        if (type == 1) {
            tvType.setText("Xe tay ga");
        }

        if (type == 2) {
            tvType.setText("Xe côn tay");
        }

        tvProducer.setText(getIntent().getExtras().getString("producer"));
        tvPrice.setText(Common.beautifyPrice(getIntent().getExtras().getLong("price")));
        tvDescribe.setText(getIntent().getExtras().getString("describe"));

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.user == null) {
                    Toast.makeText(ItemDetailActivity.this, "Hãy đăng nhập", Toast.LENGTH_SHORT).show();
                } else {
                    Item item = new Item(
                            tvId.getText().toString(),
                            tvName.getText().toString(),
                            type,
                            tvDescribe.getText().toString(),
                            getIntent().getExtras().getLong("price"),
                            getIntent().getExtras().getInt("amount"),
                            tvProducer.getText().toString(),
                            getIntent().getExtras().getString("image"));

                    if (Common.cart.size() > 0) {
                        Toast.makeText(ItemDetailActivity.this, "Bạn chỉ được chọn tối đa 1 sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Common.cart.add(item);
                        Toast.makeText(ItemDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_detail);
        items = new ArrayList<>();

        itemsDetailAdapter = new ItemsDetailAdapter(this, items);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsDetailAdapter);

        prepareDetail();

    }

    private void prepareDetail() {
        items.clear();

        for (int i = 0; i < Common.allItems.size(); i++) {
            if (Common.allItems.get(i).getType() == getIntent().getExtras().getInt("type")
                    && !Common.allItems.get(i).getId().equals(getIntent().getExtras().getString("id"))) {
                Item item = Common.allItems.get(i);
                items.add(item);
            }

            if (items.size() >= 5) {
                break;
            }
        }

        itemsDetailAdapter.notifyDataSetChanged();
    }

}
