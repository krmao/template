//
//  UIImageView+OnClickListener.h
//  Template
//
//  Created by krmao on 2020/5/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^OnClickListener) (void);

NS_ASSUME_NONNULL_BEGIN

@interface UIView (OnClickListener) // <UIGestureRecognizerDelegate>

@property(nonatomic, copy) OnClickListener clickListener;

- (void) setOnClickListener:(OnClickListener)onClickListener;

@end

NS_ASSUME_NONNULL_END
