import Foundation
import MBProgressHUD

public class CXDialogUtil: NSObject {

    @objc static func showProgress(_ delaySeconds: Double = 0, _ toView: UIView? = nil, _ isUserInteractionEnabled: Bool = true) {
        if let _toView = toView == nil ? UIApplication.shared.delegate?.window : toView {
            hideProgress()
            let hud: MBProgressHUD = MBProgressHUD.showAdded(to: _toView!, animated: true)
            hud.mode = .indeterminate
            hud.bezelView.style = MBProgressHUDBackgroundStyle.blur
            hud.bezelView.backgroundColor = UIColor("#FE000000")
            hud.activityIndicatorColor = UIColor("#FFEFEFEF")

            if (delaySeconds > 0) {
                hud.hide(true, afterDelay: delaySeconds)
            }
            //hud.center.y = hud.center.y
            hud.isUserInteractionEnabled = isUserInteractionEnabled
        }
    }

    @objc static func hideProgress(_ toView: UIView? = nil) {
        if let _toView = toView == nil ? UIApplication.shared.delegate?.window : toView {
            MBProgressHUD.hide(for: _toView!, animated: true)
        }
    }

}
