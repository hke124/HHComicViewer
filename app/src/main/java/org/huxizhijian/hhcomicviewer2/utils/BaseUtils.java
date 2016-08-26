package org.huxizhijian.hhcomicviewer2.utils;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基础工具类
 * Created by wei on 2016/8/20.
 */
public class BaseUtils {

    public static int getwidthPixels(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getheightPixels(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
     */
    public final static int CMNET = 3;
    public final static int CMWAP = 2;
    public final static int WIFI = 1;
    public final static int NONEWTWORK = -1;

    public static int getAPNType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = CMNET;
            } else {
                netType = CMWAP;
            }
        }
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = WIFI;
        }
        return netType;
    }

    public static String getAPNType_String(int type) {
        String type_status = "网络异常";
        switch (type) {
            case NONEWTWORK:
                type_status = "没有网络 ";
                break;
            case WIFI:
                type_status = "WIFI";
                break;
            case CMWAP:
                type_status = "CMWAP";
                break;
            case CMNET:
                type_status = "CMNET";
                break;
            default:
                break;
        }
        return type_status;
    }

    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        // SimpleDateFormat df = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date(System.currentTimeMillis()));// new
        // Date()为获取当前系统时间
        return date;
    }

    public static void initActionBar(ActionBar actionBar, int newColor) {
        Drawable colorDrawable = new ColorDrawable(newColor);
        LayerDrawable ld = new LayerDrawable(new Drawable[]{colorDrawable});
        if (actionBar != null) {
//            System.out.println("action bar != null");
            actionBar.setBackgroundDrawable(ld);
        }
    }
}
