package com.example.parstagram.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.models.Post;
import com.example.parstagram.adapters.ProfileAdapter;
import com.example.parstagram.R;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends BaseFragment {

    public static final String TAG = "Profile Fragment";

    //User userToFilterBy;
    RecyclerView rvPostsProfile;
    ProfileAdapter adapter;
    List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;
    TextView tvUsernameProfile;
    ImageView ivProfilePicProfile;
    Button btnFollowProfile;

    public User user = (User) ParseUser.getCurrentUser();

    public ProfileFragment(){}



    @Override
    // this is a lifecycle method that triggers just once when this fragment
    // loads on the frame
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    // lifecycle method
    public void onResume() {
        super.onResume();
        // when we come back to feed, query posts here.
        adapter.clear();
        queryPosts(0);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPostsProfile = view.findViewById(R.id.rvPostsProfile);

        btnFollowProfile = view.findViewById(R.id.btnFollowProfile);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        int numberOfColumns = 3;
        GridLayoutManager glm = new GridLayoutManager(getContext(), numberOfColumns);

        // initialize the array that will hold posts and create a PostsAdapter
        allPosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), allPosts);
        // set the adapter on the recycler view
        rvPostsProfile.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPostsProfile.setLayoutManager(glm);

        scrollListener = new EndlessRecyclerViewScrollListener(glm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                queryPosts(allPosts.size());
            }
        };
        rvPostsProfile.addOnScrollListener(scrollListener);

        tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile);
        ivProfilePicProfile = view.findViewById(R.id.ivProfilePicProfile);

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user = (User) object;
                Glide.with(ProfileFragment.this).load(user.getProfilePic().getUrl()).circleCrop().into(ivProfilePicProfile);
            }
        });

        tvUsernameProfile.setText(user.getUsername());

        if (user == ParseUser.getCurrentUser()) {
            btnFollowProfile.setVisibility(View.GONE);
        }

        ivProfilePicProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take a picture to replace pfp
                onLaunchCamera(v);
            }
        });
    }

    public void fetchTimelineAsync(int page) {

        // Remember to CLEAR OUT old items before appending in the new ones
        adapter.clear();
        queryPosts(0);
        // ...the data has come back, add new items to your adapter...
        adapter.notifyDataSetChanged();
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);

    }

    protected void queryPosts(int skip) {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.include(Post.KEY_LIKED_BY);
        query.whereEqualTo(Post.KEY_USER, user);
        // limit query to latest 20 items
        query.setLimit(20);
        query.setSkip(skip);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                Glide.with(this).load(takenImage).circleCrop().into(ivProfilePicProfile);
                user.setProfilePic(new ParseFile(photoFile));
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
