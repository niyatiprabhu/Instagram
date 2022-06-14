package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private TextView tvUsernameDetail;
    private TextView tvTimestampDetail;
    private ImageView ivPostImageDetail;
    private TextView tvDescriptionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvUsernameDetail = findViewById(R.id.tvUsernameDetail);
        tvTimestampDetail = findViewById(R.id.tvTimestampDetail);
        ivPostImageDetail = findViewById(R.id.ivPostImageDetail);
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);

        Post post = getIntent().getParcelableExtra("post");

        tvUsernameDetail.setText(post.getUser().getUsername());
        Date dateCreated = post.getCreatedAt();
        tvTimestampDetail.setText(calculateTimeAgo(dateCreated));
        tvDescriptionDetail.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostImageDetail);
        }
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