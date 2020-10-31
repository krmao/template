#
# 使用方式
# source 'https://github.com/krmao/libsforios.git'
# load 'PodReactNative.rb'                                          # eval(File.read('PodReactNative.rb'))
# rnpodremote                                                       # rnpodlocal "../../react_native/node_modules"
#
# react-native 0.62.2
# # https://reactnative.dev/docs/integration-with-existing-apps
# # ===============================
def rnpodlocal(node_modules_path="../../react_native/node_modules")
  pod 'FBLazyVector', :path => node_modules_path + "/react-native/Libraries/FBLazyVector"
  pod 'FBReactNativeSpec', :path => node_modules_path + "/react-native/Libraries/FBReactNativeSpec"
  pod 'RCTRequired', :path => node_modules_path + "/react-native/Libraries/RCTRequired"
  pod 'RCTTypeSafety', :path => node_modules_path + "/react-native/Libraries/TypeSafety"
  pod 'React', :path => node_modules_path + '/react-native/'
  pod 'React-Core', :path => node_modules_path + '/react-native/'
  pod 'React-CoreModules', :path => node_modules_path + '/react-native/React/CoreModules'
  pod 'React-Core/DevSupport', :path => node_modules_path + '/react-native/'
  pod 'React-RCTActionSheet', :path => node_modules_path + '/react-native/Libraries/ActionSheetIOS'
  pod 'React-RCTAnimation', :path => node_modules_path + '/react-native/Libraries/NativeAnimation'
  pod 'React-RCTBlob', :path => node_modules_path + '/react-native/Libraries/Blob'
  pod 'React-RCTImage', :path => node_modules_path + '/react-native/Libraries/Image'
  pod 'React-RCTLinking', :path => node_modules_path + '/react-native/Libraries/LinkingIOS'
  pod 'React-RCTNetwork', :path => node_modules_path + '/react-native/Libraries/Network'
  pod 'React-RCTSettings', :path => node_modules_path + '/react-native/Libraries/Settings'
  pod 'React-RCTText', :path => node_modules_path + '/react-native/Libraries/Text'
  pod 'React-RCTVibration', :path => node_modules_path + '/react-native/Libraries/Vibration'
  pod 'React-Core/RCTWebSocket', :path => node_modules_path + '/react-native/'
  pod 'React-cxxreact', :path => node_modules_path + '/react-native/ReactCommon/cxxreact'
  pod 'React-jsi', :path => node_modules_path + '/react-native/ReactCommon/jsi'
  pod 'React-jsiexecutor', :path => node_modules_path + '/react-native/ReactCommon/jsiexecutor'
  pod 'React-jsinspector', :path => node_modules_path + '/react-native/ReactCommon/jsinspector'
  pod 'ReactCommon/callinvoker', :path => node_modules_path + "/react-native/ReactCommon"
  pod 'ReactCommon/turbomodule/core', :path => node_modules_path + "/react-native/ReactCommon"
  
  pod 'Yoga', :path => node_modules_path + '/react-native/ReactCommon/yoga'
  pod 'DoubleConversion', :podspec => node_modules_path + '/react-native/third-party-podspecs/DoubleConversion.podspec'
  pod 'glog', :podspec => node_modules_path + '/react-native/third-party-podspecs/glog.podspec'
  pod 'Folly', :podspec => node_modules_path + '/react-native/third-party-podspecs/Folly.podspec'
  
  # https://github.com/software-mansion/react-native-gesture-handler/pull/366
  # https://github.com/software-mansion/react-native-gesture-handler/issues/205
  pod 'RNGestureHandler', :path => node_modules_path + '/react-native-gesture-handler/RNGestureHandler.podspec'
  pod 'RNReanimated', :path => node_modules_path + '/react-native-reanimated/RNReanimated.podspec'
  pod 'react-native-safe-area-context', :path => node_modules_path + '/react-native-safe-area-context/react-native-safe-area-context.podspec'
  pod 'RNVectorIcons', :path => node_modules_path + '/react-native-vector-icons/RNVectorIcons.podspec'
end

def rnpodremote(
    rn_version:'0.62.2', 
    yoga_version:'1.14.0', 
    double_conversion_version:'1.1.6', 
    glog_version:'0.3.5', 
    folly_version:'2018.10.22.00',
    rn_gesture_handler_version:'1.5.3',
    rn_reanimated_version:'1.13.1',
    react_native_safe_area_context_version:'0.6.4',
    rn_vector_icons_version:'6.7.0'
  )
  pod 'FBLazyVector', rn_version
  pod 'FBReactNativeSpec', rn_version
  pod 'RCTRequired', rn_version
  pod 'RCTTypeSafety', rn_version
  pod 'React', rn_version
  pod 'React-Core', rn_version
  pod 'React-CoreModules', rn_version
  pod 'React-Core/DevSupport', rn_version
  pod 'React-RCTActionSheet', rn_version
  pod 'React-RCTAnimation', rn_version
  pod 'React-RCTBlob', rn_version
  pod 'React-RCTImage', rn_version
  pod 'React-RCTLinking', rn_version
  pod 'React-RCTNetwork', rn_version
  pod 'React-RCTSettings', rn_version
  pod 'React-RCTText', rn_version
  pod 'React-RCTVibration', rn_version
  pod 'React-Core/RCTWebSocket', rn_version
  pod 'React-cxxreact',  rn_version
  pod 'React-jsi',  rn_version
  pod 'React-jsiexecutor',  rn_version
  pod 'React-jsinspector',  rn_version
  pod 'ReactCommon/callinvoker', rn_version
  pod 'ReactCommon/turbomodule/core', rn_version

  pod 'Yoga', yoga_version
  pod 'DoubleConversion', double_conversion_version
  pod 'glog', glog_version
  pod 'Folly', folly_version

  # https://github.com/software-mansion/react-native-gesture-handler/pull/366
  # https://github.com/software-mansion/react-native-gesture-handler/issues/205
  pod 'RNGestureHandler', rn_gesture_handler_version
  pod 'RNReanimated', rn_reanimated_version
  pod 'react-native-safe-area-context', react_native_safe_area_context_version
  pod 'RNVectorIcons', rn_vector_icons_version
end
