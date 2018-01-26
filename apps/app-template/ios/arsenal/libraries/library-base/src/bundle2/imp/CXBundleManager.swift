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
                    ),
                    {
                        patchUrl="https://www.ctrip.com/com.smart.modules.device.android_1.patch",
                        packageName:"com.smart.modules.device.android"
                        patchVersion:1
                        patchMd5:""
                        syntheticMd5:""
                    ),
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

class CXBundleManager: CXIBundleManager {

    static let INSTANCE = CXBundleManager()

    let TAG = "[hybird]"

    private let KEY_HYBIRD_VERSION = "[HybirdLocalVersion]"

    private let docDir = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray)[0] as! String + "/"

    private let suffix = ".zip"
    private let nameInAssets = "bundle"
    private let nameForUnZipDir = "hybird"
    private lazy var pathForLocalRootDir = docDir

    lazy var pathForHybirdDir: String = {
        let path = self.pathForLocalRootDir + self.nameForUnZipDir + "/"
        CXFileUtil.makeDirs(path)
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
                        CXLogUtil.d(self.TAG, "start clean now ...")
                        CXFileUtil.deleteFile(self.pathForLocalFile)
                        CXFileUtil.deleteDirectory(self.pathForHybirdDir)
                        CXLogUtil.d(self.TAG, "start copy now ...")
                        do {
                            try CXFileUtil.copy(Bundle.main.path(forResource: self.nameInAssets, ofType: "zip", inDirectory: "assets")!, self.pathForLocalFile)
                            CXLogUtil.d(self.TAG, "copy success ! isFileExistsInSdcard:", String(FileManager.default.fileExists(atPath: self.pathForLocalFile)))
                            CXLogUtil.d(self.TAG, "start unzip now ...")
                            try CXZipUtil.unzip(self.pathForLocalFile, targetDirPath: self.pathForHybirdDir)
                            self.hybirdLocalVersion = String(CXSystemUtil.versionCode) + "_" + CXSystemUtil.versionName
                            observer.onNext(0)
                            CXFileUtil.printDirs(self.docDir)
                        } catch {
                            CXLogUtil.d(self.TAG, "copy/unzip failure ! isFileExistsInSdcard:", String(FileManager.default.fileExists(atPath: self.pathForLocalFile)), error)
                            observer.onError(error)
                        }
                        return Disposables.create()
                    }
                    .subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background)) //Schedulers.io()
                    .observeOn(MainScheduler.instance) //AndroidSchedulers.mainThread()
                    .subscribe(
                            onNext: { index in
                                CXLogUtil.d(self.TAG, "copyToLocal success")
                                callback?(true, self.pathForHybirdDir)
                                self.listeners.forEach() { it in
                                    it(true, self.pathForHybirdDir)
                                }

                            ),
                            onError: { error in
                                CXLogUtil.e(self.TAG, "copyToLocal failure", error.localizedDescription)
                                callback?(false, self.pathForHybirdDir)
                                self.listeners.forEach { it in
                                    it(false, self.pathForHybirdDir)
                                }
                            ),
                            onCompleted: nil,
                            onDisposed: nil
                    )
        } else {
            CXLogUtil.w(self.TAG, "no need to copyToLocal")
            callback?(true, self.pathForHybirdDir)
        }
    }

    func verify() -> Bool {
        let versionCurrentApp = String(CXSystemUtil.versionCode) + "_" + CXSystemUtil.versionName
        var verify = versionCurrentApp == self.hybirdLocalVersion

        let hybirdLocalValid = CXFileUtil.fileExists(self.pathForHybirdDir + "index.html")
        if !hybirdLocalValid {
            CXFileUtil.deleteDirectory(self.pathForHybirdDir)
            self.hybirdLocalVersion = ""
        }

        verify = verify && hybirdLocalValid

        CXLogUtil.d(self.TAG, "[verify:\(verify)] versionCurrentApp:\(versionCurrentApp) == hybirdLocalVersion:\(hybirdLocalVersion) && hybirdLocalValid:\(hybirdLocalValid)")
        return false//verify
    }

    func installWithVerify() {
        self.installWithVerify(nil)
    }
}
