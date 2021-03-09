#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run 'pod lib lint lib_flutter_base.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'lib_flutter_base'
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
    'Classes/FlutterBase/LibFlutterBasePlugin.h',
    'Classes/FlutterBase/STFlutterUtils.h',
    'Classes/FlutterBase/STFlutterHomeViewController.h',
    'Classes/FlutterBase/STFlutterInitializer.h',
    'Classes/FlutterBase/STFlutterBusHandler.h',
    'Classes/FlutterBase/STFlutterViewController.h',
    'Classes/FlutterBase/Plugins/Application/STFlutterApplicationPlugin.h',
    'Classes/FlutterBase/Plugins/Base/STFlutterBridge.h',
    'Classes/FlutterBase/Plugins/Base/STFlutterPlugin.h',
    'Classes/FlutterBase/Plugins/Base/STFlutterPluginManager.h',
    'Classes/FlutterBase/Plugins/BridgeCompact/STFlutterBridgeCompactPlugin.h',
    'Classes/FlutterBase/Plugins/Env/STFlutterEnvPlugin.h',
    'Classes/FlutterBase/Plugins/Event/STFlutterEventPlugin.h',
    'Classes/FlutterBase/Plugins/Page/STFlutterPagePlugin.h',
    'Classes/FlutterBase/Plugins/Toast/STFlutterToastPlugin.h',
    'Classes/FlutterBase/Plugins/URL/STFlutterURLPlugin.h',
    'Classes/FlutterBoost/FlutterBoost.h',
    'Classes/FlutterBoost/FlutterBoostDelegate.h',
    'Classes/FlutterBoost/FlutterBoostPlugin.h',
    'Classes/FlutterBoost/container/FBFlutterViewContainer.h',
    'Classes/FlutterBoost/container/FBFlutterContainer.h',
    'Classes/FlutterBoost/container/FBLifecycle.h',
    'Classes/FlutterBoost/container/FBFlutterContainerManager.h',
    'Classes/FlutterBoost/messages.h'

  s.dependency 'LibIosBase', '~>0.0.2'
  s.dependency 'Flutter'
  s.libraries = 'c++'
  s.xcconfig = {
      'CLANG_CXX_LANGUAGE_STANDARD' => 'c++11',
      'CLANG_CXX_LIBRARY' => 'libc++'
  }

  s.ios.deployment_target = '8.0'
end
