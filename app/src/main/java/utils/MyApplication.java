package utils;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by kami on 15/9/6.
 */
public class MyApplication extends Application{

    private static RequestQueue queue;
    private static String cookie;
    private static AsyncHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        httpClient = new AsyncHttpClient();
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue() {
        return queue;
    }

    public static String getCookie(){
        return cookie;
    }

    public static void setCookie(String c){
        cookie = c;
    }


    public static AsyncHttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(AsyncHttpClient httpClient) {
        MyApplication.httpClient = httpClient;
    }

}
