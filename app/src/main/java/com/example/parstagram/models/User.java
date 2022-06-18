package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_PROFILE_PIC = "profilePic";

    public ParseFile getProfilePic() {
        return getParseFile(KEY_PROFILE_PIC);
    }

    public void setProfilePic(ParseFile newProfilePic) {
        put(KEY_PROFILE_PIC, newProfilePic);
    }
}
