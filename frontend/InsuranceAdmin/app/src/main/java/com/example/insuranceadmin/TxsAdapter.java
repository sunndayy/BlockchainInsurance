package com.example.insuranceadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TxsAdapter extends RecyclerView.Adapter<TxsAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Tx> txs;
    private List<Tx> txsFiltered;

    public TxsAdapter(Context context, List<Tx> txs, List<Tx> txsFiltered) {
        this.context = context;
        this.txs = txs;
        this.txsFiltered = txsFiltered;
    }

    @NonNull
    @Override
    public TxsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_tx, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TxsAdapter.MyViewHolder myViewHolder, int i) {
        final Tx tx = txsFiltered.get(i);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA).parse(tx.getTime());
            String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
            myViewHolder.tvTime.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date = new Date(Long.parseLong(tx.getRef().getExpireTime().getTimeStart()));
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
        myViewHolder.tvStartTime.setText(str);

        date = new Date(Long.parseLong(tx.getRef().getExpireTime().getTimeEnd()));
        str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
        myViewHolder.tvEndTime.setText(str);

        myViewHolder.tvStatus.setText("Chưa xác nhận");
        myViewHolder.tvOrderId.setText(String.valueOf(tx.getId()));
        myViewHolder.tvCompany.setText(tx.getRef().getPlan().getCompany());
        myViewHolder.tvIdInsurance.setText(tx.getRef().getPlan().getId());
        myViewHolder.tvUserId.setText(tx.getRef().getUserInfo().getIdentityCard());
        myViewHolder.tvUserName.setText(tx.getRef().getUserInfo().getName());
        myViewHolder.tvUserAddress.setText(tx.getRef().getUserInfo().getAddress());
        myViewHolder.tvUserphone.setText(tx.getRef().getUserInfo().getPhoneNumber());
        myViewHolder.tvUserEmail.setText(tx.getRef().getUserInfo().getEmail());
        myViewHolder.tvUserBienSo.setText(tx.getRef().getUserInfo().getLicensePlate());

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_window, null);
                Button btnYes = popupView.findViewById(R.id.btn_confirm_yes);
                Button btnNo = popupView.findViewById(R.id.btn_confirm_no);

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, false);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiService apiService = ApiUtils.getApiService();
                        apiService.UpdateOrder(tx.getId(), Common.AccessToken, true)
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(popupView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(popupView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        popupWindow.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });


                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


            }
        });
    }

    @Override
    public int getItemCount() {
        return txsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    txsFiltered = txs;
                } else {
                    List<Tx> filteredList = new ArrayList<>();
                    for (Tx row : txs) {

                        if (row.getRef().getUserInfo().getName().contains(charString)
                                || row.getRef().getUserInfo().getLicensePlate().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    txsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = txs;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                txsFiltered = (ArrayList<Tx>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTime, tvStatus, tvCompany, tvIdInsurance,
                tvUserId, tvUserName, tvUserBirthday, tvUserAddress, tvUserphone, tvUserEmail,
                tvUserBienSo, tvStartTime, tvEndTime, tvOrderId;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = (TextView) itemView.findViewById(R.id.tv_time_tx);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status_tx);
            tvOrderId = (TextView) itemView.findViewById(R.id.tv_id_tx);
            tvCompany = (TextView) itemView.findViewById(R.id.tv_company_tx);
            tvIdInsurance = (TextView) itemView.findViewById(R.id.tv_id_insurance_tx);
            tvUserId = (TextView) itemView.findViewById(R.id.tv_userid_tx);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_name_user_tx);
            tvUserAddress = (TextView) itemView.findViewById(R.id.tv_useraddress_tx);
            tvUserphone = (TextView) itemView.findViewById(R.id.tv_userphone_tx);
            tvUserEmail = (TextView) itemView.findViewById(R.id.tv_useremail_tx);
            tvStartTime = (TextView) itemView.findViewById(R.id.tv_timestart_tx);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_timeend_tx);
            tvUserBienSo = (TextView) itemView.findViewById(R.id.tv_userbienso_tx);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener i) {
            this.itemClickListener = i;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
