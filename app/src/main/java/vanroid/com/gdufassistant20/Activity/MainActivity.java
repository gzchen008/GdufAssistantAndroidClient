package vanroid.com.gdufassistant20.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import customizeView.BindingDialog;
import utils.Constants;
import utils.MyApplication;
import vanroid.com.gdufassistant20.R;


public class MainActivity extends FragmentActivity implements View.OnClickListener, BindingDialog.onBindingSucces {

    private AsyncHttpClient httpClient;
    private SharedPreferences sp;

    // 底部5个Tab
    private LinearLayout firstTab;
    private LinearLayout secondTab;
    private LinearLayout thirdTab;
    private LinearLayout fourthTab;
    private LinearLayout fiveTab;

    // 5个ImageButton用来改变选中时颜色
    private ImageButton img_one;
    private ImageButton img_two;
    private ImageButton img_three;
    private ImageButton img_four;
    private ImageButton img_five;

    // 5个Fragment，显示4页内容
    private Fragment mailFrg;
    private CourseFragment courseFrg;
    private Fragment functionFrg;
    private Fragment libraryFrg;
    private Fragment settingFrg;

    //标题栏文字
    private TextView title_tv;


    //记录返回键按下的时间
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Boolean isAuto = intent.getBooleanExtra(LoginActivity.AUTO_LOGIN_FLAG, false);
//        Boolean isAuto = false;
//        Bundle bundle = intent.getExtras();
//        if (bundle!=null){
//           isAuto = bundle.getBoolean("flag",false);
//        }
        Log.i("isAuto",isAuto+"....");
        //如果是自动登陆则在这里登陆
       if (isAuto){
           login();
       }

        initView();

        initEvent();

        selected(2);

    }

    private void initView() {

        firstTab = (LinearLayout) findViewById(R.id.tab_one);
        secondTab = (LinearLayout) findViewById(R.id.tab_two);
        thirdTab = (LinearLayout) findViewById(R.id.tab_three);
        fourthTab = (LinearLayout) findViewById(R.id.tab_four);
        fiveTab = (LinearLayout) findViewById(R.id.tab_five);

        img_one = (ImageButton) findViewById(R.id.tab_one_img);
        img_two = (ImageButton) findViewById(R.id.tab_two_img);
        img_three = (ImageButton) findViewById(R.id.tab_three_img);
        img_four = (ImageButton) findViewById(R.id.tab_four_img);
        img_five = (ImageButton) findViewById(R.id.tab_five_img);

        title_tv = (TextView) findViewById(R.id.main_title);

    }

    private void initEvent() {
        firstTab.setOnClickListener(this);
        secondTab.setOnClickListener(this);
        thirdTab.setOnClickListener(this);
        fourthTab.setOnClickListener(this);
        fiveTab.setOnClickListener(this);
    }

    //设置所有tab变暗
    private void reSetImg() {
        img_one.setImageResource(R.drawable.tab_mail);
        img_two.setImageResource(R.drawable.tab_course);
        img_three.setImageResource(R.drawable.tab_function);
        img_four.setImageResource(R.drawable.tab_library);
        img_five.setImageResource(R.drawable.tab_setting);
    }


    @Override
    public void onClick(View v) {
        //无论点击哪个按钮都先让所有图片变暗
        reSetImg();
        switch (v.getId()) {
            case R.id.tab_one:
                selected(0);
                break;
            case R.id.tab_two:
                selected(1);
                break;
            case R.id.tab_three:
                selected(2);
                break;
            case R.id.tab_four:
                selected(3);
                break;
            case R.id.tab_five:
                selected(4);
                break;
        }
    }

    //选择哪个tab的事件
    private void selected(int i) {
        // 获取fragment管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 使用faragmentManager开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 切换Tab之前先隐藏所有Fragment
        hideFragment(fragmentTransaction);

        reSetImg();
        switch (i) {
            case 0:
                if (mailFrg == null) {
                    mailFrg = new MailFragment();
                    fragmentTransaction.add(R.id.view_pager, mailFrg);
                } else {
                    fragmentTransaction.show(mailFrg);
                }
                img_one.setImageResource(R.drawable.tab_mail_pressed);
                break;
            case 1:
                if (courseFrg == null) {
                    courseFrg = new CourseFragment();
                    fragmentTransaction.add(R.id.view_pager, courseFrg);
                } else {
                    fragmentTransaction.show(courseFrg);
                }
                img_two.setImageResource(R.drawable.tab_course_pressed);
                break;
            case 2:
                if (functionFrg == null) {
                    functionFrg = new FunctionFragment();
                    fragmentTransaction.add(R.id.view_pager, functionFrg);
                } else {
                    fragmentTransaction.show(functionFrg);
                }
                img_three.setImageResource(R.drawable.tab_function_pressed);
                break;
            case 3:
                if (libraryFrg == null) {
                    libraryFrg = new LibraryFragment();
                    fragmentTransaction.add(R.id.view_pager, libraryFrg);
                } else {
                    fragmentTransaction.show(libraryFrg);
                }
                img_four.setImageResource(R.drawable.tab_library_pressed);
                break;
            case 4:
                if (settingFrg == null) {
                    settingFrg = new SettingFragment();
                    fragmentTransaction.add(R.id.view_pager, settingFrg);
                } else {
                    fragmentTransaction.show(settingFrg);
                }
                img_five.setImageResource(R.drawable.tab_setting_pressed);
                break;
        }
        fragmentTransaction.commit();
    }


    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mailFrg != null) {
            fragmentTransaction.hide(mailFrg);
        }
        if (courseFrg != null) {
            fragmentTransaction.hide(courseFrg);
        }
        if (libraryFrg != null) {
            fragmentTransaction.hide(libraryFrg);
        }
        if (settingFrg != null) {
            fragmentTransaction.hide(settingFrg);
        }
        if (functionFrg != null) {
            fragmentTransaction.hide(functionFrg);
        }
    }


    //重写返回键按钮事件，实现快速点击两次返回退出APP
    @Override
    public void onBackPressed() {
        if (lastClickTime <= 0) {
            Toast.makeText(this, "再按一次后退键退出应用", Toast.LENGTH_SHORT).show();
            lastClickTime = System.currentTimeMillis();
        } else {
            long currentClickTime = System.currentTimeMillis();
            if (currentClickTime - lastClickTime < 1000) {
                finish();
            } else {
                Toast.makeText(this, "再按一次后退键退出应用", Toast.LENGTH_SHORT).show();
                lastClickTime = currentClickTime;
            }
        }
    }

    @Override
    public void onBindingComplete() {
        Toast.makeText(this, "绑定成功，请刷新数据", Toast.LENGTH_SHORT).show();
    }

    //处理登陆事项
    private void login() {
        sp = getSharedPreferences("secret", this.MODE_PRIVATE);
        httpClient = MyApplication.getHttpClient();
        RequestParams params = new RequestParams();
        params.put("telphone",sp.getString("phone",""));
        params.put("Password",sp.getString("pwd",""));
        httpClient.post(Constants.URL_LOGIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("login Success!", "" + s.toString());
                if (s.toString().contains("成功")) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                } else if (s.toString().contains("0")) {
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
                Log.i("login Faild", "登陆失败");
                Toast.makeText(MainActivity.this, "网络不好，登陆失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
