//
//  UIButton+OnClickListener.h
//  App
//
//  Created by krmao on 2019/9/30.
//  Copyright © 2019 smart. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButton+Click.h"

NS_ASSUME_NONNULL_BEGIN

@interface UIButton (OnClickListener)

- (void) setOnClickListener:(OnClickListener)onClickListener;
- (void) setOnClickListener:(OnClickListener)onClickListener forControlEvents:(UIControlEvents)controlEvents;

@end

NS_ASSUME_NONNULL_END
