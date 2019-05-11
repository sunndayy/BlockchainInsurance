package com.example.insuranceadmin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentInsurance extends Fragment {
    private RecyclerView recyclerView;
    private InsurancePackagesAdapter insurancePackagesAdapter;
    private List<InsurancePackage> insurancePackages;

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

        insurancePackagesAdapter = new InsurancePackagesAdapter(getActivity(), insurancePackages);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(insurancePackagesAdapter);

        prepareAlbums();
        return rootView;
    }

    private void prepareAlbums() {
        InsurancePackage a = new InsurancePackage("0001", "Bao hiem A", "12.000", "30");
        insurancePackages.add(a);
        a = new InsurancePackage("0001", "Bao hiem A", "12.000", "30");
        insurancePackages.add(a);
        a = new InsurancePackage("0001", "Bao hiem A", "12.000", "30");
        insurancePackages.add(a);
        a = new InsurancePackage("0001", "Bao hiem A", "12.000", "30");
        insurancePackages.add(a);
        a = new InsurancePackage("0001", "Bao hiem A", "12.000", "30");
        insurancePackages.add(a);

        insurancePackagesAdapter.notifyDataSetChanged();
    }
}
