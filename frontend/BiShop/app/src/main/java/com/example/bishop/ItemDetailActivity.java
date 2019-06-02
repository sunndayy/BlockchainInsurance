package com.example.bishop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvType, tvProducer, tvPrice, tvDescribe;
    private ImageView imgItem;
    private Button btnAddCart;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvId = (TextView) findViewById(R.id.tv_detail_id);
        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvType = (TextView) findViewById(R.id.tv_detail_type);
        tvProducer = (TextView) findViewById(R.id.tv_detail_producer);
        tvPrice = (TextView) findViewById(R.id.tv_detail_price);
        tvDescribe = (TextView) findViewById(R.id.tv_detail_describe);

        imgItem = (ImageView) findViewById(R.id.ic_detail_item);

        Glide.with(this).load(getIntent().getExtras().getString("image"))
                .into(imgItem);

        btnAddCart = (Button) findViewById(R.id.btn_detail_addcart);

        if (Common.user == null) {
            btnAddCart.setVisibility(View.GONE);
        }

        tvId.setText(getIntent().getExtras().getString("id"));
        tvName.setText(getIntent().getExtras().getString("name"));

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
                }
                else {
                    Common.cart.add(item);
                    Toast.makeText(ItemDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
