package vanroid.com.gdufassistant20.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.BooklistAdapter;
import bean.BookInfo;
import utils.Constants;
import utils.MyApplication;
import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/9/9.
 */
public class LibraryFragment extends Fragment implements View.OnClickListener {

    private AsyncHttpClient httpClient;

    private ListView lv_booklist;
    private Button btn_search;
    private EditText et_keyword;
    private Button btn_last;
    private Button btn_next;

    private List<BookInfo> books = new ArrayList<>();
    private BooklistAdapter mAdapter;
    private String keyword = "";

    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        initView(view);
        initEvent();
        return view;
    }


    private void initView(View view) {
        lv_booklist = (ListView) view.findViewById(R.id.lv_booklist);
        btn_search = (Button) view.findViewById(R.id.btn_search);
        et_keyword = (EditText) view.findViewById(R.id.et_bookname);
        btn_last = (Button) view.findViewById(R.id.btn_bookinfo_last);
        btn_next = (Button) view.findViewById(R.id.btn_bookinfo_next);
    }

    private void initEvent() {
        btn_search.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_last.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        initDatas();

    }

    private void initDatas(String keyword,int page) {
        httpClient = MyApplication.getHttpClient();
        httpClient.get(getActivity(), Constants.URL_LIBRARY + "Keywords="+keyword+"&"+"page="+page, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("bookinfo", s);
                handleResult(s);
            }

            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
            }

        });

    }

    //解析返回的数据
    private void handleResult(String s) {
        try {
            books.clear();
            JSONObject jsob = new JSONObject(s);
            JSONArray jsay = jsob.getJSONArray("books");
            BookInfo b;
            JSONObject jsonObject;
            for (int i = 0, j = jsay.length(); i < j; i++) {
                jsonObject = jsay.getJSONObject(i);
                b = new BookInfo(jsonObject.getInt("bookId"),jsonObject.getString("title"), jsonObject.getString("author"), jsonObject.getString("press"), jsonObject.getInt("borrowableCount"), jsonObject.getString("href"));
                books.add(b);
            }
            mAdapter = new BooklistAdapter(getActivity(),books);
            lv_booklist.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bookinfo_last:
                if (page>1)page--;
                break;
            case R.id.btn_bookinfo_next:
                page++;
                break;
            case R.id.btn_search:
                keyword = et_keyword.getText().toString();
                page=1;
                Log.i("flag","run!!!!!search");
                break;
        }
        if (keyword != null &&!keyword.equals("")){
            initDatas(keyword,page);
        }
    }
}
