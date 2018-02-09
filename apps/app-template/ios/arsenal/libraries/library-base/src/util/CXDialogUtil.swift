import Foundation
import MBProgressHUD

public class CXDialogUtil: NSObject {

    @objc static func showProgress(_ delaySeconds: Double = 0, _ isUserInteractionEnabled: Bool = true) {
        let topView = UIApplication.shared.delegate?.window

        if topView != nil {
            hideProgress()
            let hud: MBProgressHUD = MBProgressHUD.showAdded(to: topView!!, animated: true)
            hud.mode = .indeterminate
            hud.bezelView.style = MBProgressHUDBackgroundStyle.blur
            hud.bezelView.backgroundColor = UIColor("#CC000000")
            hud.activityIndicatorColor = UIColor("#FFEFEFEF")

            if (delaySeconds > 0) {
                hud.hide(true, afterDelay: delaySeconds)
            }
            //hud.center.y = hud.center.y
            hud.isUserInteractionEnabled = isUserInteractionEnabled
        }
    }

    @objc static func hideProgress() {
        let topView = UIApplication.shared.delegate?.window

        if topView != nil {
            MBProgressHUD.hide(for: topView!!, animated: true)
        }
    }

}
