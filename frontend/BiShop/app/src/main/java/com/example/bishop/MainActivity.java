package com.example.bishop;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private ItemsMainAdapter itemsMainAdapter1, itemsMainAdapter2, itemsMainAdapter3;
    private List<Item> itemList1, itemList2, itemList3;
    private SliderLayout sliderLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Button btnSignIn, btnSignUp;
    private TextView txtvUserName, txtvUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        btnSignIn = (Button) headerLayout.findViewById(R.id.btn_nav_signin);
        btnSignUp = (Button) headerLayout.findViewById(R.id.btn_nav_signup);
        txtvUserName = (TextView) headerLayout.findViewById(R.id.txt_nav_username);
        txtvUserEmail = (TextView) headerLayout.findViewById(R.id.txt_nav_useremail);

        if (Common.user != null) {
            btnSignIn.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.GONE);
            txtvUserName.setText(Common.user.getUsername());
            txtvUserEmail.setText(Common.user.getEmail());
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        // slider
        sliderLayout = (SliderLayout) findViewById(R.id.slider_main);
        prepareBanner();

        //swipe
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareAlbums();
            }
        });

        // recycle view

        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view_main_1);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view_main_2);
        recyclerView3 = (RecyclerView) findViewById(R.id.recycler_view_main_3);


        itemList1 = new ArrayList<>();
        itemList2 = new ArrayList<>();
        itemList3 = new ArrayList<>();

        itemsMainAdapter1 = new ItemsMainAdapter(this, itemList1);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(itemsMainAdapter1);

        itemsMainAdapter2 = new ItemsMainAdapter(this, itemList2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(itemsMainAdapter2);

        itemsMainAdapter3 = new ItemsMainAdapter(this, itemList3);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(itemsMainAdapter3);

        prepareAlbums();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            if (Common.user != null) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Toast.makeText(MainActivity.this, "Hãy đăng nhập !!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }

        } else if (id == R.id.nav_history) {
            if (Common.user != null) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Toast.makeText(MainActivity.this, "Hãy đăng nhập !!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
        } else if (id == R.id.nav_info) {
            if (Common.user != null) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Toast.makeText(MainActivity.this, "Hãy đăng nhập !!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
        } else if (id == R.id.nav_logout) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Common.user = null;
            Common.cart.clear();
            btnSignUp.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            txtvUserEmail.setText("android.studio@android.com");
            txtvUserName.setText("Android Studio");
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void prepareAlbums() {

        itemList1.clear();
        itemList2.clear();
        itemList3.clear();

        ApiService apiService = ApiUtils.getApiService();

        apiService.GetXeSo().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                for (int i = 0; i < response.body().size(); i++) {
                    Item item = response.body().get(i);
                    item.setImage(ApiUtils.BASE_URL + "/product-image/" + item.getId());
                    itemList1.add(item);
                }

                itemsMainAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        apiService.GetXeTayGa().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    Item item = response.body().get(i);
                    item.setImage(ApiUtils.BASE_URL + "/product-image/" + item.getId());
                    itemList2.add(item);
                }

                itemsMainAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        apiService.GetXeConTay().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    Item item = response.body().get(i);
                    item.setImage(ApiUtils.BASE_URL + "/product-image/" + item.getId());
                    itemList3.add(item);
                }

                itemsMainAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    private void prepareBanner() {
        TextSliderView textSliderView1 = new TextSliderView(this);
        textSliderView1.image(R.drawable.banner1);

        TextSliderView textSliderView2 = new TextSliderView(this);
        textSliderView2.image(R.drawable.banner2);

        TextSliderView textSliderView3 = new TextSliderView(this);
        textSliderView3.image(R.drawable.banner3);

        TextSliderView textSliderView4 = new TextSliderView(this);
        textSliderView4.image(R.drawable.banner4);

        sliderLayout.addSlider(textSliderView1);
        sliderLayout.addSlider(textSliderView2);
        sliderLayout.addSlider(textSliderView3);
        sliderLayout.addSlider(textSliderView4);

        sliderLayout.setDuration(5000);
    }
}
