package net.doyouhike.app.wildbird.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-3-17.
 */
public class PermissionUtil {

    final public static int REQUEST_CODE_ASK_PERMISSIONS = 124;
    private static PermissionUtil instance = new PermissionUtil();

    public static PermissionUtil getInstance() {
        return instance;
    }

    /**
     * 存储相关权限
     *
     * @param activity
     * @return
     */
    public boolean checkStoragePermission(Activity activity) {
        return checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    public boolean checkStoragePermission(Context context) {
        return checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    public boolean onRequestStorageionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    /**
     * 定位相关权限
     *
     * @param activity
     * @return
     */
    public boolean checkLocationPermission(Activity activity) {
        return checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    public boolean onRequestLocationionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return onRequestPermissionResult(requestCode, permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION);
    }


    /**
     * 解析请求权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param requestPermission
     * @return
     */
    private boolean onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults, String requestPermission) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (permissions[0].equals(requestPermission) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPermission(Activity activity, String... permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity.checkSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }


        activity.requestPermissions(permission,
                REQUEST_CODE_ASK_PERMISSIONS);
        // Check for Rationale Option
//        if (activity.shouldShowRequestPermissionRationale(permission[0])){
//            activity.requestPermissions(permission,
//                    REQUEST_CODE_ASK_PERMISSIONS);
//            return false;
//        }
        return false;
    }
    public boolean checkPermission(Context context, String... permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.checkSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        // Check for Rationale Option
//        if (activity.shouldShowRequestPermissionRationale(permission[0])){
//            activity.requestPermissions(permission,
//                    REQUEST_CODE_ASK_PERMISSIONS);
//            return false;
//        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void insertDummyContactWrapper(final Activity activity) {

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
//        if (!addPermission(activity,permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
//            permissionsNeeded.add("定位");
        if (!addPermission(activity, permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("读写文件");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(activity, message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

    }


    private void showMessageOKCancel(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(Activity activity, List<String> permissionsList, String permission) {
        if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!activity.shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
}
