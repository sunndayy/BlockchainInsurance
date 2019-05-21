package com.example.insuranceadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TxsAdapter extends RecyclerView.Adapter<TxsAdapter.MyViewHolder> {
    private Context context;
    private List<Tx> txs;

    public TxsAdapter(Context context, List<Tx> txs) {
        this.context = context;
        this.txs = txs;
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
        final Tx tx = txs.get(i);

        myViewHolder.tvTime.setText(tx.getTime());
        myViewHolder.tvStatus.setText("Chua xu ly");
        myViewHolder.tvCompany.setText(tx.getRef().getPlan().getCompany());
        myViewHolder.tvIdInsurance.setText(tx.getRef().getPlan().getId());
        myViewHolder.tvUserId.setText(tx.getRef().getUserInfo().getIdentityCard());
        myViewHolder.tvUserName.setText(tx.getRef().getUserInfo().getName());
        myViewHolder.tvUserBirthday.setText(tx.getRef().getUserInfo().getBirthday());
        myViewHolder.tvUserAddress.setText(tx.getRef().getUserInfo().getAddress());
        myViewHolder.tvUserphone.setText(tx.getRef().getUserInfo().getPhoneNumber());
        myViewHolder.tvUserEmail.setText(tx.getRef().getUserInfo().getEmail());
        myViewHolder.tvAddressGara.setText(tx.getRef().getGaraPubKeyHashes().get(0));
        myViewHolder.tvStartTime.setText(tx.getRef().getExpireTime().getTimeStart());
        myViewHolder.tvEndTime.setText(tx.getRef().getExpireTime().getTimeEnd());
        myViewHolder.tvOrderId.setText(String.valueOf(tx.getId()));

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_window, null);
                Button btnYes = popupView.findViewById(R.id.btn_confirm_yes);
                Button btnNo = popupView.findViewById(R.id.btn_confirm_no);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiService apiService = ApiUtils.getApiService();
                        apiService.UpdateOrder(tx.getId(), Common.AccessToken, true)
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(popupView.getContext(), "successful", Toast.LENGTH_SHORT).show();
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
        return txs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTime, tvStatus, tvCompany, tvIdInsurance,
                tvUserId, tvUserName, tvUserBirthday, tvUserAddress, tvUserphone,tvUserEmail,
                tvAddressGara, tvStartTime, tvEndTime, tvOrderId;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = (TextView) itemView.findViewById(R.id.tv_time_tx);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status_tx);
            tvCompany = (TextView) itemView.findViewById(R.id.tv_company_tx);
            tvIdInsurance = (TextView) itemView.findViewById(R.id.tv_idinsurance_tx);
            tvUserId = (TextView) itemView.findViewById(R.id.tv_userid_tx);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name_tx);
            tvUserBirthday = (TextView) itemView.findViewById(R.id.tv_user_birthday_tx);
            tvUserAddress = (TextView) itemView.findViewById(R.id.tv_user_address_tx);
            tvUserphone = (TextView) itemView.findViewById(R.id.tv_user_phonenumber_tx);
            tvUserEmail = (TextView) itemView.findViewById(R.id.tv_user_email_tx);
            tvAddressGara = (TextView) itemView.findViewById(R.id.tv_gara_address_tx);
            tvStartTime = (TextView) itemView.findViewById(R.id.tv_starttime_tx);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_endtime_tx);
            tvOrderId = (TextView) itemView.findViewById(R.id.tv_orderid_tx);

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
