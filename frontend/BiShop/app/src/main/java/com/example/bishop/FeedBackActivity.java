package com.example.bishop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> comments;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout viewNotifi, viewCmt;
    private Button btnSignIn, btnSignUp, btnSend;
    private CircleImageView avatar;
    private EditText edtCmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewNotifi = (LinearLayout) findViewById(R.id.view_feedback_notifi);
        viewCmt = (LinearLayout) findViewById(R.id.view_feedback_cmt);

        if (Common.user != null) {
            viewCmt.setVisibility(View.VISIBLE);
            viewNotifi.setVisibility(View.GONE);
        } else {
            viewCmt.setVisibility(View.GONE);
            viewNotifi.setVisibility(View.VISIBLE);
        }

        btnSignIn = (Button) findViewById(R.id.btn_feedback_sign_in);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedBackActivity.this, SignInActivity.class));
            }
        });

        btnSignUp = (Button) findViewById(R.id.btn_feedback_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedBackActivity.this, SignUpActivity.class));
            }
        });

        btnSend = (Button) findViewById(R.id.btn_send_comment);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentPost commentPost = new CommentPost(edtCmt.getText().toString());

                edtCmt.setText("");

                closeKeyboard();

                ApiService apiService = ApiUtils.getApiService();
                apiService.PostComment(Common.user.getToken(), commentPost)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.errorBody() == null) {
                                    Toast.makeText(FeedBackActivity.this, "Đã gửi feedback", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FeedBackActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(FeedBackActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        avatar = (CircleImageView) findViewById(R.id.feedback_avatar);

        if (Common.user != null) {
            Glide.with(this)
                    .load(ApiUtils.BASE_URL + "/user-avatar/" + Common.user.getUsername())
                    .into(avatar);
        }

        edtCmt = (EditText) findViewById(R.id.edt_feedback_cmt);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_feedback);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareComment();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_feedback);
        comments = new ArrayList<>();

        commentsAdapter = new CommentsAdapter(this, comments);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(commentsAdapter);

        prepareComment();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void prepareComment() {
        comments.clear();

        ApiService apiService = ApiUtils.getApiService();

        apiService.GetComments()
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().size(); i++) {
                                Comment comment = response.body().get(i);
                                comments.add(comment);
                            }
                            commentsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(FeedBackActivity.this, "Không thể lấy danh sách bình luận", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(FeedBackActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
