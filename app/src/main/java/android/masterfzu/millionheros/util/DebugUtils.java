package android.masterfzu.millionheros.util;

import android.masterfzu.millionheros.hint.QandA;

/**
 * Created by Administrator on 2018/1/24.
 */

public class DebugUtils {
    public static boolean isDebug = true;


   final static String result="{"+
            "\"log_id\": 2471272194,"+
            "\"words_result_num\": 4,"+
            "\"words_result\":"+
            "["+
            "{\"words\":\"%s\"},"+
            "{\"words\":\"%s\"},"+
            "{\"words\":\"%s\"},"+
            "{\"words\": \"%s\"}"+
            "]"+
            "}";
    public static String getOcrResult() {

        StringBuffer buffer=new StringBuffer();

        String question=("9.下列说法正确的是？");
        String as1="钨丝灯发光时不会发热";
        String as2 ="电流的单位是立方米";
        String as3="食用盐的主要是氯化钠";

       String re=String.format(result,question,as1,as2,as3);
        return re;

    }

    public QandA getDebugQue() {
        QandA qandA = new QandA();
        return qandA;
    }
}
