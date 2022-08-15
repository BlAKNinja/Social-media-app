package com.example.myapplication;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize( new Parse.Configuration.Builder(this)
        .applicationId("vn55UJzH67NyWk5czfoPlADinA8I1l2x60Uo4RPb")
         .clientKey("gt1hT0FJAwBJT2Ry1REqopuVyCId2fgMNUBERTcc")
                .server("https://parseapi.back4app.com/")
                .build()

        );
        ParseACL defaultacl=new ParseACL();
        defaultacl.setPublicReadAccess(true);
        defaultacl.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultacl,true);
    }
}
