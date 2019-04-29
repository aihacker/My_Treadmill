package com.zc.Treadmill;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.Treadmill.Service.WindowService;
import com.zc.Treadmill.util.SPUtils;

public class AutoStartReceiver extends BroadcastReceiver {
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    public static String PACKAGE_NAME="package";
    private EditText editText;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
