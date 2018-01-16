### hybird framework template [android(atlas)]

/*
   上传参数: bundleKey: versionCode_versionName

   下发参数: {
                bundleKey: versionCode_versionName,
                patchList: [
                    {
                        patchUrl="https://www.ctrip.com/com.mctrip.modules.device.ios_1.patch",
                        packageName:"com.mctrip.modules.device.ios"
                        patchVersion:1
                        patchMd5:""
                        syntheticMd5:""
                    },
                    {
                        patchUrl="https://www.ctrip.com/com.mctrip.modules.device.android_1.patch",
                        packageName:"com.mctrip.modules.device.android"
                        patchVersion:1
                        patchMd5:""
                        syntheticMd5:""
                    },
                ]
            }
   本地目录: /hotpatch
            ........./app.version.1.1(bundleKey)/
            ......................../com.mctrip.modules.device.ios/
            ......................................./patch.version.1
            ......................................................./com.mctrip.modules.device.ios.patch //下载的差分文件
            ......................................................./com.mctrip.modules.device.ios.zip    //合成的目标文件
            ......................................................./com.mctrip.modules.device.ios.dex   //加载一次后生成的dex文件，如果存在可以直接加载这个(optimize)
            ......................................./patch.version.2
            ......................................................./com.mctrip.modules.device.ios.patch
            ......................................................./com.mctrip.modules.device.ios.zip
            ........./app.version.2.2(bundleKey)/
*/
