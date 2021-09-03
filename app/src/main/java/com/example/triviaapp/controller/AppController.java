package com.example.triviaapp.controller;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    private static AppController instance;
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static synchronized AppController getInstance(){
//        if(instance==null){
//            instance = new AppController();
//        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(queue == null){
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        return queue;
    }

//    private AppController(){
//        if(instance==null){
//            instance = new AppController();
//        }
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
    //  req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        if(tag.isEmpty()){
            req.setTag(TAG);

        } else {
            req.setTag(tag);

        }

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


}
