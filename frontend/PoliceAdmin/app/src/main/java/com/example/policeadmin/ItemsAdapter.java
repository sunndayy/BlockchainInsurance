package com.example.policeadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Item> items;
    private List<Item> itemsFiltered;

    public ItemsAdapter(Context context, List<Item> items, List<Item> itemsFiltered) {
        this.context = context;
        this.items = items;
        this.itemsFiltered = itemsFiltered;
    }

    @NonNull
    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.MyViewHolder myViewHolder, int i) {
        final Item item = itemsFiltered.get(i);

        myViewHolder.tvUid.setText(item.getPoliceInfo().getUid());

        if (item.getPoliceInfo().getLicensePlate() != null) {
            myViewHolder.tvBienSo.setText(item.getPoliceInfo().getLicensePlate());
        } else {
            myViewHolder.tvBienSo.setText("Chưa có biển số");
        }

        myViewHolder.tvNameProduct.setText(item.getProduct().getName());

        if (item.getProduct().getType() == 0) {
            myViewHolder.tvTypeProduct.setText("Xe số");
        } else {
            if (item.getProduct().getType() == 1) {
                myViewHolder.tvTypeProduct.setText("Xe tay ga");
            } else {
                if (item.getProduct().getType() == 2) {
                    myViewHolder.tvTypeProduct.setText("Xe côn tay");
                }
            }
        }

        myViewHolder.tvProducerProduct.setText(item.getProduct().getProducer());
        myViewHolder.tvUserName.setText(item.getUser().getName());
        myViewHolder.tvUserId.setText(item.getUser().getIdentityCard());

        myViewHolder.tvUserBir.setText(item.getUser().getBirthday().getDay() + "/"
                + item.getUser().getBirthday().getMonth() + "/"
                + item.getUser().getBirthday().getYear());

        myViewHolder.tvUserAddress.setText(item.getUser().getAddress());
        myViewHolder.tvUserPhone.setText(item.getUser().getPhoneNumber());
        myViewHolder.tvUserEmail.setText(item.getUser().getEmail());

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_confirm, null);

                final EditText edtBienSo = popupView.findViewById(R.id.edt_bienso);

                Button btnYes = popupView.findViewById(R.id.btn_yes);
                Button btnNo = popupView.findViewById(R.id.btn_no);

                if (item.getPoliceInfo().getLicensePlate() != null) {
                    LinearLayout linearLayout = (LinearLayout) popupView.findViewById(R.id.info_bienso);
                    TextView tvInfo = (TextView) popupView.findViewById(R.id.tv_notifi);

                    linearLayout.setVisibility(View.GONE);
                    tvInfo.setText("Phương tiện đã có biển số");
                    btnYes.setVisibility(View.GONE);
                }

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, true);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiService apiService = ApiUtils.getApiService();
                        apiService.CreateLicensePlate(item.getPoliceInfo().getUid(),
                                Common.AccessToken, edtBienSo.getText().toString())
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(context, "Cập nhật biển số thành công", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }
                                });
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
        return itemsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemsFiltered = items;
                } else {
                    List<Item> filteredList = new ArrayList<>();
                    for (Item row : items) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getUser().getName().toLowerCase().contains(charString)
                                || row.getProduct().getName().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    itemsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsFiltered = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvUid, tvBienSo, tvNameProduct, tvTypeProduct, tvProducerProduct,
                tvUserName, tvUserId, tvUserBir, tvUserAddress, tvUserPhone, tvUserEmail;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUid = (TextView) itemView.findViewById(R.id.tv_uid);
            tvBienSo = (TextView) itemView.findViewById(R.id.tv_bienso);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tv_name_product);
            tvTypeProduct = (TextView) itemView.findViewById(R.id.tv_type_product);
            tvProducerProduct = (TextView) itemView.findViewById(R.id.tv_producer_product);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_name_user);
            tvUserId = (TextView) itemView.findViewById(R.id.tv_userid);
            tvUserBir = (TextView) itemView.findViewById(R.id.tv_userngaysinh);
            tvUserAddress = (TextView) itemView.findViewById(R.id.tv_useraddress);
            tvUserPhone = (TextView) itemView.findViewById(R.id.tv_userphone);
            tvUserEmail = (TextView) itemView.findViewById(R.id.tv_useremail);

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
