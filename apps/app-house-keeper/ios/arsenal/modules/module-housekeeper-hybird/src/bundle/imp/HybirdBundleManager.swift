/*
   上传参数: bundleKey: versionCode_versionName

   下发参数: {
                bundleKey: versionCode_versionName,
                patchList: [
                    {
                        patchUrl="https://www.ctrip.com/com.smart.modules.device.ios_1.patch",
                        packageName:"com.smart.modules.device.ios"
                        patchVersion:1
                        patchMd5:""
                        syntheticMd5:""
                    },
                    {
                        patchUrl="https://www.ctrip.com/com.smart.modules.device.android_1.patch",
                        packageName:"com.smart.modules.device.android"
                        patchVersion:1
                        patchMd5:""
                        syntheticMd5:""
                    },
                ]
            }
   本地目录: /hotpatch
            ........./app.version.1.1(bundleKey)/
            ......................../com.smart.modules.device.ios/
            ......................................./patch.version.1
            ......................................................./com.smart.modules.device.ios.patch //下载的差分文件
            ......................................................./com.smart.modules.device.ios.zip    //合成的目标文件
            ......................................................./com.smart.modules.device.ios.dex   //加载一次后生成的dex文件，如果存在可以直接加载这个(optimize)
            ......................................./patch.version.2
            ......................................................./com.smart.modules.device.ios.patch
            ......................................................./com.smart.modules.device.ios.zip
            ........./app.version.2.2(bundleKey)/
*/

import SSZipArchive
import RxCocoa
import RxSwift

class HybirdBundleManager: IBundleManager {
    private let TAG = "HybirdBundleManager"
    private let KEY_HYBIRD_VERSION = "[HybirdLocalVersion]"

    private let docDir = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray)[0] as! String
    
    private let suffix = ".zip"
    private let nameInAssets = "bundle"
    private let nameForUnZipDir = "hybird"
    lazy private var pathForLocalRootDir: String = docDir
    lazy var pathForHybirdDir: String = { self.pathForLocalRootDir + self.nameForUnZipDir + "/"}()

    lazy var pathForLocalFile: String = {self.pathForHybirdDir + self.nameForUnZipDir + self.suffix}()

    private let listeners: Array<(_ success: Bool) -> Void> = Array()

    private var hybirdLocalVersion: String {
        set(value) {
            //HKPreferencesUtil.putString(KEY_HYBIRD_VERSION, value)
            self.hybirdLocalVersion = value
        }
        get{
            return  self.hybirdLocalVersion
        }
    }
    
    
    
    func installWithVerify(_ callback: ((Bool) -> Void)?) {
                if (!verify()) {
                    _ = Observable<Any>.create { observer in
                                HKLogUtil.d(self.TAG, "start clean now ...")
                                try HKFileUtil.deleteFile(self.pathForLocalFile)
                                try HKFileUtil.deleteDirectory(self.pathForHybirdDir)
                                HKLogUtil.d(self.TAG, "clean success")
                                HKLogUtil.d(self.TAG, "start copy now ...")
                                HKFileUtil.copy(Bundle.main.path(forResource: self.nameInAssets, ofType: "zip", inDirectory: "assets") ?? "", self.pathForLocalFile)
                                HKLogUtil.d(self.TAG, "copy success ! isFileExistsInSdcard:")//,File(self.pathForLocalFile).exists())
                                HKLogUtil.d(self.TAG, "start unzip now ...")
                                HKZipUtil.unzip(self.pathForLocalFile, targetDirPath:self.pathForLocalRootDir)
                                HKLogUtil.d(self.TAG, "unzip success")
        
                                self.hybirdLocalVersion = HKSystemUtil.versionCode+"_"+HKSystemUtil.versionName
                        
                                observer.onNext(0)
                                return Disposables.create()
                            }
                            .subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background)) //Schedulers.io()
                            .observeOn(MainScheduler.instance) //AndroidSchedulers.mainThread()
                        .subscribe(
                            onNext:{ index in
                                HKLogUtil.d(self.TAG, "copyToLocal success")
                                callback?(true)
                                self.listeners.forEach() {it in
                                    it(true)
                                }
        
                            },
                            onError: { error in
                                HKLogUtil.e(self.TAG, "copyToLocal failure", error.localizedDescription)
                                callback?(false)
                                self.listeners.forEach {it in
                                    it(false)
                                }
                            },
                            onCompleted: nil,
                            onDisposed: nil
                    )
                } else {
                    HKLogUtil.w(self.TAG, "no need to copyToLocal")
                    callback?(true)
                }
    }
    
    func verify() -> Bool {
        let versionCurrentApp = "${HKSystemUtil.versionCode}_${HKSystemUtil.versionName}"
                let verify = versionCurrentApp == self.hybirdLocalVersion
                //HKLogUtil.d(self.TAG, "versionCurrentApp:$versionCurrentApp == hybirdLocalVersion:$hybirdLocalVersion ? $verify")
                return verify
    }
    
    func installWithVerify() {
        self.installWithVerify(nil)
    }

}
