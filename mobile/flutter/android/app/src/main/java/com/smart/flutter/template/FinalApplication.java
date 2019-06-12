package com.smart.flutter.template;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.smart.library.flutter.FlutterBundle;
import com.smart.library.flutter.utils.PageRouter;
import com.smart.library.flutter.views.NativeMainActivity;
import com.taobao.idlefish.flutterboost.Debuger;
import com.taobao.idlefish.flutterboost.interfaces.IPlatform;

import java.util.Map;

import io.flutter.app.FlutterApplication;

public class FinalApplication extends FlutterApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        FlutterBundle.init(new IPlatform() {
            @Override
            public Application getApplication() {
                return FinalApplication.this;
            }

            /**
             * 获取应用入口的Activity,这个Activity在应用交互期间应该是一直在栈底的
             */
            @Override
            public Activity getMainActivity() {
                return (NativeMainActivity.sRef != null) ? NativeMainActivity.sRef.get() : null;
            }

            @Override
            public boolean isDebug() {
                return true;
            }

            /**
             * 如果flutter想打开一个本地页面，将会回调这个方法，页面参数将会拼接在url中
             * 例如：sample://nativePage?aaa=bbb
             * 参数就是类似 aaa=bbb 这样的键值对
             */
            @Override
            public boolean startActivity(Context context, String url, int requestCode) {
                Debuger.log("startActivity url=" + url);
                return PageRouter.openPageByUrl(context, url, requestCode);
            }

            @Override
            public Map getSettings() {
                return null;
            }
        });

    }
}
