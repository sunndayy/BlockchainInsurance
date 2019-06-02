package com.example.bishop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private Button btnSignUp, btnSignIn;
    private TextView tvUserName;
    private ImageView imgUserAvatar;
    private LinearLayout rowCart, rowInfo, rowFeedBack, rowLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        imgUserAvatar = (ImageView) view.findViewById(R.id.imgUserAvatar);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        btnSignUp = view.findViewById(R.id.btn_sign_up);

        if (Common.user != null) {
            btnSignIn.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.GONE);
            tvUserName.setVisibility(View.VISIBLE);
            imgUserAvatar.setVisibility(View.VISIBLE);
            tvUserName.setText(Common.user.getName());
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.GONE);
            imgUserAvatar.setVisibility(View.GONE);
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        rowCart = (LinearLayout) view.findViewById(R.id.row_cart_profile);

        rowCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.user != null) {
                    startActivity(new Intent(getActivity(), CartActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Hãy đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rowInfo = (LinearLayout) view.findViewById(R.id.row_info_profile);

        rowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.user != null) {
                    startActivity(new Intent(getActivity(), InfoActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Hãy đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rowFeedBack = (LinearLayout) view.findViewById(R.id.row_feedback_profile);

        rowFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rowLogout = (LinearLayout) view.findViewById(R.id.row_logout_profile);

        rowLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.user = null;
                Common.cart.clear();
                btnSignUp.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);
                tvUserName.setVisibility(View.GONE);
                imgUserAvatar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
