package android.masterfzu.millionheros.touch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.masterfzu.millionheros.TheApp;
import android.masterfzu.millionheros.baiduocr.AuthService;
import android.masterfzu.millionheros.preferences.SettingPreferences;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

/**
 * 获取截屏与悬浮窗权限
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int REQUEST_MEDIA_PROJECTION = 1;

    public static final String APP_ID = "10703133";
    public static final String API_KEY = "b7NfAl9GS9qp4e03sqlyiS6U";
    public static final String SECRET_KEY = "UHOfSyRPyUTIzGfcaUcgWrKrzmwr5B1R";
    private MediaProjectionManager mMediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMediaProjectionManager = (MediaProjectionManager) this.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //if (Settings.canDrawOverlays(MainActivity.this)) {
         startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        // } else {
        //若没有权限，提示获取.
        //    starToOverlayPermission();
        //    finish();
        // }

        String accessToke = SettingPreferences.getInstance(this).getAccessToken();
        long time = SettingPreferences.getInstance(this).getAccessTokenOverdue();
        if (TextUtils.isEmpty(accessToke) || time < System.currentTimeMillis()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    AuthService.getAuth(MainActivity.this);
                }
            }).start();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(MainActivity.this, "需要取得权限以截屏", Toast.LENGTH_SHORT).show();
                return;
            } else if (data != null && resultCode != 0) {
                Log.i(TAG, "user agree the application to capture screen");

                startTouchService(resultCode, data);

                Log.i(TAG, "start service Service1");

                finish();
            }
        }
    }


    private void startTouchService(int resultCode, Intent data) {
        ((TheApp) getApplication()).setResult(resultCode);
        ((TheApp) getApplication()).setIntent(data);

        Intent intent = new Intent(MainActivity.this, TouchService.class);
//        Toast.makeText(MainActivity.this, "已开启Toucher", Toast.LENGTH_SHORT).show();
        startService(intent);
    }

    private void starToOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        Toast.makeText(MainActivity.this, "需要取得权限以使用悬浮窗", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


}
