//
//  UIButtonWithImage.swift
//  fruit-shop-ios
//
//  Created by maokangren on 2017/7/31.
//  Copyright © 2017年 com.xixi. All rights reserved.
//

import UIKit
import SnapKit

class UIButtonWithImage: UIButton {

    override func layoutSubviews() {
        super.layoutSubviews()
        self.backgroundColor=UIColor.white
        
        //self.imageView?.backgroundColor=UIColor.orange
        //self.titleLabel?.backgroundColor=UIColor.yellow
        
        self.imageView?.contentMode=UIViewContentMode.scaleAspectFit
       
        self.titleLabel?.textAlignment=NSTextAlignment.center
        
        let titleSize=self.titleLabel?.sizeThatFits(CGSize(width:CGFloat(MAXFLOAT),height:30))
        
        self.titleLabel?.snp.makeConstraints({ (make) in
            make.left.right.equalTo(self)
            make.bottom.equalTo(self).offset(-5)
            make.height.equalTo((titleSize?.height)!)
        })
        
        self.imageView?.snp.makeConstraints({ (make) in
            make.left.top.equalTo(self).offset(5)
            make.right.equalTo(self).offset(-5)
            make.bottom.equalTo((self.titleLabel?.snp.top)!).offset(-0.001)
        })
    }
}
