package com.example.parstagram.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram.R;
import com.example.parstagram.adapters.CommentsAdapter;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {


    private Post post;
    private RecyclerView rvComments;
    private CommentsAdapter adapter;
    private List<Comment> allComments;

    private TextView tvUsernameDetail;
    private TextView tvTimestampDetail;
    private ImageView ivPostImageDetail;
    private TextView tvDescriptionDetail;
    private ImageButton ibLikeDetail;
    private ImageButton ibCommentDetail;
    private TextView tvNumLikesDetail;
    private ImageView ivProfilePicDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        rvComments = findViewById(R.id.rvComments);
        tvUsernameDetail = findViewById(R.id.tvUsernameDetail);
        tvTimestampDetail = findViewById(R.id.tvTimestampDetail);
        ivPostImageDetail = findViewById(R.id.ivPostImageDetail);
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        ibLikeDetail = findViewById(R.id.ibLikeDetail);
        ibCommentDetail = findViewById(R.id.ibCommentDetail);
        tvNumLikesDetail = findViewById(R.id.tvNumLikesDetail);
        ivProfilePicDetail = findViewById(R.id.ivProfilePicDetail);


        post = getIntent().getParcelableExtra("post");

        ibCommentDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to the compose comment activity
                Intent i = new Intent(DetailActivity.this, ComposeCommentActivity.class);
                i.putExtra("post", post);
                startActivity(i);
            }
        });

        ibLikeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ParseUser> likedBy = post.getLikedBy();
                if(post.isLikedByCurrentUser()) {
                    // need to unlike
                    post.unlike();
                    ibLikeDetail.setBackgroundResource(R.drawable.ufi_heart);
                } else {
                    // else need to like
                    post.like();
                    ibLikeDetail.setBackgroundResource(R.drawable.ufi_heart_active);
                }
                tvNumLikesDetail.setText(post.getLikesCount());

            }
        });

        if (post.isLikedByCurrentUser()) {
            ibLikeDetail.setBackgroundResource(R.drawable.ufi_heart_active);
        } else {
            ibLikeDetail.setBackgroundResource(R.drawable.ufi_heart);
        }
        tvNumLikesDetail.setText(post.getLikesCount());


        tvUsernameDetail.setText(post.getUser().getUsername());
        Date dateCreated = post.getCreatedAt();
        tvTimestampDetail.setText(calculateTimeAgo(dateCreated));
        tvDescriptionDetail.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostImageDetail);
        }
        ParseFile profilePic = post.getUser().getProfilePic();
        if (image != null) {
            Glide.with(this).load(profilePic.getUrl()).circleCrop().into(ivProfilePicDetail);

        }

        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(this, allComments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        queryComments();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.clear();
        queryComments();
    }

    public void queryComments() {
        // load all the comments from Parse
        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo(Comment.KEY_POST, post);
        query.orderByDescending("createdAt");
        query.include(Comment.KEY_AUTHOR);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Log.e("Failed to get comments", e.getMessage());
                    return;
                }
                adapter.comments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}