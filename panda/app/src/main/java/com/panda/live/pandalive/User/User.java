package com.panda.live.pandalive.User;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by levan on 15/03/2018.
 */

public class User {

    public String id;
    public String pwd;
    private DatabaseReference mDatabase;
// ...




    public User() {
        // ...
    }
    public User(String id, String pwd) {
        this.id = id;
        this.id = id;
    }
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }


}

