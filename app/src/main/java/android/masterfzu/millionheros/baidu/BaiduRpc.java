package android.masterfzu.millionheros.baidu;

import android.masterfzu.millionheros.TheApp;
import android.masterfzu.millionheros.preferences.SettingPreferences;
import android.masterfzu.millionheros.util.Base64Util;
import android.masterfzu.millionheros.util.Counter;
import android.masterfzu.millionheros.util.DebugUtils;
import android.masterfzu.millionheros.util.HttpUtil;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/1/24.
 *
 * 调用百度接口
 */

public class BaiduRpc {

    public static class Result {
        public String word;
        public String type;
    }

    private static final String bodyFormat=
    "{"+
        "\"text\": \"%s\""+
            "}";
    public static Result[] doRpc(String str) {


        Counter.letsgo(Counter.action_doRpc);
        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer";
        // 本地图片路径
        try {

            String json=String.format(bodyFormat,str);
            String params = json;

            String accessToken = SettingPreferences.getInstance(TheApp.get().getApplicationContext()).getAccessToken();
            if (TextUtils.isEmpty(accessToken)) {
                return null;
            }
            otherHost = otherHost+"?access_token="+accessToken;
            String resultStr = HttpUtil.postGeneralUrl(otherHost,"application/json",params,"GBK");
            System.out.println(resultStr);


            Counter.spendS(Counter.action_doRpc);

            return json2Item(resultStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static Result[] json2Item(String json)
    {
        Result result[]=null;
        try {
            JSONObject jsonObject=new JSONObject(json);
           JSONArray items= jsonObject.getJSONArray("items");
           result =new Result[items.length()];
           for(int i=0;i<items.length();i++)
           {
               JSONObject itmeJosn=items.getJSONObject(i);
               Result item=new Result();
               item.word=itmeJosn.optString("item");
               String ne=itmeJosn.optString("ne");
               if(!TextUtils.isEmpty(ne))
               {
                   item.type=ne;
               }else
               {
                   item.type=itmeJosn.optString("pos");
               }


               result[i]=item;
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return result;
    }

}
