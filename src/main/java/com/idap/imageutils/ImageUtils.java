package com.idap.imageutils;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;

/**
 * Created by eugene.kotsogub on 4/18/16.
 *
 */
public class ImageUtils {
    public static final String IMAGE_PATH_KEY = "image_path_key";

    @RequiresPermission(allOf = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    public static void takePhoto(Fragment fragment, int requestCode) {
        startActivityForResult(fragment, requestCode, "take_photo");
    }

    @RequiresPermission(allOf = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    public static void takePhoto(Activity activity, int requestCode) {
        startActivityForResult(activity, requestCode, "take_photo");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static void getImageFromGallery(Fragment fragment, int requestCode) {
        startActivityForResult(fragment, requestCode, "gallery");

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static void getImageFromGallery(Activity activity, int requestCode) {
        startActivityForResult(activity, requestCode, "gallery");
    }

    private static void startActivityForResult(Activity activity, int requestCode, String action) {
        Intent intent = new Intent(activity, ImageUtilsActivity.class);
        intent.putExtra("action", action);
        activity.startActivityForResult(intent, requestCode);
    }

    private static void startActivityForResult(Fragment fragment, int requestCode, String action) {
        Intent intent = new Intent(fragment.getActivity(), ImageUtilsActivity.class);
        intent.putExtra("action", action);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static String getImagePath(@NonNull Intent data){
        if (data.getExtras() == null){
            return null;
        }
        return data.getExtras().getString(ImageUtils.IMAGE_PATH_KEY);
    }

}
