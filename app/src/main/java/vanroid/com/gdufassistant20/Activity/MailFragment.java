package vanroid.com.gdufassistant20.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import bean.MailInfo;
import customizeView.BindingDialog;
import Adapters.MailListAdapter;
import utils.Constants;
import utils.MailListGeter;
import utils.MyApplication;
import utils.SqliteDB;
import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/9/9.
 */
public class MailFragment extends Fragment  {

    private PullToRefreshListView lv_mail;
    private ProgressBar pb_in_maillist;
    private MailListAdapter<MailInfo> mAdapter;
    private List<MailInfo> mails = new ArrayList<>();
    private ImageView btn_fileManager;
    private SqliteDB db ;

    //邮箱当前页数
    private int cPage = 1;

    private AsyncHttpClient httpClient;

    private View view;

    private boolean isCreate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
            view = inflater.inflate(R.layout.fragment_mail, container, false);
            Log.i("TAG", "mailFragment onCreate");

        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SqliteDB(getActivity());

        if (!isCreate){
            initView();
            initEvent();
            initDatas();
            isCreate = true;
        }
    }

    private void initView() {
        lv_mail = (PullToRefreshListView) getActivity().findViewById(R.id.lv_maillist);
        lv_mail.setMode(PullToRefreshBase.Mode.BOTH);//设置上下拖动
        lv_mail.getLoadingLayoutProxy(false, true).setPullLabel("下拉刷新");
        lv_mail.getLoadingLayoutProxy(false, true).setReleaseLabel("让我刷新!");
        lv_mail.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新");

        pb_in_maillist = (ProgressBar) getActivity().findViewById(R.id.pb_in_maillist);

        btn_fileManager = (ImageView) getActivity().findViewById(R.id.btn_file_manager);
    }


    //初始化事件监听
    private void initEvent() {
        //上拉加载，下拉刷新事件
            lv_mail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    if (lv_mail.isHeaderShown()) {
                        cPage = 1;
                    } else if (lv_mail.isFooterShown()) {
                        cPage++;
                    }
                    httpClient = MyApplication.getHttpClient();
                    httpClient.get(getActivity(), Constants.URL_MAILLIST + "page=" + cPage, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String s) {
                            super.onSuccess(s);
                            Log.i("onRefresh",s);
                            if (cPage == 1) {
                                mails.clear();
                                mails.addAll(MailListGeter.getMaillist(s));
                                saveAche(s,db);
                            } else {
                                mails.addAll(MailListGeter.getMaillist(s));
                            }
//                            mAdapter = new MailListAdapter<MailInfo>(getActivity(), mails);
                            mAdapter.notifyDataSetChanged();
                            lv_mail.onRefreshComplete();
                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            super.onFailure(throwable, s);
                            Toast.makeText(getActivity(), "网络有问题~", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        //邮件列表点击事件
        lv_mail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MailInfo info = mails.get(position-1);
                Intent intent = new Intent(getActivity(),MailContentActivity.class);
                intent.putExtra("id", info.getId());
                intent.putExtra("title", info.getTitle());
                intent.putExtra("sender", info.getSender());
                intent.putExtra("date", info.getDate());
                startActivity(intent);
            }
        });

        //打开文件管理器
        btn_fileManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FileBrowserActivity.class));
            }
        });

    }

    //初始化邮件数据
    private void initDatas() {
        SQLiteDatabase dbReader = db.getReadableDatabase();
        final Cursor c = dbReader.query("maillist", new String[]{"mail"}, null, null, null, null, null);
        String ache = "";
        while (c.moveToNext()){
            ache = c.getString(c.getColumnIndex("mail"));
        }
        dbReader.close();
        if (ache!=null&&!ache.equals("")){
            getDatas(ache);
        }
        //如果没有缓存则从网上获取
        else {
        //-------------------------------------
//        MailInfo mMail = new MailInfo("1315451", "", "路人乙", "测试邮件", "2015-04-22", "30kb", true, 0, "欢迎大家关注广州货币金融博物馆官方微信账号！我们将定期为您推送货币金融...", "");
//        mails.add(mMail);
//        mMail = new MailInfo("1315451", "", "路人乙", "测试邮件", "2015-04-22", "30kb", true, 0, "欢迎大家关注广州货币金融博物馆官方微信账号！我们将定期为您推送货币金融...", "");
//        mails.add(mMail);
//        mMail = new MailInfo("1315451", "", "路人乙", "测试邮件", "2015-04-22", "30kb", true, 0, "欢迎大家关注广州货币金融博物馆官方微信账号！我们将定期为您推送货币金融...", "");
//        mails.add(mMail);
//         -------------------------------------
        httpClient = MyApplication.getHttpClient();
        httpClient.get(getActivity(), Constants.URL_MAILLIST + "page=" + cPage, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                pb_in_maillist.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("mailinfo", s);
                if (s.contains("未绑定")) {
                    BindingDialog dialog = new BindingDialog();
                    dialog.show(getFragmentManager(), "binding");
                } else {
                    getDatas(s);
                    saveAche(s,db);
                }
                pb_in_maillist.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
                pb_in_maillist.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "网络有问题~", Toast.LENGTH_SHORT).show();
            }
        });

        }

        mAdapter = new MailListAdapter<>(getActivity(), mails);
        lv_mail.setAdapter(mAdapter);


    }

    private void getDatas(String s) {
        mails = MailListGeter.getMaillist(s);
        mAdapter = new MailListAdapter<>(getActivity(),mails);
        lv_mail.setAdapter(mAdapter);
    }

    private void saveAche(String s,SqliteDB db){
        SQLiteDatabase dbWriter = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id",1);
        cv.put("mail", s);
        dbWriter.update("maillist", cv, "id=?", new String[]{"1"});
        dbWriter.close();
    }


}
