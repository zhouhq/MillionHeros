package android.masterfzu.millionheros.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/1/19.
 */

public class SettingPreferences {
    SharedPreferences sp;
    private static SettingPreferences instance;

    public static SettingPreferences getInstance(Context context) {

        if (instance == null) {
            synchronized (SettingPreferences.class) {
                if (instance == null) {
                    instance = new SettingPreferences(context);
                }
            }
        }
        return instance;
    }

    private SettingPreferences(Context ctx) {
        sp = ctx.getSharedPreferences("hero", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    public void setYUPLine(int i) {
        sp.edit().putInt(PreferencesConstants.SETTINGS_UP_LINE, i).commit();
    }

    public void setYDownLine(int i) {
        sp.edit().putInt(PreferencesConstants.SETTINGS_DOWN_LINE, i).commit();
    }

    public int getYUPLine() {
        return sp.getInt(PreferencesConstants.SETTINGS_UP_LINE, -1);
    }

    public int getYDownLine() {
        return sp.getInt(PreferencesConstants.SETTINGS_DOWN_LINE, -1);
    }

    //保存token
    public void setAccessToken(String accessToken) {
        sp.edit().putString(PreferencesConstants.SETTINGS_ACCESS_TOKEN, accessToken).commit();
    }

    //获取token
    public String getAccessToken() {
        return sp.getString(PreferencesConstants.SETTINGS_ACCESS_TOKEN, "");
    }

    public void setAccessTokenOverdue(long i) {
        sp.edit().putLong(PreferencesConstants.SETTINGS_ACCESS_TOKEN_OVERDUE, i).commit();
    }

    public long getAccessTokenOverdue() {
        return sp.getLong(PreferencesConstants.SETTINGS_ACCESS_TOKEN_OVERDUE, -1);
    }
}
