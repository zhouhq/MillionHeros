package android.masterfzu.millionheros.hint;

import android.masterfzu.millionheros.TheApp;
import android.masterfzu.millionheros.baidu.BaiduOCR;
import android.masterfzu.millionheros.baidu.BaiduRpc;
import android.masterfzu.millionheros.util.StringUtil;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by zhengsiyuan on 2018/1/16.
 * 问题与答案
 */
public class QandA {
    private String question;
    private BaiduRpc.Result questionRpc;
    private String[] ans = new String[3];

    /**
     * 是否是排除的问题，
     * 如，不属于，不是，不在等等
     */
    boolean isExclude = false;
    /**
     * 是否是反相搜索，即立用答案来搜索，匹配题目
     */

    boolean reverse = true;

    @Override
    public String toString() {
        return "QandA{" +
                "question='" + question + '\'' +
                ", ans=" + Arrays.toString(ans) +
                '}';
    }

    public static QandA format(String j) {
        if (StringUtil.isEmpty(j))
            return null;

        QandA result = new QandA();

        try {
            JSONObject json = new JSONObject(j);
            if (json.getInt("words_result_num") <= 3) //问题加答案至少大于3
                return null;

            JSONArray ja = json.getJSONArray("words_result");
            int num = ja.length() - 1;
            int a = 2;
            for (; num >= ja.length() - 3; num--) {
                result.ans[a--] = pureA(ja.getJSONObject(num).getString("words"));
            }

            StringBuffer qsb = new StringBuffer();
            for (int i = 0; i <= num; i++)
                qsb.append(ja.getJSONObject(i).getString("words"));

            result.question = pureQ(qsb.toString());
            BaiduRpc.Result r = BaiduRpc.doRpc(result.question);
            result.handleRpcResult(r);

            TheApp.LogW("rpc=" + r);
            TheApp.LogW(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    private static String pureQ(String s) {
        String result = s;

        if ((isNum(s.charAt(0)) && s.charAt(1) == '.'))
            result = s.substring(2);
        else if (isNum(s.charAt(1)) && s.charAt(2) == '.')
            result = s.substring(3);


//        if (s.indexOf("?") == -1)
//            return s;
//        result = result.replaceAll("\\?", "");
        return result;
    }

    private static boolean isNum(char c) {
        if (c >= '0' && c <= '9')
            return true;

        return false;
    }

    private static String pureA(String s) {
        if (s.indexOf("《") == -1 && s.indexOf("》") == -1)
            return s;

        String result = s.replaceAll("《", "").replaceAll("》", "");
        return result;
    }

    public static void main(String[] args) {
        String result = "{\"log_id\": 8816071367637002938, \"words_result_num\": 5, \"words_result\": [{\"words\": \"《火星情报局》第三季主题\"}, {\"words\": \"曲是?\"}, {\"words\": \"《再见18岁》\"}, {\"words\": \"《火星人来过》\"}, {\"words\": \"《火星情报局》\"}]}";
        QandA.format(result);
    }

    public String getQuestion() {
        return question;
    }

    public BaiduRpc.Result getQuestionRpc() {
        return questionRpc;
    }

    /**
     * 获取搜索的关键字
     */
    public String getSearchKey() {
        return question;
    }

    public boolean answerRelateQuestion() {
        return false;
    }

    public String[] getAns() {
        return ans;
    }

    private void handleRpcResult(BaiduRpc.Result result) {
        questionRpc = result;
        for (int i = 0; i < result.size; i++) {


            if ("不是".equals(result.getWord(i)) && "v".equals(result.getType(i))) {
                //含有“不”并是的动词
                isExclude = true;
            } else if ("不".equals(result.getWord(i)) && "d".equals(result.getType(i))) {
                //含有“不”并且是副词
                isExclude = true;
            }

        }
    }
}
