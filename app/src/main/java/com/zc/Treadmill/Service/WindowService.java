package com.zc.Treadmill.Service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zc.Treadmill.MainActivity;
import com.zc.Treadmill.R;
import com.zc.Treadmill.util.SPUtils;


public class WindowService extends Service {

    private WindowManager windowManager;// 窗口管理者
    private WindowManager.LayoutParams params;// 窗口的属性
    private ImageButton btnView;
    private boolean isAdded;
    private float x1;
    private float y1;
    private int isService;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        btnView = new ImageButton(getApplicationContext());
        btnView.setBackgroundResource( R.drawable.logo );

        windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();

        // 设置Window Type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置悬浮框不可触摸
        params.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;        // 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应
        params.format = PixelFormat.RGBA_8888;
        // 设置悬浮框的宽高
        params.width = 200;
        params.height = 200;
        params.gravity = Gravity.LEFT;
        params.x = 200;
        params.y = 000;
        // 设置悬浮框的Touch监听
        btnView.setOnTouchListener( new View.OnTouchListener() {
            //保存悬浮框最后位置的变量
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        //当手指按下的时候
                        x1 = event.getX();
                        y1 = event.getY();
                        //手指抬起后是响应业务
                        isService = 1;
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        windowManager.updateViewLayout( btnView, params);
                        float x2 = event.getX();
                        float y2 = event.getY();
                        if(y1 - y2 > 15 && y1-y2<30){
                            isService=2;
                        }else if(y1 - y2 > 50) {
//                            Toast.makeText(getApplicationContext(), "向上滑", Toast.LENGTH_SHORT).show();
                            isService = 0;
                        } else if(y2 - y1 > 50) {
                            isService = 0;
//                            Toast.makeText(getApplicationContext(), "向下滑", Toast.LENGTH_SHORT).show();
                        } else if(x1 - x2 > 50) {
                            isService = 0;
//                            Toast.makeText(getApplicationContext(), "向左滑", Toast.LENGTH_SHORT).show();
                        } else if(x2 - x1 > 50) {
                            isService = 0;
//                            Toast.makeText(getApplicationContext(), "向右滑", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        switch (isService){
                            case 1:
                                try {
                                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage( SPUtils.get( getApplicationContext(), MainActivity.PACKAGE_NAME ,"").toString() );
                                    startActivity(LaunchIntent);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "启动APP失败", Toast.LENGTH_SHORT).show();
                                }
//                            Toast.makeText(getApplicationContext(), "响应手指抬起业务", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage( "com.zc.Treadmill" );
                                startActivity(LaunchIntent);
                                break;
                        }

                        break;
                }
                return true;
            }
        });
        windowManager.addView( btnView, params);
        isAdded = true;

        super.onCreate();


    }

}
