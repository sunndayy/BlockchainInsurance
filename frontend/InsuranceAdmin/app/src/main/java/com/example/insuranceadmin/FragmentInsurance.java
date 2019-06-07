package com.example.insuranceadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInsurance extends Fragment {
    private RecyclerView recyclerView;
    private InsurancePackagesAdapter insurancePackagesAdapter;
    private List<InsurancePackage> insurancePackages;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView btnAddInsurance;

    public FragmentInsurance() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_insurance, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_insurance);
        insurancePackages = new ArrayList<>();

        insurancePackagesAdapter = new InsurancePackagesAdapter(getActivity(), insurancePackages, insurancePackages);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(insurancePackagesAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_insurance);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareAlbums();
            }
        });

        prepareAlbums();

        btnAddInsurance = (ImageView) rootView.findViewById(R.id.btn_add_insurance);
        btnAddInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddInsuranceActivity.class));
            }
        });

        return rootView;
    }

    private void prepareAlbums() {

        swipeRefreshLayout.setRefreshing(true);
        insurancePackages.clear();

        ApiService apiService = ApiUtils.getApiService();
        apiService.GetInsurances(Common.AccessToken)
                .enqueue(new Callback<List<InsurancePackage>>() {
                    @Override
                    public void onResponse(Call<List<InsurancePackage>> call, Response<List<InsurancePackage>> response) {
                        for (int i = 0; i <response.body().size(); i++) {
                            InsurancePackage item = response.body().get(i);
                            if (item.getTerm().getState()) {
                                insurancePackages.add(item);
                            }
                            insurancePackagesAdapter.notifyDataSetChanged();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<InsurancePackage>> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void filterItem(String query) {
        insurancePackagesAdapter.getFilter().filter(query);
    }
}
