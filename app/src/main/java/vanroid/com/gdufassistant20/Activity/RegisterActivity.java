package vanroid.com.gdufassistant20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import utils.Constants;
import utils.MyApplication;
import vanroid.com.gdufassistant20.R;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private ImageView btn_back;
    private TextView btn_back_also;
    private Button btn_register;
    private EditText et_account;
    private EditText et_passsword_one;
    private EditText et_passsword_two;
    private TextView tv_tip;
    private ImageView icon_warning;
    private ProgressBar wating_register;


    private boolean isAccountLegal = false;
    private boolean isPasswordLegal = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initView();

        initEvent();

        btn_register.setClickable(false);

    }

    private void initView() {
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back_also = (TextView) findViewById(R.id.btn_back_also);
        btn_register = (Button) findViewById(R.id.btn_register_r);
        et_account = (EditText) findViewById(R.id.telphone);
        et_passsword_one = (EditText) findViewById(R.id.et_password_one);
        et_passsword_two = (EditText) findViewById(R.id.et_password_two);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        icon_warning = (ImageView) findViewById(R.id.icon_warning);
        wating_register = (ProgressBar) findViewById(R.id.pb_register);
    }

    //初始化事件监听
    private void initEvent() {
        btn_back.setOnClickListener(this);
        btn_back_also.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        //监听账号输入是否非空合法
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
                            btn_register.setBackgroundResource(R.drawable.selector_register);
                            btn_register.setClickable(true);
                        }
                    } else {
                        isAccountLegal = false;
                        icon_warning.setVisibility(View.VISIBLE);
                        btn_register.setBackgroundResource(R.drawable.btn_resigister_no);
                        btn_register.setClickable(false);
                    }
                } else {
                    isAccountLegal = false;
                    icon_warning.setVisibility(View.VISIBLE);
                    btn_register.setBackgroundResource(R.drawable.btn_resigister_no);
                    btn_register.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //监听密码是否非空合法
        et_passsword_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_passsword_one.getText().toString().equals("") && et_passsword_one.getText().toString().equals(et_passsword_two.getText().toString())) {
                    isPasswordLegal = true;
                    if (isAccountLegal && isPasswordLegal) {
                        btn_register.setBackgroundResource(R.drawable.selector_register);
                        btn_register.setClickable(true);
                        tv_tip.setVisibility(View.GONE);
                    }
                } else {
                    isPasswordLegal = false;
                    btn_register.setBackgroundResource(R.drawable.btn_resigister_no);
                    btn_register.setClickable(false);
                    tv_tip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_passsword_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_passsword_two.getText().toString().equals("") && et_passsword_one.getText().toString().equals(et_passsword_two.getText().toString())) {
                    isPasswordLegal = true;
                    if (isAccountLegal && isPasswordLegal) {
                        btn_register.setBackgroundResource(R.drawable.selector_register);
                        btn_register.setClickable(true);
                        tv_tip.setVisibility(View.GONE);
                    }
                } else {
                    isPasswordLegal = false;
                    btn_register.setBackgroundResource(R.drawable.btn_resigister_no);
                    btn_register.setClickable(false);
                    tv_tip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_also:
            case R.id.btn_back:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_register_r:
                register();
                break;
        }
    }

    //注册账号的实现逻辑
    private void register() {
        wating_register.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("orginal", "" + s);
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("account", et_account.getText().toString());
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("flag", "获取内容失败");
                Toast.makeText(RegisterActivity.this, "网络不好，注册失败", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> postParams = new HashMap<>();
                String telphone = et_account.getText().toString();
                String password_one = et_passsword_one.getText().toString();
                String password_two = et_passsword_two.getText().toString();
                postParams.put("telphone", telphone);
                postParams.put("Password", password_one);
                postParams.put("comfirmPassword", password_two);
                Log.i("----->telphone", "" + postParams.get("telphone"));
                return postParams;
            }
        };
        request.setTag("register");
        MyApplication.getRequestQueue().add(request);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
