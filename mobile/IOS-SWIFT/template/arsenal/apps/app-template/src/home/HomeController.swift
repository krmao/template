//
// Created by smart on 2018/8/30.
// Copyright (c) 2018 com.smart. All rights reserved.
//

import Foundation
import TransitionableTab

class HomeController: UITabBarController {
    override func viewDidLoad() {
        super.viewDidLoad()
        self.delegate = self

        let reactController = HomeChildReactController()
        reactController.tabBarItem = UITabBarItem(title: "RN", image: UIImage.init(named: "AppIcon.png"), selectedImage: UIImage.init(named: "AppIcon.png"))

        let hybirdController = HomeChildReactController()
        hybirdController.tabBarItem = UITabBarItem(title: "HYBIRD", image: UIImage.init(named: "AppIcon.png"), selectedImage: UIImage.init(named: "AppIcon.png"))

        self.viewControllers = [reactController, hybirdController]
        self.selectedIndex = 0
    }
}

extension HomeController: TransitionableTab {
    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        return animateTransition(tabBarController, shouldSelect: viewController)
    }
}