package com.example.insuranceadmin;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (viewPager.getCurrentItem() == 0) {
                    FragmentInsurance f = (FragmentInsurance) viewPagerAdapter.getItem(0);
                    f.filterItem(query);
                } else if (viewPager.getCurrentItem() == 1) {
                    FragmentOrder f = (FragmentOrder) viewPagerAdapter.getItem(1);
                    f.filterItem(query);
                } else if (viewPager.getCurrentItem() == 2) {
                    FragmentTx f = (FragmentTx) viewPagerAdapter.getItem(2);
                    f.filterItem(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (viewPager.getCurrentItem() == 0) {
                    FragmentInsurance f = (FragmentInsurance) viewPagerAdapter.getItem(0);
                    f.filterItem(query);
                } else if (viewPager.getCurrentItem() == 1) {
                    FragmentOrder f = (FragmentOrder) viewPagerAdapter.getItem(1);
                    f.filterItem(query);
                } else if (viewPager.getCurrentItem() == 2) {
                    FragmentTx f = (FragmentTx) viewPagerAdapter.getItem(2);
                    f.filterItem(query);
                }
                return false;
            }
        });
        return true;
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_item);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_order);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_tx);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }


    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentInsurance(), "Gói bảo hiểm");
        viewPagerAdapter.addFragment(new FragmentOrder(), "Đặt mua");
        viewPagerAdapter.addFragment(new FragmentTx(), "Chờ duyệt");
        viewPager.setAdapter(viewPagerAdapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
