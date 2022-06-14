package com.example.parstagram;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;


public class Parstagram extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("GMOI5k7N67icb2koSKcZsB9qV8xBqf4H9ylaJkfJ")
                .clientKey("E6IJJw3WFZbychDfUbdDiX0Gii4xINis725ZnZmf")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
