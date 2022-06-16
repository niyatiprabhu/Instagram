package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ComposeComment extends AppCompatActivity {

    Post post;
    Button btnSave;
    EditText etBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_comment);

        post = getIntent().getParcelableExtra("post");

        btnSave = findViewById(R.id.btnSave);
        etBody = findViewById(R.id.etBody);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // post the comment to parse
                String body = etBody.getText().toString();
                Comment comment = new Comment();
                comment.setAuthor(ParseUser.getCurrentUser());
                comment.setPost(post);
                comment.setBody(body);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // to go backwards
                        if (e != null) {
                            Log.e("Yikes", e.getMessage());
                            return;
                        }
                        finish();
                    }
                });
            }
        });

    }
}