package android.masterfzu.millionheros.util;

import android.masterfzu.millionheros.TheApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengsiyuan on 2018/1/15.
 * 耗时记录器
 */
public class Counter {
    private static Map<String, Long> counter = new HashMap<String, Long>();
    public static final String action_startCapture="startCapture";
    public static final String action_doOCR="doOCR";
    public static final String action_doRpc="doRpc";
    public static final String action_Analysis="analysis";
    public static final String action_search="search";
    public static final String action_prettyOut="prettyOut";


    public static void letsgo(String go) {
        counter.put(go, System.currentTimeMillis());
    }

    public static float spendS(String now) {
        if (counter.get(now) == null)
            return -1;

        long begin = counter.get(now);

        float time=(System.currentTimeMillis() - begin) / 1000.0f;
        TheApp.LogW(now+"spend time:"+time+"秒");
        return time;
    }
}
