package vanroid.com.gdufassistant20.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bean.EachCourse;
import customizeView.TextViewForLesson;
import utils.Constants;
import utils.MyApplication;
import utils.SqliteDB;
import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/9/9.
 */
public class CourseFragment extends Fragment {

    private AsyncHttpClient httpClient;
    private SqliteDB db;

    private static int height = 0;

    private String curTerm = "year=2014-2015&xq=2";
    private String courseJosn = "";

    // 课程列表
    private List<EachCourse> mCourse = new ArrayList<EachCourse>();

    // 7天，一天一个RelativeLayout
    private RelativeLayout[] aWeek = new RelativeLayout[7];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
    }

    private void initView() {
        aWeek[0] = (RelativeLayout) getActivity().findViewById(R.id.course_mon);
        aWeek[1] = (RelativeLayout) getActivity().findViewById(R.id.course_tue);
        aWeek[2] = (RelativeLayout) getActivity().findViewById(R.id.course_wed);
        aWeek[3] = (RelativeLayout) getActivity().findViewById(R.id.course_thu);
        aWeek[4] = (RelativeLayout) getActivity().findViewById(R.id.course_fri);
        aWeek[5] = (RelativeLayout) getActivity().findViewById(R.id.course_sat);
        aWeek[6] = (RelativeLayout) getActivity().findViewById(R.id.course_sun);

        aWeek[0].post(new Runnable() {
            @Override
            public void run() {
                height = aWeek[0].getHeight();
//                System.out.println("=======" + height);
//                TextViewForLesson lesson = new TextViewForLesson(getActivity());
//                EachCourse c = new EachCourse("大学英语", "7-501", 0, 3, 1, 0);
//                addCourse(lesson, c, aWeek[1]);
//                c = new EachCourse("经济法", "7-501", 0, 2, 0, 1);
//                lesson = new TextViewForLesson(getActivity());
//                addCourse(lesson, c, aWeek[2]);
                getDatas(curTerm);
            }
        });

    }

    private void initEvent() {

    }

    // 设置每节课的位置
    private void addCourse(TextViewForLesson tv, EachCourse c,
                           final RelativeLayout parent) {


        height = parent.getHeight();

//        System.out.println("-------"+height/12);

        tv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height / 12 * c
                .getHowlong()));
        tv.setY((c.getWhen() - 1) * height / 12);
        tv.setText(c.getTitle() + "@" + c.getClassroom());
        parent.addView(tv);
    }

    //获取数据,这个方法的执行应该在布局的确定之后
    private void getDatas(String term) {
        db = new SqliteDB(getActivity());
        SQLiteDatabase dbReader = db.getReadableDatabase();
        Cursor c = dbReader.query("course", new String[]{"term", "json"}, null, null, null, null, null);
        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex("term")).equals(curTerm)) {
                courseJosn = c.getString(c.getColumnIndex("json"));
                handleResult(courseJosn);
            }
        }
        if (courseJosn.equals("")) {
            httpClient = MyApplication.getHttpClient();
            httpClient.get(getActivity(), Constants.URL_COURSE + term, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    Log.i("tag", "getCourse = " + s);
                    handleResult(s);
                    saveAche(s,db);
                }
                @Override
                public void onFailure(Throwable throwable) {
                    super.onFailure(throwable);
                }
            });
        }

    }

    //解析json数据得到课表
    private void handleResult(String s) {
        try {
            JSONObject jsob_datas = new JSONObject(s);
            JSONObject jsob_course = jsob_datas.getJSONObject("datas");
            JSONArray jsonArray = jsob_course.getJSONArray("classes");
            Log.i("tag", "jsay length = " + jsonArray.length());
            TextViewForLesson lesson;
            EachCourse c;
            JSONObject jsob;
            for (int i = 0, p = jsonArray.length(); i < p; i++) {
                lesson = new TextViewForLesson(getActivity());
                jsob = jsonArray.getJSONObject(i);
                c = new EachCourse(jsob.getString("title"), jsob.getString("classroom"), jsob.getInt("cwhen"), jsob.getInt("howlong"), jsob.getInt("whichday"), jsob.getInt("whichweek"));
                addCourse(lesson, c, aWeek[c.getWhichday() - 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //跟据传入学期重置课表,用于被MainActivity调用
    public void resetTerm(String term) {
        //重置之前先清空当前课程
        for (RelativeLayout r : aWeek) {
            r.removeAllViews();
        }
        getDatas(term);
    }

    private void saveAche(String s,SqliteDB db){
        SQLiteDatabase dbWriter = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("term",curTerm);
        cv.put("json",s);
        dbWriter.update("course", cv, "term=?", new String[]{curTerm});
        dbWriter.close();
    }

}
