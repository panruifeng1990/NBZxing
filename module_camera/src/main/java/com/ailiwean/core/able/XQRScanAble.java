package com.ailiwean.core.able;

import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;

import com.ailiwean.core.Config;
import com.ailiwean.core.helper.ScanHelper;
import com.ailiwean.core.zxing.core.PlanarYUVLuminanceSource;
import com.ailiwean.core.zxing.core.Result;


/**
 * @Package: com.ailiwean.core.able
 * @ClassName: QRScanAble
 * @Description:
 * @Author: SWY
 * @CreateDate: 2020/4/23 10:18 AM
 */
public class XQRScanAble extends PixsValuesAble {

    protected Result result;

    XQRScanAble(Handler handler) {
        super(handler);
    }

    @Override
    protected void needParseDeploy(PlanarYUVLuminanceSource source, boolean isNative) {
        if (result != null && result.getText() != null)
            return;
        result = toLaunchParse(source.getHybridBinary());
        if (result != null && result.getText() != null && !"".equals(result.getText())) {
            sendMessage(Config.RT_LOCATION,
                    ScanHelper.rotatePoint(result.getResultPoints()));
            sendMessage(Config.SCAN_RESULT, covertResult(result));
        }
    }

    protected com.ailiwean.core.Result covertResult(Result result) {
        com.ailiwean.core.Result result_ = new com.ailiwean.core.Result();
        if (result != null) {
            try {
                result_.setText(result.getText());
                PointF[] pointFS = ScanHelper.rotatePoint(result.getResultPoints());
                result_.setQrPointF(ScanHelper.calCenterPointF(pointFS));
                result_.setQrLeng(ScanHelper.calQrLenghtShow(result.getResultPoints()));
                result_.setFormat(result.getBarcodeFormat());
                result_.setQrRotate(ScanHelper.calQrRotate(pointFS));
            }catch (Exception e){
                Log.w("XQRScanAble", "result: " + result, e);
                return result_;
            }
        }
        return result_;
    }
}
