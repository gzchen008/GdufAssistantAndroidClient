package customizeView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Constants;
import utils.MyApplication;
import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/9/21.
 */
public class BindingDialog extends DialogFragment {

    private EditText et_stuId;
    private EditText et_mailboxPwd;
    private EditText et_libraryPwd;
    private EditText et_eduSysPwd;
    private TextView tv_binding;
    private LinearLayout tip;
    private LinearLayout wait;

    private AsyncHttpClient httpClient;

    public interface onBindingSucces{
        void onBindingComplete();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_binding,container);
        initView(view);
        initEvent();
        return view;
    }


    private void initEvent() {
        tv_binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stuId = et_stuId.getText().toString();
                final String mailboxPwd = et_mailboxPwd.getText().toString();
                final String libraryPwd = et_libraryPwd.getText().toString();
                final String eduSysPwd = et_eduSysPwd.getText().toString();

                if (!stuId.equals("")&&!mailboxPwd.equals("")&&!libraryPwd.equals("")&&!eduSysPwd.equals("")){

                //-----------------------------------
                    httpClient = MyApplication.getHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("stuId",stuId);
                    params.put("jwcPass",eduSysPwd);
                    params.put("xnMailPass",mailboxPwd);
                    params.put("libaryPass", libraryPwd);
                    httpClient.post(Constants.URL_BINDING,params,new AsyncHttpResponseHandler(){

                        @Override
                        public void onStart() {
                            super.onStart();
                            tip.setVisibility(View.GONE);
                            wait.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onSuccess(String s) {
                            super.onSuccess(s);
                            Log.i("bingding result",s);
                            try {
                                if (s.contains("错误")){
                                    JSONObject jsob = new JSONObject(s);
                                    Toast.makeText(getActivity(), jsob.getString("msg"), Toast.LENGTH_SHORT).show();
                                }else {
                                    onBindingSucces listener = (onBindingSucces) getActivity();
                                    listener.onBindingComplete();
                                    getDialog().cancel();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            super.onFailure(throwable);
                            Toast.makeText(getActivity(),"学校系统无法访问",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(getActivity(),"某个框不能空着哦~",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView(View view) {
        et_stuId = (EditText) view.findViewById(R.id.et_student_id);
        et_mailboxPwd = (EditText) view.findViewById(R.id.et_mailbox_password);
        et_eduSysPwd = (EditText) view.findViewById(R.id.et_educational_sys);
        et_libraryPwd = (EditText) view.findViewById(R.id.et_library_password);
        tip = (LinearLayout) view.findViewById(R.id.tip_text);
        wait = (LinearLayout) view.findViewById(R.id.pb_binding);

        tv_binding = (TextView) view.findViewById(R.id.btn_binding);
    }
}
