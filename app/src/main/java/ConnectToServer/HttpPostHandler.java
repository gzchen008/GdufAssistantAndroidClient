package ConnectToServer;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import utils.MyApplication;

/**
 * Created by kami on 15/9/5.
 */
public abstract class HttpPostHandler {


    public void PostAndObtainResult(String url, final HashMap<String, String> params){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("orginal", "" + s);
                getResult(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("flag","获取内容失败");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.i("----->telphone", "" + params.get("telphone"));
                return params;
            }
        };
        request.setTag("login");
        MyApplication.getRequestQueue().add(request);
    }

    protected abstract void getResult(String s) ;

}
