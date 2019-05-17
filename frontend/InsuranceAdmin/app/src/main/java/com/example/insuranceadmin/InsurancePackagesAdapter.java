package com.example.insuranceadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

public class InsurancePackagesAdapter extends RecyclerView.Adapter<InsurancePackagesAdapter.MyViewHolder> {

    private Context context;
    private List<InsurancePackage> insurancePackages;

    public InsurancePackagesAdapter(Context context, List<InsurancePackage> insurancePackages) {
        this.context = context;
        this.insurancePackages = insurancePackages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_insurance_package, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        InsurancePackage insurancePackage = insurancePackages.get(i);
        myViewHolder.tvId.setText(insurancePackage.getId());
        myViewHolder.tvName.setText(insurancePackage.getName());
        myViewHolder.tvPrice.setText(insurancePackage.getPrice());
        myViewHolder.tvDate.setText(insurancePackage.getDate());

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Log.d("tag", String.valueOf(position));
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_window, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                Button btnYes = (Button) popupView.findViewById(R.id.btn_modify_yes);
                Button btnNo = (Button) popupView.findViewById(R.id.btn_modify_no);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return insurancePackages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvId, tvName, tvPrice, tvDate;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id_insurance);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_insurance);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price_insurance);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_insurance);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
