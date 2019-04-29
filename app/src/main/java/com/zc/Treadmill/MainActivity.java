package com.zc.Treadmill;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.Treadmill.Service.WindowService;
import com.zc.Treadmill.util.SPUtils;

import java.security.Permission;

public class MainActivity extends Activity {
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    public static String PACKAGE_NAME="package";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        askForPermission();
        iniView();
        initSp();
        editText.setText( SPUtils.get( this,PACKAGE_NAME,"" ).toString() );
    }

    private void initSp() {
        if(  SPUtils.get( this,PACKAGE_NAME,"" )==null){
            //默认包名为网易云音乐
            SPUtils.put(this ,PACKAGE_NAME,"com.netease.cloudmusic");
        }
    }

    private void iniView() {
        String html="<p class=\\\"MsoNormal\\\"><br/></p><p class=\\\"MsoNormal\\\">一起听着喜爱的音乐<strong><span style=\\\"font-size:24px;color:#E53333;\\\">奔跑</span></strong>吧！</p>";
        TextView textView = findViewById( R.id.textView );
        editText = findViewById( R.id.editText );
        textView.setText( Html.fromHtml( html ) );
        Button button  = findViewById( R.id.butt );
        Button submit  = findViewById( R.id.submit );

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(MainActivity.this ,PACKAGE_NAME,editText.getText().toString());
            }
        } );
    }

    public void iniServie(){
        Intent intent = new Intent();
        intent.setClass(this, WindowService.class);
        startService(intent);
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
    //判断当前应用是否是debug状态
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

}
