# # ===============================
# # 以下修改使用非 '..' 路径, 使得 AppCode 不会卡死在索引文件
# # ===============================
# # compatible for AppCode start
# #  plugin_pods.map do |r|
# #    symlink = File.join(symlinks_dir, r[:name])
# #    FileUtils.rm_f(symlink)
# #    File.symlink(r[:path], symlink)
# #    pod r[:name], :path => File.join(symlink, 'ios'), :inhibit_warnings => true
# #  end
#
# # https://github.com/flutter/flutter/issues/57989
# # https://www.kikt.top/posts/flutter/channel/flutter-ios-spec-edit/
# # https://youtrack.jetbrains.com/issue/OC-17145
#   plugin_pods.map { |p|
#     name = p[:name]
#     path = p[:path]
#     specPath = "#{path}/ios/#{name}.podspec"
#     pod p[:name],:path=>specPath
#   }
# # compatible for AppCode end
# # ===============================
#
# https://flutter.dev/docs/development/add-to-app/ios/project-setup#option-a---embed-with-cocoapods-and-the-flutter-sdk
def flutterPodWithSourceCode
  flutter_application_path = '../../flutter_module/'
  load File.join(flutter_application_path, '.ios', 'Flutter', 'podhelper.rb')
  install_all_flutter_pods(flutter_application_path)
end

# https://flutter.dev/docs/development/add-to-app/ios/project-setup#option-c---embed-application-and-plugin-frameworks-in-xcode-and-flutter-framework-with-cocoapods
# https://github.com/CocoaPods/CocoaPods/issues/2860#issuecomment-273208880
# https://github.com/flutter/flutter/issues/50204
#$ xcrun xcodebuild -alltargets -sdk iphoneos -configuration "Debug" ONLY_ACTIVE_ARCH=NO
#$ xcrun xcodebuild -alltargets -sdk iphoneos -configuration "Profile" ONLY_ACTIVE_ARCH=NO
#$ xcrun xcodebuild -alltargets -sdk iphoneos -configuration "Release" ONLY_ACTIVE_ARCH=NO
#flutter build ios-framework --cocoapods --pub --debug --no-profile --output=/Users/krmao/workspace/template/mobile/flutter/flutter_modules/flutter_module_template/build/host/outputs/repo-ios -v
def flutterPodWithCocoaPodSpec
  pod 'LibraryFlutter', :path => '../Libraries/LibraryFlutter'
  ENV['POD_RELEASE'] = 'false'
  puts "ENV['POD_RELEASE']=" + ENV['POD_RELEASE']
  if ENV['POD_RELEASE'] == 'true'
    pod 'Flutter', :podspec => '../../flutter_module/build-repo/ios/Release/Flutter.podspec', :configurations => ['Release']
  else
    pod 'Flutter', :podspec => '../../flutter_module/build-repo/ios/Debug/Flutter.podspec', :configurations => ['Debug']
  end
  pod 'FlutterPluginRegistrant', :podspec => '../../flutter_module/.ios/Flutter/FlutterPluginRegistrant/FlutterPluginRegistrant.podspec'
end
