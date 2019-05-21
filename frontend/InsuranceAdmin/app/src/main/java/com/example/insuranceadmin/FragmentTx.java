package com.example.insuranceadmin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTx extends Fragment {

    private RecyclerView recyclerView;
    private TxsAdapter txsAdapter;
    private List<Tx> txs;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FragmentTx() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tx, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_tx);
        txs = new ArrayList<>();

        txsAdapter = new TxsAdapter(getActivity(), txs);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(txsAdapter);

        prepareAlbums();


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_tx);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareAlbums();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void prepareAlbums() {

        txs.clear();

        ApiService apiService = ApiUtils.getApiService();

        apiService.GetTxs(Common.AccessToken)
                .enqueue(new Callback<List<Tx>>() {
                    @Override
                    public void onResponse(Call<List<Tx>> call, Response<List<Tx>> response) {
                        for (int i = 0; i < response.body().size(); i++) {
                            Tx tx = response.body().get(i);
                            if (tx.getType().equals("CONTRACT") && !tx.getStatus()) {
                                txs.add(tx);
                            }
                        }

                        txsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Tx>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
