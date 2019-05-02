package com.example.bishop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private ItemsCartAdapter itemsCartAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_cart);
        itemList = new ArrayList<>();
        itemsCartAdapter = new ItemsCartAdapter(this, itemList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsCartAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        prepareAlbums();
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
        }
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.ab,
                R.drawable.blade,
                R.drawable.lead,
                R.drawable.msx,
                R.drawable.pcx,
                R.drawable.sh,
                R.drawable.vision,
                R.drawable.wave,
                R.drawable.winner};

        Item a = new Item("AB", 45000000, covers[0]);
        itemList.add(a);

        a = new Item("Blade", 18000000, covers[1]);
        itemList.add(a);

        a = new Item("Lead", 31000000, covers[2]);
        itemList.add(a);

        a = new Item("MSX", 90000000, covers[3]);
        itemList.add(a);

        a = new Item("PCX", 81000000, covers[4]);
        itemList.add(a);

        a = new Item("SH", 63000000, covers[5]);
        itemList.add(a);

        a = new Item("Vision", 29000000, covers[6]);
        itemList.add(a);

        a = new Item("Wave", 23000000, covers[7]);
        itemList.add(a);

        a = new Item("Winner", 42000000, covers[8]);
        itemList.add(a);

        itemsCartAdapter.notifyDataSetChanged();
    }
}
