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
        myViewHolder.tvCompany.setText(insurancePackage.getCompany());
        myViewHolder.tvPrice.setText(Common.beautifyPrice(insurancePackage.getTerm().getPricePerYear()));
        myViewHolder.tvPercentage.setText(Common.beautifyPercent(insurancePackage.getTerm().getPercentage()));
        myViewHolder.tvMaxRefund.setText(Common.beautifyPrice(insurancePackage.getTerm().getMaxRefund()));

    }

    @Override
    public int getItemCount() {
        return insurancePackages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvCompany, tvPrice, tvPercentage, tvMaxRefund;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id_insurance);
            tvCompany = (TextView) itemView.findViewById(R.id.tv_company_insurance);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price_insurance);
            tvPercentage = (TextView) itemView.findViewById(R.id.tv_percentage_insurance);
            tvMaxRefund = (TextView) itemView.findViewById(R.id.tv_maxrefund_insurance);
        }
    }
}
