package cn.cbsd.dogtag.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {

    public static final int PERMISSION_REQUESTCODE = 102;

    private PermissionListener mListener;

    private PermissionHelper() {
    }

    private static PermissionHelper instance = null;


    public static PermissionHelper getInstance() {
        if (instance == null) {
            instance = new PermissionHelper();
        }
        return instance;
    }

    public void requestRunPermisssion(Activity activity, String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionLists = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionLists.add(permission);
            }
        }

        if (!permissionLists.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        } else {
            //表示全都授权了
            mListener.onGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0) {
            //存放没授权的权限
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                String permission = permissions[i];
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }
            if (deniedPermissions.isEmpty()) {
                //说明都授权了
                mListener.onGranted();
            } else {
                mListener.onDenied(deniedPermissions);
            }
        }

    }


    public interface PermissionListener {

        void onGranted();//已授权

        void onDenied(List<String> deniedPermission);//未授权

    }
}
