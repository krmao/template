
# react-native 0.62.2
# # https://reactnative.dev/docs/integration-with-existing-apps
# # ===============================
def rnpod
  # Your 'node_modules' directory is probably in the root of your project,
  # but if not, adjust the `:path` accordingly

  node_modules_path = "../../react_native/node_modules"

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


def rnpodremote
  pod 'FBLazyVector', '0.62.2'
  pod 'FBReactNativeSpec', '0.62.2'
  pod 'RCTRequired', '0.62.2'
  pod 'RCTTypeSafety', '0.62.2'
  pod 'React', '0.62.2'
  pod 'React-Core', '0.62.2'
  pod 'React-CoreModules', '0.62.2'
  pod 'React-Core/DevSupport', '0.62.2'
  pod 'React-RCTActionSheet', '0.62.2'
  pod 'React-RCTAnimation', '0.62.2'
  pod 'React-RCTBlob', '0.62.2'
  pod 'React-RCTImage', '0.62.2'
  pod 'React-RCTLinking', '0.62.2'
  pod 'React-RCTNetwork', '0.62.2'
  pod 'React-RCTSettings', '0.62.2'
  pod 'React-RCTText', '0.62.2'
  pod 'React-RCTVibration', '0.62.2'
  pod 'React-Core/RCTWebSocket', '0.62.2'
  pod 'React-cxxreact',  '0.62.2'
  pod 'React-jsi',  '0.62.2'
  pod 'React-jsiexecutor',  '0.62.2'
  pod 'React-jsinspector',  '0.62.2'
  pod 'ReactCommon/callinvoker', '0.62.2'
  pod 'ReactCommon/turbomodule/core', '0.62.2'

  pod 'Yoga', '1.14.0'
  pod 'DoubleConversion', '1.1.6'
  pod 'glog', '0.3.5'
  pod 'Folly', '2018.10.22.00'

  # https://github.com/software-mansion/react-native-gesture-handler/pull/366
  # https://github.com/software-mansion/react-native-gesture-handler/issues/205
  pod 'RNGestureHandler', '1.5.3'
  pod 'RNReanimated', '1.13.1'
  pod 'react-native-safe-area-context', '0.6.4'
  pod 'RNVectorIcons', '6.7.0'
end
