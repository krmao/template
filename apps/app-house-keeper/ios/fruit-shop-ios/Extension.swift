//
//  Button+EdgeInsets.swift
//  TaoZhiHui
//
//  Created by 耗子 on 2017/5/16.
//  Copyright © 2017年 haozi. All rights reserved.
//

import Foundation
import UIKit

/// 按钮图片和title位置风格
///
/// - imageLeft: 图片在左
/// - imageRight: 图片在右
/// - imageTop: 图片在上
/// - imageBottom: 图片在下
enum ButtonEdgeInsetsStyle: Int {
    case imageLeft = 0
    case imageRight
    case imageTop
    case imageBottom
}

extension UIButton {
    
    /// 重新布局button内图片和title位置
    ///
    /// - Parameters:
    ///   - style: 位置风格
    ///   - space: 间距
    func layoutButtonWithEdgeInsetsStyle(style: ButtonEdgeInsetsStyle,
                                         space: CGFloat) {
        let imageViewWidth: CGFloat? = self.imageView?.frame.size.width
        var labelWidth: CGFloat? = self.titleLabel?.frame.size.width
        var tempSpace = space
        
        if labelWidth == 0 {
            let titleSize = self.titleLabel?.text?.size(attributes: [NSFontAttributeName : self.titleLabel?.font ?? UIFont.systemFont(ofSize: 14)])
            labelWidth = titleSize?.width
        }
        
        var imageInsetsTop: CGFloat = 0.0
        var imageInsetsLeft: CGFloat = 0.0
        var imageInsetsRight: CGFloat = 0.0
        var imageInsetsBottom: CGFloat = 0.0
        
        var titleInsetsTop: CGFloat = 0.0
        var titleInsetsLeft: CGFloat = 0.0
        var titleInsetsRight: CGFloat = 0.0
        var titleInsetsBottom: CGFloat = 0.0
        
        switch style {
        case .imageRight:
            tempSpace *= 0.5
            
            imageInsetsLeft = labelWidth! + tempSpace
            imageInsetsRight = -imageInsetsLeft
            
            titleInsetsLeft = -(imageViewWidth! + tempSpace)
            titleInsetsRight = -titleInsetsLeft
            
        case .imageLeft:
            tempSpace *= 0.5
            
            imageInsetsLeft = -tempSpace
            imageInsetsRight = -imageInsetsLeft
            
            titleInsetsLeft = tempSpace
            titleInsetsRight = -titleInsetsLeft
            
        case .imageBottom:
            let imageHeight: CGFloat? = self.imageView?.frame.size.height
            let labelHeight: CGFloat? = self.titleLabel?.frame.size.height
            let buttonHeight: CGFloat = self.frame.size.height
            let boundsCentery: CGFloat = (imageHeight! + tempSpace + labelHeight!) * 0.5
            
            let centerX_button = self.bounds.midX
            let centerX_titleLabel = self.titleLabel?.frame.midX
            let centerX_image = self.imageView?.frame.midX
            
            let imageBottomY: CGFloat = (self.imageView?.frame.maxY)!
            let titleTopY = self.titleLabel?.frame.minY
            
            imageInsetsTop = buttonHeight - (buttonHeight * 0.5 - boundsCentery) - imageBottomY
            imageInsetsLeft = centerX_button - centerX_image!
            imageInsetsRight = -imageInsetsLeft
            imageInsetsBottom = -imageInsetsTop
            
            titleInsetsTop = buttonHeight * 0.5 - boundsCentery - titleTopY!
            titleInsetsLeft = -(centerX_titleLabel! - centerX_button)
            titleInsetsRight = -titleInsetsLeft
            titleInsetsBottom = -titleInsetsTop
            
        case .imageTop:
            let imageHeight: CGFloat? = self.imageView?.frame.size.height
            let labelHeight: CGFloat? = self.titleLabel?.frame.size.height
            let buttonHeight: CGFloat = self.frame.size.height
            let boundsCentery: CGFloat = (imageHeight! + tempSpace + labelHeight!) * 0.5
            
            let centerX_button = self.bounds.midX
            let centerX_titleLabel = self.titleLabel?.frame.midX
            let centerX_image = self.imageView?.frame.midX
            
            let imageTopY: CGFloat = (self.imageView?.frame.minY)!
            let titleBottom = self.titleLabel?.frame.maxY
            
            imageInsetsTop = (buttonHeight * 0.5 - boundsCentery) - imageTopY
            imageInsetsLeft = centerX_button - centerX_image!
            imageInsetsRight = -imageInsetsLeft
            imageInsetsBottom = -imageInsetsTop
            
            titleInsetsTop = buttonHeight - (buttonHeight * 0.5 - boundsCentery) - titleBottom!
            titleInsetsLeft = -(centerX_titleLabel! - centerX_button)
            titleInsetsRight = -titleInsetsLeft
            titleInsetsBottom = -titleInsetsTop
        }
        
        self.imageEdgeInsets = UIEdgeInsetsMake(imageInsetsTop, imageInsetsLeft, imageInsetsBottom, imageInsetsRight)
        self.titleEdgeInsets = UIEdgeInsetsMake(titleInsetsTop, titleInsetsLeft, titleInsetsBottom, titleInsetsRight)
    }
}
