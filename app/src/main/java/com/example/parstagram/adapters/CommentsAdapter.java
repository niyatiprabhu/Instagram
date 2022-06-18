package com.example.parstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram.models.Comment;
import com.example.parstagram.R;

import java.util.List;

public class CommentsAdapter extends
        RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private Context context;
    public List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View commentView = inflater.inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBody;
        public TextView tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBody =  itemView.findViewById(R.id.tvBody);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }

        public void bind(Comment comment) {
            tvAuthor.setText(comment.getAuthor().getUsername());
            tvBody.setText(comment.getBody());
        }
    }
}
