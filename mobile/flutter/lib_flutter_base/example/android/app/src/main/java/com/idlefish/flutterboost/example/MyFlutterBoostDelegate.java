package com.idlefish.flutterboost.example;

import android.content.Intent;

import com.codesdancing.flutter.boost.STFlutterBoostUtils;
import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.FlutterBoostDelegate;

import java.util.HashMap;

public class MyFlutterBoostDelegate implements FlutterBoostDelegate {
    @Override
    public void pushNativeRoute(String pageName, HashMap<String, String> arguments) {
        Intent intent = new Intent(FlutterBoost.instance().currentActivity(), NativePageActivity.class);
        FlutterBoost.instance().currentActivity().startActivity(intent);
    }

    @Override
    public void pushFlutterRoute(String pageName, String uniqueId, HashMap<String, String> arguments) {
        STFlutterBoostUtils.openFlutterPageByName(pageName, uniqueId, arguments);
    }
}