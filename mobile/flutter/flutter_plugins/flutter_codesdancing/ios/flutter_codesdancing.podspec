#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run 'pod lib lint flutter_codesdancing.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'flutter_codesdancing'
  s.version          = '0.0.1'
  s.summary          = 'A new flutter plugin project.'
  s.description      = <<-DESC
A new flutter plugin project.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/**/**/*.{h,m,mm}'

  s.public_header_files =
    'Classes/CodesDancingPlugin.h',
    'Classes/FlutterUtils/STFlutterViewController.h',
    'Classes/FlutterUtils/STFlutterRouterHandler.h',
    'Classes/FlutterUtils/STFlutterRouter.h',
    'Classes/FlutterUtils/STFlutterEmbedIntoNativeViewController.h',
    'Classes/FlutterUtils/Plugins/Application/STFlutterApplicationPlugin.h',
    'Classes/FlutterUtils/Plugins/Base/STFlutterBridge.h',
    'Classes/FlutterUtils/Plugins/Base/STFlutterPlugin.h',
    'Classes/FlutterUtils/Plugins/Base/STFlutterPluginManager.h',
    'Classes/FlutterUtils/Plugins/BridgeCompact/STFlutterBridgeCompactPlugin.h',
    'Classes/FlutterUtils/Plugins/Env/STFlutterEnvPlugin.h',
    'Classes/FlutterUtils/Plugins/Event/STFlutterEventPlugin.h',
    'Classes/FlutterUtils/Plugins/Page/STFlutterPagePlugin.h',
    'Classes/FlutterUtils/Plugins/Toast/STFlutterToastPlugin.h',
    'Classes/FlutterUtils/Plugins/URL/STFlutterURLPlugin.h',
    'Classes/FlutterBoost/Boost/FlutterBoostPlugin.h',
    'Classes/FlutterBoost/Boost/FLBPlatform.h',
    'Classes/FlutterBoost/Boost/FLBFlutterContainer.h',
    'Classes/FlutterBoost/Boost/FLBFlutterAppDelegate.h',
    'Classes/FlutterBoost/Boost/FLBTypes.h',
    'Classes/FlutterBoost/Boost/FlutterBoost.h',
    'Classes/FlutterBoost/Boost/BoostChannel.h',
    'Classes/FlutterBoost/container/FLBFlutterViewContainer.h'

  s.dependency 'Flutter'
  s.dependency 'CodesDancing', '~>0.0.2'
  s.libraries = 'c++'
  s.xcconfig = {
    'CLANG_CXX_LANGUAGE_STANDARD' => 'c++11',
    'CLANG_CXX_LIBRARY' => 'libc++'
  }
  s.platform = :ios, '8.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
end
