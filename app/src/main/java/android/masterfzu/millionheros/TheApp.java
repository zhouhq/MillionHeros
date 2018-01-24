package android.masterfzu.millionheros;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zhengsiyuan on 2018/1/15.
 * 截屏权限需要保存
 */
public class TheApp extends Application {
    public static final String TheAPPTAG = "MHheros";
    public static void LogW(String s) {
        Log.e(TheAPPTAG, s);
    }

    private int result;
    private Intent intent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        app = this;
    }

    private static TheApp app;

    public static Application get() {
        return app;
    }
    public int getResult() {
        return result;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setResult(int result1) {
        this.result = result1;
    }

    public void setIntent(Intent intent1) {
        this.intent = intent1;
    }

}
