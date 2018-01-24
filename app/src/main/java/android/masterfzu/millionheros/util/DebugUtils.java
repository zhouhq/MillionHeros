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

        String question=("9.下列哪位歌手是男性？");
        String as1="张碧晨";
        String as2 ="张学友";
        String as3="邓紫棋";

       String re=String.format(result,question,as1,as2,as3);
        return re;

    }

    public QandA getDebugQue() {
        QandA qandA = new QandA();
        return qandA;
    }
}
