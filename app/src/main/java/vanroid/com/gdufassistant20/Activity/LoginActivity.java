package vanroid.com.gdufassistant20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Constants;
import utils.MyApplication;
import vanroid.com.gdufassistant20.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    public static final String AUTO_LOGIN_FLAG = "auto_login_flag";

    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private Button btn_regist;
    private LinearLayout btn_forgot;
    private ImageView icon_warning;
    private ProgressBar wating_login;

    private SharedPreferences sp;

    private boolean isAccountLegal = false;
    private boolean isPasswordLegal = false;

    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();

        autoLogin();

        initEvent();

    }

    private void autoLogin() {
        Boolean autoOrNot = sp.getBoolean("autoOrNot", false);
//        判断是否自动登陆
        if (autoOrNot){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(AUTO_LOGIN_FLAG,true);
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("flag",true);
//            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    //初始化界面
    private void initView() {
        sp = getSharedPreferences("secret", this.MODE_PRIVATE);

        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_regist = (Button) findViewById(R.id.btn_register);
        btn_forgot = (LinearLayout) findViewById(R.id.btn_forgot);
        icon_warning = (ImageView) findViewById(R.id.icon_warning);
        wating_login = (ProgressBar) findViewById(R.id.pb_login);


        //如果之前有输入过密码则记住并自动输入
        if (sp!=null){
            et_account.setText(sp.getString("phone",""));
            et_password.setText(sp.getString("pwd",""));
        }
        //如果密码不为空则设置密码验证合法
        if (!et_password.getText().toString().equals("")){
            isPasswordLegal = true;
        }

        //如果自动填充账号密码，则设置登陆按钮可用
        if (!et_account.getText().toString().equals("")&&!et_password.getText().toString().equals("")){
            btn_login.setText("");
            btn_login.setBackgroundResource(R.drawable.selector_login);
            btn_login.setClickable(true);
        }

        //注册完成后自动输入刚才注册的手机号，但是要输入密码加深记忆.
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null&&bundle.getString("account")!=null){
            et_account.setText(bundle.getString("account") + "");
            et_password.setText("");
        }

    }

    //初始事件监听
    private void initEvent() {
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
        btn_forgot.setOnClickListener(this);

        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_account.getText().toString().equals("")) {
                    if (et_account.getText().length() == 11) {
                        isAccountLegal = true;
                        icon_warning.setVisibility(View.GONE);
                        if (isAccountLegal && isPasswordLegal) {
                            btn_login.setText("");
                            btn_login.setBackgroundResource(R.drawable.selector_login);
                            btn_login.setClickable(true);
                        }
                    } else {
                        isAccountLegal = false;
                        icon_warning.setVisibility(View.VISIBLE);
                        btn_login.setText("输入合适的账号密码才能登陆");
                        btn_login.setBackgroundColor(0xffbcbcbc);
                        btn_login.setClickable(false);
                    }
                } else {
                    isAccountLegal = false;
                    icon_warning.setVisibility(View.VISIBLE);
                    btn_login.setText("输入合适的账号密码才能登陆");
                    btn_login.setBackgroundColor(0xffbcbcbc);
                    btn_login.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //动态监听账号密码的合法性
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_password.getText().toString().equals("")){
                    isPasswordLegal = false;
                    btn_login.setText("输入合适的账号密码才能登陆");
                    btn_login.setBackgroundColor(0xffbcbcbc);
                    btn_login.setClickable(false);
                }else {
                    isPasswordLegal = true;
                    if (isAccountLegal && isPasswordLegal){
                        btn_login.setText("");
                        btn_login.setBackgroundResource(R.drawable.selector_login);
                        btn_login.setClickable(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //登陆界面所有的按钮事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                    finish();
                break;
            case R.id.btn_forgot:

                break;
        }

    }

    //处理登陆事项
    private void login() {
        wating_login.setVisibility(View.VISIBLE);
        //-----------------------------
        httpClient = MyApplication.getHttpClient();
        RequestParams params = new RequestParams();
        params.put("telphone", et_account.getText().toString());
        params.put("Password", et_password.getText().toString());
        httpClient.post(Constants.URL_LOGIN, params ,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("login Success",""+s.toString());
                if (s.toString().contains("成功")){
                    setCache();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("isAuto",false));
                    finish();
                }else if (s.toString().contains("0")){
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    wating_login.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
                Log.i("login Faild","登陆失败");
                Toast.makeText(LoginActivity.this,"网络不好，登陆失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void saveCookie(Object response) {
//        try {
//            JSONObject jsob = new JSONObject(response.toString());
////            Log.i("save cookie = ", jsob.getString("cookie"));
//            MyApplication.setCookie(jsob.getString("cookie"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    //设置自动登陆
    private void setCache() {
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("autoOrNot",true);
        ed.putString("phone", et_account.getText().toString());
        ed.putString("pwd",et_password.getText().toString());
        ed.commit();
    }


}
