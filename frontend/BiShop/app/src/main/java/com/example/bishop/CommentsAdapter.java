package com.example.bishop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comment, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.MyViewHolder myViewHolder, int i) {
        Comment comment = comments.get(i);

        myViewHolder.username.setText(comment.getUser().getUsername());
        myViewHolder.cmtContent.setText(comment.getContent());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA).parse(comment.getTime());
            String str = new SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.CHINA).format(date);
            myViewHolder.time.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(context)
                .load(ApiUtils.BASE_URL + "/user-avatar/" + comment.getUser().getUsername())
                .into(myViewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView username, time, cmtContent;
        public CircleImageView avatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.cmt_user_name);
            time = (TextView) itemView.findViewById(R.id.cmt_time);
            cmtContent = (TextView) itemView.findViewById(R.id.cmt_content);
            avatar = (CircleImageView) itemView.findViewById(R.id.cmt_user_avatar);
        }
    }
}
