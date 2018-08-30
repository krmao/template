import Foundation
import SnapKit

class HomeChildReactController: UIViewController {

    private lazy var buttonView: UIButton = {
        let _buttonView: UIButton = UIButton(type: .roundedRect)
        _buttonView.setTitle("click to start rn", for: .normal)
        _buttonView.addTarget(self, action: #selector(onClick(button:)), for: .touchUpInside)
        _buttonView.backgroundColor = .orange
        _buttonView.tag = 1
        _buttonView.setTitleColor(.black, for: .normal)
        _buttonView.setTitleColor(.blue, for: .selected)
        return _buttonView
    }()

    @objc
    func onClick(button: UIButton) {
        CXLogUtil.d("you clicked")
        self.tabBarController?.navigationController?.interactivePopGestureRecognizer?.delegate = nil
        self.tabBarController?.navigationController?.interactivePopGestureRecognizer?.isEnabled = true
        self.tabBarController?.navigationController?.pushViewController(ReactViewController(), animated: true)
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.addSubview(self.buttonView)

        self.buttonView.snp.makeConstraints { maker in
            maker.width.equalTo(self.view)
            maker.height.equalTo(50)
            maker.center.equalTo(self.view.center)
        }

    }

}
