package utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bean.MailInfo;

/**
 * Created by kami on 15/9/24.
 */
public class MailListGeter {

    public static List<MailInfo> getMaillist(String s) {
        List<MailInfo> mails = new ArrayList<>();

        try {
            JSONArray jsay = new JSONArray(s);
            MailInfo eachMail;
            JSONObject jsob;
            int length = jsay.length();
            for (int i = 0; i < length; i++) {
                jsob = (JSONObject) jsay.get(i);
                eachMail = new MailInfo(jsob.getString("id"), jsob.getString("link"), jsob.getString("sender"), jsob.getString("title"), jsob.getString("date"), jsob.getString("size"), jsob.getBoolean("isAttach"), jsob.getInt("readFlag"), jsob.getString("content"), jsob.getString("extraLinks"));
                mails.add(eachMail);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mails;
    }


}
