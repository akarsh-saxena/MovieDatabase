package com.application.akarsh.moviedatabase;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Akarsh on 21-07-2017.
 */

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue requestQueue;
    private Context context;

    public VolleySingleton(Context context){

        this.context = context;
        requestQueue = getRequestQueue();

    }

    private RequestQueue getRequestQueue() {

        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        return requestQueue;

    }

    public static synchronized VolleySingleton getInstance(Context context){

        if(mInstance == null)
            mInstance = new VolleySingleton(context);

        return mInstance;
    }

    public<T> void addToRequestQueue(Request<T> request){

        requestQueue.add(request);

    }

}
