protocol IBundleManager {

    func verify() -> Bool

    func installWithVerify()

    func installWithVerify(_ callback: ((_ success: Bool) -> Void)?)
}
