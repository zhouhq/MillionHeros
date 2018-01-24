package android.masterfzu.millionheros.hint;

import android.masterfzu.millionheros.TheApp;
import android.masterfzu.millionheros.baidu.BaiduRpc;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/24.
 */

public class SearchAndAnalysis {
    static SolveQuestion.ResultSum searchResult(QandA qa) throws IOException {


        SolveQuestion.ResultSum rs = searchAnswer(qa);

        TheApp.LogW(rs.toString());
        return rs;
    }

    @NonNull
    private static SolveQuestion.ResultSum searchQuestion(QandA qa) throws IOException {
        SolveQuestion.ResultSum rs = new SolveQuestion.ResultSum(qa);
        String path = "http://m.baidu.com/s?from=100925f&word=" + URLEncoder.encode(qa.getSearchKey(), "UTF-8");
        rs.path = path;
        String line = "";
        URL url = new URL(path);
        BufferedReader breaded = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer sb = new StringBuffer();
        while ((line = breaded.readLine()) != null) {
            sb.append(getHanZi(line));
        }

        for (int i = 0; i < rs.sum.length; i++) {
            rs.sum[i] += getCount(sb, qa.getAns()[i]);

            for (int y = 0; y < qa.getAns()[i].length(); y++) {
                int count = getCount(sb, qa.getAns()[i].substring(y, y + 1));
                if (count <= 0)
                    continue;

                rs.dumpsum[i][y] += count;
                rs.allsum[i] += count;
            }
        }
        return rs;
    }


    /***用其中一个答案去匹配*/
    private static SolveQuestion.ResultSum searchAnswer(QandA qa) throws IOException {
        SolveQuestion.ResultSum rs = new SolveQuestion.ResultSum(qa);
        String ans[] = qa.getAns();
        for (int i = 0; i < ans.length; i++) {
            searchOneAnswer(ans[i], qa.getQuestionRpc(), rs, i);
        }
        return rs;
    }

    /***用其中一个答案去匹配*/
    private static void searchOneAnswer(String answer, BaiduRpc.Result questionRpc, SolveQuestion.ResultSum rs, int index) throws IOException {

        String path = "http://m.baidu.com/s?from=100925f&word=" + URLEncoder.encode(answer, "UTF-8");
        String line = "";
        URL url = new URL(path);
        BufferedReader breaded = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer sb = new StringBuffer();
        while ((line = breaded.readLine()) != null) {
            sb.append(getHanZi(line));
        }
        for (int i = 0; i < questionRpc.size; i++) {
            if (!questionRpc.isNeedMatch(i)) {
                continue;
            }
            rs.sum[index] += getCount(sb, questionRpc.getWord(i));
        }
    }


    /**
     * 获取汉字
     */
    private static String getHanZi(String s) {
        StringBuffer r = new StringBuffer();
        Pattern pattern = Pattern.compile("[^\\x00-\\xff]");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            r.append(matcher.group());
        }

        return r.toString();
    }

    private static int getCount(StringBuffer sb, String des) {
        des = des.replaceAll("\\\\?", ""); //防止出错
        Pattern pattern = Pattern.compile(des);
        Matcher matcher = pattern.matcher(sb);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
