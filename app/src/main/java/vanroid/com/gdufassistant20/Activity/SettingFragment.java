package vanroid.com.gdufassistant20.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Constants;
import utils.MyApplication;
import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/9/9.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private Button btn_esc;
    private ImageView iv_gender;
    private TextView tv_name;
    private TextView tv_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_gender = (ImageView) view.findViewById(R.id.iv_gender);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_id = (TextView) view.findViewById(R.id.tv_id);

        btn_esc = (Button) view.findViewById(R.id.btn_esc);
        btn_esc.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
    }

    //初始化个人信息
    private void initDatas() {
        AsyncHttpClient httpClient = MyApplication.getHttpClient();
        httpClient.get(getActivity(), Constants.URL_USERINFO, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("info", s);
                handleResult(s);
            }

            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
                Toast.makeText(getActivity(), "网络有问题", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //解析返回的数据
    private void handleResult(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject jsob = jsonObject.getJSONObject("userInfo");
            String sex = jsob.getString("sex");
            String name = jsob.getString("realName");
            String stuId = jsob.getString("stuId");
            if (sex.equals("男")){
                iv_gender.setImageResource(R.drawable.icon_man);
            }else {
                iv_gender.setImageResource(R.drawable.icon_woman);
            }
            if (name!=null&&!name.equals("null")) tv_name.setText(name+"");
            if (stuId!=null)tv_id.setText(stuId+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_esc:
                SharedPreferences sp = getActivity().getSharedPreferences("secret", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("autoOrNot",false);
                ed.commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
                break;
        }
    }
}
