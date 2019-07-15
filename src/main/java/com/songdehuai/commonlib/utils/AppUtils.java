package com.songdehuai.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.songdehuai.commonlib.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

/**
 * 与app相关的辅助工具
 *
 * @author songdehuai
 */
public class AppUtils {




    /**
     * 获得View的截屏 Bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap getMagicDrawingCache(Context context, View view, boolean quick_cache) {
        Bitmap bitmap = (Bitmap) view.getTag(R.id.cacheBitmapKey);
        Boolean dirty = (Boolean) view.getTag(R.id.cacheBitmapDirtyKey);
        if (view.getWidth() + view.getHeight() == 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        if (bitmap == null || bitmap.getWidth() != viewWidth || bitmap.getHeight() != viewHeight) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            view.setTag(R.id.cacheBitmapKey, bitmap);
            dirty = true;
        }
        if (dirty == true || !quick_cache) {
            bitmap.eraseColor(context.getResources().getColor(android.R.color.transparent));
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            view.setTag(R.id.cacheBitmapDirtyKey, false);
        }
        return bitmap;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @return
     */
    public static void saveBitmap(Bitmap bitmap, String filename) {
        File filePic;
        String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        savePath = savePath + "/" + filename + ".png";
        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 获取app缓存目录
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/mCache";
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Uri转filePath
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 安装APK
     *
     * @param
     * @return
     * @throws
     * @date
     */
    public static void install(Context context, String packName, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = FileProvider.getUriForFile(context, packName + ".fileProvider", new File(filePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public static void telePhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Activity activity, String phoneNum) {
        new AlertDialog.Builder(activity).setMessage("是否拨打集速运客服电话")
                .setPositiveButton("呼叫", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phoneNum);
                    intent.setData(data);
                    activity.startActivity(intent);
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Activity activity, String type, String phoneNum) {
        new AlertDialog.Builder(activity).setMessage("是否拨打" + type + "电话")
                .setPositiveButton("呼叫", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phoneNum);
                    intent.setData(data);
                    activity.startActivity(intent);
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 拨打客服电话（跳转到拨号界面，用户手动点击拨打）
     */
    public static void callServicePhone(Activity activity) {
        new AlertDialog.Builder(activity).setMessage("是否拨打集速运客服电话")
                .setPositiveButton("呼叫", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + "0431-89162956");
                    intent.setData(data);
                    activity.startActivity(intent);
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }


    /**
     * 获取一段带颜色的文字
     *
     * @param text
     * @param color
     * @return
     */
    public static CharSequence getColorText(String text, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        //SpannableStringBuilder实现CharSequence接口
        style.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }


    /**
     * 获取一段设置大小的文字
     *
     * @param dp
     * @return
     */
    public static CharSequence getSizeText(String text, int dp) {
        SpannableString ss = new SpannableString(text);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(dp, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannedString(ss);
    }


}
