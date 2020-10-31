# 删除每个库的 IPHONEOS_DEPLOYMENT_TARGET, 消除很多不必要的警告
# https://stackoverflow.com/questions/37160688/set-deployment-target-for-cocoapodss-pod
def removeLibDeploymentTarget
  post_install do |lib|
    lib.pods_project.targets.each do |target|
      target.build_configurations.each do |config|
        config.build_settings.delete 'IPHONEOS_DEPLOYMENT_TARGET'
      end
    end
  end
end
