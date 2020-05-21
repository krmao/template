//
//  UIButton+OnClickListener.m
//  App
//
//  Created by krmao on 2019/9/30.
//  Copyright © 2019 smart. All rights reserved.
//

#import "UIButton+OnClickListener.h"
#import <objc/runtime.h>

@interface UIButton ()
@property(nonatomic, weak) OnClickListener clickListener;
- (void) setClickListener:(OnClickListener)onClickListener;
@end

@implementation UIButton (OnClickListener)

- (void) setOnClickListener:(OnClickListener)onClickListener{
    self.clickListener = onClickListener;
    [self addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
}

- (void) setOnClickListener:(OnClickListener)onClickListener forControlEvents:(UIControlEvents)controlEvents{
    self.clickListener = onClickListener;
    [self addTarget:self action:@selector(onClick:) forControlEvents:controlEvents];
}

- (void)onClick:(id)sender{
    if (self.clickListener){
        self.clickListener(self);
    }
}

- (void) setClickListener:(OnClickListener)onClickListener{
    self.clickListener =onClickListener;
}

@end
