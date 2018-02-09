import Foundation
import MBProgressHUD

class CXToastUtil: NSObject {

    @objc static func show(_ text: String? = nil, _ delaySeconds: Double = 3) {
        var topView = UIApplication.shared.delegate?.window

        if (topView != nil) {
            MBProgressHUD.hide(for: topView!!, animated: true)

            let hud: MBProgressHUD = MBProgressHUD.showAdded(to: topView!!, animated: true)
            hud.mode = .text
            hud.bezelView.style = MBProgressHUDBackgroundStyle.blur
            hud.bezelView.backgroundColor = UIColor("#FE000000")

            hud.labelFont = UIFont.systemFont(ofSize: 14)
            hud.label.textColor = .white

            hud.hide(true, afterDelay: delaySeconds)


            hud.center.y = hud.center.y * 1.7

            hud.isUserInteractionEnabled = false;
            if (text != nil && text!.isEmpty == false) {
                hud.labelText = text!
            }
        }
    }

}
