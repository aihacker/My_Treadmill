package com.zc.Treadmill;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.zc.Treadmill.Service.WindowService;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        askForPermission();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent( event );
    }

    public void iniServie(){
        Intent intent = new Intent();
        intent.setClass(this, WindowService.class);
        startService(intent);
        this.finish();

    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                iniServie();
            }
        }else{
            iniServie();
        }
    }
    /**
     * 用户返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays( this )) {
                    Toast.makeText( this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( this, "权限授予成功！", Toast.LENGTH_SHORT ).show();
                    iniServie();
                }
            }

        }
    }
}
