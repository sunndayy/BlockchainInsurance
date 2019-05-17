package com.example.garaadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private List<Item> itemList;
    private Context context;

    public ItemsAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Item item = itemList.get(i);
        myViewHolder.tvBienSo.setText(item.getBienSo());
        myViewHolder.tvName.setText(item.getName());
        myViewHolder.tvPrice.setText(String.valueOf(item.getMoney()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBienSo, tvName, tvPrice;
        public Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBienSo = (TextView) itemView.findViewById(R.id.tv_bienso);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_money);
            aSwitch = (Switch) itemView.findViewById(R.id.switch_check);

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(context, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
