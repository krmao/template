protocol CXIBundleManager {

    func verify() -> Bool

    func installWithVerify()

    func installWithVerify(_ callback: ((_ success: Bool, _ rootDir:String) -> Void)?)
}
