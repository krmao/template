package com.smart.library.flutter.plugins;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smart.library.STInitializer;
import com.smart.library.util.STLogUtil;
import com.smart.library.util.STThreadUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.JSONMethodCodec;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

@SuppressWarnings("ALL")
public class STFlutterBridgeChannel implements FlutterPlugin {

    public static final String TAG = "[flutter_bridge_channel]";
    public static final String ERROR_NO_PLUGIN = "10001";
    public static final String ERROR_PLUGIN_ERROR = "10002";
    public static final String ERROR_BUSINESS_ERROR = "10003";
    private static final String BRIDGE_CHANNEL_NAME = "codesdancing.flutter.bridge/callNative";
    private static final String BRIDGE_EVENT_NAME = "__codesdancing_flutter_event__";

    private MethodChannel methodChannel;
    private static Map<String, CTFlutterPluginMethodHolder> flutterBridgeMethodMap = new ConcurrentHashMap<>();

    @Override
    public void onAttachedToEngine(@NonNull final FlutterPluginBinding binding) {
        methodChannel = new MethodChannel(binding.getBinaryMessenger(), BRIDGE_CHANNEL_NAME, JSONMethodCodec.INSTANCE);
        methodChannel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @SuppressWarnings("deprecation")
            @Override
            public void onMethodCall(@NotNull MethodCall call, @NotNull MethodChannel.Result result) {
                invokeMethodCall(binding.getFlutterEngine(), call, result);
            }
        });
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        if (methodChannel != null) {
            methodChannel.setMethodCallHandler(null);
            methodChannel = null;
        }
    }

    public void sendEventToDart(final String eventName, final JSONObject data) {
        Runnable sendEventToDart = new Runnable() {
            @Override
            public void run() {
                if (methodChannel != null) {
                    JSONObject eventData = new JSONObject();
                    try {
                        eventData.put("eventName", eventName);
                        eventData.put("eventInfo", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    methodChannel.invokeMethod(BRIDGE_EVENT_NAME, eventData, new MethodChannel.Result() {
                        @Override
                        public void success(@Nullable Object result) {
                            STLogUtil.d(TAG, "invokeMethod success result=$result");
                        }

                        @Override
                        public void error(String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {
                            STLogUtil.e(TAG, "invokeMethod error errorCode=$errorCode, errorMessage=$errorMessage, errorDetails=$errorDetails");
                        }

                        @Override
                        public void notImplemented() {
                            STLogUtil.e(TAG, "invokeMethod notImplemented");
                        }
                    });
                } else {
                    STLogUtil.e(TAG, "invokeMethod methodChannel == null");
                }
            }
        };
        if (STThreadUtils.isMainThread()) {
            sendEventToDart.run();
        } else {
            STThreadUtils.runOnUiThread(sendEventToDart);
        }
    }

    public static class CTFlutterPluginMethodHolder {
        @NonNull
        public STFlutterBasePlugin plugin;
        @NonNull
        public Method method;

        public CTFlutterPluginMethodHolder(@NonNull STFlutterBasePlugin plugin, @NonNull Method method) {
            this.plugin = plugin;
            this.method = method;
        }
    }

    private static class InstanceHolder {
        public static STFlutterBridgeChannel instance = new STFlutterBridgeChannel();
    }

    public static STFlutterBridgeChannel INSTANCE() {
        return InstanceHolder.instance;
    }

    public void registerPlugins(List<STFlutterBasePlugin> plugins) {
        if (plugins == null) {
            return;
        }
        for (STFlutterBasePlugin plugin : plugins) {
            registerPlugin(plugin);
        }
    }

    public void registerPlugin(STFlutterBasePlugin plugin) {
        if (plugin == null) {
            return;
        }
        Method[] methods = plugin.getClass().getDeclaredMethods();
        for (Method method : methods) {
            STFlutterPluginMethod pluginMethod = method.getAnnotation(STFlutterPluginMethod.class);
            if (pluginMethod == null) {
                continue;
            }
            Class<?>[] paramClzs = method.getParameterTypes();
            boolean isIllegal = false;
            if (paramClzs.length >= 3) {
                if (paramClzs[0] != Activity.class
                        || paramClzs[1] != FlutterEngine.class
                        || paramClzs[2] != JSONObject.class
                        || paramClzs[3] != MethodChannel.Result.class) {
                    isIllegal = true;
                }
            } else {
                isIllegal = true;
            }
            if (isIllegal) {
                throw new IllegalArgumentException("The parameter types of pluginMethod is illegal!");
            }
            flutterBridgeMethodMap.put(plugin.getPluginName() + "-" + method.getName(), new CTFlutterPluginMethodHolder(plugin, method));
        }
    }

    private void invokeMethodCall(FlutterEngine flutterEngine, MethodCall call, MethodChannel.Result result) {
        if (flutterBridgeMethodMap.containsKey(call.method)) {
            try {
                CTFlutterPluginMethodHolder holder = flutterBridgeMethodMap.get(call.method);
                if (holder != null) {
                    holder.plugin.setFlutterBridgeChannel(this);
                    holder.method.invoke(holder.plugin, STInitializer.currentActivity(), flutterEngine, call.arguments, result);
                }
            } catch (Exception e) {
                result.error(ERROR_PLUGIN_ERROR, ERROR_PLUGIN_ERROR, "invoke plugin method error:" + call.method + "," + e.getMessage() + "," + e.getCause());
            }
        } else {
            result.notImplemented();
        }
    }

}
