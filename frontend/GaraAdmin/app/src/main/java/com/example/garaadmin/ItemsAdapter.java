package com.example.garaadmin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final Item item = itemList.get(i);
        myViewHolder.tvId.setText(String.valueOf(item.getId()));
        if (item.getStatus()) {
            myViewHolder.tvStatus.setText("Đã sửa xong");
            myViewHolder.tvStatus.setTextColor(Color.rgb(31, 168, 63));
        } else {
            myViewHolder.tvStatus.setText("Chưa sửa xong");
            myViewHolder.tvStatus.setTextColor(Color.RED);
        }

        myViewHolder.tvUserId.setText(item.getUser().getIdentityCard());
        myViewHolder.tvUserName.setText(item.getUser().getName());
        myViewHolder.tvUserEmail.setText(item.getUser().getEmail());
        myViewHolder.tvUserAddress.setText(item.getUser().getAddress());
        myViewHolder.tvUserPhone.setText(item.getUser().getPhoneNumber());

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_confirm, null);
                final Spinner spinner = (Spinner) popupView.findViewById(R.id.spinner_popup);
                final List<String> categories = new ArrayList<String>();


                Button btnYes = popupView.findViewById(R.id.btn_yes);
                Button btnNo = popupView.findViewById(R.id.btn_no);
                final EditText edtBienSo = (EditText) popupView.findViewById(R.id.edt_bienso);
                final EditText edtSotien = (EditText) popupView.findViewById(R.id.edt_sotien);
                TextView tvNotifi = popupView.findViewById(R.id.tv_notifi);
                LinearLayout linearLayout = popupView.findViewById(R.id.info_suaxe);


                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, true);

                if (item.getStatus()) {
                    btnYes.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);

                    tvNotifi.setText("Đơn hàng đã được hoàn tất");
                } else {
                    final ApiInsurance apiInsurance = new Retrofit.Builder()
                            .baseUrl("http://bcinsurence.herokuapp.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ApiInsurance.class);

                    apiInsurance.GetInsuranceInfo(Common.AccessToken)
                            .enqueue(new Callback<List<InsuranceInfo>>() {
                                @Override
                                public void onResponse(Call<List<InsuranceInfo>> call, Response<List<InsuranceInfo>> response) {
                                    if (response.body().size() != 0) {
                                        for (int i = 0; i < response.body().size(); i++) {
                                            String id = response.body().get(i).getId();
                                            categories.add(id);
                                        }

                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, categories);
                                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(dataAdapter);
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                Common.BufferId = parent.getItemAtPosition(position).toString();
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<InsuranceInfo>> call, Throwable t) {
                                    Toast.makeText(popupView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }


                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiService apiService = ApiUtils.getApiService();
                        UpdateInfo updateInfo = new UpdateInfo();
                        updateInfo.setStatus(true);
                        updateInfo.setLicensePlate(edtBienSo.getText().toString());
                        updateInfo.setTotal(Integer.parseInt(edtSotien.getText().toString()));
                        updateInfo.getInsurence().setId(Common.BufferId);
                        updateInfo.getInsurence().setCompany("phhoang");

                        apiService.UpdateOrder(item.getId(), Common.AccessToken, updateInfo)
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


                popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvId, tvStatus, tvUserId, tvUserName,
                tvUserBienSo, tvUserPhone, tvUserEmail, tvUserAddress;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvUserId = (TextView) itemView.findViewById(R.id.tv_userid);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_username);
            tvUserPhone = (TextView) itemView.findViewById(R.id.tv_userphone);
            tvUserEmail = (TextView) itemView.findViewById(R.id.tv_useremail);
            tvUserAddress = (TextView) itemView.findViewById(R.id.tv_useraddress);

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
