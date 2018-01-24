package android.masterfzu.millionheros.baidu;

import android.masterfzu.millionheros.TheApp;
import android.masterfzu.millionheros.preferences.SettingPreferences;
import android.masterfzu.millionheros.util.Counter;
import android.masterfzu.millionheros.util.HttpUtil;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/1/24.
 * <p>
 * 调用百度接口
 */

public class BaiduRpc {

    public static class Result {
        public int size;

        Result(int size) {
            word = new String[size];
            type = new String[size];
            this.size = size;
        }

        private String word[];
        private String type[];

        public String getWord(int index) {
            return word[index];
        }

        public String getType(int index) {
            return type[index];
        }

        public void putWord(int index, String s) {
            word[index] = s;
        }

        public void putType(int index, String t) {
            type[index] = t;
        }

        public void add(int index, String s, String t) {
            word[index] = s;
            type[index] = t;
        }

        /**
         * 这个词是需要用来匹配
         */
        public boolean isNeedMatch(int index) {

            return isNounAfterVerb(index);
            // return isNounBeforVerb(index)||isNounAfterVerb(index);
        }

        /**
         * 是否是动词或者
         */
        private boolean isNounBeforVerb(int index) {
            //最后一个名词
            if (index >= (size - 1)) return false;
            if (isNoun(index) && type[index + 1].equals("v")) {
                return true;
            }
            return false;
        }

        /**
         * 是否是动词或者
         */
        private boolean isNounAfterVerb(int index) {
            //最后一个名词
            if (index <1) return false;
            if (isNoun(index) && type[index - 1].equals("v")) {
                return true;
            }
            return false;
        }

        private boolean isNoun(int index) {
            if (type[index].startsWith("n")) return true;
            return false;
        }
    }

    private static final String bodyFormat =
            "{" +
                    "\"text\": \"%s\"" +
                    "}";

    public static Result doRpc(String str) {


        Counter.letsgo(Counter.action_doRpc);
        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer";
        // 本地图片路径
        try {

            String json = String.format(bodyFormat, str);
            String params = json;

            String accessToken = SettingPreferences.getInstance(TheApp.get().getApplicationContext()).getAccessToken();
            if (TextUtils.isEmpty(accessToken)) {
                return null;
            }
            otherHost = otherHost + "?access_token=" + accessToken;
            String resultStr = HttpUtil.postGeneralUrl(otherHost, "application/json", params, "GBK");
            System.out.println(resultStr);


            Counter.spendS(Counter.action_doRpc);

            return json2Item(resultStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Result json2Item(String json) {
        Result result = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray items = jsonObject.getJSONArray("items");
            result = new Result(items.length());
            for (int i = 0; i < items.length(); i++) {
                JSONObject itmeJosn = items.getJSONObject(i);


                result.putWord(i, itmeJosn.optString("item"));
                String ne = itmeJosn.optString("ne");
                if (!TextUtils.isEmpty(ne)) {
                    result.putType(i, ne);
                } else {
                    result.putType(i, itmeJosn.optString("pos"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
