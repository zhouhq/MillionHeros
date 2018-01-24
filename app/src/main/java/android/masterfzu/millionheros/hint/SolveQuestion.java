package android.masterfzu.millionheros.hint;

import android.masterfzu.millionheros.baidu.BaiduOCR;
import android.masterfzu.millionheros.util.Counter;
import android.masterfzu.millionheros.util.StringUtil;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by zhengsiyuan on 2018/1/16.
 * 识图-解析问题与答案-搜索-分析搜索结果
 */
public class SolveQuestion {

    public static void search(final byte [] img, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Counter.letsgo(Counter.action_Analysis);
                    makeMessage(handler, "开始识图……");

                    String s = BaiduOCR.doOCR(img);
                    if (StringUtil.isEmpty(s)) {
                        makeMessage(handler, "!!!!!!识图失败，马上重试!!!!!");
                        return;
                    }

                    QandA qa = QandA.format(s);

                    if (qa == null) {
                        makeMessage(handler, "!!!!!!无法识别问题，可以尝试重试!!!!!");
                        return;
                    }

                    makeMessage(handler, "识图成功，问题是：\n" + qa.getQuestion() + "\n 请等待提示……");
                    Counter.letsgo(Counter.action_search);
                    ResultSum rs =SearchAndAnalysis.searchResult(qa);
                    Counter.spendS(Counter.action_search);

                    Counter.letsgo(Counter.action_prettyOut);
                    String result = prettyOut(qa, rs);
                    Counter.spendS(Counter.action_prettyOut);

                    Log.w("search", result);
                    makeMessage(handler, result);
                    Counter.spendS(Counter.action_Analysis);
                } catch (IOException e) {
                    e.printStackTrace();
                    makeMessage(handler, "Something Error!!!");
                }
            }
        }).start();

    }

    private static void makeMessage(Handler handler, String s) {
        Message m = handler.obtainMessage();
        m.getData().putString("result", s);
        handler.sendMessage(m);
    }



    private final  static String html_f="<font color=\"#ff0000\">";
    private final  static String html_font ="</font>";
    private final  static String html_br="<br/>";
    static String prettyOut(QandA qa, ResultSum r) {
        StringBuffer rsb = new StringBuffer();
        long startTime = System.currentTimeMillis();

        boolean allzero = true; //是否无精确匹配
        for (int i : r.sum) {
            if (i != 0) {
                allzero = false;
                break;
            }
        }

        int index = getBigone(r.sum);
        int max = getBigNum(r.sum);
        if (!allzero) {
            for (int i = 0; i < r.sum.length; i++) {
                if (r.sum[i] <= 0) {
                    rsb.append("错误：" + qa.getAns()[i]);
                    rsb.append("<br/>");
                    continue;
                }

                System.out.println("命中：" + qa.getAns()[i] + ":" + r.sum[i] + (index == i ? " ,  最多！" : "") + "\t 总和：" + r.allsum[i]);
                if (r.sum[i] == max) {
                    rsb.append(html_f+"正确：" + qa.getAns()[i] + " : " + r.sum[i]+ html_font);
                } else {
                    rsb.append("错误：" + qa.getAns()[i] + " : " + r.sum[i]);
                }
                rsb.append(html_br);
            }

            return rsb.toString();
        }
        return "无答案";
    }

    private static int getBigone(int[] allsum) {
        int index = 0;
        int a = allsum[0];
        for (int i = 1; i < allsum.length; i++) {
            if (allsum[i] == a)
                return -1;

            if (allsum[i] > a) {
                a = allsum[i];
                index = i;
            }
        }

        return index;
    }

    /**得到最大结果数*/
    private static int getBigNum(int[] allsum) {
        int max = allsum[0];
        for (int i = 1; i < allsum.length; i++) {
            if (allsum[i] > max) {
                max = allsum[i];
            }
        }
        return max;
    }


    /**
     * 保存分析结果
     */
    static class ResultSum {
        public String path; //搜索路径
        public int sum[]; //每个答案命中次数
        public int dumpsum[][]; //每个答案中的文字出现的次数
        public int allsum[]; //单字出现次数总和

        ResultSum(QandA qa) {
            sum = new int[qa.getAns().length];
            dumpsum = new int[qa.getAns().length][];
            allsum = new int[qa.getAns().length];

            for (int i = 0; i < qa.getAns().length; i++) {
                dumpsum[i] = new int[qa.getAns()[i].length()];
            }
        }

        @Override
        public String toString() {
            return "ResultSum{" +
                    "path='" + path + '\'' +
                    ", sum=" + Arrays.toString(sum) +
                    ", dumpsum=" + Arrays.toString(dumpsum) +
                    ", allsum=" + Arrays.toString(allsum) +
                    '}';
        }
    }
}
