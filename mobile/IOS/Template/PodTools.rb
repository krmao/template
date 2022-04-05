# 删除每个库的 IPHONEOS_DEPLOYMENT_TARGET, 消除很多不必要的警告
# https://stackoverflow.com/questions/37160688/set-deployment-target-for-cocoapodss-pod
def removeLibDeploymentTarget
  post_install do |lib|
    lib.pods_project.targets.each do |target|
      target.build_configurations.each do |config|
        config.build_settings.delete 'IPHONEOS_DEPLOYMENT_TARGET'
      end
    end
    
    ## Fix for XCode 12.5
    find_and_replace("../../react_native/node_modules/react-native/React/CxxBridge/RCTCxxBridge.mm",
    "_initializeModules:(NSArray<id<RCTBridgeModule>> *)modules", "_initializeModules:(NSArray<Class> *)modules")
    find_and_replace("../../react_native/node_modules/react-native/ReactCommon/turbomodule/core/platform/ios/RCTTurboModuleManager.mm",
    "RCTBridgeModuleNameForClass(module))", "RCTBridgeModuleNameForClass(Class(module)))")
    
  end
end

def find_and_replace(dir, findstr, replacestr)
  Dir[dir].each do |name|
      text = File.read(name)
      replace = text.gsub(findstr,replacestr)
      if text != replace
          puts "Fix: " + name
          File.open(name, "w") { |file| file.puts replace }
          STDOUT.flush
      end
  end
  Dir[dir + '*/'].each(&method(:find_and_replace))
end
