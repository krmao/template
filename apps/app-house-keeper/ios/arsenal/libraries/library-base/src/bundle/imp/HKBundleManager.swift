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

import RxCocoa
import RxSwift

class HKBundleManager: HKIBundleManager {

    static let INSTANCE = HKBundleManager()

    let TAG = "[hybird]"

    private let KEY_HYBIRD_VERSION = "[HybirdLocalVersion]"

    private let docDir = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray)[0] as! String + "/"

    private let suffix = ".zip"
    private let nameInAssets = "bundle"
    private let nameForUnZipDir = "hybird"
    private lazy var pathForLocalRootDir = docDir

    lazy var pathForHybirdDir: String = {
        let path = self.pathForLocalRootDir + self.nameForUnZipDir + "/"
        HKFileUtil.makeDirs(path)
        return path
    }()

    lazy var pathForLocalFile: String = {
        self.pathForHybirdDir + self.nameForUnZipDir + self.suffix
    }()

    private let listeners: Array<(_ success: Bool, _ rootDir: String) -> Void> = Array()

    private var hybirdLocalVersion: String {
        set(value) {
            UserDefaults.standard.set(value, forKey: KEY_HYBIRD_VERSION)
        }
        get {
            return UserDefaults.standard.string(forKey: KEY_HYBIRD_VERSION) ?? ""
        }
    }

    func installWithVerify(_ callback: ((_ success: Bool, _ rootDir: String) -> Void)?) {
        if (!verify()) {
            _ = Observable<Any>.create { observer in
                        HKLogUtil.d(self.TAG, "start clean now ...")
                        HKFileUtil.deleteFile(self.pathForLocalFile)
                        HKFileUtil.deleteDirectory(self.pathForHybirdDir)
                        HKLogUtil.d(self.TAG, "start copy now ...")
                        do {
                            try HKFileUtil.copy(Bundle.main.path(forResource: self.nameInAssets, ofType: "zip", inDirectory: "assets")!, self.pathForLocalFile)
                            HKLogUtil.d(self.TAG, "copy success ! isFileExistsInSdcard:", String(FileManager.default.fileExists(atPath: self.pathForLocalFile)))
                            HKLogUtil.d(self.TAG, "start unzip now ...")
                            try HKZipUtil.unzip(self.pathForLocalFile, targetDirPath: self.pathForHybirdDir)
                            self.hybirdLocalVersion = String(HKSystemUtil.versionCode) + "_" + HKSystemUtil.versionName
                            observer.onNext(0)
                            HKFileUtil.printDirs(self.docDir)
                        } catch {
                            HKLogUtil.d(self.TAG, "copy/unzip failure ! isFileExistsInSdcard:", String(FileManager.default.fileExists(atPath: self.pathForLocalFile)), error)
                            observer.onError(error)
                        }
                        return Disposables.create()
                    }
                    .subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background)) //Schedulers.io()
                    .observeOn(MainScheduler.instance) //AndroidSchedulers.mainThread()
                    .subscribe(
                            onNext: { index in
                                HKLogUtil.d(self.TAG, "copyToLocal success")
                                callback?(true, self.pathForHybirdDir)
                                self.listeners.forEach() { it in
                                    it(true, self.pathForHybirdDir)
                                }

                            },
                            onError: { error in
                                HKLogUtil.e(self.TAG, "copyToLocal failure", error.localizedDescription)
                                callback?(false, self.pathForHybirdDir)
                                self.listeners.forEach { it in
                                    it(false, self.pathForHybirdDir)
                                }
                            },
                            onCompleted: nil,
                            onDisposed: nil
                    )
        } else {
            HKLogUtil.w(self.TAG, "no need to copyToLocal")
            callback?(true, self.pathForHybirdDir)
        }
    }

    func verify() -> Bool {
        let versionCurrentApp = String(HKSystemUtil.versionCode) + "_" + HKSystemUtil.versionName
        let verify = versionCurrentApp == self.hybirdLocalVersion
        HKLogUtil.d(self.TAG, "versionCurrentApp:", versionCurrentApp, " == hybirdLocalVersion:", hybirdLocalVersion, "?", String(verify))
        return false//verify
    }

    func installWithVerify() {
        self.installWithVerify(nil)
    }

}
