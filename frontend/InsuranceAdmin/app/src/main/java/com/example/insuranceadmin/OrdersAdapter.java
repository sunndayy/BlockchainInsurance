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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Order order = orders.get(i);
        myViewHolder.tvId.setText(order.getId());
        myViewHolder.tvCompany.setText(order.getCompany());

        Date date = new Date(order.getExpireTime().getTimeStart());
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
        myViewHolder.tvTimeStart.setText(str);

        date = new Date(order.getExpireTime().getTimeEnd());
        str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
        myViewHolder.tvTimeEnd.setText(str);

        myViewHolder.tvUserID.setText(order.getUserInfo().getIdentityCard());
        myViewHolder.tvBienSo.setText(order.getUserInfo().getLicensePlate());

        if (order.getUserInfo().getName() != null) {
            myViewHolder.tvUserName.setText(order.getUserInfo().getName());
        } else {
            myViewHolder.tvUserName.setText("");
        }

        if (order.getUserInfo().getAddress() != null) {
            myViewHolder.tvUserAddress.setText(order.getUserInfo().getAddress());
        } else {
            myViewHolder.tvUserAddress.setText("");
        }

        if (order.getUserInfo().getPhoneNumber() != null) {
            myViewHolder.tvUserPhone.setText(order.getUserInfo().getPhoneNumber());
        } else {
            myViewHolder.tvUserPhone.setText("");
        }

        if (order.getUserInfo().getEmail() != null) {
            myViewHolder.tvUserEmail.setText(order.getUserInfo().getEmail());
        } else {
            myViewHolder.tvUserEmail.setText("");
        }

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_refund, null);
                Button btnClose = popupView.findViewById(R.id.btn_refund_close);
                TextView tvNotifi = (TextView) popupView.findViewById(R.id.tv_refund_notifi);
                TextView tvTotal = (TextView) popupView.findViewById(R.id.tv_refund_total);
                TextView tvRefund = (TextView) popupView.findViewById(R.id.tv_refund_refund);
                TextView tvTime = (TextView) popupView.findViewById(R.id.tv_refund_time);
                TextView tvGaraPubKeyHash = (TextView) popupView.findViewById(R.id.tv_refund_pubkeyhash);
                LinearLayout viewRefund = (LinearLayout) popupView.findViewById(R.id.refund_info_popup);

                if (order.getRefunds().size() != 0) {
                    tvNotifi.setText("Danh sách hoàn trả");
                    tvTotal.setText(Common.beautifyPrice(order.getRefunds().get(0).getTotal()));
                    tvRefund.setText(Common.beautifyPrice(order.getRefunds().get(0).getRefund()));
                    tvGaraPubKeyHash.setText(order.getRefunds().get(0).getGaraPubKeyHash());

                    Date d = new Date(order.getRefunds().get(0).getTime());
                    String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(d);
                    tvTime.setText(s);

                } else {
                    viewRefund.setVisibility(View.GONE);
                }

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, false);


                btnClose.setOnClickListener(new View.OnClickListener() {
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
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvId, tvCompany, tvTimeStart, tvTimeEnd,
                tvUserID, tvUserName, tvBienSo, tvUserAddress, tvUserEmail, tvUserPhone;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id_order);
            tvCompany = itemView.findViewById(R.id.tv_company_order);
            tvTimeStart = itemView.findViewById(R.id.tv_timestart_order);
            tvTimeEnd = itemView.findViewById(R.id.tv_timeend_order);
            tvUserID = itemView.findViewById(R.id.tv_userid_order);
            tvUserName = itemView.findViewById(R.id.tv_name_user_order);
            tvBienSo = itemView.findViewById(R.id.tv_userbienso_order);
            tvUserAddress = itemView.findViewById(R.id.tv_useraddress_order);
            tvUserEmail = itemView.findViewById(R.id.tv_useremail_order);
            tvUserPhone = itemView.findViewById(R.id.tv_userphone_order);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener l) {
            this.itemClickListener = l;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
