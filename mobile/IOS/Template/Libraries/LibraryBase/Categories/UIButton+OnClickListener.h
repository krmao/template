//
//  UIButton+OnClickListener.h
//  App
//
//  Created by krmao on 2019/9/30.
//  Copyright © 2019 smart. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^OnClickListener) (void);

NS_ASSUME_NONNULL_BEGIN

@interface UIButton (OnClickListener)

//@property(nonatomic, strong) OnClickListener clickListener;
//
//- (void) setOnClickListener:(OnClickListener)onClickListener;

@end

NS_ASSUME_NONNULL_END
