package vanroid.com.gdufassistant20.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import utils.Constants;
import utils.Download;
import utils.MyApplication;
import utils.SqliteDB;
import vanroid.com.gdufassistant20.R;

public class MailContentActivity extends Activity implements View.OnClickListener {

    private AsyncHttpClient httpClient;

    private WebView wv_content;
    private TextView tv_sender;
    private TextView tv_title;
    private TextView tv_date;


    //右下角那个圆的按钮
    private ImageView point;
    private ImageView x;
    private ImageView scale_big;
    private boolean isOpen;

    //标题栏上得返回按钮
    private LinearLayout ll_back;
    //底部的圆形按钮
    private RelativeLayout rl_circle_button;
    //可下载文件的列表
    private int attachCount;
    private LinearLayout ll_file_list;
    private TextView[] filelist;
    private JSONArray attachArry;

    private NotificationManager manager;
    private final static int NOTIFICATION_ID = 0x009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_content);

        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/myDownload");
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }

        initView();
        initEvent();
        initDatas();

    }

    private void initView() {
        tv_sender = (TextView) findViewById(R.id.tv_content_sender);
        tv_title = (TextView) findViewById(R.id.tv_content_title);
        tv_date = (TextView) findViewById(R.id.tv_content_date);
        wv_content = (WebView) findViewById(R.id.wv_content);

        point = (ImageView) findViewById(R.id.img_point);
        x = (ImageView) findViewById(R.id.img_x);
        scale_big = (ImageView) findViewById(R.id.img_scale);

        rl_circle_button = (RelativeLayout) findViewById(R.id.rl_cirlce_button);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        ll_file_list = (LinearLayout) findViewById(R.id.ll_file_list);
        filelist = new TextView[3];
        filelist[0] = (TextView) findViewById(R.id.file_one);
        filelist[1] = (TextView) findViewById(R.id.file_two);
        filelist[2] = (TextView) findViewById(R.id.file_three);
    }

    //初始化事件监听
    private void initEvent() {
        ll_back.setOnClickListener(this);
        rl_circle_button.setOnClickListener(this);

        for (int i = 0 ; i<filelist.length;  i++ ){
            filelist[i].setOnClickListener(this);
        }

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void initDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String sender = bundle.getString("sender");
        String date = bundle.getString("date");
        String id = bundle.getString("id");

        tv_title.setText(title);
        tv_sender.setText(sender);
        tv_date.setText(date);

        httpClient = MyApplication.getHttpClient();
        httpClient.get(this, Constants.URL_READMAIL + "Id=" + id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("onSuccess", s);
                handleResult(s);
            }

            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
            }
        });

    }


    //解析获取到的内容
    private void handleResult(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (!jsonObject.getString("attaches").equals("null")) {
                attachArry = jsonObject.getJSONArray("attaches");
                Log.i("attachArray", " attachArry = " + attachArry.length());
                attachCount = attachArry.length();
                for (int i = 0; i < attachCount; i++) {
                    filelist[i].setText(getFileName(attachArry.getString(i)));
                }

            }


            String content = jsonObject.getString("content");
            wv_content.loadData(content, "text/html; charset=UTF-8", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //右下角按钮的动画
    private void startAnim() {
        float scale_start, scale_end;
        float rotation_start, rotation_end;

        if (!isOpen) {
            scale_start = 1.0f;
            scale_end = 0;
            rotation_start = 0;
            rotation_end = 180f;
            translateAnim(isOpen);
            isOpen = true;
        } else {
            scale_start = 0;
            scale_end = 1.0f;
            rotation_start = 180f;
            rotation_end = 0;
            translateAnim(isOpen);
            isOpen = false;
        }

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(point, "scaleX", scale_start, scale_end);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(point, "scaleY", scale_start, scale_end);

        x.setVisibility(View.VISIBLE);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(x, "scaleX", scale_end, scale_start);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(x, "scaleY", scale_end, scale_start);
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(x, "rotation", rotation_start, rotation_end);

        scale_big.setVisibility(View.VISIBLE);
        ObjectAnimator anim6 = ObjectAnimator.ofFloat(scale_big, "scaleX", 1.0f, 2.0f);
        ObjectAnimator anim7 = ObjectAnimator.ofFloat(scale_big, "scaleY", 1.0f, 2.0f);
        ObjectAnimator anim8 = ObjectAnimator.ofFloat(scale_big, "alpha", 1.0f, 0);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2);

        animSet.play(anim3).with(anim4);
        animSet.play(anim4).with(anim5);

        animSet.play(anim6).with(anim7);
        animSet.play(anim7).with(anim8);
        animSet.start();


    }

    //附件列表动画
    private void translateAnim(boolean status) {
        TranslateAnimation translate;
        AlphaAnimation alpha;
        for (int i = 0; i < attachCount; i++) {
            if (!status) {
                translate = new TranslateAnimation(750, 0, 0, 0);
                alpha = new AlphaAnimation(0,1.0f);
                filelist[i].setVisibility(View.VISIBLE);
            } else {
                translate = new TranslateAnimation(0, 750, 0, 0);
                alpha = new AlphaAnimation(1.0f,0);
                final int finalI = i;
                translate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        filelist[finalI].setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(alpha);
            set.addAnimation(translate);
            set.setDuration(300);
            filelist[i].startAnimation(set);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_cirlce_button:
                startAnim();
                break;
            case R.id.file_one:
                isOpen = false;
                filelist[0].startAnimation(scaleBigAnim(300));
                try {
                    Download d = new Download(001,attachArry.get(0).toString(), Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/myDownload");
                    d.setOnDownloadListener(new downloadListener());
                    d.start(false);
                    sendNotification();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.file_two:
                isOpen = false;
                filelist[1].startAnimation(scaleBigAnim(300));
                try {
                    Download d = new Download(002,attachArry.get(1).toString(), Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/myDownload");
                    d.setOnDownloadListener(new downloadListener());
                    d.start(false);
                    sendNotification();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.file_three:
                isOpen = false;
                filelist[2].startAnimation(scaleBigAnim(300));
                try {
                    Download d = new Download(003,attachArry.get(2).toString(), Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/myDownload");
                    d.setOnDownloadListener(new downloadListener());
                    d.start(false);
                    sendNotification();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //提取文件名
    public String getFileName(String url) {
        String name = "";

        name = url.substring(url.indexOf("Name=") + 5, url.indexOf("doc"));
        name += "...";
        return name;
    }


    //下载监听
    class downloadListener implements Download.OnDownloadListener {
        @Override
        public void onSuccess(int downloadId) {
            System.out.println(downloadId + "下载成功");
        }

        @Override
        public void onStart(int downloadId, long fileSize) {
            System.out.println(downloadId + "开始下载，文件大小：" + fileSize);
        }

        @Override
        public void onPublish(int downloadId, long size) {
            System.out.println("更新文件" + downloadId + "大小：" + size);
        }

        @Override
        public void onPause(int downloadId) {
            System.out.println("暂停下载" + downloadId);
        }

        @Override
        public void onGoon(int downloadId, long localSize) {
            System.out.println("继续下载" + downloadId);
        }

        @Override
        public void onError(int downloadId) {
            System.out.println("下载出错" + downloadId);
        }

        @Override
        public void onCancel(int downloadId) {
            System.out.println("取消下载" + downloadId);
        }
    }

    //点击下载附近时的动画
    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                for (int i = 0; i < attachCount; i++) {
                    filelist[i].setVisibility(View.GONE);
                    filelist[i].setClickable(false);
                }
            }
        });
        return animationSet;

    }

    private void sendNotification() {
        Intent intent = new Intent(MailContentActivity.this,
                FileBrowserActivity.class);
        PendingIntent pt = PendingIntent.getActivity(MailContentActivity.this,
                0, intent, 0);
        Notification.Builder builder = new Notification.Builder(MailContentActivity.this);
        builder.setSmallIcon(R.drawable.icon_u);
        builder.setTicker("下载完成");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("下载完成");
        builder.setContentText("文件保存在/myDownload/");
        builder.setContentIntent(pt);

        Notification notify = builder.getNotification();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;

        manager.notify(NOTIFICATION_ID, notify);
    }


}
