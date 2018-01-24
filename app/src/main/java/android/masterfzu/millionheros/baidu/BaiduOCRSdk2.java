package android.masterfzu.millionheros.baidu;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;

import java.io.File;

/**
 * OCR 通用识别
 */
public class BaiduOCRSdk2 {

    /**
     * 重要提示代码中所需工具类

     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("begin...");
        // 通用识别url
        // 本地图片路径
        String filePath = "g:\\testocr\\微信截图_20180114231559.png";
        try {

            System.out.println("spend:" + (System.currentTimeMillis() - start) / 1000.0 + "s");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static String result;

    public static interface ServiceResult {
         void OnResult(String re);
    }



    public static void doOCR(String filePath, final ServiceResult listen) {
            result = null;
            if (TextUtils.isEmpty(filePath)) {
                return;
            }
            // 本地图片路径
            try {

                OnResultListener<GeneralResult> listener1 = new OnResultListener<GeneralResult>() {
                    @Override
                    public void onResult(GeneralResult generalResult) {
                        Log.e("zhouhq", "OCR识别成功");
                        result = generalResult.getJsonRes();
                        listen.OnResult( result);
                    }

                    @Override
                    public void onError(OCRError ocrError) {
                        Log.e("zhouhq", "OCR识别失败：" + ocrError.getMessage());
                        listen.OnResult( result);
                    }
                };
                // 通用文字识别参数设置
                GeneralBasicParams param = new GeneralBasicParams();
                param.setDetectDirection(true);
                param.setImageFile(new File(filePath));

                OCR.getInstance().recognizeGeneralBasic(param, listener1);
            } catch (Exception e) {
                e.printStackTrace();
                listen.OnResult("");
            }

    }
}
